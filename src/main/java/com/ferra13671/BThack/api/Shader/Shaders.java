package com.ferra13671.BThack.api.Shader;

import net.minecraft.client.render.VertexFormats;

public class Shaders {
    public static Shaders INSTANCE;

    public final ShaderProgram POSITION = ShaderProgram.of("render/position", VertexFormats.POSITION);
}
