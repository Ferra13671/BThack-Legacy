package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.Block.AttackBlockEvent;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.MathUtils;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.BThack.impl.Modules.PLAYER.AutoTool;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SuperInstaMine extends Module {

    public static BooleanSetting packetSwitch;
    public static BooleanSetting postSwitch;

    public static NumberSetting length;
    public static NumberSetting extraHeight;
    public static NumberSetting extraWidth;

    public static BooleanSetting sequence;

    public SuperInstaMine() {
        super("SuperInstaMine",
                "lang.module.SuperInstaMine",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        packetSwitch = new BooleanSetting("Packet Switch", this, true);
        postSwitch = new BooleanSetting("Post Switch", this, true, packetSwitch::getValue);

        length = new NumberSetting("Length", this, 1, 1, 3, true);
        extraHeight = new NumberSetting("Extra Height", this, 0, 0, 2, true);
        extraWidth = new NumberSetting("Extra Width", this, 0, 0, 2, true);

        sequence = new BooleanSetting("Sequence", this, true);

        initSettings(
                packetSwitch,
                postSwitch,

                length,
                extraHeight,
                extraWidth,

                sequence
        );
    }

    private final HashMap<BlockState, ArrayList<BlockPos>> poses = new HashMap<>();

    @Override
    public void onEnable() {
        super.onEnable();
        ModuleList.packetMine.setToggled(false);
        poses.clear();
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        mineAction();
    }

    @EventSubscriber
    public void onAttack(AttackBlockEvent e) {
        if (e.getBlockPos() == null || mc.world.isAir(e.getBlockPos()) || mc.world.getBlockState(e.getBlockPos()).getBlock() instanceof FluidBlock) return;

        BlockPos pos = e.getBlockPos();
        addBlocks(pos, (int) length.getValue(), (int) extraWidth.getValue(), (int) extraHeight.getValue());
    }

    public void mineAction() {
        poses.forEach((block, list) -> {
            int slot = AutoTool.getBestSlot(block, 9);
            if (slot != -1) {
                final int oldSlot = mc.player.getInventory().selectedSlot;
                if (checkSlots(oldSlot, slot)) {
                    if (packetSwitch.getValue()) {
                        if (oldSlot != slot)
                            Managers.NETWORK_MANAGER.sendPacket(new UpdateSelectedSlotC2SPacket(slot));
                    }
                    for (BlockPos pos : list) {
                        if (MathUtils.getDistance(mc.player.getPos(), pos.toCenterPos()) > 7) continue;
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
            Managers.NETWORK_MANAGER.sendSequencePacket(id -> new PlayerActionC2SPacket(action, pos, AimBotUtils.getInvertedFacingEntity(mc.player), id));
        else
            Managers.NETWORK_MANAGER.sendPacket(new PlayerActionC2SPacket(action, pos, AimBotUtils.getInvertedFacingEntity(mc.player)));
    }

    public void addBlocks(BlockPos pos, int length, int extraWidth, int extraHeight) {
        poses.clear();
        addBlock(pos);
        for (int i = 0; i < length + 1; i++) {
            switch (AimBotUtils.getFacing(mc.player)) {
                case WEST, EAST -> {
                    for (int i2 = -1 - extraHeight; i2 < 1 + extraHeight + 1; i2++) {
                        for (int i3 = -1 - extraWidth; i3 < 1 + extraWidth + 1; i3++) {
                            addBlock(pos.add(i, i2, i3));
                        }
                    }
                }
                case NORTH, SOUTH -> {
                    for (int i2 = -1 - extraHeight; i2 < 1 + extraHeight + 1; i2++) {
                        for (int i3 = -1 - extraWidth; i3 < 1 + extraWidth + 1; i3++) {
                            addBlock(pos.add(i3, i2, i));
                        }
                    }
                }
                case UP, DOWN -> {
                    for (int i2 = -1 - extraHeight; i2 < 1 + extraHeight + 1; i2++) {
                        for (int i3 = -1 - extraWidth; i3 < 1 + extraWidth + 1; i3++) {
                            addBlock(pos.add(i2, i, i3));
                        }
                    }
                }
            }
        }
    }

    public void addBlock(BlockPos pos) {
        BlockState block = mc.world.getBlockState(pos);
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

    public boolean checkSlots(int oldSlot, int slot) {
        if (!packetSwitch.getValue()) {
            return oldSlot == slot;
        } else {
            return true;
        }
    }
}
