package com.ferra13671.BThack.mixins.input;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Events.InputEvent;
import net.minecraft.client.Keyboard;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class MixinKeyboard {

    @Inject(method = "onKey", at = @At("HEAD"))
    public void modifyOnKey(long window, int keyCode, int scancode, int action, int modifiers, CallbackInfo ci) {
        BThack.EVENT_BUS.activate(new InputEvent.KeyInputEvent(keyCode, InputUtil.fromKeyCode(keyCode, scancode), action == 1 ? InputEvent.KeyInputEvent.Action.PRESS : action == 0 ? InputEvent.KeyInputEvent.Action.RELEASE : InputEvent.KeyInputEvent.Action.JAMMED));
    }
}
