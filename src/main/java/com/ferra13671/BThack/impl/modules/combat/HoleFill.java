package com.ferra13671.BThack.impl.modules.combat;

import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.managers.impl.place.PlaceManager;
import com.ferra13671.BThack.managers.impl.place.PlaceThread3D;
import com.ferra13671.BThack.managers.impl.setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.api.utils.BlockUtils;
import com.ferra13671.BThack.api.utils.HoleUtils;
import com.ferra13671.BThack.api.utils.MathUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@ModuleInfo(name = "HoleFill", description = "lang.module.HoleFill", category = "COMBAT")
public class HoleFill extends Module {

    public final BooleanSetting onlyObsidian = new BooleanSetting("Only Obsidian", this, true);

    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onTick(ClientTickEvent e) {
        if (nullCheck() || PlaceManager.isBuilding) return;

        ArrayList<BlockPos> blockPoses = new ArrayList<>();
        for (PlayerEntity player : mc.world.getPlayers())
            if (HoleUtils.isMutableHole(mc.player.getBlockPos(), false))
                blockPoses.add(player.getBlockPos());

        BlockPos blockPos = BlockUtils.getSphere(mc.player.getBlockPos(), 4, 4, false, true, 0).stream().filter(blockPos1 -> HoleUtils.isMutableHole(blockPos1, false) && !blockPoses.contains(blockPos1))
                .min(Comparator.comparing(blockPos2 -> MathUtils.getDistance(mc.player.getPos(), blockPos2.toCenterPos()))).orElse(null);

        if (blockPos == null) return;

        PlaceThread3D builder3D = new PlaceThread3D();
        builder3D.set3DSchematic(0, new ArrayList<>(List.of(blockPos)), new BlockPos(0,0,0));
        builder3D.setNeedBlocks(onlyObsidian.getValue() ? Arrays.asList(Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN) : new ArrayList<>());
        builder3D.start();
    }
}
