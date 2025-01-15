package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.managers.Destroy.DestroyManager;
import com.ferra13671.BThack.api.Managers.managers.Destroy.DestroyThread3D;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.DataList.BlockList;
import com.ferra13671.BThack.api.Utils.DataList.DataLists;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Breaker extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", this, Arrays.asList("WhiteList", "BlackList"));

    public Breaker() {
        super("Breaker",
                "lang.module.Breaker",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        initSettings(
                mode
        );
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck() || DestroyManager.isDestroying) return;

        List<BlockPos> blockPoses = BlockUtils.getAllInBox(mc.player.getBlockPos(), 4).stream().filter(this::check)
                .toList();
        List<Vec3i> schematic = new ArrayList<>();
        schematic.addAll(blockPoses);

        DestroyThread3D destroyThread3D = new DestroyThread3D();
        destroyThread3D.set3DSchematic(schematic, BlockPos.ORIGIN);
        destroyThread3D.start();
    }

    public boolean check(BlockPos pos) {
        if (mode.getValue().equals("WhiteList")) {
            return DataLists.get("Breaker", BlockList.class).values.contains(mc.world.getBlockState(pos).getBlock());
        } else {
            return !DataLists.get("Breaker", BlockList.class).values.contains(mc.world.getBlockState(pos).getBlock());
        }
    }
}
