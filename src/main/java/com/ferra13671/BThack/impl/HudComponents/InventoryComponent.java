package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.api.Module.HudComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;

public class InventoryComponent extends HudComponent {

    public InventoryComponent() {
        super("Inventory",
                (MinecraftClient.getInstance().getWindow().getScaledWidth() / 2f) + (MinecraftClient.getInstance().getWindow().getScaledWidth() / 7.5f),
                MinecraftClient.getInstance().getWindow().getScaledHeight() - 60,
                true
        );

        this.width = 145 + 6;
        this.height = 49 + 6;
    }

    @Override
    public void render() {
        if (nullCheck()) return;

        BThackRender.drawHudPlate(getX(), getY(), getX() + width, getY() + height);

        for (int i = 0; i < 27; i++) {
            ItemStack itemStack = mc.player.getInventory().main.get(i + 9);

            int offsetX = (int) getX() + (i % 9) * 16;
            int offsetY = (int) getY() + (i / 9) * 16;

            BThackRender.drawItem(itemStack, offsetX + 3, offsetY + 3, null, true);
        }
    }
}
