package com.ferra13671.BThack.impl.HudComponents;


import com.ferra13671.BThack.api.HudComponent.HudComponent;
import com.ferra13671.BThack.api.Utils.PlayerUtils;
import net.minecraft.util.Formatting;

public class CoordinatesComponent extends HudComponent {

    public CoordinatesComponent() {
        super("Coordinates",
                5,
                57,
                true
        );
    }

    String xyz1 = "";
    String xyz2 = "";

    @Override
    public void render() {
        if (nullCheck()) return;

        drawText(xyz1, (int) getX(), (int) getY());
        drawText(xyz2, (int) getX(), (int) (getY() + 9.3f));
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

        this.width = Math.max(mc.textRenderer.getWidth(xyz1), mc.textRenderer.getWidth(xyz2));
        this.height = mc.textRenderer.fontHeight * 2.2f;
    }
}
