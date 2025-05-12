package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.core.Render.Font.FontRenderManager;
import com.ferra13671.BThack.core.Render.Font.FontUtils;
import com.ferra13671.BThack.api.Module.HudComponent;
import com.ferra13671.BThack.api.Utils.PlayerUtils;
import net.minecraft.util.Formatting;

@ModuleInfo(name = "Coordinates", category = "HUD", autoEnabled = true)
public class CoordinatesComponent extends HudComponent {

    public CoordinatesComponent() {
        super(5, 62);
    }

    String xyz1 = "";
    String xyz2 = "";

    @Override
    public void render() {
        if (nullCheck()) return;

        BThackRender.drawHudPlate(getX(), getY(), getX() + width, getY() + height);

        drawText(xyz1, getX() + 3, getY() + 3);
        drawText(xyz2, getX() + 3, getY() + FontUtils.getTextHeight(xyz1, FontRenderManager.DrawMode.NORMAL_BOLD) + 7);
    }

    @Override
    public void tick() {
        int overWorldX;
        int overWorldZ;
        int netherX;
        int netherZ;
        if (PlayerUtils.isInNether()) {
            overWorldX = (int) (mc.player.getX() * 8d);
            overWorldZ = (int) (mc.player.getZ() * 8d);
            netherX = (int) mc.player.getX();
            netherZ = (int) mc.player.getZ();
        } else {
            overWorldX = (int) mc.player.getX();
            overWorldZ = (int) mc.player.getZ();
            netherX = (int) (mc.player.getX() / 8d);
            netherZ = (int) (mc.player.getZ() / 8.0D);
        }

        xyz1 = "XYZ: " + Formatting.WHITE + overWorldX + " " + Math.round(mc.player.getY()) + " " + overWorldZ;
        xyz2 = "Nether: " + Formatting.WHITE + netherX + " " + Math.round(mc.player.getY()) + " " + netherZ;

        width = Math.max(FontUtils.getTextWidth(xyz1, FontRenderManager.DrawMode.NORMAL_BOLD), FontUtils.getTextWidth(xyz2, FontRenderManager.DrawMode.NORMAL_BOLD)) + 6;
        height = (FontUtils.getTextHeight(xyz1, FontRenderManager.DrawMode.NORMAL_BOLD) + FontUtils.getTextHeight(xyz2, FontRenderManager.DrawMode.NORMAL_BOLD)) + 10;
    }
}
