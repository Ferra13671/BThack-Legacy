package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Module.HudComponent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;

public class InventoryComponent extends HudComponent {

    public final NumberSetting backGroundAlpha = new NumberSetting("BG Alpha", this, 135, 0, 255, true);
    public final NumberSetting outlineAlpha = new NumberSetting("Outline Alpha", this, 125, 0, 255, true);

    public InventoryComponent() {
        super("Inventory",
                (MinecraftClient.getInstance().getWindow().getScaledWidth() / 2f) + (MinecraftClient.getInstance().getWindow().getScaledWidth() / 7.5f),
                MinecraftClient.getInstance().getWindow().getScaledHeight() - 60,
                true
        );

        initSettings(
                backGroundAlpha,
                outlineAlpha
        );

        this.width = 144;
        this.height = 48;
    }

    @Override
    public void render() {
        if (nullCheck()) return;

        BThackRender.drawRect((int) getX() - 3, (int) getY() - 3, (int) (getX() + width) + 3, (int) (getY() + height) + 3, ColorUtils.fastRGBA(20, 20, 20, (int) backGroundAlpha.getValue()));
        BThackRender.drawOutlineRect((int) getX() - 4, (int) getY() - 4, (int) (getX() + width) + 4, (int) (getY() + height) + 4, 1, ColorUtils.fastRGBA(255, 255, 255, (int) outlineAlpha.getValue()));

        for (int i = 0; i < 27; i++) {
            ItemStack itemStack = mc.player.getInventory().main.get(i + 9);

            int offsetX = (int) getX() + (i % 9) * 16;
            int offsetY = (int) getY() + (i / 9) * 16;

            BThackRender.drawItem(BThackRender.guiGraphics, itemStack, offsetX, offsetY, null, true);
        }
    }
}
