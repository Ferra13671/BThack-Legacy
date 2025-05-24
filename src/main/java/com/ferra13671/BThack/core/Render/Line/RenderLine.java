package com.ferra13671.BThack.core.Render.Line;

import com.ferra13671.BThack.api.Utils.Mc;
import com.ferra13671.BThack.api.Utils.EntityUtils;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class RenderLine implements Mc {

    public final Vec3d vec3d;
    public final float red;
    public final float green;
    public final float blue;
    public final float alpha;

    public RenderLine(Vec3d vec3d, float red, float green, float blue, float alpha) {
        this.vec3d = vec3d;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public RenderLine(Entity entity, float red, float green, float blue, float alpha) {
        this(EntityUtils.getLerpedBox(entity, mc.getRenderTickCounter().getTickDelta(true)).getCenter(), red, green, blue, alpha);
    }

    public RenderLine(BlockEntity blockEntity, float red, float green, float blue, float alpha) {
        this(EntityUtils.getLerpedBox(blockEntity, mc.getRenderTickCounter().getTickDelta(true)).getCenter(), red, green, blue, alpha);
    }
}
