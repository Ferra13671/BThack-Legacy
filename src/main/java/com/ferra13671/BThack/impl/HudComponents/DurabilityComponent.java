package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Font.FontRenderManager;
import com.ferra13671.BThack.Core.Render.Font.FontUtils;
import com.ferra13671.BThack.api.Module.HudComponent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import net.minecraft.util.Formatting;

import java.util.Arrays;

public class DurabilityComponent extends HudComponent {

    private final ModeSetting mode;

    public DurabilityComponent() {
        super("Durability",
                5,
                220,
                false
        );

        mode = new ModeSetting("Info", this, Arrays.asList("Normal", "Full 1", "Full 2"));

        initSettings(
                mode
        );
    }

    @Override
    public void render() {
        String text;

        BThackRender.drawHudPlate(getX(), getY(), getX() + width, getY() + height);

        if (mode.getValue().equals("Full 1")) {
            text = "Left HandD: " + Formatting.WHITE + ItemUtils.getItemDurability(mc.player.getOffHandStack()) + Formatting.RESET + "  Right HandD: " + Formatting.WHITE + ItemUtils.getItemDurability(mc.player.getInventory().getMainHandStack());
        } else {
            text = "Durability: " + Formatting.WHITE + ItemUtils.getItemDurability(mc.player.getInventory().getMainHandStack());
        }
        drawText(text, (int) getX() + 3, (int) getY() + 3);
        width = FontUtils.getTextWidth(text, FontRenderManager.DrawMode.NORMAL_BOLD) + 6;
        height = FontUtils.getTextHeight(text, FontRenderManager.DrawMode.NORMAL_BOLD) + 6;
    }
}
