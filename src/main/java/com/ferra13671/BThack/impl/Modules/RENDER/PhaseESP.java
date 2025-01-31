package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Box.RenderBox;
import com.ferra13671.BThack.api.Events.Render.RenderWorldLastEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ColorSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3i;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PhaseESP extends Module {
    public final BooleanSetting outline = new BooleanSetting("Outline", this, true);
    public final BooleanSetting fill = new BooleanSetting("Fill", this, true);

    public final ColorSetting goodColor = new ColorSetting("Good Color", this, new Color(0, 178, 0)).withBlockedAlpha();
    public final ColorSetting normalColor = new ColorSetting("Normal Color", this, new Color(0, 0, 178)).withBlockedAlpha();
    public final ColorSetting dangerColor = new ColorSetting("Danger Color", this, new Color(178, 0, 0)).withBlockedAlpha();

    public PhaseESP() {
        super("PhaseESP",
                "lang.module.PhaseESP",
                KeyboardUtils.RELEASE,
                Module.MCategory.RENDER,
                false
        );
        initSettings(
                outline,
                fill,

                goodColor,
                normalColor,
                dangerColor
        );

    }

    private final List<Block> normalBlocks = Arrays.asList(Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN);

    List<RenderBox> boxes = new CopyOnWriteArrayList<>();
    final List<Vec3i> phaseBlocksVectors = Arrays.asList(
            new Vec3i(1, 0, 0),
            new Vec3i(0, 0, 1),
            new Vec3i(1, 0, 1),
            new Vec3i(-1, 0, 0),
            new Vec3i(0, 0, -1),
            new Vec3i(-1, 0, -1),
            new Vec3i(1, 0, -1),
            new Vec3i(-1, 0, 1)
    );

    @EventSubscriber
    public void onRender (RenderWorldLastEvent e){
        List<RenderBox> renderBoxes = new CopyOnWriteArrayList<>();
        if (nullCheck() || mc.player == null ||!mc.player.isOnGround()) return;
        for (Vec3i vec : phaseBlocksVectors) {
            BlockPos blockPos = BlockPos.ofFloored(mc.player.getX() + vec.getX(), mc.player.getY(), mc.player.getZ() + vec.getZ());
            BlockPos blockPosy = BlockPos.ofFloored(mc.player.getX() + vec.getX(), mc.player.getY() - 1, mc.player.getZ() + vec.getZ());
            Box box = BlockUtils.createBox(blockPos, 0.5, 0.5, 0.03, false);
            if (mc.world.isAir(blockPos)) continue;
            if (isGood(blockPos, blockPosy)) {
                renderBoxes.add(new RenderBox(
                        box,
                        goodColor.getValue().getRed() / 255f,
                        goodColor.getValue().getGreen() / 255f,
                        goodColor.getValue().getBlue() / 255f,
                        outline.getValue() ? 0.6f : 0,
                        goodColor.getValue().getRed() / 255f,
                        goodColor.getValue().getGreen() / 255f,
                        goodColor.getValue().getBlue() / 255f,
                        fill.getValue() ? 0.3f : 0
                ));
            } else
            if (isNormal(blockPos, blockPosy)) {
                renderBoxes.add(new RenderBox(
                        box,
                        normalColor.getValue().getRed() / 255f,
                        normalColor.getValue().getGreen() / 255f,
                        normalColor.getValue().getBlue() / 255f,
                        outline.getValue() ? 0.6f : 0,
                        normalColor.getValue().getRed() / 255f,
                        normalColor.getValue().getGreen() / 255f,
                        normalColor.getValue().getBlue() / 255f,
                        fill.getValue() ? 0.3f : 0
                ));
            } else {
                renderBoxes.add(new RenderBox(
                        box,
                        dangerColor.getValue().getRed() / 255f,
                        dangerColor.getValue().getGreen() / 255f,
                        dangerColor.getValue().getBlue() / 255f,
                        outline.getValue() ? 0.6f : 0,
                        dangerColor.getValue().getRed() / 255f,
                        dangerColor.getValue().getGreen() / 255f,
                        dangerColor.getValue().getBlue() / 255f,
                        fill.getValue() ? 0.3f : 0
                ));
            }
        }
        boxes = renderBoxes;
        BThackRender.boxRender.prepareBoxRender();
        BThackRender.boxRender.renderBoxes(new ArrayList<>(boxes));
        BThackRender.boxRender.stopBoxRender();
    }

    private boolean isGood(BlockPos pos, BlockPos downPos) {
        return !BlockUtils.canBreak(pos) && !BlockUtils.canBreak(downPos) && !mc.world.isAir(downPos);
    }

    private boolean isNormal(BlockPos pos, BlockPos downPos) {
        return (!BlockUtils.canBreak(pos) || normalBlocks.contains(mc.world.getBlockState(pos).getBlock())) && (!BlockUtils.canBreak(downPos) || normalBlocks.contains(mc.world.getBlockState(downPos).getBlock())) && !mc.world.isAir(downPos);
    }
}
