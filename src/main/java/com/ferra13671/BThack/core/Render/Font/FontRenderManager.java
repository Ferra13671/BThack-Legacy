package com.ferra13671.BThack.core.Render.Font;

import com.ferra13671.BThack.core.Render.BThackRender;

import java.awt.*;
import java.io.Closeable;

public class FontRenderManager implements Closeable {
    //Normal
    public final FontRenderer normalFontRenderer;
    public final FontRenderer smallFontRenderer;
    //Bold
    public final FontRenderer normalBoldFontRenderer;
    public final FontRenderer smallBoldFontRenderer;

    public FontRenderManager(Font font) {
        //Normal
        normalFontRenderer = new FontRenderer(font, 17);
        smallFontRenderer = new FontRenderer(font, 13);
        //Bold
        normalBoldFontRenderer = new FontRenderer(font.deriveFont(Font.BOLD, 17), 17);
        smallBoldFontRenderer = new FontRenderer(font.deriveFont(Font.BOLD, 13), 13);
    }

    public FontRenderManager(FontRenderer impl) {
        //Normal
        normalFontRenderer = impl;
        smallFontRenderer = new FontRenderer(impl.getFont().deriveFont(Font.PLAIN, 13), 13, impl.getPageSize(), impl.getPaddingBetweenChars(), impl.getPrebakeGlyphs());
        //Bold
        normalBoldFontRenderer = new FontRenderer(impl.getFont().deriveFont(Font.BOLD, 17), 17, impl.getPageSize(), impl.getPaddingBetweenChars(), impl.getPrebakeGlyphs());
        smallBoldFontRenderer = new FontRenderer(impl.getFont().deriveFont(Font.BOLD, 13), 13, impl.getPageSize(), impl.getPaddingBetweenChars(), impl.getPrebakeGlyphs());
    }

    public void draw(String text, float x, float y, int color, boolean shadow, DrawMode drawMode) {
        fontRendererOf(drawMode).draw(BThackRender.getGuiGraphics().getMatrices(), text, x, y, color, shadow);
    }

    public FontRenderer fontRendererOf(DrawMode drawMode) {
        return switch (drawMode) {
            case NORMAL -> normalFontRenderer;
            case NORMAL_BOLD -> normalBoldFontRenderer;
            case SMALL -> smallFontRenderer;
            case SMALL_BOLD -> smallBoldFontRenderer;
        };
    }

    /**
     * Use this method when overwriting your font render manager so that all current font renders are deleted correctly.
     * If you don't do this before overwriting the field, all textures of past font renders will remain in OpenGL memory, which is not a good thing.
     */
    @Override
    public void close() {
        normalFontRenderer.close();
        smallFontRenderer.close();
        normalBoldFontRenderer.close();
        smallBoldFontRenderer.close();
    }

    public enum DrawMode {
        NORMAL(1f),
        NORMAL_BOLD(1f),
        SMALL(0.7f),
        SMALL_BOLD(0.7f);

        private final float size;

        DrawMode(float size) {
            this.size = size;
        }

        public float getSize() {
            return size;
        }
    }
}
