package com.ferra13671.BThack.api.Managers.managers.Destroy;


import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Interfaces.Pc;
import com.ferra13671.BThack.api.Utils.Initializable;
import com.ferra13671.BThack.api.Utils.Rotate.RotateUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class DestroyManager implements Initializable, Mc, Pc {
    public static boolean isDestroying = false;
    public static BlockPos currentBlockPos;

    private boolean isInteractDestroying = false;

    @Override
    public void init() {
        BThack.EVENT_BUS.register(this);
        BThack.debug("Destroy Manager inited!");
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (mc.player == null || mc.world == null || mc.isPaused()) return;

        if (currentBlockPos == null) {
            isInteractDestroying = false;
            return;
        }


        float[] rots = RotateUtils.rotations(currentBlockPos);

        try {
            if (!pc.isBreakingBlock() && !isInteractDestroying) {
                if (pc.startBlockBreaking(currentBlockPos, RotateUtils.getInvertedFacing(rots[0], rots[1], true))) {
                    mc.player.swingHand(Hand.MAIN_HAND);
                }
                isInteractDestroying = true;
            } else {
                if (pc.updateBlockBreaking(currentBlockPos, RotateUtils.getInvertedFacing(rots[0], rots[1], true))) {
                    mc.player.swingHand(Hand.MAIN_HAND);
                }
            }
        } catch (Exception ignored) {}
    }
}
