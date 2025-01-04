package com.ferra13671.BThack.impl.Modules.COMBAT;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Build.BuildManager;
import com.ferra13671.BThack.api.Managers.Build.BuildThread3D;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.HoleUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.MathUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class HoleFill extends Module {

    public final BooleanSetting onlyObsidian = new BooleanSetting("Only Obsidian", this, true);

    public HoleFill() {
        super("HoleFill",
                "lang.module.HoleFill",
                KeyboardUtils.RELEASE,
                MCategory.COMBAT,
                false
        );

        initSettings(
                onlyObsidian
        );
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck() || BuildManager.isBuilding) return;

        ArrayList<BlockPos> blockPoses = new ArrayList<>();
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (HoleUtils.isMutableHole(mc.player.getBlockPos(), false)) {
                blockPoses.add(player.getBlockPos());
            }
        }

        BlockPos blockPos = BlockUtils.getSphere(mc.player.getBlockPos(), 4, 4, false, true, 0).stream().filter(blockPos1 -> HoleUtils.isMutableHole(blockPos1, false) && !blockPoses.contains(blockPos1))
                .min(Comparator.comparing(blockPos2 -> MathUtils.getDistance(mc.player.getPos(), blockPos2.toCenterPos()))).orElse(null);

        if (blockPos == null) return;

        BuildThread3D buildThread3D = new BuildThread3D();
        buildThread3D.set3DSchematic(0, new ArrayList<>(Arrays.asList(blockPos)), new BlockPos(0,0,0));
        buildThread3D.setNeedBlocks(onlyObsidian.getValue() ? Arrays.asList(Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN) : new ArrayList<>());
        buildThread3D.start();
    }
}
