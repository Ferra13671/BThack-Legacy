package com.ferra13671.BThack.mixins.accessor.packet;

import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ExplosionS2CPacket.class)
public interface IExplosionS2CPacket {

    @Mutable
    @Accessor("playerVelocityX")
    void setPlayerVelocityX(float playerVelocityX);

    @Accessor("playerVelocityX")
    float _getPlayerVelocityX();

    @Mutable
    @Accessor("playerVelocityY")
    void setPlayerVelocityY(float playerVelocityY);

    @Accessor("playerVelocityY")
    float _getPlayerVelocityY();

    @Mutable
    @Accessor("playerVelocityZ")
    void setPlayerVelocityZ(float playerVelocityZ);

    @Accessor("playerVelocityZ")
    float _getPlayerVelocityZ();
}
