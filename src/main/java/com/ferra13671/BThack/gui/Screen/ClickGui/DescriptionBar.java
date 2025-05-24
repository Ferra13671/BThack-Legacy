package com.ferra13671.BThack.gui.Screen.ClickGui;

import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.core.Render.BThackMatrix;
import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.core.Render.Font.FontUtils;
import com.ferra13671.BThack.core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Animation.Animation;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.api.Utils.Mc;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.PluginModule;

import java.io.Closeable;

public class DescriptionBar implements Closeable, Mc {
    private final Animation moveAnimation = new Animation(Easing.SINE_OUT, 400);
    private final Animation alphaAnimation = new Animation(Easing.LINEAR, 250);
    private final Module module;

    private boolean closing = false;

    public DescriptionBar(Module module) {
        this.module = module;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
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
        ModuleList.clickGui.prepareCurrentShader(alpha / 255f, 1);

        int scaledHeight = (int) (mc.getWindow().getScaledHeight() / ModuleList.clickGui.guiScale.getValue());

        BThackMatrix.push();
        if (module instanceof PluginModule pluginMod) {
            float pluginNameLength = FontUtils.getTextWidth("Plugin: " + pluginMod.plugin.pluginName) + 10;
            float descriptionLength = FontUtils.getTextWidth(module.getDescription()) + 10;
            float length = Math.max(pluginNameLength, descriptionLength);

            BThackMatrix.translate((float) -((1 - moveAnimation.getEase()) * length), 0, 0);

            BThackRender.drawRect(1, scaledHeight - 30, length, scaledHeight -1, ColorUtils.integrateAlpha(ModuleList.clickGui.backgroundColor.getValue().hashCode(), alpha));
            BThackRender.drawShaderOutlineRect(ModuleList.clickGui.getCurrentShader(), 1, scaledHeight - 30, length, scaledHeight -1, 1);
            BThackRender.drawString(module.getDescription(), 6, scaledHeight - 7 - (FontUtils.getTextHeight(module.getDescription()) / 2f), ColorUtils.integrateAlpha(ModuleList.clickGui.textColor.getValue().hashCode(), alpha));
            BThackRender.drawString("Plugin: " + pluginMod.plugin.pluginName, 6, scaledHeight - 19 - (FontUtils.getTextHeight(pluginMod.plugin.pluginName) / 2f), ColorUtils.integrateAlpha(ModuleList.clickGui.textColor.getValue().hashCode(), alpha));
        } else {
            float length = FontUtils.getTextWidth(module.getDescription()) + 10;

            BThackMatrix.translate((float) -((1 - moveAnimation.getEase()) * length), 0, 0);

            BThackRender.drawRect(1, scaledHeight - 18, length, scaledHeight -1, ColorUtils.integrateAlpha(ModuleList.clickGui.backgroundColor.getValue().hashCode(), alpha));
            BThackRender.drawShaderOutlineRect(ModuleList.clickGui.getCurrentShader(), 1, scaledHeight - 18, length, scaledHeight -1, 1);
            BThackRender.drawString(module.getDescription(), 6, scaledHeight - 9.5f - (FontUtils.getTextHeight(module.getDescription()) / 2f), ColorUtils.integrateAlpha(ModuleList.clickGui.textColor.getValue().hashCode(), alpha));
        }
        BThackMatrix.pop();
    }
}
