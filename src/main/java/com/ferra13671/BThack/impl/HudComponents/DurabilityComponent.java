package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Font.FontUtils;
import com.ferra13671.BThack.api.HudComponent.HudComponent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import net.minecraft.util.Formatting;

import java.util.Arrays;

public class DurabilityComponent extends HudComponent {

    private final ModeSetting mode;

    public DurabilityComponent() {
        super("Durability",
                5,
                215,
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

        switch (mode.getValue()) {
            case "Normal":
                text = "Durability: " + Formatting.WHITE + ItemUtils.getItemDurability(mc.player.getInventory().getMainHandStack());
                drawText(text, (int) getX(), (int) getY());
                this.width = FontUtils.getTextWidth(text);
                this.height = FontUtils.getTextHeight(text);
                break;
            case "Full 1":
                text = "Left HandD: " + Formatting.WHITE + ItemUtils.getItemDurability(mc.player.getOffHandStack()) + Formatting.RESET + "  Right HandD: " + Formatting.WHITE + ItemUtils.getItemDurability(mc.player.getInventory().getMainHandStack());
                drawText(text, (int) getX(), (int) getY());
                this.width = FontUtils.getTextWidth(text);
                this.height = FontUtils.getTextHeight(text);
                break;
            case "Full 2":
                text = "Left     : " + Formatting.WHITE + ItemUtils.getItemDurability(mc.player.getOffHandStack()) + Formatting.RESET + "  Right     :" + Formatting.WHITE + ItemUtils.getItemDurability(mc.player.getInventory().getMainHandStack());
                drawText(text, (int) getX(), (int) getY());
                BThackRender.drawItem(BThackRender.guiGraphics, mc.player.getOffHandStack(), (int) (getX() + FontUtils.getTextWidth("Left") + 1), (int) getY() - 5, null, false);
                BThackRender.drawItem(BThackRender.guiGraphics, mc.player.getInventory().getMainHandStack(), (int) (getX() + FontUtils.getTextWidth("Left     : " + Formatting.WHITE + ItemUtils.getItemDurability(mc.player.getOffHandStack()) + Formatting.RESET + "  Right") + 1), (int) getY() - 5, null, false);
                this.width = FontUtils.getTextWidth(text);
                this.height = FontUtils.getTextHeight(text);
                break;
        }
    }
}
