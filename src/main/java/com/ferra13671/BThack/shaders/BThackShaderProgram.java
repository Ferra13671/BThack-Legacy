package com.ferra13671.BThack.shaders;

import com.ferra13671.BThack.api.Interfaces.Mc;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.ShaderProgramKey;
import org.joml.Matrix4f;

public class BThackShaderProgram implements Mc {
    private final ShaderProgramKey programKey;
    private ShaderProgram shader;

    public BThackShaderProgram(ShaderProgramKey programKey) {
        this.programKey = programKey;
        CoreShaderLoader.addBThackProgramToLoad(this);
    }

    public ShaderProgramKey getProgramKey() {
        return programKey;
    }

    public ShaderProgram getShader() {
        return shader;
    }

    public void setShader(ShaderProgram shader) {
        this.shader = shader;
    }

    public void use() {
        RenderSystem.setShader(shader);
    }

    public void release() {
        //No actions
    }

    public void setUniformValue(String uniformName, float value) {
        GlUniform uniform = shader.getUniform(uniformName);
        if (uniform != null) uniform.set(value);
    }

    public void setUniformValue(String uniformName, int value) {
        GlUniform uniform = shader.getUniform(uniformName);
        if (uniform != null) uniform.set(value);
    }

    public void setUniformValue(String uniformName, float value1, float value2) {
        GlUniform uniform = shader.getUniform(uniformName);
        if (uniform != null) uniform.set(value1, value2);
    }

    public void setUniformValue(String uniformName, int value1, int value2) {
        GlUniform uniform = shader.getUniform(uniformName);
        if (uniform != null) uniform.set(value1, value2);
    }

    public void setUniformValue(String uniformName, float value1, float value2, float value3) {
        GlUniform uniform = shader.getUniform(uniformName);
        if (uniform != null) uniform.set(value1, value2, value3);
    }

    public void setUniformValue(String uniformName, int value1, int value2, int value3) {
        GlUniform uniform = shader.getUniform(uniformName);
        if (uniform != null) uniform.set(value1, value2, value3);
    }

    public void setUniformValue(String uniformName, float value1, float value2, float value3, float value4) {
        GlUniform uniform = shader.getUniform(uniformName);
        if (uniform != null) uniform.set(value1, value2, value3, value4);
    }

    public void setUniformValue(String uniformName, int value1, int value2, int value3, int value4) {
        GlUniform uniform = shader.getUniform(uniformName);
        if (uniform != null) uniform.set(value1, value2, value3, value4);
    }

    public void setUniformValue(String uniformName, Matrix4f value) {
        GlUniform uniform = shader.getUniform(uniformName);
        if (uniform != null) uniform.set(value);
    }

    public static BThackShaderProgram of(ShaderProgramKey programKey) {
        return new BThackShaderProgram(programKey);
    }

    public static BThackShaderProgram of(String id) {
        return of(CoreShaderLoader.getShaderKeys().get(id));
    }
}
