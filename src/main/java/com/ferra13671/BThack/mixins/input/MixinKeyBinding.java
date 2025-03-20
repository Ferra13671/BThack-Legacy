package com.ferra13671.BThack.mixins.input;

import com.ferra13671.BThack.api.IMixin.ModifyKeyBinding;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(KeyBinding.class)
public class MixinKeyBinding implements ModifyKeyBinding {
    @Shadow private InputUtil.Key boundKey;

    @Override
    public InputUtil.Key _getBoundKey() {
        return this.boundKey;
    }
}
