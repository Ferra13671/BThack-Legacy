package com.ferra13671.BThack.Core.Render.Font;

import com.ferra13671.BThack.Core.Render.BThackRender;

import java.awt.*;

public class FontRenderManager {
    public final FontRenderer normalFontRenderer;
    public final FontRenderer smallFontRenderer;

    public FontRenderManager(Font font) {
        normalFontRenderer = new FontRenderer(font, 17);
        smallFontRenderer = new FontRenderer(font, 13);
    }

    public FontRenderManager(FontRenderer impl) {
        normalFontRenderer = impl;
        smallFontRenderer = new FontRenderer(impl.getFont().deriveFont(Font.PLAIN, 6.5f), 13, impl.getPageSize(), impl.getPaddingBetweenChars(), impl.getPrebakeGlyphs());
    }

    public void drawNormal(String text, float x, float y, int color, boolean shadow) {
        normalFontRenderer.draw(BThackRender.guiGraphics.getMatrices(), text, x, y, color, shadow);
    }

    public void drawSmall(String text, float x, float y, int color, boolean shadow) {
        smallFontRenderer.draw(BThackRender.guiGraphics.getMatrices(), text, x, y, color, shadow);
    }
}
