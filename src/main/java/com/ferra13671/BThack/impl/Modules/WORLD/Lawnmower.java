package com.ferra13671.BThack.impl.Modules.WORLD;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.IMixin.ModifyClientPlayerInteractionManager;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.MathUtils;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.BThack.api.Utils.Grim.GrimUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Comparator;

public class Lawnmower extends Module {

    public final NumberSetting range = new NumberSetting("Range", this, 4.0,1,7,false);
    public final BooleanSetting flowers = new BooleanSetting("Flowers", this, true);
    public final BooleanSetting ignoreWalls = new BooleanSetting("Ignore Walls", this, false);

    public Lawnmower() {
        super("Lawnmower",
                "lang.module.Lawnmower",
                KeyboardUtils.RELEASE,
                MCategory.WORLD,
                false
        );

        initSettings(
                range,
                flowers,
                ignoreWalls
        );
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        arrayListInfo = "" + range.getValue();

        BlockPos positions = BlockUtils.getSphere(mc.player.getBlockPos(), (float) range.getValue(), (float) range.getValue(), false, true, 0).stream()
                .filter(this::isValidBlockPos)
                .min(Comparator.comparing(pos -> MathUtils.getDistance(mc.player.getPos(), pos.toCenterPos())))
                .orElse(null);

        if (positions != null) {
            float[] rotations = AimBotUtils.rotations(positions.toCenterPos());

            GrimUtils.sendPreActionGrimPackets(rotations[0], rotations[1]);
            ((ModifyClientPlayerInteractionManager) mc.interactionManager).attackBlockNoEvent(positions, Direction.UP);
            GrimUtils.sendPostActionGrimPackets();
        }
    }

    private boolean isValidBlockPos(BlockPos pos) {
        BlockState state = mc.world.getBlockState(pos);

        if (state.getBlock() instanceof TallPlantBlock || state.getBlock() instanceof ShortPlantBlock)
            return true;

        return flowers.getValue() && state.getBlock() instanceof FlowerBlock && isVisible(pos);
    }

    private boolean isVisible(BlockPos pos) {
        if (ignoreWalls.getValue())
            return true;

        return BlockUtils.hasLineOfSight(pos.toCenterPos());
    }
}
