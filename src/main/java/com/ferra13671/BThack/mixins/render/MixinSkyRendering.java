package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.api.IMixin.ModifySkyRendering;
import com.ferra13671.BThack.core.Client.ModuleList;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SkyRendering.class)
public abstract class MixinSkyRendering implements ModifySkyRendering {
    @Mutable
    @Shadow @Final private VertexBuffer starBuffer;

    @Shadow protected abstract void tessellateStar(VertexConsumer vertexConsumer);

    @Override
    public void generateStarsMap() {
        if (starBuffer != null)
            starBuffer.close();

        starBuffer = VertexBuffer.createAndUpload(
                VertexFormat.DrawMode.QUADS,
                VertexFormats.POSITION,
                ModuleList.ambience.isEnabled() && ModuleList.ambience.customStars.getValue() ? ModuleList.ambience::tessellateStar : this::tessellateStar
        );
    }
}
