package com.ferra13671.BThack.shaders;

import net.minecraft.client.gl.ShaderProgramKey;

import java.util.Arrays;
import java.util.List;

public final class ShaderKeys {

    public static List<ShaderProgramKey> getRenderShaderKeys() {
        return Arrays.asList(
                getRenderShaderKey("position"),
                getRenderShaderKey("rounded_rect"),
                getRenderShaderKey("rounded_rect_with_outline"),
                getRenderShaderKey("xy_gradient_rounded_rect_with_outline"),
                getRenderShaderKey("x_rainbow"),
                getRenderShaderKey("xy_gradient"),
                getRenderShaderKey("snow")
        );
    }

    public static List<ShaderProgramKey> getMenuShaderKeys() {
        return Arrays.asList(
                getMenuShaderKey("blobs"),
                getMenuShaderKey("bluegrid"),
                getMenuShaderKey("bluenebula"),
                getMenuShaderKey("bluevortex"),
                getMenuShaderKey("borealis"),
                getMenuShaderKey("bthack"),
                getMenuShaderKey("bubble"),
                getMenuShaderKey("burger"),
                getMenuShaderKey("cave"),
                getMenuShaderKey("cave2"),
                getMenuShaderKey("cubicpulse"),
                getMenuShaderKey("cybernet"),
                getMenuShaderKey("desert"),
                getMenuShaderKey("disintegration"),
                getMenuShaderKey("disintegration2"),
                getMenuShaderKey("doublegrid"),
                getMenuShaderKey("doughnuts"),
                getMenuShaderKey("fire"),
                getMenuShaderKey("fire2"),
                getMenuShaderKey("jumpingpenis"),
                getMenuShaderKey("jupiter"),
                getMenuShaderKey("liquid"),
                getMenuShaderKey("lmao"),
                getMenuShaderKey("mandelbrot"),
                getMenuShaderKey("matrix"),
                getMenuShaderKey("minecraft"),
                getMenuShaderKey("mountains"),
                getMenuShaderKey("neon"),
                getMenuShaderKey("neon2"),
                getMenuShaderKey("neonbagel"),
                getMenuShaderKey("neonwave"),
                getMenuShaderKey("neonwave2"),
                getMenuShaderKey("neonwave3"),
                getMenuShaderKey("neonwave4"),
                getMenuShaderKey("northernlights"),
                getMenuShaderKey("palette"),
                getMenuShaderKey("palette2"),
                getMenuShaderKey("palette3"),
                getMenuShaderKey("paper"),
                getMenuShaderKey("penises"),
                getMenuShaderKey("pixels"),
                getMenuShaderKey("planet"),
                getMenuShaderKey("purplegrid"),
                getMenuShaderKey("purplemist"),
                getMenuShaderKey("redglow"),
                getMenuShaderKey("rubbingballs"),
                getMenuShaderKey("sea"),
                getMenuShaderKey("seaandmoon"),
                getMenuShaderKey("simplevortex"),
                getMenuShaderKey("simplevortex2"),
                getMenuShaderKey("sky"),
                getMenuShaderKey("snake"),
                getMenuShaderKey("space"),
                getMenuShaderKey("space2"),
                getMenuShaderKey("space3"),
                getMenuShaderKey("steam"),
                getMenuShaderKey("storm"),
                getMenuShaderKey("sun"),
                getMenuShaderKey("swastica"),
                getMenuShaderKey("triangle")
        );
    }

    private static ShaderProgramKey getMenuShaderKey(String name) {
        return CoreShaderLoader.createShaderProgramKey("mainmenu/info/" + name);
    }

    private static ShaderProgramKey getRenderShaderKey(String name) {
        return CoreShaderLoader.createShaderProgramKey("render/" + name);
    }
}
