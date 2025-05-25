package com.ferra13671.BThack.shaders.MainMenu;

import com.ferra13671.BThack.shaders.BThackShaderProgram;
import com.ferra13671.BThack.shaders.CoreShaderLoader;
import com.ferra13671.BThack.core.Render.Utils.BThackRenderUtils;
import com.ferra13671.BThack.api.Utils.Mc;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.ShaderProgramKey;

public class MainMenuBThackShader extends BThackShaderProgram implements Mc {

    public MainMenuBThackShader(ShaderProgramKey programKey) {
        super(programKey);
    }

    public void setParameters(float mouseX, float mouseY, float screenWidth, float screenHeight, float _time) {
        GlUniform resolution = getShader().getUniform("resolution");
        GlUniform mouse = getShader().getUniform("mouse");
        GlUniform time = getShader().getUniform("time");

        int guiScale = BThackRenderUtils.getGuiScale();
        if (resolution != null)
            resolution.set(screenWidth * guiScale, screenHeight * guiScale);
        if (mouse != null)
            mouse.set(mouseX / screenWidth, (screenHeight - 1.0f - mouseY) / screenHeight);
        if (time != null)
            time.set(_time);
    }

    public static MainMenuBThackShader of(ShaderProgramKey programKey) {
        return new MainMenuBThackShader(programKey);
    }

    public static MainMenuBThackShader of(String id) {
        return of(CoreShaderLoader.getShaderKey(id));
    }
}
