package com.ferra13671.BThack.mixins.accessor;

import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Screen.class)
public interface IScreen {

    @Invoker("init")
    void _init();
}
