package com.ferra13671.BThack.shaders;

import com.ferra13671.BThack.api.Interfaces.Mc;
import net.minecraft.client.gl.ShaderProgramKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class CoreShaderLoader implements Mc {
    private static final HashMap<String, ShaderProgramKey> shaderKeys = new HashMap<>();
    private static final List<BThackShaderProgram> bthackPrograms = new ArrayList<>();

    public static void addShaderKey(String id, ShaderProgramKey key) {
        shaderKeys.put(id, key);
    }

    public static void addBThackProgramToLoad(BThackShaderProgram bthackProgram) {
        bthackPrograms.add(bthackProgram);
    }

    public static HashMap<String, ShaderProgramKey> getShaderKeys() {
        return new HashMap<>(shaderKeys);
    }

    public static void loadPrograms() {
        bthackPrograms.forEach(program -> program.setShader(mc.getShaderLoader().getOrCreateProgram(program.getProgramKey())));
    }
}
