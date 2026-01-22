package com.ferra13671.BThack.managers.impl.Break;


import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.api.utils.Mc;
import com.ferra13671.BThack.api.utils.Initializable;
import com.ferra13671.BThack.api.utils.rotate.RotateUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class BreakManager implements Initializable, Mc {
    public static boolean isDestroying = false;
    public static BlockPos currentBlockPos;

    private boolean isInteractDestroying = false;

    @Override
    public void init() {
        BThack.EVENT_BUS.register(this);
        BThack.debug("Destroy Manager inited!");
    }

    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onTick(ClientTickEvent e) {
        if (mc.player == null || mc.world == null || mc.isPaused()) return;

        if (currentBlockPos == null) {
            isInteractDestroying = false;
            return;
        }

        float[] rots = RotateUtils.rotations(currentBlockPos);

        try {
            if (!mc.interactionManager.isBreakingBlock() && !isInteractDestroying) {
                if (mc.interactionManager.attackBlock(currentBlockPos, RotateUtils.getInvertedFacing(rots[0], rots[1], true)))
                    mc.player.swingHand(Hand.MAIN_HAND);
                isInteractDestroying = true;
            } else
                if (mc.interactionManager.updateBlockBreakingProgress(currentBlockPos, RotateUtils.getInvertedFacing(rots[0], rots[1], true)))
                    mc.player.swingHand(Hand.MAIN_HAND);
        } catch (Exception ignored) {}
    }
}
