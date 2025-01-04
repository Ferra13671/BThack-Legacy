package com.ferra13671.BTbot.api.Utils.Controller;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.List;

public class ClientPlayerController implements PlayerController {

    @Override
    public boolean breakBlock(BlockPos pos) {
        return mc.interactionManager.breakBlock(pos);
    }

    @Override
    public boolean startBlockBreaking(BlockPos pos, Direction direction) {
        return mc.interactionManager.attackBlock(pos, direction);
    }

    @Override
    public boolean updateBlockBreaking(BlockPos pos, Direction direction) {
        return mc.interactionManager.updateBlockBreakingProgress(pos, direction);
    }

    @Override
    public void cancelBlockBreaking() {
        mc.interactionManager.cancelBlockBreaking();
    }

    @Override
    public void tick() {
        mc.interactionManager.tick();
    }

    @Override
    public void sendPacket(Packet<?> packet) {
        mc.player.networkHandler.sendPacket(packet);
    }

    @Override
    public void onDisconnect(String reason) {
        mc.player.networkHandler.onDisconnect(new DisconnectS2CPacket(Text.of(reason)));
    }

    @Override
    public void syncSelectedSlot() {
        mc.interactionManager.syncSelectedSlot();
    }

    @Override
    public ActionResult interactBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult) {
        return mc.interactionManager.interactBlock(player, hand, hitResult);
    }

    @Override
    public ActionResult interactItem(PlayerEntity player, Hand hand) {
        return mc.interactionManager.interactItem(player, hand);
    }

    @Override
    public void attackEntity(PlayerEntity player, Entity target) {
        mc.interactionManager.attackEntity(player, target);
    }

    @Override
    public ActionResult interactEntity(PlayerEntity player, Entity entity, Hand hand) {
        return mc.interactionManager.interactEntity(player, entity, hand);
    }

    @Override
    public ActionResult interactEntityAtLocation(PlayerEntity player, Entity entity, EntityHitResult hitResult, Hand hand) {
        return mc.interactionManager.interactEntityAtLocation(player, entity, hitResult, hand);
    }

    @Override
    public void clickSlot(int syncId, int slotId, int button, SlotActionType actionType) {
        mc.interactionManager.clickSlot(syncId, slotId, button, actionType, mc.player);
    }

    public void packetClickSlot(int syncId, int slotId, int button, SlotActionType actionType) {
        DefaultedList<Slot> defaultedList = mc.player.currentScreenHandler.slots;
        int i = defaultedList.size();
        List<ItemStack> list = Lists.newArrayListWithCapacity(i);

        for (Slot slot : defaultedList) {
            list.add(slot.getStack().copy());
        }
        Int2ObjectMap<ItemStack> int2ObjectMap = new Int2ObjectOpenHashMap<>();

        for(int j = 0; j < i; ++j) {
            ItemStack itemStack = list.get(j);
            ItemStack itemStack2 = (defaultedList.get(j)).getStack();
            if (!ItemStack.areEqual(itemStack, itemStack2)) {
                int2ObjectMap.put(j, itemStack2.copy());
            }
        }

        mc.player.networkHandler.sendPacket(new ClickSlotC2SPacket(syncId, mc.player.currentScreenHandler.getRevision(), slotId, button, actionType, mc.player.currentScreenHandler.getCursorStack().copy(), int2ObjectMap));
    }

    @Override
    public void stopUsingItem(PlayerEntity player) {
        mc.interactionManager.stopUsingItem(player);
    }

    @Override
    public boolean isBreakingBlock() {
        return mc.interactionManager.isBreakingBlock();
    }

    @Override
    public int getBlockBreakingProgress() {
        return mc.interactionManager.getBlockBreakingProgress();
    }

    @Override
    public void closeScreen() {
        RenderSystem.recordRenderCall(() -> mc.player.closeHandledScreen());
    }
}
