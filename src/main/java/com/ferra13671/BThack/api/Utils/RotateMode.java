package com.ferra13671.BThack.api.Utils;

import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.Grim.GrimUtils;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;

import java.util.function.BiConsumer;

public enum RotateMode implements Mc {
    NONE((yaw, pitch) -> {}, () -> {}),
    PACKET1(AimBotUtils::packetRotate, () -> AimBotUtils.packetRotate(mc.player.getYaw(), mc.player.getPitch())),
    PACKET2(AimBotUtils::packetRotate, () -> {}),
    GRIM(GrimUtils::sendPreActionGrimPackets, GrimUtils::sendPostActionGrimPackets),
    VANILLA(AimBotUtils::rotate, () -> {});

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
