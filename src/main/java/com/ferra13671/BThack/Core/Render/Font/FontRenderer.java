package com.ferra13671.BThack.Core.Render.Font;

import com.ferra13671.BTbot.api.Utils.Generate.StringGenerator;
import com.ferra13671.BThack.Core.Render.Utils.BThackRenderUtils;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.Constants;
import com.ferra13671.BThack.api.Utils.MathUtils;
import com.ferra13671.TextureUtils.TextureStorage;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.chars.Char2IntArrayMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

import java.awt.*;
import java.io.Closeable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FontRenderer implements Closeable, Mc {
    private static final Char2IntArrayMap colorCodes = new Char2IntArrayMap() {{
        put('0', 0x000000);
        put('1', 0x0000AA);
        put('2', 0x00AA00);
        put('3', 0x00AAAA);
        put('4', 0xAA0000);
        put('5', 0xAA00AA);
        put('6', 0xFFAA00);
        put('7', 0xAAAAAA);
        put('8', 0x555555);
        put('9', 0x5555FF);
        put('A', 0x55FF55);
        put('B', 0x55FFFF);
        put('C', 0xFF5555);
        put('D', 0xFF55FF);
        put('E', 0xFFFF55);
        put('F', 0xFFFFFF);
    }};

    private static final ExecutorService ASYNC_WORKER = Executors.newCachedThreadPool();
    private final Object2ObjectMap<String, ObjectList<DrawEntry>> GLYPH_PAGE_CACHE = new Object2ObjectOpenHashMap<>();
    private final float originalSize;
    private final ObjectList<GlyphMap> maps = new ObjectArrayList<>();
    private final Char2ObjectArrayMap<Glyph> allGlyphs = new Char2ObjectArrayMap<>();
    private final int pageSize;
    private final int paddingBetweenChars;
    private final String prebakeGlyphs;
    private int scaleMul = 0;
    private Font font;
    private int previousGameScale = -1;
    private Future<Void> prebakeGlyphsFuture;
    private boolean initialized;

    public FontRenderer(Font font, float sizePx, int charactersPerPage, int paddingBetweenChars, String prebakeCharacters) {
        this.originalSize = sizePx;
        this.pageSize = charactersPerPage;
        this.paddingBetweenChars = paddingBetweenChars;
        this.prebakeGlyphs = prebakeCharacters;
        init(font, sizePx);
    }

    public FontRenderer(Font font, float sizePx) {
        this(font, sizePx / 2, Constants.FONT_RENDERER_DEFAULT_PAGE_SIZE, Constants.FONT_RENDERER_DEFAULT_PADDING_BETWEEN_CHARS, null);
    }

    private int floorNearestMulN(int x) {
        return pageSize * (int) Math.floor((double) x / (double) pageSize);
    }

    public static String stripControlCodes(String text) {
        char[] chars = text.toCharArray();
        StringBuilder f = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '§') {
                i++;
                continue;
            }
            f.append(c);
        }
        return f.toString();
    }

    private void sizeCheck() {
        int gs = (int) mc.getWindow().getScaleFactor();
        if (gs != previousGameScale) {
            close();
            init(font, originalSize);
        }
    }

    private void init(Font font, float sizePx) {
        if (initialized) throw new IllegalStateException("Double call to init()");
        initialized = true;
        previousGameScale = (int) mc.getWindow().getScaleFactor();
        scaleMul = previousGameScale + 1; //Yes
        this.font = font.deriveFont(sizePx * scaleMul);
        if (prebakeGlyphs != null && !prebakeGlyphs.isEmpty()) {
            prebakeGlyphsFuture = prebake();
        }
    }

    private Future<Void> prebake() {
        return ASYNC_WORKER.submit(() -> {
            for (char c : prebakeGlyphs.toCharArray()) {
                if (Thread.interrupted()) break;
                locateGlyph(c);
            }
            return null;
        });
    }

    private GlyphMap generateMap(char from, char to) {
        GlyphMap gm = new GlyphMap(from, to, font, StringGenerator.generateNextString(32, false, false, false), paddingBetweenChars);
        maps.add(gm);
        return gm;
    }

    private Glyph locateGlyph(char glyph) {
        return allGlyphs.computeIfAbsent(glyph, this::locateGlyphInternal);
    }

    private Glyph locateGlyphInternal(char glyph) {
        for (GlyphMap map : maps) {
            if (map.contains(glyph)) {
                return map.getGlyph(glyph);
            }
        }
        int base = floorNearestMulN(glyph);
        GlyphMap glyphMap = generateMap((char) base, (char) (base + pageSize));
        return glyphMap.getGlyph(glyph);
    }

    public void draw(MatrixStack stack, String s, float x, float y, int rgb, boolean shadow) {
        float[] color = ColorUtils.hashCodeToRGBA(rgb);

        if (prebakeGlyphsFuture != null && !prebakeGlyphsFuture.isDone()) {
            try {
                prebakeGlyphsFuture.get();
            } catch (InterruptedException | ExecutionException ignored) {}
        }

        sizeCheck();
        float[] colors = new float[]{color[0], color[1], color[2]};
        stack.push();
        y -= 3f;
        stack.translate(MathUtils.roundToDecimal(x, 1), MathUtils.roundToDecimal(y, 1), 0.04f);
        stack.scale(1f / scaleMul, 1f / scaleMul, 1f);

        BThackRenderUtils.applyBlend();
        RenderSystem.disableCull();

        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
        BufferBuilder bb;
        Matrix4f mat = stack.peek().getPositionMatrix();
        char[] chars = s.toCharArray();
        float xOffset = 0;
        float yOffset = 0;
        boolean inSel = false;
        int lineStart = 0;
        synchronized (GLYPH_PAGE_CACHE) {
            for (int i = 0; i < chars.length; i++) {
                char c = chars[i];
                if (inSel) {
                    inSel = false;
                    char c1 = Character.toUpperCase(c);
                    if (colorCodes.containsKey(c1))
                        colors = ColorUtils.hashCodeToRGB(colorCodes.get(c1));
                    else if (c1 == 'R')
                        colors = new float[]{color[0], color[1], color[2]};
                    continue;
                }

                if (c == '§') {
                    inSel = true;
                    continue;
                } else if (c == '\n') {
                    yOffset += getStringHeight(s.substring(lineStart, i)) * scaleMul;
                    xOffset = 0;
                    lineStart = i + 1;
                    continue;
                }
                Glyph glyph = locateGlyph(c);
                if (glyph != null) {
                    if (glyph.value() != ' ') {
                        String i1 = glyph.owner().textureKey;
                        DrawEntry entry = new DrawEntry(xOffset, yOffset, colors[0], colors[1], colors[2], glyph);
                        GLYPH_PAGE_CACHE.computeIfAbsent(i1, integer -> new ObjectArrayList<>()).add(entry);
                    }
                    xOffset += glyph.width();
                }
            }
            for (String textureKey : GLYPH_PAGE_CACHE.keySet()) {
                TextureStorage.getTexture(textureKey).bind();
                RenderSystem.setShaderTexture(0, TextureStorage.getTexture(textureKey).getTexId());
                List<DrawEntry> objects = GLYPH_PAGE_CACHE.get(textureKey);

                bb = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

                for (DrawEntry object : objects) {
                    float xo = object.atX;
                    float yo = object.atY;
                    float cr = object.r;
                    float cg = object.g;
                    float cb = object.b;
                    Glyph glyph = object.toDraw;
                    GlyphMap owner = glyph.owner();
                    float w = glyph.width();
                    float h = glyph.height();
                    float u1 = (float) glyph.u() / owner.width;
                    float v1 = (float) glyph.v() / owner.height;
                    float u2 = (float) (glyph.u() + glyph.width()) / owner.width;
                    float v2 = (float) (glyph.v() + glyph.height()) / owner.height;

                    if (shadow) {
                        xo++;
                        yo++;
                        bb.vertex(mat, xo + 0, yo + h, 0).texture(u1, v2).color(0, 0, 0, color[3]);
                        bb.vertex(mat, xo + w, yo + h, 0).texture(u2, v2).color(0, 0, 0, color[3]);
                        bb.vertex(mat, xo + w, yo + 0, 0).texture(u2, v1).color(0, 0, 0, color[3]);
                        bb.vertex(mat, xo + 0, yo + 0, 0).texture(u1, v1).color(0, 0, 0, color[3]);
                        xo--;
                        yo--;
                    }
                    bb.vertex(mat, xo + 0, yo + h, 0).texture(u1, v2).color(cr, cg, cb, color[3]);
                    bb.vertex(mat, xo + w, yo + h, 0).texture(u2, v2).color(cr, cg, cb, color[3]);
                    bb.vertex(mat, xo + w, yo + 0, 0).texture(u2, v1).color(cr, cg, cb, color[3]);
                    bb.vertex(mat, xo + 0, yo + 0, 0).texture(u1, v1).color(cr, cg, cb, color[3]);
                }
                BThackRenderUtils.drawNoReset(bb.end());
            }

            GLYPH_PAGE_CACHE.clear();
        }
        stack.pop();
        BThackRenderUtils.resetShader();
    }

    public float getStringWidth(String text) {
        char[] c = stripControlCodes(text).toCharArray();
        float currentLine = 0;
        float maxPreviousLines = 0;
        for (char c1 : c) {
            if (c1 == '\n') {
                maxPreviousLines = Math.max(currentLine, maxPreviousLines);
                currentLine = 0;
                continue;
            }
            Glyph glyph = locateGlyph(c1);
            currentLine += glyph == null ? 0 : (glyph.width() / (float) this.scaleMul);
        }
        return Math.max(currentLine, maxPreviousLines);
    }

    public float getStringHeight(String text) {
        char[] c = stripControlCodes(text).toCharArray();
        if (c.length == 0) {
            c = new char[]{' '};
        }
        float currentLine = 0;
        float previous = 0;
        for (char c1 : c) {
            if (c1 == '\n') {
                if (currentLine == 0) {
                    currentLine = (locateGlyph(' ') == null ? 0 : (Objects.requireNonNull(locateGlyph(' ')).height() / (float) this.scaleMul));
                }
                previous += currentLine;
                currentLine = 0;
                continue;
            }
            Glyph glyph = locateGlyph(c1);
            currentLine = Math.max(glyph == null ? 0 : (glyph.height() / (float) this.scaleMul), currentLine);
        }
        return currentLine + previous;
    }


    @Override
    public void close() {
        try {
            if (prebakeGlyphsFuture != null && !prebakeGlyphsFuture.isDone() && !prebakeGlyphsFuture.isCancelled()) {
                prebakeGlyphsFuture.cancel(true);
                prebakeGlyphsFuture.get();
                prebakeGlyphsFuture = null;
            }
            maps.forEach(GlyphMap::destroy);
            maps.clear();
            allGlyphs.clear();
            initialized = false;
        } catch (Exception ignored) {
        }
    }

    public Font getFont() {
        return font;
    }

    public float getOriginalSize() {
        return originalSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPaddingBetweenChars() {
        return paddingBetweenChars;
    }

    public String getPrebakeGlyphs() {
        return prebakeGlyphs;
    }

    public int getScaleMul() {
        return scaleMul;
    }

    public boolean isInitialized() {
        return initialized;
    }

    record DrawEntry(float atX, float atY, float r, float g, float b, Glyph toDraw) {
    }
}
