package com.ferra13671.BThack.impl.hud;

import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.core.render.BThackRender;
import com.ferra13671.BThack.core.render.font.FontRenderManager;
import com.ferra13671.BThack.core.render.font.FontUtils;
import com.ferra13671.BThack.core.render.utils.ColorUtils;
import com.ferra13671.BThack.api.module.HudComponent;
import com.ferra13671.BThack.managers.impl.setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.utils.ItemUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Arrays;

@ModuleInfo(name = "Armor", category = "HUD", autoEnabled = true)
public class ArmorComponent extends HudComponent {

    public final ModeSetting mode = new ModeSetting("Mode", this, Arrays.asList("Vertically", "Horizontally"));

    public ArmorComponent() {
        super(
                (MinecraftClient.getInstance().getWindow().getScaledWidth() / 2f) + (MinecraftClient.getInstance().getWindow().getScaledWidth() / 7.5f),
                MinecraftClient.getInstance().getWindow().getScaledHeight() - 140
        );
    }

    @Override
    public void render() {
        if (nullCheck()) return;

        BThackRender.drawHudPlate(getX(), getY(), getX() + width, getY() + height);

        if (mode.getValue().equals("Vertically")) renderVertically();
        else renderHorizontally();
    }

    @SuppressWarnings("DataFlowIssue")
    public void renderVertically() {
        int y = 0;

        float maxWidth = 0;

        for (int i = 3; i > -1; i--) {
            ItemStack armorStack = mc.player.getInventory().armor.get(i);

            if (armorStack.getItem() != Items.AIR) {
                String text = ItemUtils.getItemDurability(armorStack) + "/" + ItemUtils.getItemMaxDurability(armorStack) + " (" + ItemUtils.getItemDurabilityInPercentages(armorStack) + ")";

                float textWidth = FontUtils.getTextWidth(text, FontRenderManager.DrawMode.NORMAL_BOLD);
                if (textWidth > maxWidth)
                    maxWidth = textWidth;

                BThackRender.drawItem(armorStack, (int) getX() + 3, (int) getY() + 3 + y, false);
                BThackRender.drawString(text, (int) getX() + 23, (int) getY() + y + 3, ColorUtils.fastRGBA(armorStack.getItemBarColor()), true, FontRenderManager.DrawMode.NORMAL_BOLD);
                BThackRender.drawRect((int) getX() + 20 + 3, (int) (getY() + y + 2 + FontUtils.getTextHeight(text, FontRenderManager.DrawMode.NORMAL_BOLD)) + 3, (int) getX() + 20 + 50 + 3, (int) (getY() + y + FontUtils.getTextHeight(text, FontRenderManager.DrawMode.NORMAL_BOLD) + 8), ColorUtils.BLACK);
                if (ItemUtils.getItemDurabilityInPercentages(armorStack) > 0)
                    BThackRender.drawRect((int) getX() + 20 + 3, (int) (getY() + y + 2 + FontUtils.getTextHeight(text, FontRenderManager.DrawMode.NORMAL_BOLD)) + 3, (int) (getX() + 20 + (50 * (ItemUtils.getItemDurabilityInPercentages(armorStack) / 100f))) + 3, (int) (getY() + y + FontUtils.getTextHeight(text, FontRenderManager.DrawMode.NORMAL_BOLD) + 8), ColorUtils.fastRGBA(armorStack.getItemBarColor()));
            }

            y += 15;
        }

        this.height = y + 6;
        this.width = 20 + maxWidth + 6;
    }

    @SuppressWarnings("DataFlowIssue")
    public void renderHorizontally() {
        int x = 0;

        for (int i = 3; i > -1; i--) {
            ItemStack armorStack = mc.player.getInventory().armor.get(i);
            if (armorStack.getItem() != Items.AIR) {
                BThackRender.drawItem(armorStack, (int) (getX() + x + 3), (int) (getY() + 6), false);
                String text = "" + ((int) ItemUtils.getItemDurabilityInPercentages(armorStack));
                BThackRender.drawCenteredString(text, (int) (getX() + x + 11), (int) getY() + 3, ColorUtils.fastRGBA(armorStack.getItemBarColor()), FontRenderManager.DrawMode.SMALL);
            }

            x += 20;
        }

        height = 22;
        width = x + 3;
    }
}
