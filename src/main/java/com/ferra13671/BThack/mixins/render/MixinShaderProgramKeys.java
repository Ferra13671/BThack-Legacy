package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.shaders.CoreShaderLoader;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderProgramKeys;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ShaderProgramKeys.class)
public abstract class MixinShaderProgramKeys {

    @Shadow @Final private static List<ShaderProgramKey> ALL;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void modifyStaticInit(CallbackInfo ci) {
        CoreShaderLoader.initShaderKeys(ALL);
    }
}
