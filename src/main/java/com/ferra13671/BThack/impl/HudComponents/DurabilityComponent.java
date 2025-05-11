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

    public final ModeSetting mode = new ModeSetting("Info", this, Arrays.asList("Normal", "Full 1"));

    public DurabilityComponent() {
        super("Durability",
                5,
                220,
                false
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
        drawText(text, getX() + 3, getY() + 3);
        width = FontUtils.getTextWidth(text, FontRenderManager.DrawMode.NORMAL_BOLD) + 6;
        height = FontUtils.getTextHeight(text, FontRenderManager.DrawMode.NORMAL_BOLD) + 6;
    }
}
