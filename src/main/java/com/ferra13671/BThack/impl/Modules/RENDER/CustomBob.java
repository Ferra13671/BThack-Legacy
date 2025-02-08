package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class CustomBob extends Module {

    public final NumberSetting TXStrength = new NumberSetting("TX Strength", this, 0.45, 0, 10, false);
    public final NumberSetting TYStrength = new NumberSetting("TY Strength", this, 0.3, 0, 10, false);
    public final NumberSetting RXStrength = new NumberSetting("RX Strength", this, 1.35, 0, 10, false);
    public final NumberSetting RZStrength = new NumberSetting("RZ Strength", this, 1.5, 0, 10, false);

    public CustomBob() {
        super("CustomBob",
                "lang.module.CustomBob",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        initSettings(
                TXStrength,
                TYStrength,
                RXStrength,
                RZStrength
        );
    }

    public void customBob(MatrixStack matrices) {
        PlayerEntity playerEntity = (PlayerEntity) mc.getCameraEntity();
        float f = playerEntity.horizontalSpeed - playerEntity.prevHorizontalSpeed;
        float g = -(playerEntity.horizontalSpeed + f * mc.getRenderTickCounter().getTickDelta(true));
        float h = MathHelper.lerp(mc.getRenderTickCounter().getTickDelta(true), playerEntity.prevStrideDistance, playerEntity.strideDistance);
        matrices.translate((MathHelper.sin(g * 3.1415927F) * h * 0.5F) * (float) TXStrength.getValue(), (-Math.abs(MathHelper.cos(g * 3.1415927F) * h)) * (float) TYStrength.getValue(), 0.0F);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((MathHelper.sin(g * 3.1415927F) * h * 3.0F) * (float) RZStrength.getValue()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((Math.abs(MathHelper.cos(g * 3.1415927F - 0.2F) * h) * 5.0F) * (float) RXStrength.getValue()));
    }

    public double getFullStrength() {
        return TXStrength.getValue() + TYStrength.getValue() + RXStrength.getValue() + RZStrength.getValue();
    }
}
