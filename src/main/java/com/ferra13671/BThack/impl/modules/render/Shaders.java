package com.ferra13671.BThack.impl.modules.render;

import com.ferra13671.BThack.managers.impl.setting.Settings.*;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.mixins.accessor.IPostEffectProcessor;
import com.ferra13671.BThack.mixins.accessor.IShaderProgram;
import com.ferra13671.BThack.shaders.CoreShaders;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.DefaultFramebufferSet;
import net.minecraft.client.render.FrameGraphBuilder;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.Arrays;
import java.util.function.BiConsumer;

@ModuleInfo(name = "Shaders", description = "lang.module.Shaders", category = "RENDER")
public class Shaders extends Module {
    public PostEffectProcessor[] defaultShader;
    public PostEffectProcessor[] gradientShader;
    public PostEffectProcessor[] rainbowXShader;
    public PostEffectProcessor[] rainbowYShader;
    public PostEffectProcessor[] rainbowXYShader;

    public final ModeSetting shaderMode = new ModeSetting("Shader", this, Arrays.asList("Default", "Gradient", "Rainbow_xy", "Rainbow_x", "Rainbow_y"));

    //Default
    public final ColorSetting fillColor = new ColorSetting("Fill Color", this, new Color(255, 255, 255, 0), () -> shaderMode.getValue().equals("Default"));
    public final ColorSetting outlineColor = new ColorSetting("Outline Color", this, new Color(213, 142, 253, 255), () -> shaderMode.getValue().equals("Default"));

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

    public final NumberSetting lineWidth = new NumberSetting("Line Width", this, 1, 0, 6, true);
    //Bloom
    public final CategorySetting bloomCategory = new CategorySetting("Bloom", this);
    public final BooleanSetting bloom = new BooleanSetting("Bloom", this, true).inCategory(bloomCategory);
    public final NumberSetting bloomWidth = new NumberSetting("Bloom Width", this, 5, 0, 15, true, bloom::getValue).inCategory(bloomCategory);
    public final NumberSetting bloomFactor = new NumberSetting("Bloom Factor", this, 5, 1, 20, true, bloom::getValue).inCategory(bloomCategory);

    public final CategorySetting targetsCategory = new CategorySetting("Targets", this);
    public final BooleanSetting players = new BooleanSetting("Players", this, true).inCategory(targetsCategory);
    public final BooleanSetting items = new BooleanSetting("Items", this, true).inCategory(targetsCategory);
    public final BooleanSetting hostiles = new BooleanSetting("Hostiles", this, true).inCategory(targetsCategory);
    public final BooleanSetting golems = new BooleanSetting("Golems", this, true).inCategory(targetsCategory);
    public final BooleanSetting passive = new BooleanSetting("Passive", this, true).inCategory(targetsCategory);
    public final BooleanSetting hands = new BooleanSetting("Hands", this, true).inCategory(targetsCategory);
    public final BooleanSetting self = new BooleanSetting("Self", this, true).inCategory(targetsCategory);
    public final BooleanSetting crystals = new BooleanSetting("Crystals", this, true).inCategory(targetsCategory);

