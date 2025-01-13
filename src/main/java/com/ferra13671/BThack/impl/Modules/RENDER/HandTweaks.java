package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.api.Events.Render.TransformFirstPersonEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.Arm;

public class HandTweaks extends Module {

    public final NumberSetting lHandX = new NumberSetting("LHand X", this, 0, -2.0, 2.0, false);
    public final NumberSetting lHandY = new NumberSetting("LHand Y", this, 0, -2.0, 2.0, false);
    public final NumberSetting lHandZ = new NumberSetting("LHand Z", this, -1.2, -2.0, 2.0, false);
    public final NumberSetting lHandYaw = new NumberSetting("LHand Yaw", this, 0, -100, 100, true);
    public final NumberSetting lHandPitch = new NumberSetting("LHand Pitch", this, 0, -100, 100, true);
    public final NumberSetting lHandRoll = new NumberSetting("LHand Roll", this, 0, -100, 100, true);
    public final BooleanSetting lArmAlso = new BooleanSetting("LArm Also", this, true);

    public final NumberSetting rHandX = new NumberSetting("RHand X", this, 0, -2.0, 2.0, false);
    public final NumberSetting rHandY = new NumberSetting("RHand Y", this, 0, -2.0, 2.0, false);
    public final NumberSetting rHandZ = new NumberSetting("RHand Z", this, -1.2, -2.0, 2.0, false);
    public final NumberSetting rHandYaw = new NumberSetting("RHand Yaw", this, 0, -100, 100, true);
    public final NumberSetting rHandPitch = new NumberSetting("RHand Pitch", this, 0, -100, 100, true);
    public final NumberSetting rHandRoll = new NumberSetting("RHand Roll", this, 0, -100, 100, true);

    public final BooleanSetting noEatAnim = new BooleanSetting("No Eat Anim", this, false);

    public HandTweaks() {
        super("HandTweaks",
                "lang.module.HandTweaks",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        initSettings(
                lHandX,
                lHandY,
                lHandZ,
                lHandYaw,
                lHandPitch,
                lHandRoll,
                lArmAlso,

                rHandX,
                rHandY,
                rHandZ,
                rHandYaw,
                rHandPitch,
                rHandRoll,

                noEatAnim
        );
    }

    @EventSubscriber
    public void onTransformSideFirstPerson(TransformFirstPersonEvent.Pre e) {
        if (noEatAnim.getValue())
            if (e.transformType == TransformFirstPersonEvent.TransformType.EAT)
                    e.setCancelled(true);
        if (e.arm == Arm.LEFT) {
            if (!lArmAlso.getValue())
                if (e.transformType == TransformFirstPersonEvent.TransformType.ARM) return;
            e.matrices.translate(lHandX.getValue(), lHandY.getValue(), lHandZ.getValue());
        } else if (e.arm == Arm.RIGHT) {
            e.matrices.translate(rHandX.getValue(), rHandY.getValue(), rHandZ.getValue());
        }
    }

    @EventSubscriber
    public void onTransFormPost(TransformFirstPersonEvent.Post e) {
        if (e.arm == Arm.LEFT) {
            if (!lArmAlso.getValue())
                if (e.transformType == TransformFirstPersonEvent.TransformType.ARM) return;
            e.matrices.peek().getPositionMatrix().rotate((float) Math.toRadians(lHandYaw.getValue()),0,1,0);
            e.matrices.peek().getPositionMatrix().rotate((float) Math.toRadians(lHandPitch.getValue()),1,0,0);
            e.matrices.peek().getPositionMatrix().rotate((float) Math.toRadians(lHandRoll.getValue()),0,0,1);
        } else if (e.arm == Arm.RIGHT) {
            e.matrices.peek().getPositionMatrix().rotate((float) Math.toRadians(rHandYaw.getValue()),0,1,0);
            e.matrices.peek().getPositionMatrix().rotate((float) Math.toRadians(rHandPitch.getValue()),1,0,0);
            e.matrices.peek().getPositionMatrix().rotate((float) Math.toRadians(rHandRoll.getValue()),0,0,1);
        }
    }
}
