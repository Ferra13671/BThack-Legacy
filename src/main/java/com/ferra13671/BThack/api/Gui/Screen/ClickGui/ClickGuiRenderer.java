package com.ferra13671.BThack.api.Gui.Screen.ClickGui;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Font.FontUtils;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.Core.Render.Utils.RainbowUtils;
import com.ferra13671.BThack.api.Animation.Animation;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.PluginModule;
import com.ferra13671.BThack.api.Utils.Data;

public class ClickGuiRenderer implements Mc {

    public static void drawDescriptionBar(Data<Module> descriptionModule, Animation descriptionAnimation) {
        float[] rSettings = RainbowUtils.getRainbowRectSpeed((int) ModuleList.clickGui.rainbowSpeed.getValue());
        int rainbow = ColorUtils.rainbow((int) rSettings[1], (int) rSettings[0]);

        if (descriptionModule.get() != null) {
            int alpha = (int) (descriptionModule.get() != null ? (descriptionAnimation.getEase() * 255) : (1 - (descriptionAnimation.getEase() * 255)));

            if (descriptionModule.get() instanceof PluginModule pluginMod) {
                float pluginNameLength = FontUtils.getTextWidth("Plugin: " + pluginMod.plugin.pluginName) + 10;
                float descriptionLength = FontUtils.getTextWidth(descriptionModule.get().getDescription()) + 10;
                float length = Math.max(pluginNameLength, descriptionLength);
                BThackRender.drawRect(1, ClickGuiScreen.descriptionY - 17, length, (ClickGuiScreen.descriptionY + 12), ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().backgroundColor()), alpha));
                BThackRender.drawOutlineRect(1, ClickGuiScreen.descriptionY - 17, length, (ClickGuiScreen.descriptionY + 12), 1, ColorUtils.integrateAlpha(ColorUtils.fastRGBA(rainbow), alpha));
                BThackRender.drawString(descriptionModule.get().getDescription(), 6, (ClickGuiScreen.descriptionY + 6) - (FontUtils.getTextHeight(descriptionModule.get().getDescription()) / 2f), ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().moduleDisabledColor()), alpha));
                BThackRender.drawString("Plugin: " + pluginMod.plugin.pluginName, 6, (ClickGuiScreen.descriptionY - 6) - (FontUtils.getTextHeight(descriptionModule.get().getDescription()) / 2f), ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().moduleDisabledColor()), alpha));
            } else {
                float length = FontUtils.getTextWidth(descriptionModule.get().getDescription()) + 10;
                BThackRender.drawRect(1, ClickGuiScreen.descriptionY - 5, length, (ClickGuiScreen.descriptionY + 12), ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().backgroundColor()), alpha));
                BThackRender.drawOutlineRect(1, ClickGuiScreen.descriptionY - 5, length, (ClickGuiScreen.descriptionY + 12), 1, ColorUtils.integrateAlpha(ColorUtils.fastRGBA(rainbow), alpha));
                BThackRender.drawString(descriptionModule.get().getDescription(), 6, (ClickGuiScreen.descriptionY + 6) - (FontUtils.getTextHeight(descriptionModule.get().getDescription()) / 2f), ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().moduleDisabledColor()), alpha));
            }
        }
    }
}