    public void drawShader(FrameGraphBuilder builder, int textureWidth, int textureHeight, PostEffectProcessor.FramebufferSet framebufferSet) {
        PostEffectProcessor postEffectProcessor = switch (shaderMode.getValue()) {
            default -> {
                PostEffectProcessor processor = getEffectProcessor(defaultShader);
                setupUniforms(processor, (name, uniform) -> {
                    defaultSetup(name, uniform);
                    switch (name) {
                        case "color" -> uniform.set(fillColor.getValue().getRed() / 255f, fillColor.getValue().getGreen() / 255f, fillColor.getValue().getBlue() / 255f, fillColor.getValue().getAlpha() / 255f);
                        case "outlinecolor" -> uniform.set(outlineColor.getValue().getRed() / 255f, outlineColor.getValue().getGreen() / 255f, outlineColor.getValue().getBlue() / 255f, outlineColor.getValue().getAlpha() / 255f);
                    }
                });
                yield processor;
            }
            case "Gradient" -> {
                PostEffectProcessor processor = getEffectProcessor(gradientShader);
                setupUniforms(processor, (name, uniform) -> {
                    defaultSetup(name, uniform);
                    modifiedSetup(name, uniform);
                    switch (name) {
                        case "color1" -> uniform.set((float) color1.getValue().getRed() / 255f, (float) color1.getValue().getGreen() / 255f, (float) color1.getValue().getBlue() / 255f);
                        case "color2" -> uniform.set((float) color2.getValue().getRed() / 255f, (float) color2.getValue().getGreen() / 255f, (float) color2.getValue().getBlue() / 255f);
                    }
                });
                yield processor;
            }
            case "Rainbow_xy" -> {
                PostEffectProcessor processor = getEffectProcessor(rainbowXYShader);
                setupUniforms(processor, (name, uniform) -> {
                    defaultSetup(name, uniform);
                    modifiedSetup(name, uniform);
                    rainbowSetup(name, uniform);
                });
                yield processor;
            }
            case "Rainbow_x" -> {
                PostEffectProcessor processor = getEffectProcessor(rainbowXShader);
                setupUniforms(processor, (name, uniform) -> {
                    defaultSetup(name, uniform);
                    modifiedSetup(name, uniform);
                    rainbowSetup(name, uniform);
                });
                yield processor;
            }
            case "Rainbow_y" -> {
                PostEffectProcessor processor = getEffectProcessor(rainbowYShader);
                setupUniforms(processor, (name, uniform) -> {
                    defaultSetup(name, uniform);
                    modifiedSetup(name, uniform);
                    rainbowSetup(name, uniform);
                });
                yield processor;
            }
        };
        postEffectProcessor.render(builder, textureWidth, textureHeight, framebufferSet);
    }

    public PostEffectProcessor getEffectProcessor(PostEffectProcessor[] shader) {
        return bloom.getValue() ? shader[1] : shader[0];
    }

    public void defaultSetup(String name, GlUniform uniform) {
        switch (name) {
            case "quality" -> uniform.set(lineWidth.getValue().intValue());
            case "extra_quality" -> uniform.set(bloomWidth.getValue().intValue());
            case "Radius" -> uniform.set(bloomFactor.getValue().floatValue());
        }
    }

    public void modifiedSetup(String name, GlUniform uniform) {
        switch (name) {
            case "scale" -> uniform.set((float) (scale.getValue() * 1000));
            case "time" -> uniform.set(CoreShaders.shaderTicker.getPassedTime() / 1000f);
            case "resolution" -> uniform.set((float) mc.getWindow().getWidth(), mc.getWindow().getHeight());
            case "fillAlpha" -> uniform.set(fillAlpha.getValue().floatValue() / 255f);
            case "outlineAlpha" -> uniform.set(outlineAlpha.getValue().floatValue() / 255f);
            case "speed" -> uniform.set(speed.getValue().floatValue() * 3);
        }
    }

    public void rainbowSetup(String name, GlUniform uniform) {
        switch (name) {
            case "brightness" -> uniform.set(brightness.getValue().floatValue());
            case "saturation" -> uniform.set(saturation.getValue().floatValue());
        }
    }

    public void setupUniforms(PostEffectProcessor postEffectProcessor, BiConsumer<String, GlUniform> biConsumer) {
        ((IPostEffectProcessor) postEffectProcessor)._getPasses().forEach(postEffectPass -> {
            ShaderProgram shaderProgram = postEffectPass.getProgram();
            ((IShaderProgram) shaderProgram)._getUniforms().forEach(biConsumer);
        });
    }

    public void loadShaders() {
        defaultShader = loadShader("default");
        gradientShader = loadShader("gradient");
        rainbowXShader = loadShader("rainbowx");
        rainbowYShader = loadShader("rainbowy");
        rainbowXYShader = loadShader("rainbowxy");
    }

    private PostEffectProcessor[] loadShader(String path) {
        return new PostEffectProcessor[]{
                mc.getShaderLoader().loadPostEffect(Identifier.of("bthack", path + "_outline"), DefaultFramebufferSet.MAIN_AND_ENTITY_OUTLINE),
                mc.getShaderLoader().loadPostEffect(Identifier.of("bthack", path + "_bloom_outline"), DefaultFramebufferSet.MAIN_AND_ENTITY_OUTLINE)
        };
    }
}
