package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.*;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.mixins.accessor.IPostEffectProcessor;
import com.ferra13671.BThack.mixins.accessor.IShaderProgram;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.DefaultFramebufferSet;
import net.minecraft.client.render.FrameGraphBuilder;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.Arrays;

@ModuleInfo(name = "Shaders", description = "lang.module.Shaders", category = "RENDER")
public class Shaders extends Module {
    public PostEffectProcessor defaultShader;
    public PostEffectProcessor gradientShader;
    public PostEffectProcessor rainbowXShader;
    public PostEffectProcessor rainbowYShader;
    public PostEffectProcessor rainbowXYShader;

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


    private FrameGraphBuilder builder;
    private int textureWidth, textureHeight;
    private PostEffectProcessor.FramebufferSet framebufferSet;

    public void drawShader() {

        switch (shaderMode.getValue()) {
            case "Default" -> {
                ((IPostEffectProcessor) defaultShader)._getPasses().forEach(postEffectPass -> {
                    ShaderProgram shaderProgram = postEffectPass.getProgram();
                    ((IShaderProgram) shaderProgram)._getUniforms().forEach((name, uniform) -> {
                        switch (name) {
                            case "quality" -> uniform.set(lineWidth.getValue().intValue());
                            case "color" -> uniform.set(fillColor.getValue().getRed() / 255f, fillColor.getValue().getGreen() / 255f, fillColor.getValue().getBlue() / 255f, fillColor.getValue().getAlpha() / 255f);
                            case "outlinecolor" -> uniform.set(outlineColor.getValue().getRed() / 255f, outlineColor.getValue().getGreen() / 255f, outlineColor.getValue().getBlue() / 255f, outlineColor.getValue().getAlpha() / 255f);
                        }
                    });
                });
                defaultShader.render(builder, textureWidth, textureHeight, framebufferSet);
            }
            case "Gradient" -> {
                ((IPostEffectProcessor) gradientShader)._getPasses().forEach(postEffectPass -> {
                    ShaderProgram shaderProgram = postEffectPass.getProgram();
                    ((IShaderProgram) shaderProgram)._getUniforms().forEach((name, uniform) -> {
                        switch (name) {
                            case "quality" -> uniform.set(lineWidth.getValue().intValue());
                            case "scale" -> uniform.set((float) (scale.getValue() * 1000));
                            case "time" -> uniform.set(com.ferra13671.BThack.api.Shaders.Shaders.INSTANCE.shaderTicker.getPassedTime() / 1000f);
                            case "resolution" -> uniform.set((float) mc.getWindow().getWidth(), mc.getWindow().getHeight());
                            case "fillAlpha" -> uniform.set(fillAlpha.getValue().floatValue() / 255f);
                            case "outlineAlpha" -> uniform.set(outlineAlpha.getValue().floatValue() / 255f);
                            case "color1" -> uniform.set((float) color1.getValue().getRed() / 255f, (float) color1.getValue().getGreen() / 255f, (float) color1.getValue().getBlue() / 255f);
                            case "color2" -> uniform.set((float) color2.getValue().getRed() / 255f, (float) color2.getValue().getGreen() / 255f, (float) color2.getValue().getBlue() / 255f);
                            case "speed" -> uniform.set(speed.getValue().floatValue() * 3);
                        }
                    });
                });
                gradientShader.render(builder, textureWidth, textureHeight, framebufferSet);
            }
            case "Rainbow_xy" -> {
                ((IPostEffectProcessor) rainbowXYShader)._getPasses().forEach(postEffectPass -> {
                    ShaderProgram shaderProgram = postEffectPass.getProgram();
                    ((IShaderProgram) shaderProgram)._getUniforms().forEach((name, uniform) -> {
                        switch (name) {
                            case "quality" -> uniform.set(lineWidth.getValue().intValue());
                            case "scale" -> uniform.set((float) (scale.getValue() * 1000));
                            case "time" -> uniform.set(com.ferra13671.BThack.api.Shaders.Shaders.INSTANCE.shaderTicker.getPassedTime() / 1000f);
                            case "resolution" -> uniform.set((float) mc.getWindow().getWidth(), mc.getWindow().getHeight());
                            case "brightness" -> uniform.set(brightness.getValue().floatValue());
                            case "saturation" -> uniform.set(saturation.getValue().floatValue());
                            case "fillAlpha" -> uniform.set(fillAlpha.getValue().floatValue() / 255f);
                            case "outlineAlpha" -> uniform.set(outlineAlpha.getValue().floatValue() / 255f);
                            case "speed" -> uniform.set(speed.getValue().floatValue() * 3);
                        }
                    });
                });
                rainbowXYShader.render(builder, textureWidth, textureHeight, framebufferSet);
            }
            case "Rainbow_x" -> {
                ((IPostEffectProcessor) rainbowXShader)._getPasses().forEach(postEffectPass -> {
                    ShaderProgram shaderProgram = postEffectPass.getProgram();
                    ((IShaderProgram) shaderProgram)._getUniforms().forEach((name, uniform) -> {
                        switch (name) {
                            case "quality" -> uniform.set(lineWidth.getValue().intValue());
                            case "scale" -> uniform.set((float) (scale.getValue() * 1000));
                            case "time" -> uniform.set(com.ferra13671.BThack.api.Shaders.Shaders.INSTANCE.shaderTicker.getPassedTime() / 1000f);
                            case "resolution" -> uniform.set((float) mc.getWindow().getWidth(), mc.getWindow().getHeight());
                            case "brightness" -> uniform.set(brightness.getValue().floatValue());
                            case "saturation" -> uniform.set(saturation.getValue().floatValue());
                            case "fillAlpha" -> uniform.set(fillAlpha.getValue().floatValue() / 255f);
                            case "outlineAlpha" -> uniform.set(outlineAlpha.getValue().floatValue() / 255f);
                            case "speed" -> uniform.set(speed.getValue().floatValue() * 3);
                        }
                    });
                });
                rainbowXShader.render(builder, textureWidth, textureHeight, framebufferSet);
            }
            case "Rainbow_y" -> {
                ((IPostEffectProcessor) rainbowYShader)._getPasses().forEach(postEffectPass -> {
                    ShaderProgram shaderProgram = postEffectPass.getProgram();
                    ((IShaderProgram) shaderProgram)._getUniforms().forEach((name, uniform) -> {
                        switch (name) {
                            case "quality" -> uniform.set(lineWidth.getValue().intValue());
                            case "scale" -> uniform.set((float) (scale.getValue() * 1000));
                            case "time" -> uniform.set(com.ferra13671.BThack.api.Shaders.Shaders.INSTANCE.shaderTicker.getPassedTime() / 1000f);
                            case "resolution" -> uniform.set((float) mc.getWindow().getWidth(), mc.getWindow().getHeight());
                            case "brightness" -> uniform.set(brightness.getValue().floatValue());
                            case "saturation" -> uniform.set(saturation.getValue().floatValue());
                            case "fillAlpha" -> uniform.set(fillAlpha.getValue().floatValue() / 255f);
                            case "outlineAlpha" -> uniform.set(outlineAlpha.getValue().floatValue() / 255f);
                            case "speed" -> uniform.set(speed.getValue().floatValue() * 3);
                        }
                    });
                });
                rainbowYShader.render(builder, textureWidth, textureHeight, framebufferSet);
            }
        }
    }

