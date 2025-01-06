package com.ferra13671.BThack.api.Shader.MainMenu;

import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Shader.ShaderProgram;
import ladysnake.satin.api.managed.uniform.Uniform1f;
import ladysnake.satin.api.managed.uniform.Uniform2f;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public class MainMenuShader extends ShaderProgram implements Mc {
    private final Uniform2f resolution;
    private final Uniform2f mouse;
    private final Uniform1f time;
    private final boolean needFixResolution;

    public MainMenuShader(Identifier identifier, VertexFormat vertexFormat, boolean needFixResolution) {
        super(identifier, vertexFormat);
        this.needFixResolution = needFixResolution;
        resolution = shader.findUniform2f("resolution");
        mouse = shader.findUniform2f("mouse");
        time = shader.findUniform1f("time");
    }

    public boolean isNeedFixResolution() {
        return needFixResolution;
    }

    public void setParameters(float mouseX, float mouseY, float screenWidth, float screenHeight, float time) {
        if (resolution != null)
            resolution.set(screenWidth * (needFixResolution ? 2 : 1), screenHeight * (needFixResolution ? 2 : 1));
        if (mouse != null)
            mouse.set(mouseX / screenWidth, (screenHeight - 1.0f - mouseY) / screenHeight);
        if (this.time != null)
            this.time.set(time);
        //setUniformValue("resolution", screenWidth * (needFixResolution ? 2 : 1), screenHeight * (needFixResolution ? 2 : 1));
        //setUniformValue("mouse", mouseX / screenWidth, (screenHeight - 1.0f - mouseY) / screenHeight);
        //setUniformValue("time", time);
    }

    public static MainMenuShader of(String name) {
        return of("bthack", name, true);
    }

    public static MainMenuShader of(String name, boolean needFixResolution) {
        return of("bthack", name, needFixResolution);
    }

    public static MainMenuShader of(String nameSpace, String name, boolean needFixResolution) {
        return new MainMenuShader(Identifier.of(nameSpace, name), VertexFormats.POSITION, needFixResolution);
    }
}
