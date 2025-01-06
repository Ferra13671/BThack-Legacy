package com.ferra13671.BThack.mixins;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Interfaces.Mc;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler implements Mc {

    @ModifyArgs(method = "onPlayerPositionLook", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setYaw(F)V"))
    public void modifySetYawOnOnPlayerPositionLook(Args args) {
        if (ModuleList.noSRotations.isEnabled())
            args.set(0, mc.player.getYaw());
    }

    @ModifyArgs(method = "onPlayerPositionLook", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setPitch(F)V"))
    public void modifySetPitchOnOnPlayerPositionLook(Args args) {
        if (ModuleList.noSRotations.isEnabled())
            args.set(0, mc.player.getPitch());
    }

    @Inject(method = "onPlayerPositionLook", at = @At("TAIL"))
    public void modifyOnPlayerPositionLook(PlayerPositionLookS2CPacket packet, CallbackInfo ci) {
        if (ModuleList.noSRotations.isEnabled()) {
            mc.player.prevYaw = mc.player.getYaw();
            mc.player.prevPitch = mc.player.getPitch();
        }
    }
}
