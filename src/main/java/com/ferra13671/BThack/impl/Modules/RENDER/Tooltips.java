package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Core.Render.BThackMatrix;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Font.FontRenderManager;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ColorSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Shader.Shaders;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;

import java.awt.*;
import java.util.List;

public class Tooltips extends Module {

    public final BooleanSetting shulkers = new BooleanSetting("Shulkers", this, true);
    public final BooleanSetting maps = new BooleanSetting("Maps", this, true);

    public final BooleanSetting frameRainbow = new BooleanSetting("Frame Rainbow", this, true);
    public final ColorSetting frameColor = new ColorSetting("Frame Color", this, new Color(161, 0, 255), () -> !frameRainbow.getValue()).withBlockedAlpha();

    public final NumberSetting backGroundAlpha = new NumberSetting("BGround Alpha", this, 255, 10, 255, true);

    public Tooltips() {
        super("Tooltips",
                "lang.module.Tooltips",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        initSettings(
                shulkers,
                maps,

                frameRainbow,
                frameColor,

                backGroundAlpha
        );
    }

    public void renderShulkerTooltip(ItemStack itemStack, List<ItemStack> stacks, int x, int y) {
        if (stacks.isEmpty()) return;

        BThackMatrix.push();
        BThackMatrix.translate(0f, 0f, 600f);

        BThackRender.drawVerticalGradientOutlineRect(x + 7, y - 22, x + 159, y + 49, 1, ColorUtils.WHITE, ColorUtils.fastRGBA(120, 120, 120, 255));
        if (frameRainbow.getValue())
            BThackRender.drawShader(Shaders.INSTANCE.X_RAINBOW, x + 8, y - 21, x + 158, y - 6);
        else
            BThackRender.drawRect(x + 8, y - 21, x + 158, y - 6, getFrameColor());
        BThackRender.drawVerticalGradientRect(x + 8, y - 6, x + 158, y + 48, ColorUtils.fastRGBA(5, 5, 5, backGroundAlpha.getValue().intValue()), ColorUtils.fastRGBA(50, 50, 50, backGroundAlpha.getValue().intValue()));

        BThackRender.drawString(itemStack.getName().getString(), x + 10, y - 16, -1, true, FontRenderManager.DrawMode.NORMAL_BOLD);

        int slot = 0;
        for (ItemStack stack : stacks) {
            int offsetX = x + (slot % 9) * 16 + 11;
            int offsetY = y + (slot / 9) * 16 - 3;

            BThackRender.drawItem(stack, offsetX, offsetY, null, true);
            slot++;
        }

        BThackMatrix.pop();
    }

    public void renderMapTooltip(DrawContext context, ItemStack stack, int x, int y) {

        RenderSystem.enableBlend();
        context.getMatrices().push();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int y1 = y - 10;
        int y2 = y1 + 116;
        int x1 = x + 8;
        int x2 = x1 + 118;
        int z = 601;

        MapState mapState = FilledMapItem.getMapState(stack, mc.world);

        if (mapState != null) {
            mapState.getPlayerSyncData(mc.player);
            double scale = 0.8;
            context.getMatrices().translate(x + 16, y - 4, z);
            context.getMatrices().scale((float) scale, (float) scale, 0);

            BThackMatrix.push();
            BThackMatrix.translate(0f, 0f, 600f);
            BThackRender.drawVerticalGradientRect(x1, y1 - 10, x2, y2, ColorUtils.fastRGBA(5, 5, 5, backGroundAlpha.getValue().intValue()), ColorUtils.fastRGBA(100, 100, 100, backGroundAlpha.getValue().intValue()));
            if (frameRainbow.getValue())
                BThackRender.drawShaderOutlineRect(Shaders.INSTANCE.X_RAINBOW,x1, y1 - 10, x2, y2, 1);
            else
                BThackRender.drawOutlineRect(x1, y1 - 10, x2, y2, 1, getFrameColor()); //yea

            BThackMatrix.scale(0.75f, 0.75f, 0.75f);
            BThackRender.drawString(stack.getItem().getName().getString(), (int) ((x1 + 5) * 1.3333), (int) ((y1 - 5) * 1.3333), -1, true, FontRenderManager.DrawMode.NORMAL_BOLD);

            BThackMatrix.pop();

            mc.gameRenderer.getMapRenderer().draw(context.getMatrices(), BThackRender.bufferSource, stack.get(DataComponentTypes.MAP_ID), mapState, false, 0xF000F0);
        }
        context.getMatrices().pop();
    }

    private int getFrameColor() {
        return ColorUtils.fastRGBA(frameColor.getValue().getRed(), frameColor.getValue().getGreen(), frameColor.getValue().getBlue(), 255);
    }
}
