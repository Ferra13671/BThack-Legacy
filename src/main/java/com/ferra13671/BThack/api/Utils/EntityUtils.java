package com.ferra13671.BThack.api.Utils;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class EntityUtils {

    public static Vec3d getLerpedPos(Entity e, float partialTicks) {
        if (e.isRemoved())
            return e.getPos();

        double x = MathHelper.lerp(partialTicks, e.lastRenderX, e.getX());
        double y = MathHelper.lerp(partialTicks, e.lastRenderY, e.getY());
        double z = MathHelper.lerp(partialTicks, e.lastRenderZ, e.getZ());
        return new Vec3d(x, y, z);
    }

    public static Box getLerpedBox(Entity e, float partialTicks) {
        if (e.isRemoved())
            return e.getBoundingBox();

        Vec3d offset = getLerpedPos(e, partialTicks).subtract(e.getPos());
        return e.getBoundingBox().offset(offset);
    }

    public static Vec3d getLerpedPos(BlockEntity e, float partialTicks) {
        Vec3d pos = e.getPos().toCenterPos();
        double x = MathHelper.lerp(partialTicks, pos.getX(), pos.getX());
        double y = MathHelper.lerp(partialTicks, pos.getY(), pos.getY());
        double z = MathHelper.lerp(partialTicks, pos.getZ(), pos.getZ());
        return new Vec3d(x, y, z);
    }

    public static Box getLerpedBox(BlockEntity e, float partialTicks) {
        Vec3d offset = getLerpedPos(e, partialTicks).subtract(e.getPos().toCenterPos());
        return BlockUtils.getBox(e).offset(offset);
    }
}
