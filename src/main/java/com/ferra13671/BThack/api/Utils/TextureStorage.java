package com.ferra13671.BThack.api.Utils;

import com.ferra13671.TextureUtils.GLTexture;
import com.ferra13671.TextureUtils.PathMode;

import java.util.HashMap;
import java.util.Map;

public class TextureStorage {
    public static final GLTexture EN_FLAG = GLTexture.fromPath("assets/bthack/flags/en_flag.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA);
    public static final GLTexture RU_FLAG = GLTexture.fromPath("assets/bthack/flags/ru_flag.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA);
    public static final GLTexture PL_FLAG = GLTexture.fromPath("assets/bthack/flags/pl_flag.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA);
    public static final GLTexture BTHACK_LOGO = GLTexture.fromPath("assets/bthack/bthacklogo.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA);

    private static final Map<String, GLTexture> TEXTURES = new HashMap<>();

    public static boolean addTexture(String key, GLTexture texture) {
        if (TEXTURES.containsKey(key)) return false;
        TEXTURES.put(key, texture);
        return true;
    }

    public static boolean removeTexture(String key) {
        if (!TEXTURES.containsKey(key)) return false;
        TEXTURES.remove(key);
        return true;
    }

    public static GLTexture getTexture(String key) {
        return TEXTURES.get(key);
    }

    private static GLTexture register(String key, GLTexture texture) {
        TEXTURES.put(key, texture);
        return texture;
    }
}
