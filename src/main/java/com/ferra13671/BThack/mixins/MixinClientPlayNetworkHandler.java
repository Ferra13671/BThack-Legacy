package com.ferra13671.BThack.mixins;

import com.ferra13671.BThack.core.Client.Client;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler implements Mc {

    @ModifyArg(method = "onPlayerPositionLook", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setYaw(F)V"))
    public float modifySetYawOnOnPlayerPositionLook(float yaw) {
        if (ModuleList.noSRotations.isEnabled())
            return mc.player.getYaw();
        else return yaw;
    }

    @ModifyArg(method = "onPlayerPositionLook", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setPitch(F)V"))
    public float modifySetPitchOnOnPlayerPositionLook(float pitch) {
        if (ModuleList.noSRotations.isEnabled())
            return mc.player.getPitch();
        else return pitch;
    }

    @Inject(method = "onPlayerPositionLook", at = @At("TAIL"))
    public void modifyOnPlayerPositionLook(PlayerPositionLookS2CPacket packet, CallbackInfo ci) {
        if (ModuleList.noSRotations.isEnabled()) {
            mc.player.prevYaw = mc.player.getYaw();
            mc.player.prevPitch = mc.player.getPitch();
        }
    }

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void modifySendChatMessage(@NotNull String message, CallbackInfo ci) {
        if(Module.nullCheck()) return;
        if (message.startsWith(Client.clientInfo.getChatPrefix())) {
            try {
                Managers.COMMAND_MANAGER.getDispatcher().execute(
                        message.substring(Client.clientInfo.getChatPrefix().length()),
                        Managers.COMMAND_MANAGER.getSource()
                );
            } catch (CommandSyntaxException e) {
                ChatUtils.sendMessage(Formatting.RED + e.getMessage());
            }

            ci.cancel();
        }
    }
}
