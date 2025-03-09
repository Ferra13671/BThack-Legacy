package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.api.IMixin.ModifyPostEffectProcessor;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.*;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.util.Identifier;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.ladysnake.satin.api.managed.ShaderEffectManager;

import java.awt.*;
import java.util.Arrays;

public class Shaders extends Module {
    public ManagedShaderEffect defaultShader = ShaderEffectManager.getInstance().manage(Identifier.of("bthack", "shaders/post/default_outline.json"));
    public ManagedShaderEffect rainbowXShader = ShaderEffectManager.getInstance().manage(Identifier.of("bthack", "shaders/post/rainbowx_outline.json"));
    public ManagedShaderEffect rainbowYShader = ShaderEffectManager.getInstance().manage(Identifier.of("bthack", "shaders/post/rainbowy_outline.json"));
    public ManagedShaderEffect rainbowXYShader = ShaderEffectManager.getInstance().manage(Identifier.of("bthack", "shaders/post/rainbowxy_outline.json"));
    public boolean shaderInited = false;

    public final ModeSetting shaderMode = new ModeSetting("Shader", this, Arrays.asList("Default", "Rainbow_xy", "Rainbow_x", "Rainbow_y"));

    public final BooleanSetting players = new BooleanSetting("Players", this, true);
    public final BooleanSetting items = new BooleanSetting("Items", this, true);
    public final BooleanSetting hostiles = new BooleanSetting("Hostiles", this, true);
    public final BooleanSetting golems = new BooleanSetting("Golems", this, true);
    public final BooleanSetting passive = new BooleanSetting("Passive", this, true);
    //public final BooleanSetting hands = new BooleanSetting("Hands", this, true);  TODO

    //Default
    public final ColorSetting fillColor = new ColorSetting("Fill Color", this, new Color(118, 13, 179, 90), () -> shaderMode.getValue().equals("Default"));
    public final ColorSetting outlineColor = new ColorSetting("Outline Color", this, new Color(161, 0, 255, 255), () -> shaderMode.getValue().equals("Default"));

    //Rainbow
    public final NumberSetting scale = new NumberSetting("Scale", this, 10, 1, 20, false, () -> !shaderMode.getValue().equals("Default"));
    public final NumberSetting brightness = new NumberSetting("Brightness", this, 1, 0.1, 1, false, () -> !shaderMode.getValue().equals("Default"));
    public final NumberSetting saturation = new NumberSetting("Saturation", this, 0.6, 0, 1, false, () -> !shaderMode.getValue().equals("Default"));
    public final NumberSetting fillAlpha = new NumberSetting("Fill Alpha", this, 90, 0, 255, true, () -> !shaderMode.getValue().equals("Default"));
    public final NumberSetting outlineAlpha = new NumberSetting("Outline Alpha", this, 255, 0, 255, true, () -> !shaderMode.getValue().equals("Default"));
    public final NumberSetting speed = new NumberSetting("Speed", this, 1, 0.5, 5, false, () -> !shaderMode.getValue().equals("Default"));

    public final NumberSetting lineWidth = new NumberSetting("Line Width", this, 2, 0, 6, true);

