package com.ferra13671.BThack.shaders.MainMenu;

import com.ferra13671.BThack.shaders.BThackShaderProgram;
import com.ferra13671.BThack.shaders.CoreShaderLoader;
import com.ferra13671.BThack.core.Render.Utils.BThackRenderUtils;
import com.ferra13671.BThack.api.Interfaces.Mc;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.ShaderProgramKey;

public class MainMenuBThackShader extends BThackShaderProgram implements Mc {
    private GlUniform resolution;
    private GlUniform mouse;
    private GlUniform time;

    public MainMenuBThackShader(ShaderProgramKey programKey) {
        super(programKey);
    }

    public void setParameters(float mouseX, float mouseY, float screenWidth, float screenHeight, float time) {
        if (resolution == null)
            resolution = getShader().getUniform("resolution");
        if (mouse == null)
            mouse = getShader().getUniform("mouse");
        if (this.time == null)
            this.time = getShader().getUniform("time");

        int guiScale = BThackRenderUtils.getGuiScale();
        if (resolution != null)
            resolution.set(screenWidth * guiScale, screenHeight * guiScale);
        if (mouse != null)
            mouse.set(mouseX / screenWidth, (screenHeight - 1.0f - mouseY) / screenHeight);
        if (this.time != null)
            this.time.set(time);
    }

    public static MainMenuBThackShader of(ShaderProgramKey programKey) {
        return new MainMenuBThackShader(programKey);
    }

    public static MainMenuBThackShader of(String id) {
        return of(CoreShaderLoader.getShaderKeys().get(id));
    }
}
