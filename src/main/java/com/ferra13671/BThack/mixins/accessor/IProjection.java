package com.ferra13671.BThack.mixins.accessor;

import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Camera.Projection.class)
public interface IProjection {

    @Accessor("center")
    Vec3d getCenter();
}
