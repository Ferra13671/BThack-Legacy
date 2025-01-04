package com.ferra13671.BThack.mixins;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.impl.Modules.CLIENT.BThackCape;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.SkinTextures;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListEntry.class)
public class MixinPlayerListEntry implements Mc {

    @Shadow @Final private GameProfile profile;

    @Inject(method = "getSkinTextures", at = @At("TAIL"), cancellable = true)
    private void hookGetSkinTextures(CallbackInfoReturnable<SkinTextures> cir) {
        if (ModuleList.bthackCape.isEnabled() && profile.getName().equals(mc.player.getGameProfile().getName())) {
            SkinTextures t = cir.getReturnValue();
            SkinTextures customCapeTexture = new SkinTextures(t.texture(), t.textureUrl(), BThackCape.BThack_Cape, BThackCape.BThack_Cape, t.model(), t.secure());
            cir.setReturnValue(customCapeTexture);
        }
    }
}
