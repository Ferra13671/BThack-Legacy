package com.ferra13671.BThack.api.Utils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public final class ModifyBlockPos extends BlockPos {

    public ModifyBlockPos(double x, double y, double z) {
        super(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
    }

    public ModifyBlockPos(int x, int y, int z) {
        super(x, y, z);
    }

    public ModifyBlockPos(Vec3i vec3i) {
        super(vec3i);
    }

    public ModifyBlockPos(Vec3d vec3d) {
        super(MathHelper.floor(vec3d.x), MathHelper.floor(vec3d.y), MathHelper.floor(vec3d.z));
    }
}
