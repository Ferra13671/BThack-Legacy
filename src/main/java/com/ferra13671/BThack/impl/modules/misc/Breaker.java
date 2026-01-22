package com.ferra13671.BThack.impl.modules.misc;

import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.managers.impl.Break.BreakManager;
import com.ferra13671.BThack.managers.impl.Break.BreakThread3D;
import com.ferra13671.BThack.managers.impl.setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.api.utils.BlockUtils;
import com.ferra13671.BThack.api.utils.datalist.BlockList;
import com.ferra13671.BThack.api.utils.datalist.DataLists;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ModuleInfo(name = "Breaker", description = "lang.module.Breaker", category = "MISC")
public class Breaker extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", this, Arrays.asList("WhiteList", "BlackList"));


    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onTick(ClientTickEvent e) {
        if (nullCheck() || BreakManager.isDestroying) return;

        List<BlockPos> blockPoses = BlockUtils.getSphere(mc.player.getBlockPos(), 4, 4, false, true, 0).stream().filter(this::check)
                .toList();
        List<Vec3i> schematic = new ArrayList<>(blockPoses);

        BreakThread3D destroyThread3D = new BreakThread3D();
        destroyThread3D.set3DSchematic(schematic, BlockPos.ORIGIN);
        destroyThread3D.start();
    }

    @SuppressWarnings("DataFlowIssue")
    public boolean check(BlockPos pos) {
        boolean value = DataLists.get("Breaker", BlockList.class).values.contains(mc.world.getBlockState(pos).getBlock());
        return mode.getValue().equals("WhiteList") == value;
    }
}
