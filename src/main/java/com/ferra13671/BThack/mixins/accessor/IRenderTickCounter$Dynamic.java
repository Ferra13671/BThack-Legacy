package com.ferra13671.BThack.mixins.accessor;

import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderTickCounter.Dynamic.class)
public interface IRenderTickCounter$Dynamic {

    @Accessor("tickTime")
    float getTickTime();
}
