package com.ferra13671.BThack.impl.Modules.WORLD;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.managers.Build.BuildManager;
import com.ferra13671.BThack.api.Managers.managers.Build.BuildThread3D;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.Grim.GrimUtils;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LiquidFiller extends Module {

    public final NumberSetting range = new NumberSetting("Range", this, 4, 3, 7, false);
    public final NumberSetting delayTicks = new NumberSetting("Delay Ticks", this, 1, 0, 5, true);
    public final BooleanSetting water = new BooleanSetting("Water", this, true);
    public final BooleanSetting lava = new BooleanSetting("Lava", this, true);
    public final BooleanSetting other = new BooleanSetting("Other (Mods)", this, true);

    public final BooleanSetting rotate = new BooleanSetting("Rotate", this, false);
    public final ModeSetting rotateMode = new ModeSetting("Rotate Mode", this, Arrays.asList("Packet", "Grim"), rotate::getValue);
    public final BooleanSetting ignoreWalls = new BooleanSetting("Ignore Walls", this, true);

    public final ModeSetting swap = new ModeSetting("Swap", this, Arrays.asList("Client", "Packet"));
    public final ModeSetting interact = new ModeSetting("Interact", this, Arrays.asList("Client", "Packet"));

    public LiquidFiller() {
        super("LiquidFiller",
                "lang.module.LiquidFiller",
                KeyboardUtils.RELEASE,
                MCategory.WORLD,
                false
        );

        initSettings(
                range,
                delayTicks,
                water,
                lava,
                other,

                rotate,
                rotateMode,
                ignoreWalls,

                swap,
                interact
        );
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck() || BuildManager.isBuilding) return;

        List<Vec3i> sch = new ArrayList<>();

        filterAction(sch);
        interactAction(sch);
    }

    public void filterAction(List<Vec3i> sch) {
        for (BlockPos pos : BlockUtils.getSphere(BlockPos.ofFloored(mc.player.getX(), mc.player.getY(), mc.player.getZ()), (float) range.getValue(), (float) range.getValue(), false, true, 0)) {
            Block block = mc.world.getBlockState(pos).getBlock();

            if (!ignoreWalls.getValue())
                if (!BlockUtils.hasLineOfSight(mc.player.getPos(), pos.toCenterPos())) continue;

            if (block == Blocks.WATER) {
                if (water.getValue()) sch.add(pos);
            } else if (block == Blocks.LAVA) {
                if (lava.getValue()) sch.add(pos);
            } else if (block instanceof FluidBlock) {
                if (other.getValue()) sch.add(pos);
            }
        }
    }

    public void interactAction(List<Vec3i> sch) {
        for (Vec3i pos : sch) {
            int slot = InventoryUtils.findItem(BlockItem.class);
            if (slot == -1) return;
            int oldSlot = mc.player.getInventory().selectedSlot;

            InventoryUtils.swapAction(oldSlot, slot, false, swap.getValue());
            interactActionInternal(new BlockPos(pos));
            InventoryUtils.swapAction(oldSlot, slot, true, swap.getValue());
        }
        BuildThread3D thread3D = new BuildThread3D();
        thread3D.set3DSchematic((int) delayTicks.getValue(), sch, BlockPos.ORIGIN);
        thread3D.start();
    }

    public void interactActionInternal(BlockPos pos) {
        rotatePreAction(pos);
        switch (interact.getValue()) {
            case "Client" -> mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, BuildManager.getHitResult(pos, false, Direction.UP));
            case "Packet" -> Managers.NETWORK_MANAGER.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, BuildManager.getHitResult(pos, false, Direction.UP), 0));
        }
        rotatePostAction();
    }

    public void rotatePreAction(BlockPos pos) {
        if (rotate.getValue()) {
            float[] rots = AimBotUtils.rotations(pos);
            if (rotateMode.getValue().equals("Packet"))
                AimBotUtils.packetRotate(rots[0], rots[1]);
            else
                GrimUtils.sendPreActionGrimPackets(rots[0], rots[1]);
        }
    }

    public void rotatePostAction() {
        if (rotate.getValue())
            if (rotateMode.getValue().equals("Grim"))
                GrimUtils.sendPostActionGrimPackets();
    }
}
