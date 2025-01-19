package com.ferra13671.BThack.mixins.accessor.packet;

import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerInputC2SPacket.class)
public interface IPlayerInputC2SPacket {

    @Mutable
    @Accessor("sideways")
    float _getSideways();

    @Mutable
    @Accessor("sideways")
    void _setSideways(float sideways);

    @Mutable
    @Accessor("forward")
    float _getForward();

    @Mutable
    @Accessor("forward")
    void _setForward(float forward);

    @Mutable
    @Accessor("jumping")
    boolean _getJumping();

    @Mutable
    @Accessor("jumping")
    void _setJumping(boolean jumping);

    @Mutable
    @Accessor("sneaking")
    boolean _getSneaking();

    @Mutable
    @Accessor("sneaking")
    void _setSneaking(boolean sneaking);
}
