package com.ferra13671.BThack.impl.modules.misc;

import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.managers.impl.Break.BreakManager;
import com.ferra13671.BThack.managers.impl.Break.BreakThread3D;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.api.utils.BlockUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "Scrapper", description = "lang.module.Scrapper", category = "MISC")
public class Scrapper extends Module {

    private BreakThread3D thread3D;

    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onTick(ClientTickEvent e) {
        if (nullCheck() || BreakManager.isDestroying) return;

        List<BlockPos> poses = BlockUtils.getSphere(mc.player.getBlockPos(), 4, 4, false, true, 0).stream().filter(pos -> pos.getY() >= (int) mc.player.getY()).toList();
        ArrayList<Vec3i> sch = new ArrayList<>(poses);

        thread3D = new BreakThread3D();
        thread3D.set3DSchematic(sch, BlockPos.ORIGIN);
        thread3D.start();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        reset();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        reset();
    }

    public void reset() {
        if (thread3D != null)
            thread3D.closeThread();
    }
}
