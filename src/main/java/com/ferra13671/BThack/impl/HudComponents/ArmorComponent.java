package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Font.FontRenderManager;
import com.ferra13671.BThack.Core.Render.Font.FontUtils;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.HudComponent.HudComponent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Arrays;

public class ArmorComponent extends HudComponent {

    public final ModeSetting mode;

    public ArmorComponent() {
        super("Armor",
                (MinecraftClient.getInstance().getWindow().getScaledWidth() / 2f) + (MinecraftClient.getInstance().getWindow().getScaledWidth() / 7.5f),
                MinecraftClient.getInstance().getWindow().getScaledHeight() - 140,
                true
        );

        mode = new ModeSetting("Mode", this, Arrays.asList("Vertically", "Horizontally"));

        initSettings(
                mode
        );
    }

    @Override
    public void render() {
        if (nullCheck()) return;

        if (mode.getValue().equals("Vertically")) renderVertically();
        else renderHorizontally();
    }

    public void renderVertically() {
        int y = 0;

        float maxWidth = 0;

        for (int i = 3; i > -1; i--) {
            ItemStack armorStack = mc.player.getInventory().armor.get(i);

            if (armorStack.getItem() != Items.AIR) {
                String text = ItemUtils.getItemDurability(armorStack) + "/" + ItemUtils.getItemMaxDurability(armorStack) + " (" + ItemUtils.getItemDurabilityInPercentages(armorStack) + ")";

                if (FontUtils.getTextWidth(text) > maxWidth) {
                    maxWidth = FontUtils.getTextWidth(text);
                }

                BThackRender.drawItem(BThackRender.guiGraphics, armorStack, (int) getX(), (int) getY() + y, null, false);
                BThackRender.drawString(text, (int) getX() + 20, (int) getY() + y, ColorUtils.fastRGBA(armorStack.getItemBarColor()));
                BThackRender.drawRect((int) getX() + 20, (int) (getY() + y + 2 + FontUtils.getTextHeight(text)), (int) getX() + 20 + 50, (int) (getY() + y + FontUtils.getTextHeight(text) + 5), ColorUtils.BLACK);
                if (ItemUtils.getItemDurabilityInPercentages(armorStack) > 0)
                    BThackRender.drawRect((int) getX() + 20, (int) (getY() + y + 2 + FontUtils.getTextHeight(text)), (int) (getX() + 20 + (50 * (ItemUtils.getItemDurabilityInPercentages(armorStack) / 100f))), (int) (getY() + y + FontUtils.getTextHeight(text) + 5), ColorUtils.fastRGBA(armorStack.getItemBarColor()));
            }

            y += 20;
        }

        this.height = y;
        this.width = 20 + maxWidth;
    }

    public void renderHorizontally() {
        int x = 0;

        for (int i = 3; i > -1; i--) {
            ItemStack armorStack = mc.player.getInventory().armor.get(i);
            if (armorStack.getItem() != Items.AIR) {
                BThackRender.drawItem(BThackRender.guiGraphics, armorStack, (int) (getX() + x), (int) (getY() + 3), null, false);
            }

            x += 20;
        }

        x = 0;

        BThackRender.guiGraphics.getMatrices().push();
        for (int i = 3; i > -1; i--) {
            ItemStack armorStack = mc.player.getInventory().armor.get(i);

            if (armorStack.getItem() != Items.AIR) {
                BThackRender.drawCenteredString("" + ((int) ItemUtils.getItemDurabilityInPercentages(armorStack)), (int) (getX() + x + 8), (int) getY(), ColorUtils.fastRGBA(armorStack.getItemBarColor()), FontRenderManager.DrawMode.SMALL);
            }

            x += 20;
        }
        BThackRender.guiGraphics.getMatrices().pop();

        this.height = 23;
        this.width = x;
    }
}
