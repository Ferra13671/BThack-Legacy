package com.ferra13671.BThack.mixins.accessor;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MinecraftClient.class)
public interface IMinecraftClient {

    @Invoker("doItemUse")
    void useItem();

    @Invoker("doAttack")
    boolean attack();
}
