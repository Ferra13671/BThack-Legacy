package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.Block.AttackBlockEvent;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.*;
import com.ferra13671.BThack.impl.Modules.PLAYER.AutoTool;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.BlockState;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SuperInstaMine extends Module {

    public final BooleanSetting packetSwitch = new BooleanSetting("Packet Switch", this, true);
    public final BooleanSetting postSwitch = new BooleanSetting("Post Switch", this, true, packetSwitch::getValue);

    public final NumberSetting length = new NumberSetting("Length", this, 1, 1, 3, true);
    public final NumberSetting extraHeight = new NumberSetting("Extra Height", this, 0, 0, 2, true);
    public final NumberSetting extraWidth = new NumberSetting("Extra Width", this, 0, 0, 2, true);

    public final BooleanSetting sequence = new BooleanSetting("Sequence", this, true);

    public final BooleanSetting infinityAttempts = new BooleanSetting("Infinity Attempts", this, false);
    public final NumberSetting maxAttempts = new NumberSetting("Max Attempts", this, 7, 1, 25, true, () -> !infinityAttempts.getValue());

    public SuperInstaMine() {
        super("SuperInstaMine",
                "lang.module.SuperInstaMine",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        initSettings(
                packetSwitch,
                postSwitch,

                length,
                extraHeight,
                extraWidth,

                sequence,

                infinityAttempts,
                maxAttempts
        );
    }
    //Screw it, I'll just calculate the positions by processing them through a matrix.
    //TODO: Well, I need to do something similar in HighwayBuilder.
    private Matrix4f matrix4f;
    private final HashMap<BlockState, ArrayList<BlockInfo>> poses = new HashMap<>();

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
        if (e.getBlockPos() == null || !BlockUtils.canBreak(e.getBlockPos())) return;

        BlockPos pos = e.getBlockPos();
        matrix4f = new Matrix4f();
        switch (e.getDirection()) {
            case DOWN -> matrix4f.rotate((float) Math.toRadians(90), -1, 0, 0);
            case UP -> matrix4f.rotate((float) Math.toRadians(90), 1, 0, 0);
            case NORTH -> {
                //nothing
            }
            case SOUTH -> matrix4f.rotate((float) Math.toRadians(180), 0, 1, 0);
            case WEST -> matrix4f.rotate((float) Math.toRadians(90), 0, 1, 0);
            case EAST -> matrix4f.rotate((float) Math.toRadians(90), 0, -1, 0);
        }
        addBlocks(pos, (int) length.getValue(), (int) extraWidth.getValue(), (int) extraHeight.getValue());
    }

    public void mineAction() {
        List<BlockState> removeKeys = new ArrayList<>();
        poses.forEach((block, list) -> {
            if (list.isEmpty()) { //Clearing empty slots.
                removeKeys.add(block);
                return;
            }
            //Removing positions if they are too far away or if a block within it cannot be broken
            list.removeIf(bInfo -> MathUtils.getDistance(mc.player.getPos(), bInfo.pos.toCenterPos()) > 7 || !BlockUtils.canBreak(bInfo.pos));
            int slot = AutoTool.getBestSlot(block, 9);
            if (slot != -1) {
                final int oldSlot = mc.player.getInventory().selectedSlot;
                if (checkSlots(oldSlot, slot)) {
                    if (packetSwitch.getValue()) {
                        if (oldSlot != slot)
                            Managers.NETWORK_MANAGER.sendPacket(new UpdateSelectedSlotC2SPacket(slot));
                    }
                    for (BlockInfo bInfo : list) {
                        sendPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, bInfo.pos);
                        sendPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, bInfo.pos);
                        bInfo.attempts++;
                    }
                    //Deleting positions if the maximum number of attempts to break them has been made
                    if (!infinityAttempts.getValue())
                        list.removeIf(bInfo -> bInfo.attempts >= (int) maxAttempts.getValue());
                    
                    if (packetSwitch.getValue() && postSwitch.getValue() && oldSlot != slot)
                        Managers.NETWORK_MANAGER.sendPacket(new UpdateSelectedSlotC2SPacket(oldSlot));
                }
            } else
                removeKeys.add(block);
        });
        removeKeys.forEach(poses::remove);
    }

    public void sendPacket(PlayerActionC2SPacket.Action action, BlockPos pos) {
        if (sequence.getValue())
            Managers.NETWORK_MANAGER.sendSequencePacket(id -> new PlayerActionC2SPacket(action, pos, RotateUtils.getInvertedFacingEntity(mc.player), id));
        else
            Managers.NETWORK_MANAGER.sendPacket(new PlayerActionC2SPacket(action, pos, RotateUtils.getInvertedFacingEntity(mc.player)));
    }

    public void addBlocks(BlockPos pos, int length, int extraWidth, int extraHeight) {
        poses.clear();

        for (int l = 0; l < length; l++) {
            for (int w = -1 - extraWidth; w < 2 + extraWidth; w++) {
                for (int h = -1 - extraHeight; h < 2 + extraHeight; h++) {
                    Vec3d vec = MathUtils.transformPos(matrix4f, w, h, l);
                    addBlock(pos.add((int) Math.round(vec.getX()), (int) Math.round(vec.getY()), (int) Math.round(vec.getZ())));
                }
            }
        }
    }

    public void addBlock(BlockPos pos) {
        BlockState block = mc.world.getBlockState(pos);
        if (poses.containsKey(block)) {
            if (!poses.get(block).contains(new BlockInfo(pos)))
                poses.get(block).add(new BlockInfo(pos));
        } else
            poses.put(block, new ArrayList<>(List.of(new BlockInfo(pos))));
    }

    public boolean checkSlots(int oldSlot, int slot) {
        return packetSwitch.getValue() || oldSlot == slot;
    }

    private static class BlockInfo {
        private final BlockPos pos;
        private int attempts = 0;

        private BlockInfo(BlockPos pos) {
            this.pos = pos;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof BlockInfo blockInfo)
                return this.pos.equals(blockInfo.pos);
            else return false;
        }
    }
}
