package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.managers.Destroy.DestroyManager;
import com.ferra13671.BThack.api.Managers.managers.Destroy.DestroyThread3D;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.List;

public class Scrapper extends Module {

    public Scrapper() {
        super("Scrapper",
                "lang.module.Scrapper",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );
    }

    private DestroyThread3D thread3D;

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck() || DestroyManager.isDestroying) return;

        List<BlockPos> poses = BlockUtils.getSphere(mc.player.getBlockPos(), 4, 4, false, true, 0).stream().filter(pos -> pos.getY() >= (int) mc.player.getY()).toList();
        ArrayList<Vec3i> sch = new ArrayList<>(poses);

        thread3D = new DestroyThread3D();
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
        if (thread3D != null) {
            thread3D.closeThread();
        }
    }
}
