package com.ferra13671.BThack.api.Utils;

import com.ferra13671.TextureUtils.GLGif;
import com.ferra13671.TextureUtils.GLTexture;
import com.ferra13671.TextureUtils.PathMode;

public class Textures {
    public static final GLTexture EN_FLAG = GLTexture.fromPath("assets/bthack/flags/en_flag.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA);
    public static final GLTexture RU_FLAG = GLTexture.fromPath("assets/bthack/flags/ru_flag.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA);
    public static final GLTexture PL_FLAG = GLTexture.fromPath("assets/bthack/flags/pl_flag.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA);
    public static final GLTexture BTHACK_LOGO = GLTexture.fromPath("assets/bthack/bthacklogo.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA);
    public static final GLTexture CHIBI1 = GLTexture.fromPath("assets/bthack/chibi/chibi1.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA);
    public static final GLTexture CHIBI2 = GLTexture.fromPath("assets/bthack/chibi/chibi2.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA);

    //Gifs
    public static final GLGif CAIPIRINHA = GLGif.fromInputStream(Textures.class.getClassLoader().getResourceAsStream("assets/bthack/gifs/caipirinha.gif"), GLGif.DecompileMode.DELTAS, 150);
    public static final GLGif CUTIE1 = GLGif.fromInputStream(Textures.class.getClassLoader().getResourceAsStream("assets/bthack/gifs/cutie.gif"), GLGif.DecompileMode.DELTAS, 50);
    public static final GLGif CUTIE2 = GLGif.fromInputStream(Textures.class.getClassLoader().getResourceAsStream("assets/bthack/gifs/cutie2.gif"), GLGif.DecompileMode.DELTAS, 100);
}
