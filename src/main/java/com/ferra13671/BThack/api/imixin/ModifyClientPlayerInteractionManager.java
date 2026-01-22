package com.ferra13671.BThack.api.imixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public interface ModifyClientPlayerInteractionManager {

    void attackBlockNoEvent(BlockPos pos, Direction direction);
}
