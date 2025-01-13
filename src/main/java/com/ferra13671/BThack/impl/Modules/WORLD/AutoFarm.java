package com.ferra13671.BThack.impl.Modules.WORLD;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.IMixin.ModifyClientPlayerInteractionManager;
import com.ferra13671.BThack.api.Managers.Build.BuildManager;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.*;
import com.ferra13671.BThack.api.Utils.Grim.GrimUtils;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Arrays;
import java.util.HashMap;

public class AutoFarm extends Module {

    public final BooleanSetting rotate = new BooleanSetting("Rotate", this, false);
    public final ModeSetting rotateMode = new ModeSetting("Rotate Mode", this, Arrays.asList("Grim", "Packet"), rotate::getValue);

    public final ModeSetting swap = new ModeSetting("Swap", this, Arrays.asList("Packet", "Client"));

    public AutoFarm() {
        super("AutoFarm",
                "lang.module.AutoFarm",
                KeyboardUtils.RELEASE,
                MCategory.WORLD,
                false
        );

        initSettings(
                rotate,
                rotateMode,

                swap
        );
    }

    private HashMap<CropBlock, BlockPos> crops = new HashMap<>();

    @Override
    public void onEnable() {
        super.onEnable();
        crops.clear();
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        filterAction();
        generalAction();
    }

    public void filterAction() {
        HashMap<CropBlock, BlockPos> temp = new HashMap<>();
        for (BlockPos pos : BlockUtils.getSphere(mc.player.getBlockPos(), 4, 4, false, true, 0)) {
            BlockState state = mc.world.getBlockState(pos);
            if (state.getBlock() instanceof CropBlock block) {
                if (block.getAge(state) >= block.getMaxAge()) temp.put(block, pos);
            }
        }
        crops = temp;
    }

    public void generalAction() {
        crops.forEach((crop, pos) -> {
            float[] rotations = AimBotUtils.rotations(pos.toCenterPos());
            GrimUtils.sendPreActionGrimPackets(rotations[0], rotations[1]);
            ((ModifyClientPlayerInteractionManager) mc.interactionManager).attackBlockNoEvent(pos, Direction.UP);
            mc.world.breakBlock(pos, false);

            Item seedItem = crop.getPickStack(mc.world, pos, mc.world.getBlockState(pos)).getItem();
            BlockPos tempPos = pos.add(0, -1, 0);
            int slot = InventoryUtils.findItem(seedItem);
            if (mc.player.getInventory().getStack(mc.player.getInventory().selectedSlot).getItem() == seedItem) slot = mc.player.getInventory().selectedSlot;
            if (slot != -1) {
                int oldSlot = mc.player.getInventory().selectedSlot;
                if (oldSlot != slot)
                    InventoryUtils.swapAction(oldSlot, slot, false, "Packet");
                rotations = AimBotUtils.rotations(tempPos.toCenterPos());
                GrimUtils.sendPreActionGrimPackets(rotations[0], rotations[1]);
                ItemUtils.useItemOnBlock(BuildManager.getHitResult(tempPos, false, Direction.UP));
                GrimUtils.sendPostActionGrimPackets();
                if (oldSlot != slot)
                    InventoryUtils.swapAction(oldSlot, slot, true, "Packet");
            }
        });
    }

    public void rotatePre(float[] rots) {
        if (rotate.getValue()) {
            switch (rotateMode.getValue()) {
                case "Grim" -> GrimUtils.sendPreActionGrimPackets(rots[0], rots[1]);
                case "Packet" -> AimBotUtils.packetRotate(rots[0], rots[1]);
            }
        }
    }

    public void rotatePost() {
        if (rotate.getValue())
            if (rotateMode.getValue().equals("Grim")) GrimUtils.sendPostActionGrimPackets();
    }
}
