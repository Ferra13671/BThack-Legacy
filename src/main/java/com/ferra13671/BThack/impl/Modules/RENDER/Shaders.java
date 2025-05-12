package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.api.IMixin.ModifyPostEffectProcessor;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.*;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.util.Identifier;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.ladysnake.satin.api.managed.ShaderEffectManager;

import java.awt.*;
import java.util.Arrays;

@ModuleInfo(name = "Shaders", description = "lang.module.Shaders", category = "RENDER")
public class Shaders extends Module {
    public ManagedShaderEffect defaultShader;
    public ManagedShaderEffect gradientShader;
    public ManagedShaderEffect rainbowXShader;
    public ManagedShaderEffect rainbowYShader;
    public ManagedShaderEffect rainbowXYShader;
    public boolean shaderInited = false;

    public final ModeSetting shaderMode = new ModeSetting("Shader", this, Arrays.asList("Default", "Gradient", "Rainbow_xy", "Rainbow_x", "Rainbow_y"));

    //Default
    public final ColorSetting fillColor = new ColorSetting("Fill Color", this, new Color(118, 13, 179, 90), () -> shaderMode.getValue().equals("Default"));
    public final ColorSetting outlineColor = new ColorSetting("Outline Color", this, new Color(161, 0, 255, 255), () -> shaderMode.getValue().equals("Default"));

    //Gradient
    public final ColorSetting color1 = new ColorSetting("Color1", this, new Color(213, 142, 253), () -> shaderMode.getValue().equals("Gradient")).withBlockedAlpha();
    public final ColorSetting color2 = new ColorSetting("Color2", this, new Color(42, 0, 67), () -> shaderMode.getValue().equals("Gradient")).withBlockedAlpha();

    //Rainbow
    public final NumberSetting brightness = new NumberSetting("Brightness", this, 1, 0.1, 1, false, () -> shaderMode.getValue().equals("Rainbow_xy") || shaderMode.getValue().equals("Rainbow_x") || shaderMode.getValue().equals("Rainbow_y"));
    public final NumberSetting saturation = new NumberSetting("Saturation", this, 0.6, 0, 1, false, () -> shaderMode.getValue().equals("Rainbow_xy") || shaderMode.getValue().equals("Rainbow_x") || shaderMode.getValue().equals("Rainbow_y"));

    //Gradient & Rainbow
    public final NumberSetting speed = new NumberSetting("Speed", this, 1, 0.5, 5, false, () -> !shaderMode.getValue().equals("Default"));
    public final NumberSetting scale = new NumberSetting("Scale", this, 10, 1, 20, false, () -> !shaderMode.getValue().equals("Default"));
    public final NumberSetting fillAlpha = new NumberSetting("Fill Alpha", this, 90, 0, 255, true, () -> !shaderMode.getValue().equals("Default"));
    public final NumberSetting outlineAlpha = new NumberSetting("Outline Alpha", this, 255, 0, 255, true, () -> !shaderMode.getValue().equals("Default"));

    public final NumberSetting lineWidth = new NumberSetting("Line Width", this, 2, 0, 6, true);

    public final CategorySetting targetsCategory = new CategorySetting("Targets", this);
    public final BooleanSetting players = new BooleanSetting("Players", this, true).inCategory(targetsCategory);
    public final BooleanSetting items = new BooleanSetting("Items", this, true).inCategory(targetsCategory);
    public final BooleanSetting hostiles = new BooleanSetting("Hostiles", this, true).inCategory(targetsCategory);
    public final BooleanSetting golems = new BooleanSetting("Golems", this, true).inCategory(targetsCategory);
    public final BooleanSetting passive = new BooleanSetting("Passive", this, true).inCategory(targetsCategory);
    public final BooleanSetting hands = new BooleanSetting("Hands", this, true).inCategory(targetsCategory);
    public final BooleanSetting self = new BooleanSetting("Self", this, true).inCategory(targetsCategory);
    public final BooleanSetting crystals = new BooleanSetting("Crystals", this, true).inCategory(targetsCategory);


