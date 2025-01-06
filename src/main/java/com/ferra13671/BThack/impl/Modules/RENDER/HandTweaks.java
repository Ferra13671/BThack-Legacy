package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.api.Events.Render.TransformFirstPersonEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.Arm;

public class HandTweaks extends Module {

    public static NumberSetting lHandX;
    public static NumberSetting lHandY;
    public static NumberSetting lHandZ;
    public static NumberSetting lHandYaw;
    public static NumberSetting lHandPitch;
    public static NumberSetting lHandRoll;
    public static BooleanSetting lArmAlso;

    public static NumberSetting rHandX;
    public static NumberSetting rHandY;
    public static NumberSetting rHandZ;
    public static NumberSetting rHandYaw;
    public static NumberSetting rHandPitch;
    public static NumberSetting rHandRoll;

    public static BooleanSetting noEatAnim;

    public HandTweaks() {
        super("HandTweaks",
                "lang.module.HandTweaks",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        lHandX = new NumberSetting("LHand X", this, 0, -2.0, 2.0, false);
        lHandY = new NumberSetting("LHand Y", this, 0, -2.0, 2.0, false);
        lHandZ = new NumberSetting("LHand Z", this, -1.2, -2.0, 2.0, false);

        lHandYaw = new NumberSetting("LHand Yaw", this, 0, -100, 100, true);
        lHandPitch = new NumberSetting("LHand Pitch", this, 0, -100, 100, true);
        lHandRoll = new NumberSetting("LHand Roll", this, 0, -100, 100, true);
        lArmAlso = new BooleanSetting("LArm Also", this, true);



        rHandX = new NumberSetting("RHand X", this, 0, -2.0, 2.0, false);
        rHandY = new NumberSetting("RHand Y", this, 0, -2.0, 2.0, false);
        rHandZ = new NumberSetting("RHand Z", this, -1.2, -2.0, 2.0, false);

        rHandYaw = new NumberSetting("RHand Yaw", this, 0, -100, 100, true);
        rHandPitch = new NumberSetting("RHand Pitch", this, 0, -100, 100, true);
        rHandRoll = new NumberSetting("RHand Roll", this, 0, -100, 100, true);

        noEatAnim = new BooleanSetting("No Eat Anim", this, false);

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
