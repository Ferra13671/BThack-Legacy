package com.ferra13671.BThack.mixins;

import com.ferra13671.BThack.core.Client.Client;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.api.Utils.Mc;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerPosition;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler implements Mc {

    @SuppressWarnings("DataFlowIssue")
    @ModifyArg(method = "setPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setYaw(F)V"))
    private static float modifySetYawOnOnPlayerPositionLook(float yaw) {
        if (ModuleList.noSRotations.isEnabled())
            return mc.player.getYaw();
        else return yaw;
    }

    @SuppressWarnings("DataFlowIssue")
    @ModifyArg(method = "setPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setPitch(F)V"))
    private static float modifySetPitchOnOnPlayerPositionLook(float pitch) {
        if (ModuleList.noSRotations.isEnabled())
            return mc.player.getPitch();
        else return pitch;
    }

    @SuppressWarnings("DataFlowIssue")
    @Inject(method = "setPosition", at = @At("TAIL"))
    private static void modifyOnPlayerPositionLook(PlayerPosition pos, Set<PositionFlag> flags, Entity entity, boolean bl, CallbackInfoReturnable<Boolean> cir) {
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
