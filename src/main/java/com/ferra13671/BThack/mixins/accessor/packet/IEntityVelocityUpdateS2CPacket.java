package com.ferra13671.BThack.mixins.accessor.packet;

import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityVelocityUpdateS2CPacket.class)
public interface IEntityVelocityUpdateS2CPacket {

    @Mutable
    @Accessor("velocityX")
    void setVelocityX(int velocityX);

    @Accessor("velocityX")
    int _getVelocityX();

    @Mutable
    @Accessor("velocityY")
    void setVelocityY(int velocityY);

    @Accessor("velocityY")
    int _getVelocityY();

    @Mutable
    @Accessor("velocityZ")
    void setVelocityZ(int velocityZ);

    @Accessor("velocityZ")
    int _getVelocityZ();
}
