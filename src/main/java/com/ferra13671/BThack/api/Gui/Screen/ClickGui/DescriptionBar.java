package com.ferra13671.BThack.api.Gui.Screen.ClickGui;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Font.FontUtils;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.Core.Render.Utils.RainbowUtils;
import com.ferra13671.BThack.api.Animation.Animation;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.PluginModule;

import java.io.Closeable;

public class DescriptionBar implements Closeable {
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
        float[] rSettings = RainbowUtils.getRainbowRectSpeed((int) ModuleList.clickGui.rainbowSpeed.getValue());
        int rainbow = ColorUtils.rainbow((int) rSettings[1], (int) rSettings[0]);

        int alpha = closing ? (int) ((1 - alphaAnimation.getEase()) * 255) : (int) (alphaAnimation.getEase() * 255);

        if (module instanceof PluginModule pluginMod) {
            float pluginNameLength = FontUtils.getTextWidth("Plugin: " + pluginMod.plugin.pluginName) + 10;
            float descriptionLength = FontUtils.getTextWidth(module.getDescription()) + 10;
            float length = Math.max(pluginNameLength, descriptionLength);

            BThackRender.guiGraphics.getMatrices().push();
            BThackRender.guiGraphics.getMatrices().translate(-((1 - moveAnimation.getEase()) * length), 0, 0);
            BThackRender.drawRect(1, ClickGuiScreen.descriptionY - 17, length, (ClickGuiScreen.descriptionY + 12), ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().backgroundColor()), alpha));
            BThackRender.drawOutlineRect(1, ClickGuiScreen.descriptionY - 17, length, (ClickGuiScreen.descriptionY + 12), 1, ColorUtils.integrateAlpha(ColorUtils.fastRGBA(rainbow), alpha));
            BThackRender.drawString(module.getDescription(), 6, (ClickGuiScreen.descriptionY + 6) - (FontUtils.getTextHeight(module.getDescription()) / 2f), ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().moduleDisabledColor()), alpha));
            BThackRender.drawString("Plugin: " + pluginMod.plugin.pluginName, 6, (ClickGuiScreen.descriptionY - 6) - (FontUtils.getTextHeight(module.getDescription()) / 2f), ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().moduleDisabledColor()), alpha));
            BThackRender.guiGraphics.getMatrices().pop();
        } else {
            float length = FontUtils.getTextWidth(module.getDescription()) + 10;

            BThackRender.guiGraphics.getMatrices().push();
            BThackRender.guiGraphics.getMatrices().translate(-((1 - moveAnimation.getEase()) * length), 0, 0);
            BThackRender.drawRect(1, ClickGuiScreen.descriptionY - 5, length, (ClickGuiScreen.descriptionY + 12), ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().backgroundColor()), alpha));
            BThackRender.drawOutlineRect(1, ClickGuiScreen.descriptionY - 5, length, (ClickGuiScreen.descriptionY + 12), 1, ColorUtils.integrateAlpha(ColorUtils.fastRGBA(rainbow), alpha));
            BThackRender.drawString(module.getDescription(), 6, (ClickGuiScreen.descriptionY + 3.5f) - (FontUtils.getTextHeight(module.getDescription()) / 2f), ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().moduleDisabledColor()), alpha));
            BThackRender.guiGraphics.getMatrices().pop();
        }
    }
}
