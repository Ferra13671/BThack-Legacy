package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class CustomBob extends Module {

    public final NumberSetting strength = new NumberSetting("Strength", this, 0, 0, 10, false);

    public CustomBob() {
        super("CustomBob",
                "lang.module.CustomBob",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        initSettings(
                strength
        );
    }

    public void customBob(MatrixStack matrices) {
        PlayerEntity playerEntity = (PlayerEntity) mc.getCameraEntity();
        float f = playerEntity.horizontalSpeed - playerEntity.prevHorizontalSpeed;
        float g = -(playerEntity.horizontalSpeed + f * mc.getRenderTickCounter().getTickDelta(true));
        float h = MathHelper.lerp(mc.getRenderTickCounter().getTickDelta(true), playerEntity.prevStrideDistance, playerEntity.strideDistance);
        float strength = (float) this.strength.getValue();
        matrices.translate((MathHelper.sin(g * 3.1415927F) * h * 0.5F) * strength, (-Math.abs(MathHelper.cos(g * 3.1415927F) * h)) * strength, 0.0F);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((MathHelper.sin(g * 3.1415927F) * h * 3.0F) * strength));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((Math.abs(MathHelper.cos(g * 3.1415927F - 0.2F) * h) * 5.0F) * strength));
    }
}
