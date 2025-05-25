package com.ferra13671.BThack.shaders;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Utils.Mc;
import net.minecraft.client.gl.Defines;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

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

    public static ShaderProgramKey getShaderKey(String id) {
        ShaderProgramKey shaderProgramKey = shaderKeys.get(id);
        if (shaderProgramKey == null) BThack.error(String.format("ShaderProgramKey with id '%s' is null!", id));
        return shaderProgramKey;
    }

    public static void loadPrograms() {
        bthackPrograms.forEach(program -> program.setShader(mc.getShaderLoader().getOrCreateProgram(program.getProgramKey())));
    }

    public static void initShaderKeys(List<ShaderProgramKey> minecraftShaderKeysList) {
        minecraftShaderKeysList.addAll(ShaderKeys.getRenderShaderKeys());
        minecraftShaderKeysList.addAll(ShaderKeys.getMenuShaderKeys());
    }

    public static ShaderProgramKey createShaderProgramKey(String path) {
        ShaderProgramKey shaderProgramKey = new ShaderProgramKey(Identifier.of("bthack", "core/" + path), VertexFormats.POSITION, Defines.EMPTY);
        addShaderKey(path, shaderProgramKey);
        return shaderProgramKey;
    }
}
