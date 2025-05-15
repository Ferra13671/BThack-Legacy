package com.ferra13671.BThack.api.Shader.MainMenu;

import java.util.HashMap;

@SuppressWarnings("unused")
public final class MainMenuShaders {
    private static final HashMap<String, MainMenuShader> shaders = new HashMap<>();

    public static final MainMenuShader BLOBS = of("blobs");
    public static final MainMenuShader BLUEGRID = of("bluegrid");
    public static final MainMenuShader BLUENEBULA = of("bluenebula");
    public static final MainMenuShader BLUEVORTEX = of("bluevortex");
    public static final MainMenuShader BOREALIS = of("borealis");
    public static final MainMenuShader BTHACK = of("bthack");
    public static final MainMenuShader BUBBLE = of("bubble");
    public static final MainMenuShader BURGER = of("burger");
    public static final MainMenuShader CAVE = of("cave");
    public static final MainMenuShader CAVE2 = of("cave2");
    public static final MainMenuShader CUBICPULSE = of("cubicpulse");
    public static final MainMenuShader CYBERNET = of("cybernet");
    public static final MainMenuShader DESERT = of("desert");
    public static final MainMenuShader DISINTEGRATION = of("disintegration");
    public static final MainMenuShader DISINTEGRATION2 = of("disintegration2");
    public static final MainMenuShader DOUBLEGRID = of("doublegrid");
    public static final MainMenuShader DOUGHNUTS = of("doughnuts");
    public static final MainMenuShader FIRE = of("fire");
    public static final MainMenuShader FIRE2 = of("fire2");
    public static final MainMenuShader JUMPINGPENIS = of("jumpingpenis");
    public static final MainMenuShader JUPITER = of("jupiter");
    public static final MainMenuShader LIQUID = of("liquid");
    public static final MainMenuShader LMAO = of("lmao");
    public static final MainMenuShader MANDELBROT = of("mandelbrot");
    public static final MainMenuShader MATRIX = of("matrix");
    public static final MainMenuShader MINECRAFT = of("minecraft");
    public static final MainMenuShader MOUNTAINS = of("mountains");
    public static final MainMenuShader NEON = of("neon");
    public static final MainMenuShader NEON2 = of("neon2");
    public static final MainMenuShader NEONBAGEL = of("neonbagel");
    public static final MainMenuShader NEONWAVE = of("neonwave");
    public static final MainMenuShader NEONWAVE2 = of("neonwave2");
    public static final MainMenuShader NEONWAVE3 = of("neonwave3");
    public static final MainMenuShader NEONWAVE4 = of("neonwave4");
    public static final MainMenuShader NORTHERNLIGHTS = of("northernlights");
    public static final MainMenuShader PALETTE = of("palette");
    public static final MainMenuShader PALETTE2 = of("palette2");
    public static final MainMenuShader PALETTE3 = of("palette3");
    public static final MainMenuShader PAPER = of("paper");
    public static final MainMenuShader PENISES = of("penises");
    public static final MainMenuShader PIXELS = of("pixels");
    public static final MainMenuShader PLANET = of("planet");
    public static final MainMenuShader PURPLEGRID = of("purplegrid");
    public static final MainMenuShader PURPLEMIST = of("purplemist");
    public static final MainMenuShader REDGLOW = of("redglow");
    public static final MainMenuShader RUBBINGBALLS = of("rubbingballs");
    public static final MainMenuShader SEA = of("sea");
    public static final MainMenuShader SEAANDMOON = of("seaandmoon");
    public static final MainMenuShader SIMPLEVORTEX = of("simplevortex");
    public static final MainMenuShader SIMPLEVORTEX2 = of("simplevortex2");
    public static final MainMenuShader SKY = of("sky");
    public static final MainMenuShader SNAKE = of("snake");
    public static final MainMenuShader SPACE = of("space");
    public static final MainMenuShader SPACE2 = of("space2");
    public static final MainMenuShader SPACE3 = of("space3");
    public static final MainMenuShader STEAM = of("steam");
    public static final MainMenuShader STORM = of("storm");
    public static final MainMenuShader SUN = of("sun");
    public static final MainMenuShader SWASTICA = of("swastica");
    public static final MainMenuShader TRIANGLE = of("triangle");

    public static HashMap<String, MainMenuShader> getShaders() {
        return new HashMap<>(shaders);
    }

    private static MainMenuShader of(String path) {
        MainMenuShader mainMenuShader = MainMenuShader.of("mainmenu/info/" + path);
        shaders.put(path, mainMenuShader);
        return mainMenuShader;
    }
}