    public Shaders() {
        super("Shaders",
                "lang.module.Shaders",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        initSettings(
                shaderMode,

                players,
                items,
                hostiles,
                golems,
                passive,

                fillColor,
                outlineColor,
                lineWidth,

                scale,
                brightness,
                saturation,
                fillAlpha,
                outlineAlpha,
                speed
        );
    }

    public void drawShader(float tickDelta) {
        switch (shaderMode.getValue()) {
            case "Default" -> {
                defaultShader.setUniformValue("lineWidth", (int) lineWidth.getValue());
                defaultShader.setUniformValue("quality", (int) lineWidth.getValue());
                defaultShader.setUniformValue("color", fillColor.getValue().getRed() / 255f, fillColor.getValue().getGreen() / 255f, fillColor.getValue().getBlue() / 255f, fillColor.getValue().getAlpha() / 255f);
                defaultShader.setUniformValue("outlinecolor", outlineColor.getValue().getRed() / 255f, outlineColor.getValue().getGreen() / 255f, outlineColor.getValue().getBlue() / 255f, outlineColor.getValue().getAlpha() / 255f);
                defaultShader.render(tickDelta);
            }
            case "Rainbow_xy" -> {
                rainbowXYShader.setUniformValue("lineWidth", (int) lineWidth.getValue());
                rainbowXYShader.setUniformValue("quality", (int) lineWidth.getValue());
                rainbowXYShader.setUniformValue("scale", (float) (int) (scale.getValue() * 1000));
                rainbowXYShader.setUniformValue("time", com.ferra13671.BThack.api.Shader.Shaders.INSTANCE.shaderTicker.getPassedTime() / 1000f);
                rainbowXYShader.setUniformValue("resolution", (float) mc.getWindow().getWidth(), mc.getWindow().getHeight());
                rainbowXYShader.setUniformValue("brightness", (float) brightness.getValue());
                rainbowXYShader.setUniformValue("saturation", (float) saturation.getValue());
                rainbowXYShader.setUniformValue("fillAlpha", (float) fillAlpha.getValue() / 255f);
                rainbowXYShader.setUniformValue("outlineAlpha", (float) outlineAlpha.getValue() / 255f);
                rainbowXYShader.setUniformValue("speed", (float) speed.getValue() * 3);
                rainbowXYShader.render(tickDelta);
            }
            case "Rainbow_x" -> {
                rainbowXShader.setUniformValue("lineWidth", (int) lineWidth.getValue());
                rainbowXShader.setUniformValue("quality", (int) lineWidth.getValue());
                rainbowXShader.setUniformValue("scale", (float) (int) (scale.getValue() * 1000));
                rainbowXShader.setUniformValue("time", com.ferra13671.BThack.api.Shader.Shaders.INSTANCE.shaderTicker.getPassedTime() / 1000f);
                rainbowXShader.setUniformValue("resolution", (float) mc.getWindow().getWidth(), mc.getWindow().getHeight());
                rainbowXShader.setUniformValue("brightness", (float) brightness.getValue());
                rainbowXShader.setUniformValue("saturation", (float) saturation.getValue());
                rainbowXShader.setUniformValue("fillAlpha", (float) fillAlpha.getValue() / 255f);
                rainbowXShader.setUniformValue("outlineAlpha", (float) outlineAlpha.getValue() / 255f);
                rainbowXShader.setUniformValue("speed", (float) speed.getValue() * 3);
                rainbowXShader.render(tickDelta);
            }
            case "Rainbow_y" -> {
                rainbowYShader.setUniformValue("lineWidth", (int) lineWidth.getValue());
                rainbowYShader.setUniformValue("quality", (int) lineWidth.getValue());
                rainbowYShader.setUniformValue("scale", (float) (int) (scale.getValue() * 1000));
                rainbowYShader.setUniformValue("time", com.ferra13671.BThack.api.Shader.Shaders.INSTANCE.shaderTicker.getPassedTime() / 1000f);
                rainbowYShader.setUniformValue("resolution", (float) mc.getWindow().getWidth(), mc.getWindow().getHeight());
                rainbowYShader.setUniformValue("brightness", (float) brightness.getValue());
                rainbowYShader.setUniformValue("saturation", (float) saturation.getValue());
                rainbowYShader.setUniformValue("fillAlpha", (float) fillAlpha.getValue() / 255f);
                rainbowYShader.setUniformValue("outlineAlpha", (float) outlineAlpha.getValue() / 255f);
                rainbowYShader.setUniformValue("speed", (float) speed.getValue() * 3);
                rainbowYShader.render(tickDelta);
            }
        }
    }

    public ManagedShaderEffect getShader() {
        return switch (shaderMode.getValue()) {
            default -> defaultShader;
            case "Rainbow_xy" -> rainbowXYShader;
            case "Rainbow_x" -> rainbowXShader;
            case "Rainbow_y" -> rainbowYShader;
        };
    }

    public void reloadShader() {
        shaderInited = true;

        initShader(defaultShader);
        initShader(rainbowXShader);
        initShader(rainbowYShader);
        initShader(rainbowXYShader);
    }

    public void initShader(ManagedShaderEffect shaderEffect) {
        PostEffectProcessor effect = shaderEffect.getShaderEffect();

        ((ModifyPostEffectProcessor) effect)._addTargetHook("bufIn", mc.worldRenderer.getEntityOutlinesFramebuffer());
        ((ModifyPostEffectProcessor) effect)._addTargetHook("bufOut", mc.worldRenderer.getEntityOutlinesFramebuffer());
    }
}