    public void drawShader(float tickDelta) {
        switch (shaderMode.getValue()) {
            case "Default" -> {
                defaultShader.setUniformValue("quality", lineWidth.getValue().intValue());
                defaultShader.setUniformValue("color", fillColor.getValue().getRed() / 255f, fillColor.getValue().getGreen() / 255f, fillColor.getValue().getBlue() / 255f, fillColor.getValue().getAlpha() / 255f);
                defaultShader.setUniformValue("outlinecolor", outlineColor.getValue().getRed() / 255f, outlineColor.getValue().getGreen() / 255f, outlineColor.getValue().getBlue() / 255f, outlineColor.getValue().getAlpha() / 255f);
                defaultShader.render(tickDelta);
            }
            case "Gradient" -> {
                gradientShader.setUniformValue("quality", lineWidth.getValue().intValue());
                gradientShader.setUniformValue("scale", (float) (int) (scale.getValue() * 1000));
                gradientShader.setUniformValue("time", com.ferra13671.BThack.api.Shader.Shaders.INSTANCE.shaderTicker.getPassedTime() / 1000f);
                gradientShader.setUniformValue("resolution", (float) mc.getWindow().getWidth(), mc.getWindow().getHeight());
                gradientShader.setUniformValue("fillAlpha", fillAlpha.getValue().floatValue() / 255f);
                gradientShader.setUniformValue("outlineAlpha", outlineAlpha.getValue().floatValue() / 255f);
                gradientShader.setUniformValue("color1", (float) color1.getValue().getRed() / 255f, (float) color1.getValue().getGreen() / 255f, (float) color1.getValue().getBlue() / 255f);
                gradientShader.setUniformValue("color2", (float) color2.getValue().getRed() / 255f, (float) color2.getValue().getGreen() / 255f, (float) color2.getValue().getBlue() / 255f);
                gradientShader.setUniformValue("speed", speed.getValue().floatValue() * 3);
                gradientShader.render(tickDelta);
            }
            case "Rainbow_xy" -> {
                rainbowXYShader.setUniformValue("quality", lineWidth.getValue().intValue());
                rainbowXYShader.setUniformValue("scale", (float) (int) (scale.getValue() * 1000));
                rainbowXYShader.setUniformValue("time", com.ferra13671.BThack.api.Shader.Shaders.INSTANCE.shaderTicker.getPassedTime() / 1000f);
                rainbowXYShader.setUniformValue("resolution", (float) mc.getWindow().getWidth(), mc.getWindow().getHeight());
                rainbowXYShader.setUniformValue("brightness", brightness.getValue().floatValue());
                rainbowXYShader.setUniformValue("saturation", saturation.getValue().floatValue());
                rainbowXYShader.setUniformValue("fillAlpha", fillAlpha.getValue().floatValue() / 255f);
                rainbowXYShader.setUniformValue("outlineAlpha", outlineAlpha.getValue().floatValue() / 255f);
                rainbowXYShader.setUniformValue("speed", speed.getValue().floatValue() * 3);
                rainbowXYShader.render(tickDelta);
            }
            case "Rainbow_x" -> {
                rainbowXShader.setUniformValue("quality", lineWidth.getValue().intValue());
                rainbowXShader.setUniformValue("scale", (float) (int) (scale.getValue() * 1000));
                rainbowXShader.setUniformValue("time", com.ferra13671.BThack.api.Shader.Shaders.INSTANCE.shaderTicker.getPassedTime() / 1000f);
                rainbowXShader.setUniformValue("resolution", (float) mc.getWindow().getWidth(), mc.getWindow().getHeight());
                rainbowXShader.setUniformValue("brightness", brightness.getValue().floatValue());
                rainbowXShader.setUniformValue("saturation", saturation.getValue().floatValue());
                rainbowXShader.setUniformValue("fillAlpha", fillAlpha.getValue().floatValue() / 255f);
                rainbowXShader.setUniformValue("outlineAlpha", outlineAlpha.getValue().floatValue() / 255f);
                rainbowXShader.setUniformValue("speed", speed.getValue().floatValue() * 3);
                rainbowXShader.render(tickDelta);
            }
            case "Rainbow_y" -> {
                rainbowYShader.setUniformValue("quality", lineWidth.getValue().intValue());
                rainbowYShader.setUniformValue("scale", (float) (int) (scale.getValue() * 1000));
                rainbowYShader.setUniformValue("time", com.ferra13671.BThack.api.Shader.Shaders.INSTANCE.shaderTicker.getPassedTime() / 1000f);
                rainbowYShader.setUniformValue("resolution", (float) mc.getWindow().getWidth(), mc.getWindow().getHeight());
                rainbowYShader.setUniformValue("brightness", brightness.getValue().floatValue());
                rainbowYShader.setUniformValue("saturation", saturation.getValue().floatValue());
                rainbowYShader.setUniformValue("fillAlpha", fillAlpha.getValue().floatValue() / 255f);
                rainbowYShader.setUniformValue("outlineAlpha", outlineAlpha.getValue().floatValue() / 255f);
                rainbowYShader.setUniformValue("speed", speed.getValue().floatValue() * 3);
                rainbowYShader.render(tickDelta);
            }
        }
    }

    public ManagedShaderEffect getShader() {
        return switch (shaderMode.getValue()) {
            default -> defaultShader;
            case "Gradient" -> gradientShader;
            case "Rainbow_xy" -> rainbowXYShader;
            case "Rainbow_x" -> rainbowXShader;
            case "Rainbow_y" -> rainbowYShader;
        };
    }

    public void reloadShader() {
        shaderInited = true;

        defaultShader = ShaderEffectManager.getInstance().manage(Identifier.of("bthack", "shaders/post/default_outline.json"));
        gradientShader = ShaderEffectManager.getInstance().manage(Identifier.of("bthack", "shaders/post/gradient1_outline.json"));
        rainbowXShader = ShaderEffectManager.getInstance().manage(Identifier.of("bthack", "shaders/post/rainbowx_outline.json"));
        rainbowYShader = ShaderEffectManager.getInstance().manage(Identifier.of("bthack", "shaders/post/rainbowy_outline.json"));
        rainbowXYShader = ShaderEffectManager.getInstance().manage(Identifier.of("bthack", "shaders/post/rainbowxy_outline.json"));

        initShader(defaultShader);
        initShader(gradientShader);
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
