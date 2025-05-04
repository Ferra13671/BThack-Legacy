package com.ferra13671.BThack.api.Utils.Rotate;

import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.Grim.GrimUtils;

import java.util.function.BiConsumer;

public enum RotateMode implements Mc {
    NONE((yaw, pitch) -> {}, () -> {}),
    PACKET1(RotateUtils::packetRotate, () -> RotateUtils.packetRotate(mc.player.getYaw(), mc.player.getPitch())),
    PACKET2(RotateUtils::packetRotate, () -> {}),
    GRIM(GrimUtils::sendPreActionGrimPackets, GrimUtils::sendPostActionGrimPackets),
    VANILLA(RotateUtils::rotate, () -> {});

    private final BiConsumer<Float, Float> preRotate;
    private final Runnable postRotate;

    RotateMode(BiConsumer<Float, Float> preRotate, Runnable postRotate) {
        this.preRotate = preRotate;
        this.postRotate = postRotate;
    }

    public void preRotate(float yaw, float pitch) {
        preRotate.accept(yaw, pitch);
    }

    public void postRotate() {
        postRotate.run();
    }
}
