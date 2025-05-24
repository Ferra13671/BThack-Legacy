package com.ferra13671.BThack.impl.Modules.Misc;

import com.ferra13671.BThack.managers.managers.Setting.Settings.*;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.core.Render.Box.RenderBox;
import com.ferra13671.BThack.api.Animation.Animation;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.events.Render.RenderWorldLastEvent;
import com.ferra13671.BThack.api.IMixin.ModifyClientPlayerInteractionManager;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.*;
import com.ferra13671.BThack.api.Utils.Rotate.RotateMode;
import com.ferra13671.BThack.api.Utils.Rotate.RotateUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.awt.*;
import java.util.*;
import java.util.List;

@ModuleInfo(name = "Lawnmower", description = "lang.module.Lawnmower", category = "MISC")
public class Lawnmower extends Module {

    public final NumberSetting range = new NumberSetting("Range", this, 4.0,1,7,false);
    public final ModeSetting rotateMode = new ModeSetting("Rotate", this, Arrays.asList("None", "Packet", "Grim")).defaultValue("Grim");
    public final BooleanSetting flowers = new BooleanSetting("Flowers", this, true);
    public final BooleanSetting ignoreWalls = new BooleanSetting("Ignore Walls", this, false);

    public final CategorySetting renderCategory = new CategorySetting("Render", this);
    public final BooleanSetting render = new BooleanSetting("Render", this, true).inCategory(renderCategory);
    public final ColorSetting color = new ColorSetting("Color", this, new Color(213, 142, 253), render::getValue).withBlockedAlpha().inCategory(renderCategory);
    public final NumberSetting boxAlpha = new NumberSetting("Box Alpha", this, 76, 0, 255, true, render::getValue).inCategory(renderCategory);
    public final NumberSetting linesAlpha = new NumberSetting("Lines Alpha", this, 255, 0, 255, true, render::getValue).inCategory(renderCategory);
    public final NumberSetting hideTime = new NumberSetting("Hide Time", this, 500, 100, 2000, true, render::getValue).inCategory(renderCategory);


    private final Map<BlockPos, Animation> breakedBoxes = new HashMap<>();

    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        arrayListInfo = "" + range.getValue();

        BlockPos pos = BlockUtils.getSphere(mc.player.getBlockPos(), range.getValue().floatValue(), range.getValue().floatValue(), false, true, 0).stream()
                .filter(this::isValidBlockPos)
                .min(Comparator.comparing(pos2 -> MathUtils.getDistance(mc.player.getPos(), pos2.toCenterPos())))
                .orElse(null);

        if (pos != null) {
            float[] rotations = RotateUtils.rotations(pos.toCenterPos());

            RotateMode rm = RotateMode.valueOf(rotateMode.getValue().toUpperCase());
            rm.preRotate(rotations[0], rotations[1]);
            ((ModifyClientPlayerInteractionManager) mc.interactionManager).attackBlockNoEvent(pos, Direction.UP);
            if (!breakedBoxes.containsKey(pos)) breakedBoxes.put(pos, new Animation(Easing.LINEAR, hideTime.getValue().intValue()));
            rm.postRotate();
        }

        removeFinishedBoxes();
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onWorldRender(RenderWorldLastEvent e) {
        if (!render.getValue() || nullCheck() || breakedBoxes.isEmpty()) return;

        List<RenderBox> renderBoxes = new ArrayList<>();
        breakedBoxes.forEach((pos, animation) ->
                renderBoxes.add(new RenderBox(
                        BlockUtils.createBox(pos, 0.5, 0.5, 0.5, true),
                        (float) color.getValue().getRed() / 255f,
                        (float) color.getValue().getGreen() / 255f,
                        (float) color.getValue().getBlue() / 255f,
                        (float) ((linesAlpha.getValue() / 255f) * (1 - animation.getEase())),
                        (float) color.getValue().getRed() / 255f,
                        (float) color.getValue().getGreen() / 255f,
                        (float) color.getValue().getBlue() / 255f,
                        (float) ((boxAlpha.getValue() / 255f) * (1 - animation.getEase()))
                ))
        );
        BThackRender.boxRender.prepareBoxRender();
        BThackRender.boxRender.renderBoxes(renderBoxes);
        BThackRender.boxRender.stopBoxRender();
    }

    private void removeFinishedBoxes() {
        if (!breakedBoxes.isEmpty()) {
            List<BlockPos> removePoses = new ArrayList<>();
            breakedBoxes.forEach((pos, animation) -> {
                if (animation.getEase() >= 1) removePoses.add(pos);
            });
            if (!removePoses.isEmpty()) removePoses.forEach(breakedBoxes::remove);
        }
    }

    @SuppressWarnings("DataFlowIssue")
    private boolean isValidBlockPos(BlockPos pos) {
        BlockState state = mc.world.getBlockState(pos);
        return state.getBlock() instanceof TallPlantBlock || state.getBlock() instanceof ShortPlantBlock || (flowers.getValue() && state.getBlock() instanceof FlowerBlock && isVisible(pos));
    }

    private boolean isVisible(BlockPos pos) {
        return ignoreWalls.getValue() || BlockUtils.hasLineOfSight(pos.toCenterPos());
    }
}
