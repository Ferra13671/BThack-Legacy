package com.ferra13671.BThack.Core.Render.Font;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.api.Interfaces.Mc;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public final class FontUtils implements Mc {


    public static float getTextWidth(String text) {
        if (ModuleList.customFont != null && ModuleList.customFont.isEnabled()) return BThackRender.fontRenderManager.normalFontRenderer.getStringWidth(text);
        else return mc.textRenderer.getWidth(text);
    }

    public static float getTextWidth(String text, FontRenderManager.DrawMode drawMode) {
        if (ModuleList.customFont != null && ModuleList.customFont.isEnabled()) {
            return BThackRender.fontRenderManager.fontRendererOf(drawMode).getStringWidth(text);
        } else return mc.textRenderer.getWidth(text);
    }

    public static float getTextHeight(String text) {
        if (ModuleList.customFont != null && ModuleList.customFont.isEnabled()) return BThackRender.fontRenderManager.normalFontRenderer.getStringHeight(text) / 2;
        else return mc.textRenderer.fontHeight;
    }

    public static float getTextHeight(String text, FontRenderManager.DrawMode drawMode) {
        if (ModuleList.customFont != null && ModuleList.customFont.isEnabled()) {
            return BThackRender.fontRenderManager.fontRendererOf(drawMode).getStringHeight(text) / 2;
        } else return mc.textRenderer.fontHeight;
    }

    public static Font createFont(InputStream inputStream, float size) throws IOException, FontFormatException {
        return Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(inputStream)).deriveFont(Font.PLAIN, size);
    }

    public static Font createFontNoThrow(InputStream inputStream, float size) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(inputStream)).deriveFont(Font.PLAIN, size);
        } catch (Exception ignored) {
            return null;
        }
    }
}