    public void setArguments(FrameGraphBuilder builder, int textureWidth, int textureHeight, PostEffectProcessor.FramebufferSet framebufferSet) {
        this.builder = builder;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.framebufferSet = framebufferSet;
    }

    public PostEffectProcessor getShader() {
        return switch (shaderMode.getValue()) {
            default -> defaultShader;
            case "Gradient" -> gradientShader;
            case "Rainbow_xy" -> rainbowXYShader;
            case "Rainbow_x" -> rainbowXShader;
            case "Rainbow_y" -> rainbowYShader;
        };
    }

    public void loadShaders() {
        defaultShader = mc.getShaderLoader().loadPostEffect(Identifier.of("bthack", "default_outline"), DefaultFramebufferSet.MAIN_AND_ENTITY_OUTLINE);
        gradientShader = mc.getShaderLoader().loadPostEffect(Identifier.of("bthack", "gradient1_outline"), DefaultFramebufferSet.MAIN_AND_ENTITY_OUTLINE);
        rainbowXShader = mc.getShaderLoader().loadPostEffect(Identifier.of("bthack", "rainbowx_outline"), DefaultFramebufferSet.MAIN_AND_ENTITY_OUTLINE);
        rainbowYShader = mc.getShaderLoader().loadPostEffect(Identifier.of("bthack", "rainbowy_outline"), DefaultFramebufferSet.MAIN_AND_ENTITY_OUTLINE);
        rainbowXYShader = mc.getShaderLoader().loadPostEffect(Identifier.of("bthack", "rainbowxy_outline"), DefaultFramebufferSet.MAIN_AND_ENTITY_OUTLINE);
    }
}
