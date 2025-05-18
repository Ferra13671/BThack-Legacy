package com.ferra13671.BThack.mixins.entity;

import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.MovementType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends AbstractClientPlayerEntity {

    @Shadow @Final protected MinecraftClient client;

    @Shadow public Input input;

    @Shadow protected int ticksLeftToDoubleTapSprint;

    @Unique
    public int tempTicksLeftToDoubleTapSprint;
    @Unique
    public float tempForwardInput;
    @Unique
    public float tempSidewaysInput;

    public MixinClientPlayerEntity(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Override
    protected boolean clipAtLedge() {
        return super.clipAtLedge() || (ModuleList.safeWalk.isEnabled() && !ModuleList.safeWalk.mode.getValue().equals("Legit Shift"));
    }

    @Override
    protected Vec3d adjustMovementForSneaking(Vec3d movement, MovementType type) {
        Vec3d result = super.adjustMovementForSneaking(movement, type);

        if(movement != null) {
            ModuleList.safeWalk.onClipAtLedge(!movement.equals(result));
        }

        return result;
    }

    @ModifyConstant(method = "tickNewAi", constant = @Constant(floatValue = 0.5F))
    public float modifyRenderRotStepConstantInTickNewAi(float constant) {
        return ModuleList.handTweaks.isEnabled() ? ModuleList.handTweaks.handAnimStep.getValue().floatValue() : constant;
    }

    @Redirect(method = "tickNausea", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;shouldPause()Z"))
    public boolean modifyShouldPauseOnTickNausea(Screen instance) {
        if (ModuleList.portalGod.isEnabled()) return true;
        else return instance.shouldPause();
    }

    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/TutorialManager;onMovement(Lnet/minecraft/client/input/Input;)V"))
    public void modifyTickMovementPreItemSlow(CallbackInfo ci) {
        tempTicksLeftToDoubleTapSprint = ticksLeftToDoubleTapSprint;
        tempForwardInput = client.player.input.movementForward;
        tempSidewaysInput = client.player.input.movementSideways;
    }

    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;canStartSprinting()Z"))
    public void modifyTickMovementPostItemSlow(CallbackInfo ci) {
        if (ModuleList.noSlow.isEnabled() && ModuleList.noSlow.useItems.getValue()) {
            input.movementSideways = tempSidewaysInput;
            input.movementForward = tempForwardInput;
            if (!input.playerInput.sneak())
                ticksLeftToDoubleTapSprint = tempTicksLeftToDoubleTapSprint;
        }
    }

    @Inject(method = "tickMovement", at = @At("HEAD"))
    @SuppressWarnings("ConstantConditions")
    public void modifyTickMovement(CallbackInfo ci) {
        if (ModuleList.elytraFlight.isEnabled() && ModuleList.elytraFlight.mode.getValue().equals("1.12.2 Control")) {
            if (ModuleList.elytraFlight.travelPacket != null) {
                if (ModuleList.elytraFlight.travelPacket.rotate()) {
                    Managers.NETWORK_MANAGER.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(ModuleList.elytraFlight.travelPacket.rot().x, ModuleList.elytraFlight.travelPacket.rot().y, client.player.isOnGround(), client.player.horizontalCollision));
                }
                client.player.ticksSinceLastPositionPacketSent++;
            }
        }
    }

    @Inject(method = "sendMovementPackets", at = @At("HEAD"))
    public void modifySendMovementPackets(CallbackInfo ci) {
        if (ModuleList.noSlow.isEnabled() && ModuleList.noSlow.useItems.getValue() && ModuleList.noSlow.grim.getValue()) {
            if (client.player.isUsingItem() && !client.player.isSneaking()) {
                ItemStack offHandStack = client.player.getOffHandStack();
                if (client.player.getActiveHand() == Hand.OFF_HAND) {
                    Managers.NETWORK_MANAGER.sendPacket(new UpdateSelectedSlotC2SPacket(client.player.getInventory().selectedSlot % 8 + 1));
                    Managers.NETWORK_MANAGER.sendPacket(new UpdateSelectedSlotC2SPacket(client.player.getInventory().selectedSlot));
                } else if (!ItemUtils.isFood(offHandStack) && offHandStack.getItem() != Items.BOW && offHandStack.getItem() != Items.CROSSBOW && offHandStack.getItem() != Items.SHIELD) {
                    Managers.NETWORK_MANAGER.sendSequencePacket(id -> new PlayerInteractItemC2SPacket(Hand.OFF_HAND, id, client.player.getYaw(), client.player.getPitch()));
                }
            }
        }
    }

    @Inject(method = "pushOutOfBlocks", at = @At("HEAD"), cancellable = true)
    public void modifyPushOutOfBlocks(double x, double z, CallbackInfo ci) {
        if (ModuleList.noPush.isEnabled())
            if (ModuleList.noPush.blocks.getValue())
                ci.cancel();
    }

    @Override
    public double getBlockInteractionRange() {
        double value = super.getBlockInteractionRange();
        if (ModuleList.reach.isEnabled()) value += ModuleList.reach.range.getValue();
        return value;
    }

    @Override
    public double getEntityInteractionRange() {
        double value = super.getEntityInteractionRange();
        if (ModuleList.reach.isEnabled()) value += ModuleList.reach.range.getValue();
        return value;
    }
}
