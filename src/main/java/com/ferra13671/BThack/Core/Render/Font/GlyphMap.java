package com.ferra13671.BThack.Core.Render.Font;

import com.ferra13671.TextureUtils.GLTexture;
import com.ferra13671.TextureUtils.TextureStorage;
import it.unimi.dsi.fastutil.chars.Char2ObjectArrayMap;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class GlyphMap {
    final char fromIncl, toExcl;
    final Font font;
    final String textureKey;
    final int pixelPadding;
    private final Char2ObjectArrayMap<Glyph> glyphs = new Char2ObjectArrayMap<>();
    int width, height;

    boolean generated = false;

    public GlyphMap(char from, char to, Font font, String textureKey, int padding) {
        this.fromIncl = from;
        this.toExcl = to;
        this.font = font;
        this.textureKey = textureKey;
        this.pixelPadding = padding;
    }

    public Glyph getGlyph(char c) {
        if (!generated) {
            generate();
        }
        return glyphs.get(c);
    }

    public void destroy() {
        TextureStorage.getTexture(textureKey).delete();
        TextureStorage.removeTexture(textureKey);
        this.glyphs.clear();
        this.width = -1;
        this.height = -1;
        generated = false;
    }

    public boolean contains(char c) {
        return c >= fromIncl && c < toExcl;
    }

    public void generate() {
        if (generated) {
            return;
        }
        int range = toExcl - fromIncl - 1;
        int charsVert = (int) (Math.ceil(Math.sqrt(range)) * 1.5);
        glyphs.clear();
        int generatedChars = 0;
        int charNX = 0;
        int maxX = 0, maxY = 0;
        int currentX = 0, currentY = 0;
        int currentRowMaxY = 0;
        List<Glyph> glyphs1 = new ArrayList<>();
        AffineTransform af = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(af, true, false);
        while (generatedChars <= range) {
            char currentChar = (char) (fromIncl + generatedChars);
            Rectangle2D stringBounds = font.getStringBounds(String.valueOf(currentChar), frc);

            int width = (int) Math.ceil(stringBounds.getWidth());
            int height = (int) Math.ceil(stringBounds.getHeight());
            generatedChars++;
            maxX = Math.max(maxX, currentX + width);
            maxY = Math.max(maxY, currentY + height);
            if (charNX >= charsVert) {
                currentX = 0;
                currentY += currentRowMaxY + pixelPadding;
                charNX = 0;
                currentRowMaxY = 0;
            }
            currentRowMaxY = Math.max(currentRowMaxY, height);
            glyphs1.add(new Glyph(currentX, currentY, width, height, currentChar, this));
            currentX += width + pixelPadding;
            charNX++;
        }
        BufferedImage bi = new BufferedImage(Math.max(maxX + pixelPadding, 1), Math.max(maxY + pixelPadding, 1),
                BufferedImage.TYPE_INT_ARGB);
        width = bi.getWidth();
        height = bi.getHeight();
        Graphics2D g2d = bi.createGraphics();
        g2d.setColor(new Color(255, 255, 255, 0));
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(Color.WHITE);

        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        for (Glyph glyph : glyphs1) {
            g2d.setFont(this.font);
            FontMetrics fontMetrics = g2d.getFontMetrics();
            g2d.drawString(String.valueOf(glyph.value()), glyph.u(), glyph.v() + fontMetrics.getAscent());
            glyphs.put(glyph.value(), glyph);
        }
        TextureStorage.addTexture(textureKey, GLTexture.fromBufferedImage(bi));
        generated = true;
    }
}