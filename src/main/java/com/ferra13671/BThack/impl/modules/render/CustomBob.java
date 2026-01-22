package com.ferra13671.BThack.impl.modules.render;

import com.ferra13671.BThack.managers.impl.setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@ModuleInfo(name = "CustomBob", description = "lang.module.CustomBob", category = "RENDER")
public class CustomBob extends Module {

    public final NumberSetting TXStrength = new NumberSetting("TX Strength", this, 0.45, 0, 10, false);
    public final NumberSetting TYStrength = new NumberSetting("TY Strength", this, 0.3, 0, 10, false);
    public final NumberSetting RXStrength = new NumberSetting("RX Strength", this, 1.35, 0, 10, false);
    public final NumberSetting RZStrength = new NumberSetting("RZ Strength", this, 1.5, 0, 10, false);

    @SuppressWarnings("DataFlowIssue")
    public void customBob(MatrixStack matrices) {
        AbstractClientPlayerEntity playerEntity = (AbstractClientPlayerEntity) mc.getCameraEntity();
        float f = playerEntity.distanceMoved - playerEntity.lastDistanceMoved;
        float g = -(playerEntity.distanceMoved + f * mc.getRenderTickCounter().getTickDelta(true));
        float h = MathHelper.lerp(mc.getRenderTickCounter().getTickDelta(true), playerEntity.prevStrideDistance, playerEntity.strideDistance);
        matrices.translate((MathHelper.sin(g * 3.1415927F) * h * 0.5F) * TXStrength.getValue().floatValue(), (-Math.abs(MathHelper.cos(g * 3.1415927F) * h)) * TYStrength.getValue().floatValue(), 0.0F);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((MathHelper.sin(g * 3.1415927F) * h * 3.0F) * RZStrength.getValue().floatValue()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((Math.abs(MathHelper.cos(g * 3.1415927F - 0.2F) * h) * 5.0F) * RXStrength.getValue().floatValue()));
    }

    public double getFullStrength() {
        return TXStrength.getValue() + TYStrength.getValue() + RXStrength.getValue() + RZStrength.getValue();
    }
}
