package com.ferra13671.BThack.api.Shader.MainMenu;

import java.util.HashMap;

public final class MainMenuShaders {

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
    /*
    very heavy shader, DO NOT TRY TO RUN IT!!!
    public static final MainMenuShader ROUGHSEA = new MainMenuShader(Identifier.of("bthack", "roughsea"), VertexFormats.POSITION, true);
     */
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



    //public static final MainMenuShader ROBOT = new MainMenuShader(Identifier.of("bthack", "robot"), VertexFormats.POSITION, true);

    private static final HashMap<String, MainMenuShader> shaders = new HashMap<>();
    static {
        //Oh yeah, such a number of shaders any client would be jealous)))

        shaders.put("BLOBS", BLOBS);
        shaders.put("BLUEGRID", BLUEGRID);
        shaders.put("BLUENEBULA", BLUENEBULA);
        shaders.put("BLUEVORTEX", BLUEVORTEX);
        shaders.put("BOREALIS", BOREALIS);
        shaders.put("BTHACK", BTHACK);
        shaders.put("BUBBLE", BUBBLE);
        shaders.put("BURGER", BURGER);
        shaders.put("CAVE", CAVE);
        shaders.put("CAVE2", CAVE2);
        shaders.put("CUBICPULSE", CUBICPULSE);
        shaders.put("CYBERNET", CYBERNET);
        shaders.put("DESERT", DESERT);
        shaders.put("DISINTEGRATION", DISINTEGRATION);
        shaders.put("DISINTEGRATION2", DISINTEGRATION2);
        shaders.put("DOUBLEGRID", DOUBLEGRID);
        shaders.put("DOUGHNUTS", DOUGHNUTS);
        shaders.put("FIRE", FIRE);
        shaders.put("FIRE2", FIRE2);
        shaders.put("JUMPINGPENIS", JUMPINGPENIS);
        shaders.put("JUPITER", JUPITER);
        shaders.put("LIQUID", LIQUID);
        shaders.put("LMAO", LMAO);
        shaders.put("MANDELBROT", MANDELBROT);
        shaders.put("MATRIX", MATRIX);
        shaders.put("MINECRAFT", MINECRAFT);
        shaders.put("MOUNTAINS", MOUNTAINS);
        shaders.put("NEON", NEON);
        shaders.put("NEON2", NEON2);
        shaders.put("NEONBAGEL", NEONBAGEL);
        shaders.put("NEONWAVE", NEONWAVE);
        shaders.put("NEONWAVE2", NEONWAVE2);
        shaders.put("NEONWAVE3", NEONWAVE3);
        shaders.put("NEONWAVE4", NEONWAVE4);
        shaders.put("NORTHERNLIGHTS", NORTHERNLIGHTS);
        shaders.put("PALETTE", PALETTE);
        shaders.put("PALETTE2", PALETTE2);
        shaders.put("PALETTE3", PALETTE3);
        shaders.put("PAPER", PAPER);
        shaders.put("PENISES", PENISES);
        shaders.put("PIXELS", PIXELS);
        shaders.put("PLANET", PLANET);
        shaders.put("PURPLEGRID", PURPLEGRID);
        shaders.put("PURPLEMIST", PURPLEMIST);
        shaders.put("REDGLOW", REDGLOW);
        //shaders.put("ROUGHSEA", ROUGHSEA);
        shaders.put("RUBBINGBALLS", RUBBINGBALLS);
        shaders.put("SEA", SEA);
        shaders.put("SEAANDMOON", SEAANDMOON);
        shaders.put("SIMPLEVORTEX", SIMPLEVORTEX);
        shaders.put("SIMPLEVORTEX2", SIMPLEVORTEX2);
        shaders.put("SKY", SKY);
        shaders.put("SNAKE", SNAKE);
        shaders.put("SPACE", SPACE);
        shaders.put("SPACE2", SPACE2);
        shaders.put("SPACE3", SPACE3);
        shaders.put("STEAM", STEAM);
        shaders.put("STORM", STORM);
        shaders.put("SUN", SUN);
        shaders.put("SWASTICA", SWASTICA);
        shaders.put("TRIANGLE", TRIANGLE);


        /*
        for (Field field : MainMenuShaders.class.getFields()) {   <------- I fuck this
            if ( Modifier.isStatic(field.getModifiers())
                    && Modifier.isPublic(field.getModifiers())
                    && Modifier.isFinal(field.getModifiers())
                    && field.getType().equals(MainMenuShader.class)
            ) {
                try {
                    MainMenuShader shader = (MainMenuShader) field.get(null);
                    shaders.put(field.getName(), shader);
                } catch (IllegalAccessException ignored) {}
            }
        }

         */
    }

    public static HashMap<String, MainMenuShader> getShaders() {
        return new HashMap<>(shaders);
    }

    private static MainMenuShader of(String path) {
        return MainMenuShader.of("mainmenu/info/" + path);
    }
}