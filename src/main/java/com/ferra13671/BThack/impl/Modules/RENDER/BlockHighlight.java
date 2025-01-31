package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Box.RenderBox;
import com.ferra13671.BThack.api.Events.Render.RenderWorldLastEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ColorSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.FluidBlock;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Box;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class BlockHighlight extends Module {

    public final ColorSetting boxColor = new ColorSetting("Box Color", this, new Color(200, 200, 200, 220));
    public final NumberSetting linesAlpha = new NumberSetting("Lines Alpha", this, 255, 0, 255, true);

    public BlockHighlight() {
        super("BlockHighlight",
                "lang.module.BlockHighlight",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        initSettings(
                boxColor,
                linesAlpha
        );
    }

    @EventSubscriber
    public void onBlockOutlineRender(RenderWorldLastEvent e) {
        if (mc.crosshairTarget != null) {
            if (mc.crosshairTarget instanceof BlockHitResult result) {
                if (!mc.world.isAir(result.getBlockPos()) && !(mc.world.getBlockState(result.getBlockPos()).getBlock() instanceof FluidBlock)) {
                    float red = (float) boxColor.getValue().getRed() / 255f;
                    float green = (float) boxColor.getValue().getGreen() / 255f;
                    float blue = (float) boxColor.getValue().getBlue() / 255f;
                    float alpha = (float) boxColor.getValue().getAlpha() / 255f;
                    float lAlpha = (float) linesAlpha.getValue() / 255f;

                    Box box = BlockUtils.getBoundingBox(result.getBlockPos());
                    if (box == null) {
                        return;
                    }

                    BThackRender.boxRender.prepareBoxRender();
                    BThackRender.boxRender.renderBoxes(new ArrayList<>(Arrays.asList(new RenderBox(box, red, green, blue, lAlpha, red, green, blue, alpha))));
                    BThackRender.boxRender.stopBoxRender();
                }
            }
        }
    }
}
