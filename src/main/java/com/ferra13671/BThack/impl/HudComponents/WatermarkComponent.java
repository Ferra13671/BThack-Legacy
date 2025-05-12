package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.core.Client.Client;
import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.core.Render.Font.FontRenderManager;
import com.ferra13671.BThack.core.Render.Font.FontUtils;
import com.ferra13671.BThack.api.Module.HudComponent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Utils.Textures;

import java.util.Arrays;

@ModuleInfo(name = "Watermark", category = "HUD", autoEnabled = true)
public class WatermarkComponent extends HudComponent {

    public final ModeSetting logoType = new ModeSetting("Logo Type", this, Arrays.asList("Logo", "Text"));

    public WatermarkComponent() {
        super(5, 5);
    }

    @Override
    public void render() {
        if (nullCheck()) return;

        if (logoType.getValue().equals("Text")) {
            BThackRender.drawHudPlate(getX(), getY(), getX() + width, getY() + height);
            drawText(Client.clientInfo.getCName(), getX() + 3, getY() + 3);
        } else {
            BThackRender.drawTextureRect(Textures.BTHACK_LOGO, getX(), getY() - 18, getX() + 138, getY() + 54);
        }
    }

    @Override
    public void tick() {
        if (nullCheck()) return;

        if (logoType.getValue().equals("Text")) {
            width = FontUtils.getTextWidth(Client.clientInfo.getCName(), FontRenderManager.DrawMode.NORMAL_BOLD) + 6;
            height = FontUtils.getTextHeight(Client.clientInfo.getCName(), FontRenderManager.DrawMode.NORMAL_BOLD) + 6;
        } else {
            width = 138;
            height = 42;
        }
    }
}
