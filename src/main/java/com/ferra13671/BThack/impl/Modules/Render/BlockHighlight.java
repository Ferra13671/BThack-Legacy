package com.ferra13671.BThack.impl.Modules.Render;

import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.core.Render.Box.RenderBox;
import com.ferra13671.BThack.api.Animation.Animation;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.events.Render.RenderWorldLastEvent;
import com.ferra13671.BThack.managers.managers.Setting.Settings.ColorSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.FluidBlock;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "BlockHighlight", description = "lang.module.BlockHighlight", category = "RENDER")
public class BlockHighlight extends Module {

    public final ColorSetting boxColor = new ColorSetting("Box Color", this, new Color(200, 200, 200, 220));
    public final NumberSetting linesAlpha = new NumberSetting("Lines Alpha", this, 255, 0, 255, true);
    public final NumberSetting animTime = new NumberSetting("Anim. Time", this, 350, 100, 1000, true);


    private Box currentBox = null;
    private Box prevBox = null;
    private boolean canceled = true;
    private final Animation moveAnimation = new Animation(Easing.LINEAR, animTime.getValue().intValue());
    private final Animation alphaAnimation = new Animation(Easing.LINEAR, animTime.getValue().intValue());

    @Override
    public void onChangeSetting(Setting<?> setting) {
        if (setting == animTime) {
            moveAnimation.setMillis(animTime.getValue().intValue());
            alphaAnimation.setMillis(animTime.getValue().intValue());
        }
    }

    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onBlockOutlineRender(RenderWorldLastEvent e) {
        RenderBox renderBox = null;

        if (mc.crosshairTarget instanceof BlockHitResult result &&
                !(mc.world.isAir(result.getBlockPos()) || mc.world.getBlockState(result.getBlockPos()).getBlock() instanceof FluidBlock)) {
            Box box = null;
            try {
                box = BlockUtils.getBoundingBox(result.getBlockPos());
            } catch (Exception ignored) {}
            if (box == null) {
                reset();
                return;
            }

            if (currentBox == null) alphaAnimation.reset();
            if (prevBox != null && !prevBox.equals(box)) moveAnimation.reset();

            prevBox = box;

            double ease = moveAnimation.getEase();
            currentBox = currentBox == null ? box : new Box(
                    MathHelper.lerp(ease, currentBox.minX, box.minX),
                    MathHelper.lerp(ease, currentBox.minY, box.minY),
                    MathHelper.lerp(ease, currentBox.minZ, box.minZ),
                    MathHelper.lerp(ease, currentBox.maxX, box.maxX),
                    MathHelper.lerp(ease, currentBox.maxY, box.maxY),
                    MathHelper.lerp(ease, currentBox.maxZ, box.maxZ)
            );

            renderBox = getRenderBox(currentBox);
            canceled = false;
        } else
            reset();

        if (renderBox == null && alphaAnimation.getEase() < 1 && prevBox != null)
            renderBox = getRenderBox(prevBox);

        if (renderBox != null) {
            BThackRender.boxRender.prepareBoxRender();
            BThackRender.boxRender.renderBoxes(new ArrayList<>(List.of(renderBox)));
            BThackRender.boxRender.stopBoxRender();
        }
    }

    public RenderBox getRenderBox(Box box) {
        float red = (float) boxColor.getValue().getRed() / 255f;
        float green = (float) boxColor.getValue().getGreen() / 255f;
        float blue = (float) boxColor.getValue().getBlue() / 255f;
        double ease = currentBox == null ? 1 - alphaAnimation.getEase() : alphaAnimation.getEase();
        float alpha = (float) ((boxColor.getValue().getAlpha() / 255f) * ease);
        float lAlpha = (float) ((linesAlpha.getValue().floatValue() / 255f) * ease);

        return new RenderBox(box, red, green, blue, lAlpha, red, green, blue, alpha);
    }

    public void reset() {
        if (!canceled) {
            alphaAnimation.reset();
            canceled = true;
        }
        currentBox = null;
    }
}
