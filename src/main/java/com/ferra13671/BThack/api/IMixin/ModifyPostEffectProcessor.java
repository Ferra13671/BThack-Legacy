package com.ferra13671.BThack.api.IMixin;

import net.minecraft.client.gl.Framebuffer;

public interface ModifyPostEffectProcessor {
    void _addTargetHook(String name, Framebuffer buffer);
}
