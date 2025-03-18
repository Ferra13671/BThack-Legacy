package com.ferra13671.BThack.api.Shader.MainMenu;

import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Shader.ShaderProgram;
import org.ladysnake.satin.api.managed.uniform.Uniform1f;
import org.ladysnake.satin.api.managed.uniform.Uniform2f;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public class MainMenuShader extends ShaderProgram implements Mc {
    private final Uniform2f resolution;
    private final Uniform2f mouse;
    private final Uniform1f time;

    public MainMenuShader(Identifier identifier, VertexFormat vertexFormat) {
        super(identifier, vertexFormat);
        resolution = shader.findUniform2f("resolution");
        mouse = shader.findUniform2f("mouse");
        time = shader.findUniform1f("time");
    }

    public void setParameters(float mouseX, float mouseY, float screenWidth, float screenHeight, float time) {
        int guiScale = getGuiScale();
        if (resolution != null)
            resolution.set(screenWidth * guiScale, screenHeight * guiScale);
        if (mouse != null)
            mouse.set(mouseX / screenWidth, (screenHeight - 1.0f - mouseY) / screenHeight);
        if (this.time != null)
            this.time.set(time);
    }

    private int getGuiScale() {
        int value = mc.options.getGuiScale().getValue();
        if (value <= 0) value = mc.getWindow().calculateScaleFactor(0, mc.forcesUnicodeFont());
        return value;
    }

    public static MainMenuShader of(String name) {
        return of("bthack", name);
    }

    public static MainMenuShader of(String nameSpace, String name) {
        return new MainMenuShader(Identifier.of(nameSpace, name), VertexFormats.POSITION);
    }
}
