package com.ferra13671.BThack.impl.Modules.Misc;

import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Utils.*;
import com.ferra13671.BThack.api.Utils.Rotate.RotateUtils;
import com.ferra13671.BThack.impl.Modules.Player.AutoTool;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ModuleInfo(name = "InstaNuker", description = "lang.module.InstaNuker", category = "MISC")
public class InstaNuker extends Module {

    public final NumberSetting range = new NumberSetting("Range", this, 4, 1, 15, false);
    public final NumberSetting tickDelay = new NumberSetting("Tick Delay", this, 2, 0, 5, true);

    public final BooleanSetting packetSwitch = new BooleanSetting("Packet Switch", this, true);
    public final BooleanSetting postSwitch = new BooleanSetting("Post Switch", this, true, packetSwitch::getValue);

    public final BooleanSetting sequence = new BooleanSetting("Sequence", this, true);
    public final BooleanSetting pauseIfJump = new BooleanSetting("Pause If Jump", this, true);


    private final HashMap<Block, ArrayList<BlockPos>> poses = new HashMap<>();
    private final Ticker ticker = new Ticker();

    @Override
    public void onEnable() {
        super.onEnable();
        ticker.reset();
    }

    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;
        if (pauseIfJump.getValue())
            if (!mc.player.verticalCollision) return;

        if (ticker.passed(tickDelay.getValue() * 50)) {

            filterAction();
            instaBreakAction();

            ticker.reset();
        }
    }

    @SuppressWarnings("DataFlowIssue")
    public void filterAction() {
        for (BlockPos pos : BlockUtils.getSphere(mc.player.getBlockPos(), range.getValue().floatValue(), range.getValue().floatValue(), false, true, 0)) {
            if (pos.getY() >= (int) mc.player.getY()) {
                Block _block = mc.world.getBlockState(pos).getBlock();
                if (BlockUtils.canBreak(pos) || _block != Blocks.OBSIDIAN || _block != Blocks.CRYING_OBSIDIAN) {
                    Block block = mc.world.getBlockState(pos).getBlock();
                    if (!poses.containsKey(block)) {
                        poses.put(block, new ArrayList<>(List.of(pos)));
                        return;
                    }

                    boolean add = true;

                    for (BlockPos pos2 : poses.get(block)) {
                        if (pos2.equals(pos)) {
                            add = false;
                            break;
                        }
                    }
                    if (add)
                        poses.get(block).add(pos);
                }
            }
        }
    }

    @SuppressWarnings("DataFlowIssue")
    public void instaBreakAction() {
        poses.forEach((block, list) -> {
            int slot = AutoTool.getBestSlot(mc.world.getBlockState(list.getFirst()), 9);
            if (slot != -1) {
                final int oldSlot = mc.player.getInventory().selectedSlot;
                if (checkSlots(oldSlot, slot)) {
                    if (packetSwitch.getValue() && oldSlot != slot)
                            Managers.NETWORK_MANAGER.sendPacket(new UpdateSelectedSlotC2SPacket(slot));
                    for (BlockPos pos : list) {
                        if (MathUtils.getDistance(mc.player.getPos(), pos.toCenterPos()) > range.getValue()) continue;
                        if (!BlockUtils.canBreak(pos)) continue;
                        sendPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, pos);
                        sendPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, pos);
                    }
                    if (packetSwitch.getValue() && postSwitch.getValue() && oldSlot != slot)
                        Managers.NETWORK_MANAGER.sendPacket(new UpdateSelectedSlotC2SPacket(oldSlot));
                }
            }
        });
    }

    public void sendPacket(PlayerActionC2SPacket.Action action, BlockPos pos) {
        if (sequence.getValue())
            Managers.NETWORK_MANAGER.sendSequencePacket(id -> new PlayerActionC2SPacket(action, pos, RotateUtils.getInvertedFacingEntity(mc.player), id));
        else
            Managers.NETWORK_MANAGER.sendPacket(new PlayerActionC2SPacket(action, pos, RotateUtils.getInvertedFacingEntity(mc.player)));
    }

    public boolean checkSlots(int oldSlot, int slot) {
        if (!packetSwitch.getValue()) {
            return oldSlot == slot;
        } else {
            return true;
        }
    }
}
