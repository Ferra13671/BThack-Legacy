package com.ferra13671.BThack.api.Gui.Screen.ClickGui;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackMatrix;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Font.FontUtils;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Animation.Animation;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.PluginModule;
import com.ferra13671.BThack.api.Shader.Shaders;

import java.io.Closeable;

public class DescriptionBar implements Closeable, Mc {
    private final Animation moveAnimation = new Animation(Easing.SINE_OUT, 400);
    private final Animation alphaAnimation = new Animation(Easing.LINEAR, 250);
    private final Module module;

    private boolean closing = false;

    public DescriptionBar(Module module) {
        this.module = module;
    }

    public boolean isClosing() {
        return closing;
    }

    @Override
    public void close() {
        closing = true;
        alphaAnimation.reset();
    }

    public boolean needRemove() {
        return closing && alphaAnimation.getEase() >= 1;
    }

    public Module getModule() {
        return module;
    }

    public void render() {
        int alpha = closing ? (int) ((1 - alphaAnimation.getEase()) * 255) : (int) (alphaAnimation.getEase() * 255);
        Shaders.INSTANCE.X_RAINBOW.setUniformValue("alpha", alpha / 255f);

        int scaledHeight = (int) (mc.getWindow().getScaledHeight() / ModuleList.clickGui.guiScale.getValue());

        BThackMatrix.push();
        if (module instanceof PluginModule pluginMod) {
            float pluginNameLength = FontUtils.getTextWidth("Plugin: " + pluginMod.plugin.pluginName) + 10;
            float descriptionLength = FontUtils.getTextWidth(module.getDescription()) + 10;
            float length = Math.max(pluginNameLength, descriptionLength);

            BThackMatrix.translate((float) -((1 - moveAnimation.getEase()) * length), 0, 0);

            BThackRender.drawRect(1, scaledHeight - 30, length, scaledHeight -1, ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().backgroundColor()), alpha));
            BThackRender.drawShaderOutlineRect(Shaders.INSTANCE.X_RAINBOW, 1, scaledHeight - 30, length, scaledHeight -1, 1);
            BThackRender.drawString(module.getDescription(), 6, scaledHeight - 7 - (FontUtils.getTextHeight(module.getDescription()) / 2f), ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().moduleDisabledColor()), alpha));
            BThackRender.drawString("Plugin: " + pluginMod.plugin.pluginName, 6, scaledHeight - 19 - (FontUtils.getTextHeight(pluginMod.plugin.pluginName) / 2f), ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().moduleDisabledColor()), alpha));
        } else {
            float length = FontUtils.getTextWidth(module.getDescription()) + 10;

            BThackMatrix.translate((float) -((1 - moveAnimation.getEase()) * length), 0, 0);

            BThackRender.drawRect(1, scaledHeight - 18, length, scaledHeight -1, ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().backgroundColor()), alpha));
            BThackRender.drawShaderOutlineRect(Shaders.INSTANCE.X_RAINBOW, 1, scaledHeight - 18, length, scaledHeight -1, 1);
            BThackRender.drawString(module.getDescription(), 6, scaledHeight - 9.5f - (FontUtils.getTextHeight(module.getDescription()) / 2f), ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().moduleDisabledColor()), alpha));
        }
        BThackMatrix.pop();
    }
}
