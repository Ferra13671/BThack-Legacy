package com.ferra13671.BThack.mixins.accessor;

import net.minecraft.client.gl.PostEffectPass;
import net.minecraft.client.gl.PostEffectProcessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(PostEffectProcessor.class)
public interface IPostEffectProcessor {

    @Accessor("passes")
    List<PostEffectPass> _getPasses();
}
