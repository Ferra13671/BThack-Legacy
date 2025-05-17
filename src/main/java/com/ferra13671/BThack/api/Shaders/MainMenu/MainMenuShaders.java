package com.ferra13671.BThack.api.Shaders.MainMenu;

import java.util.HashMap;

@SuppressWarnings("unused")
public final class MainMenuShaders {
    private static final HashMap<String, MainMenuBThackShader> shaders = new HashMap<>();

    public static final MainMenuBThackShader BLOBS = of("blobs");
    public static final MainMenuBThackShader BLUEGRID = of("bluegrid");
    public static final MainMenuBThackShader BLUENEBULA = of("bluenebula");
    public static final MainMenuBThackShader BLUEVORTEX = of("bluevortex");
    public static final MainMenuBThackShader BOREALIS = of("borealis");
    public static final MainMenuBThackShader BTHACK = of("bthack");
    public static final MainMenuBThackShader BUBBLE = of("bubble");
    public static final MainMenuBThackShader BURGER = of("burger");
    public static final MainMenuBThackShader CAVE = of("cave");
    public static final MainMenuBThackShader CAVE2 = of("cave2");
    public static final MainMenuBThackShader CUBICPULSE = of("cubicpulse");
    public static final MainMenuBThackShader CYBERNET = of("cybernet");
    public static final MainMenuBThackShader DESERT = of("desert");
    public static final MainMenuBThackShader DISINTEGRATION = of("disintegration");
    public static final MainMenuBThackShader DISINTEGRATION2 = of("disintegration2");
    public static final MainMenuBThackShader DOUBLEGRID = of("doublegrid");
    public static final MainMenuBThackShader DOUGHNUTS = of("doughnuts");
    public static final MainMenuBThackShader FIRE = of("fire");
    public static final MainMenuBThackShader FIRE2 = of("fire2");
    public static final MainMenuBThackShader JUMPINGPENIS = of("jumpingpenis");
    public static final MainMenuBThackShader JUPITER = of("jupiter");
    public static final MainMenuBThackShader LIQUID = of("liquid");
    public static final MainMenuBThackShader LMAO = of("lmao");
    public static final MainMenuBThackShader MANDELBROT = of("mandelbrot");
    public static final MainMenuBThackShader MATRIX = of("matrix");
    public static final MainMenuBThackShader MINECRAFT = of("minecraft");
    public static final MainMenuBThackShader MOUNTAINS = of("mountains");
    public static final MainMenuBThackShader NEON = of("neon");
    public static final MainMenuBThackShader NEON2 = of("neon2");
    public static final MainMenuBThackShader NEONBAGEL = of("neonbagel");
    public static final MainMenuBThackShader NEONWAVE = of("neonwave");
    public static final MainMenuBThackShader NEONWAVE2 = of("neonwave2");
    public static final MainMenuBThackShader NEONWAVE3 = of("neonwave3");
    public static final MainMenuBThackShader NEONWAVE4 = of("neonwave4");
    public static final MainMenuBThackShader NORTHERNLIGHTS = of("northernlights");
    public static final MainMenuBThackShader PALETTE = of("palette");
    public static final MainMenuBThackShader PALETTE2 = of("palette2");
    public static final MainMenuBThackShader PALETTE3 = of("palette3");
    public static final MainMenuBThackShader PAPER = of("paper");
    public static final MainMenuBThackShader PENISES = of("penises");
    public static final MainMenuBThackShader PIXELS = of("pixels");
    public static final MainMenuBThackShader PLANET = of("planet");
    public static final MainMenuBThackShader PURPLEGRID = of("purplegrid");
    public static final MainMenuBThackShader PURPLEMIST = of("purplemist");
    public static final MainMenuBThackShader REDGLOW = of("redglow");
    public static final MainMenuBThackShader RUBBINGBALLS = of("rubbingballs");
    public static final MainMenuBThackShader SEA = of("sea");
    public static final MainMenuBThackShader SEAANDMOON = of("seaandmoon");
    public static final MainMenuBThackShader SIMPLEVORTEX = of("simplevortex");
    public static final MainMenuBThackShader SIMPLEVORTEX2 = of("simplevortex2");
    public static final MainMenuBThackShader SKY = of("sky");
    public static final MainMenuBThackShader SNAKE = of("snake");
    public static final MainMenuBThackShader SPACE = of("space");
    public static final MainMenuBThackShader SPACE2 = of("space2");
    public static final MainMenuBThackShader SPACE3 = of("space3");
    public static final MainMenuBThackShader STEAM = of("steam");
    public static final MainMenuBThackShader STORM = of("storm");
    public static final MainMenuBThackShader SUN = of("sun");
    public static final MainMenuBThackShader SWASTICA = of("swastica");
    public static final MainMenuBThackShader TRIANGLE = of("triangle");

    public static HashMap<String, MainMenuBThackShader> getShaders() {
        return new HashMap<>(shaders);
    }

    private static MainMenuBThackShader of(String path) {
        MainMenuBThackShader mainMenuShader = MainMenuBThackShader.of(path);
        shaders.put(path, mainMenuShader);
        return mainMenuShader;
    }
}