package com.ferra13671.BThack.api.Shader;

import com.mojang.blaze3d.systems.RenderSystem;
import org.ladysnake.satin.api.managed.ManagedCoreShader;
import org.ladysnake.satin.api.managed.ShaderEffectManager;
import org.ladysnake.satin.api.managed.uniform.*;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

public class ShaderProgram {

    public final ManagedCoreShader shader;

    public ShaderProgram(Identifier identifier, VertexFormat vertexFormat) {
        shader = ShaderEffectManager.getInstance().manageCoreShader(identifier, vertexFormat);
    }

    public void use() {
        RenderSystem.setShader(shader::getProgram);
    }

    public void release() {
        //No actions
    }

    public void setUniformValue(String uniformName, float value) {
        Uniform1f uniform = shader.findUniform1f(uniformName);
        if (uniform != null) uniform.set(value);
    }

    public void setUniformValue(String uniformName, int value) {
        Uniform1i uniform = shader.findUniform1i(uniformName);
        if (uniform != null) uniform.set(value);
    }

    public void setUniformValue(String uniformName, float value1, float value2) {
        Uniform2f uniform = shader.findUniform2f(uniformName);
        if (uniform != null) uniform.set(value1, value2);
    }

    public void setUniformValue(String uniformName, int value1, int value2) {
        Uniform2i uniform = shader.findUniform2i(uniformName);
        if (uniform != null) uniform.set(value1, value2);
    }

    public void setUniformValue(String uniformName, float value1, float value2, float value3) {
        Uniform3f uniform = shader.findUniform3f(uniformName);
        if (uniform != null) uniform.set(value1, value2, value3);
    }

    public void setUniformValue(String uniformName, int value1, int value2, int value3) {
        Uniform3i uniform = shader.findUniform3i(uniformName);
        if (uniform != null) uniform.set(value1, value2, value3);
    }

    public void setUniformValue(String uniformName, float value1, float value2, float value3, float value4) {
        Uniform4f uniform = shader.findUniform4f(uniformName);
        if (uniform != null) uniform.set(value1, value2, value3, value4);
    }

    public void setUniformValue(String uniformName, int value1, int value2, int value3, int value4) {
        Uniform4i uniform = shader.findUniform4i(uniformName);
        if (uniform != null) uniform.set(value1, value2, value3, value4);
    }

    public void setUniformValue(String uniformName, Matrix4f value) {
        UniformMat4 uniform = shader.findUniformMat4(uniformName);
        if (uniform != null) uniform.set(value);
    }

    public static ShaderProgram of(Identifier identifier, VertexFormat vertexFormat) {
        return new ShaderProgram(identifier, vertexFormat);
    }

    public static ShaderProgram of(String nameSpace, String name, VertexFormat vertexFormat) {
        return of(Identifier.of(nameSpace, name), vertexFormat);
    }

    public static ShaderProgram of(String name, VertexFormat vertexFormat) {
        return of(Identifier.of("bthack", name), vertexFormat);
    }
}
