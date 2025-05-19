package com.ferra13671.BThack.mixins.accessor;

import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.ShaderProgram;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(ShaderProgram.class)
public interface IShaderProgram {

    @Accessor("uniformsByName")
    Map<String, GlUniform> _getUniforms();
}
