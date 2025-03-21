package com.ferra13671.BThack.api.Utils;

import com.ferra13671.TextureUtils.GLGif;
import com.ferra13671.TextureUtils.GLTexture;
import com.ferra13671.TextureUtils.PathMode;

public class Textures {
    public static final GLTexture EN_FLAG = GLTexture.fromPath("assets/bthack/textures/flags/en_flag.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA, false);
    public static final GLTexture RU_FLAG = GLTexture.fromPath("assets/bthack/textures/flags/ru_flag.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA, false);
    public static final GLTexture PL_FLAG = GLTexture.fromPath("assets/bthack/textures/flags/pl_flag.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA, false);
    public static final GLTexture BTHACK_LOGO = GLTexture.fromPath("assets/bthack/textures/bthacklogo.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA, true);
    public static final GLTexture CHIBI1 = GLTexture.fromPath("assets/bthack/textures/chibi/chibi1.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA, false);
    public static final GLTexture CHIBI2 = GLTexture.fromPath("assets/bthack/textures/chibi/chibi2.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA, false);
    public static final GLTexture CONFIGS = GLTexture.fromPath("assets/bthack/textures/configs.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA, true);
    public static final GLTexture CONFIG_FILE = GLTexture.fromPath("assets/bthack/textures/config_file.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA, true);
    public static final GLTexture SHOW = GLTexture.fromPath("assets/bthack/textures/show.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA, false);
    public static final GLTexture HIDE = GLTexture.fromPath("assets/bthack/textures/hide.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA, false);

    //Gifs
    public static final GLGif CAIPIRINHA = GLGif.fromInputStream(Textures.class.getClassLoader().getResourceAsStream("assets/bthack/gifs/caipirinha.gif"), GLGif.DecompileMode.DELTAS, 150);
    public static final GLGif CUTIE1 = GLGif.fromInputStream(Textures.class.getClassLoader().getResourceAsStream("assets/bthack/gifs/cutie.gif"), GLGif.DecompileMode.DELTAS, 50);
    public static final GLGif CUTIE2 = GLGif.fromInputStream(Textures.class.getClassLoader().getResourceAsStream("assets/bthack/gifs/cutie2.gif"), GLGif.DecompileMode.DELTAS, 100);
}
