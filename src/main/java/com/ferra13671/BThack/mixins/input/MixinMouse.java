package com.ferra13671.BThack.mixins.input;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Events.InputEvent;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MixinMouse {

    @Inject(method = "onMouseButton", at = @At("HEAD"), cancellable = true)
    private void modifyOnMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        InputEvent.MouseInputEvent event = new InputEvent.MouseInputEvent(button, action);
        BThack.EVENT_BUS.activate(event);
        if (event.isCancelled()) ci.cancel();
    }
}
