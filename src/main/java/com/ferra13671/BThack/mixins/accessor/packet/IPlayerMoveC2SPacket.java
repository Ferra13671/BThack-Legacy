package com.ferra13671.BThack.mixins.accessor.packet;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerMoveC2SPacket.class)
public interface IPlayerMoveC2SPacket {

    @Mutable
    @Accessor("x")
    void setX(double x);

    @Accessor("x")
    double _getX();

    @Mutable
    @Accessor("y")
    void setY(double y);

    @Accessor("y")
    double _getY();

    @Mutable
    @Accessor("z")
    void setZ(double z);

    @Accessor("z")
    double _getZ();

    @Mutable
    @Accessor("yaw")
    void setYaw(float yaw);

    @Accessor("yaw")
    float _getYaw();

    @Mutable
    @Accessor("pitch")
    void setPitch(float pitch);

    @Accessor("pitch")
    float _getPitch();

    @Mutable
    @Accessor("onGround")
    void setOnGround(boolean onGround);
}
