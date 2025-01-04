package com.ferra13671.BThack.api.Utils;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.GoalXZ;
import baritone.api.utils.BlockOptionalMetaLookup;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import net.minecraft.block.Block;
import net.minecraft.util.math.ChunkPos;

import java.util.List;

public class BaritoneUtils implements Mc {
    private static final int border = 30000000;


    public static void autoWalkUpdate() {
        ChunkPos chunkPos = mc.player.getChunkPos();
        if (!mc.world.isChunkLoaded(chunkPos.x, chunkPos.z)) return;

        byte[] moveF = AimBotUtils.getCordFactorFromDirection(mc.player);
        int x = (int) mc.player.getX() + moveF[0] * border;
        int z = (int) mc.player.getZ() + moveF[1] * border;

        cancelAllPathing();
        BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new GoalXZ(x, z));
    }

    public static void mine(List<Block> blocks) {
        BaritoneAPI.getProvider().getPrimaryBaritone().getMineProcess().mine(new BlockOptionalMetaLookup(blocks));
    }

    public static void cancelAllPathing() {
        BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().cancelEverything();
    }

    public static void cancelMine() {
        BaritoneAPI.getProvider().getPrimaryBaritone().getMineProcess().cancel();
    }

    public static boolean isActive() {
        return BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().isActive();
    }

    public static boolean isPathing() {
        return BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().isPathing();
    }
}
