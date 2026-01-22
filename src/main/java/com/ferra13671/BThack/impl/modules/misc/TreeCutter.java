package com.ferra13671.BThack.impl.modules.misc;

import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.core.client.ModuleList;
import com.ferra13671.BThack.events.block.AttackBlockEvent;
import com.ferra13671.BThack.managers.impl.Break.BreakManager;
import com.ferra13671.BThack.managers.impl.setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ModuleInfo(name = "TreeCutter", description = "lang.module.TreeCutter", category = "MISC")
public class TreeCutter extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", this, Arrays.asList("Only Up", "Only Down", "Both")).defaultValue("Both");

    @Override
    @SuppressWarnings("DataFlowIssue")
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        if (!ModuleList.packetMine.conveyorMode.getValue()) {
            sendNotification(Formatting.YELLOW + LanguageSystem.translate("lang.module.TreeCutter.ConveyorNotEnabled"));
            mc.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP);
            setEnabled(false);
            return;
        }

        super.onEnable();
        ModuleList.packetMine.setEnabled(true);
    }

    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onAttackBlock(AttackBlockEvent e) {
        if (nullCheck() || BreakManager.isDestroying) return;
        if (e.getBlockPos() != null) {
            if (!mc.world.getBlockState(e.getBlockPos()).isIn(BlockTags.LOGS)) return;
            for (BlockPos pos : getBlockPoses(e.getBlockPos())) {
                if (ModuleList.packetMine.currentBreakingBlock.blockPos.equals(pos)) return;
                if (ModuleList.packetMine.conveyorContains(pos)) return;
                ModuleList.packetMine.updateBlock(pos);
            }
        }
    }

    private List<BlockPos> getBlockPoses(BlockPos startPos) {
        List<BlockPos> poses = new ArrayList<>();
        switch (mode.getValue()) {
            case "Only Up" -> poses.addAll(getBlockPosesInternal(startPos, 1));
            case "Only Down" -> poses.addAll(getBlockPosesInternal(startPos, -1));
            case "Both" -> {
                poses.addAll(getBlockPosesInternal(startPos, 1));
                poses.addAll(getBlockPosesInternal(startPos, -1));
            }
        }
        return poses;
    }

    @SuppressWarnings("DataFlowIssue")
    private List<BlockPos> getBlockPosesInternal(BlockPos startPos, int moveDelta) {
        List<BlockPos> result = new ArrayList<>();
        int step = moveDelta;
        while (mc.world.getBlockState(startPos.add(0, step, 0)).isIn(BlockTags.LOGS)) {
            result.add(startPos.add(0, step, 0));
            step += moveDelta;
        }
        return result;
    }
}
