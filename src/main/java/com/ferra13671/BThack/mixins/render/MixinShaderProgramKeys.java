package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.api.Shaders.CoreShaderLoader;
import net.minecraft.client.gl.Defines;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ShaderProgramKeys.class)
public abstract class MixinShaderProgramKeys {

    @Shadow @Final private static List<ShaderProgramKey> ALL;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void modifyStaticInit(CallbackInfo ci) {
        //MainMenu Shaders
        regMainMenu("blobs");
        regMainMenu("bluegrid");
        regMainMenu("bluenebula");
        regMainMenu("bluevortex");
        regMainMenu("borealis");
        regMainMenu("bthack");
        regMainMenu("bubble");
        regMainMenu("burger");
        regMainMenu("cave");
        regMainMenu("cave2");
        regMainMenu("cubicpulse");
        regMainMenu("cybernet");
        regMainMenu("desert");
        regMainMenu("disintegration");
        regMainMenu("disintegration2");
        regMainMenu("doublegrid");
        regMainMenu("doughnuts");
        regMainMenu("fire");
        regMainMenu("fire2");
        regMainMenu("jumpingpenis");
        regMainMenu("jupiter");
        regMainMenu("liquid");
        regMainMenu("lmao");
        regMainMenu("mandelbrot");
        regMainMenu("matrix");
        regMainMenu("minecraft");
        regMainMenu("mountains");
        regMainMenu("neon");
        regMainMenu("neon2");
        regMainMenu("neonbagel");
        regMainMenu("neonwave");
        regMainMenu("neonwave2");
        regMainMenu("neonwave3");
        regMainMenu("neonwave4");
        regMainMenu("northernlights");
        regMainMenu("palette");
        regMainMenu("palette2");
        regMainMenu("palette3");
        regMainMenu("paper");
        regMainMenu("penises");
        regMainMenu("pixels");
        regMainMenu("planet");
        regMainMenu("purplegrid");
        regMainMenu("purplemist");
        regMainMenu("redglow");
        regMainMenu("rubbingballs");
        regMainMenu("sea");
        regMainMenu("seaandmoon");
        regMainMenu("simplevortex");
        regMainMenu("simplevortex2");
        regMainMenu("sky");
        regMainMenu("snake");
        regMainMenu("space");
        regMainMenu("space2");
        regMainMenu("space3");
        regMainMenu("steam");
        regMainMenu("storm");
        regMainMenu("sun");
        regMainMenu("swastica");
        regMainMenu("triangle");

        //Render Shaders
        regRender("position");
        regRender("rounded_rect");
        regRender("rounded_rect_with_outline");
        regRender("xy_gradient_rounded_rect_with_outline");
        regRender("x_rainbow");
        regRender("xy_gradient");
        regRender("snow");
    }

    @Unique
    private static void regMainMenu(String name) {
        CoreShaderLoader.addShaderKey(name, regInternal("mainmenu/info/" + name));
    }

    @Unique
    private static void regRender(String name) {
        CoreShaderLoader.addShaderKey(name, regInternal("render/" + name));
    }

    @Unique
    private static ShaderProgramKey regInternal(String is) {
        ShaderProgramKey shaderProgramKey = new ShaderProgramKey(Identifier.of("bthack", "core/" + is), VertexFormats.POSITION, Defines.EMPTY);
        ALL.add(shaderProgramKey);
        return shaderProgramKey;
    }
}
