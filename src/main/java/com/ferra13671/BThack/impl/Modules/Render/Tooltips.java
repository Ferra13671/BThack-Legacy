package com.ferra13671.BThack.impl.Modules.Render;

import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.core.Render.BThackMatrix;
import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.core.Render.Font.FontRenderManager;
import com.ferra13671.BThack.core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.ColorSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.shaders.BThackShaderProgram;
import com.ferra13671.BThack.shaders.CoreShaders;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.MapRenderState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;

import java.awt.*;
import java.util.List;

@ModuleInfo(name = "Tooltips", description = "lang.module.Tooltips", category = "RENDER")
public class Tooltips extends Module {

    public final BooleanSetting shulkers = new BooleanSetting("Shulkers", this, true);
    public final BooleanSetting maps = new BooleanSetting("Maps", this, true);

    public BooleanSetting rainbow;
    public NumberSetting rainbowAlpha;

    public BooleanSetting gradient;
    public ColorSetting color1;
    public ColorSetting color2;

    public NumberSetting scale;
    public NumberSetting speed;

    public final ColorSetting color = new ColorSetting("Color", this, new Color(161, 0, 255), () -> !rainbow.getValue() && !gradient.getValue());

    public final ColorSetting backGroundColor = new ColorSetting("BackGround", this, new Color(5, 5, 5, 255));

    public Tooltips() {
        rainbow = new BooleanSetting("Rainbow", this, false, () -> !gradient.getValue());
        rainbowAlpha = new NumberSetting("Rainbow Alpha", this, 255, 0, 255, true, () -> rainbow.getValue() && !gradient.getValue());

        gradient = new BooleanSetting("Gradient", this, true, () -> !(rainbow.getValue() && !this.gradient.getValue()));

        color1 = new ColorSetting("Color1", this, new Color(195, 85, 251), gradient::getValue).withBlockedAlpha();
        color2 = new ColorSetting("Color2", this, new Color(105, 0, 166), gradient::getValue).withBlockedAlpha();

        scale = new NumberSetting("Scale", this, 1, 0.3, 4, false, () -> rainbow.getValue() || gradient.getValue());
        speed = new NumberSetting("Speed", this, 1, 0.3, 4, false, () -> rainbow.getValue() || gradient.getValue());
    }

    private final MapRenderState mapRenderState = new MapRenderState();

    @SuppressWarnings("ExtractMethodRecommender")
    public void renderShulkerTooltip(ItemStack itemStack, List<ItemStack> stacks, int x, int y) {
        if (stacks.isEmpty()) return;

        BThackMatrix.push();
        BThackMatrix.translate(0f, 0f, 600f);

        BThackRender.drawVerticalGradientOutlineRect(x + 7, y - 22, x + 159, y + 49, 1, ColorUtils.WHITE, ColorUtils.fastRGBA(120, 120, 120, 255));
        if (isShaderRender()) {
            BThackShaderProgram shader = getCurrentShader(); //Just ignore this warn
            shader.setUniformValue("scale", scale.getValue().floatValue());
            shader.setUniformValue("speed", speed.getValue().floatValue());
            if (rainbow.getValue())
                shader.setUniformValue("alpha", rainbowAlpha.getValue().intValue() / 255f);
            else {
                shader.setUniformValue("color1", color1.getValue().getRed() / 255f, color1.getValue().getGreen() / 255f, color1.getValue().getBlue() / 255f, color1.getValue().getAlpha() / 255f);
                shader.setUniformValue("color2", color2.getValue().getRed() / 255f, color2.getValue().getGreen() / 255f, color2.getValue().getBlue() / 255f, color2.getValue().getAlpha() / 255f);
            }
            BThackRender.drawShader(shader, x + 8, y - 21, x + 158, y - 6);
        } else
            BThackRender.drawRect(x + 8, y - 21, x + 158, y - 6, getColor());
        BThackRender.drawRect(x + 8, y - 6, x + 158, y + 48, ColorUtils.fastRGBA(backGroundColor.getValue().getRed(), backGroundColor.getValue().getGreen(), backGroundColor.getValue().getBlue(), backGroundColor.getValue().getAlpha()));

        BThackRender.drawString(itemStack.getName().getString(), x + 10, y - 16, -1, true, FontRenderManager.DrawMode.NORMAL_BOLD);

        int slot = 0;
        for (ItemStack stack : stacks) {
            int offsetX = x + (slot % 9) * 16 + 11;
            int offsetY = y + (slot / 9) * 16 - 3;

            BThackRender.drawItem(stack, offsetX, offsetY, true);
            slot++;
        }

        BThackMatrix.pop();
    }

    @SuppressWarnings("ExtractMethodRecommender")
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
            double _scale = 0.8;
            context.getMatrices().translate(x + 16, y - 4, z);
            context.getMatrices().scale((float) _scale, (float) _scale, 0);

            BThackMatrix.push();
            BThackMatrix.translate(0f, 0f, 600f);
            BThackRender.drawRect(x1, y1 - 10, x2, y2, ColorUtils.fastRGBA(backGroundColor.getValue().getRed(), backGroundColor.getValue().getGreen(), backGroundColor.getValue().getBlue(), backGroundColor.getValue().getAlpha()));
            if (isShaderRender()) {
                BThackShaderProgram shader = getCurrentShader();
                shader.setUniformValue("scale", scale.getValue().floatValue());
                shader.setUniformValue("speed", speed.getValue().floatValue());
                if (rainbow.getValue())
                    shader.setUniformValue("alpha", rainbowAlpha.getValue().intValue() / 255f);
                else {
                    shader.setUniformValue("color1", color1.getValue().getRed() / 255f, color1.getValue().getGreen() / 255f, color1.getValue().getBlue() / 255f, color1.getValue().getAlpha() / 255f);
                    shader.setUniformValue("color2", color2.getValue().getRed() / 255f, color2.getValue().getGreen() / 255f, color2.getValue().getBlue() / 255f, color2.getValue().getAlpha() / 255f);
                }
                BThackRender.drawShaderOutlineRect(shader,x1, y1 - 10, x2, y2, 1);
            } else
                BThackRender.drawOutlineRect(x1, y1 - 10, x2, y2, 1, getColor()); //yea

            BThackMatrix.scale(0.75f, 0.75f, 0.75f);
            BThackRender.drawString(stack.getItem().getName().getString(), (int) ((x1 + 5) * 1.3333), (int) ((y1 - 5) * 1.3333), -1, true, FontRenderManager.DrawMode.NORMAL_BOLD);

            BThackMatrix.pop();

            mc.getMapRenderer().update(stack.get(DataComponentTypes.MAP_ID), mapState, mapRenderState);
            mc.getMapRenderer().draw(mapRenderState, context.getMatrices(), BThackRender.bufferSource, false, 0xF000F0);
        }
        context.getMatrices().pop();
    }

    public boolean isShaderRender() {
        return rainbow.getValue() || gradient.getValue();
    }

    public BThackShaderProgram getCurrentShader() {
        return gradient.getValue() ? CoreShaders.XY_GRADIENT : CoreShaders.X_RAINBOW;
    }

    private int getColor() {
        return ColorUtils.fastRGBA(color.getValue().getRed(), color.getValue().getGreen(), color.getValue().getBlue(), 255);
    }
}
