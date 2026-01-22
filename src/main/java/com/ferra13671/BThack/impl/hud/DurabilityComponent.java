package com.ferra13671.BThack.impl.hud;

import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.core.render.BThackRender;
import com.ferra13671.BThack.core.render.font.FontRenderManager;
import com.ferra13671.BThack.core.render.font.FontUtils;
import com.ferra13671.BThack.api.module.HudComponent;
import com.ferra13671.BThack.managers.impl.setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.utils.ItemUtils;
import net.minecraft.util.Formatting;

import java.util.Arrays;

@ModuleInfo(name = "Durability", category = "HUD")
public class DurabilityComponent extends HudComponent {

    public final ModeSetting mode = new ModeSetting("Info", this, Arrays.asList("Normal", "Full 1"));

    public DurabilityComponent() {
        super(5, 220);
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public void render() {
        BThackRender.drawHudPlate(getX(), getY(), getX() + width, getY() + height);

        String text = mode.getValue().equals("Full 1") ?
                "Left HandD: " + Formatting.WHITE + ItemUtils.getItemDurability(mc.player.getOffHandStack()) + Formatting.RESET + "  Right HandD: " + Formatting.WHITE + ItemUtils.getItemDurability(mc.player.getInventory().getMainHandStack()) :
                        "Durability: " + Formatting.WHITE + ItemUtils.getItemDurability(mc.player.getInventory().getMainHandStack());

        drawText(text, getX() + 3, getY() + 3);
        width = FontUtils.getTextWidth(text, FontRenderManager.DrawMode.NORMAL_BOLD) + 6;
        height = FontUtils.getTextHeight(text, FontRenderManager.DrawMode.NORMAL_BOLD) + 6;
    }
}
