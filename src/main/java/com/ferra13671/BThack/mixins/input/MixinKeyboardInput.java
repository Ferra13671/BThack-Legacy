package com.ferra13671.BThack.mixins.input;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Events.Entity.UpdateInputEvent;
import net.minecraft.client.input.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class MixinKeyboardInput {

    @Inject(method = "tick", at = @At("RETURN"))
    public void modifyTick(CallbackInfo ci) {
        BThack.EVENT_BUS.activate(new UpdateInputEvent());
    }
}
