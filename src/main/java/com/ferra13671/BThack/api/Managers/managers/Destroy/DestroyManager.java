package com.ferra13671.BThack.api.Managers.managers.Destroy;


import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Interfaces.Pc;
import com.ferra13671.BThack.api.Managers.Manager;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class DestroyManager implements Manager, Mc, Pc {
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


        float[] rots = AimBotUtils.rotations(currentBlockPos);

        try {
            if (!pc.isBreakingBlock() && !isInteractDestroying) {
                if (pc.startBlockBreaking(currentBlockPos, AimBotUtils.getInvertedFacing(rots[0], rots[1], true))) {
                    mc.player.swingHand(Hand.MAIN_HAND);
                }
                isInteractDestroying = true;
            } else {
                if (pc.updateBlockBreaking(currentBlockPos, AimBotUtils.getInvertedFacing(rots[0], rots[1], true))) {
                    mc.player.swingHand(Hand.MAIN_HAND);
                }
            }
        } catch (Exception ignored) {}
    }

    public static void delay(long milliseconds, Thread thread) {
        try {
            thread.sleep(milliseconds);
        } catch (InterruptedException ignored) {}
    }
}
