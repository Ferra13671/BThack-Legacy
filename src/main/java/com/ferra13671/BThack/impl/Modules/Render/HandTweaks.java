package com.ferra13671.BThack.impl.Modules.Render;

import com.ferra13671.BThack.events.Render.TransformFirstPersonEvent;
import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.CategorySetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.Arm;
import org.joml.Matrix4f;

@ModuleInfo(name = "HandTweaks", description = "lang.module.HandTweaks", category = "RENDER")
public class HandTweaks extends Module {

    public final CategorySetting leftHandCategory = new CategorySetting("Left Hand", this);
    public final NumberSetting lHandX = new NumberSetting("LHand X", this, -0.63, -2.0, 2.0, false).inCategory(leftHandCategory);
    public final NumberSetting lHandY = new NumberSetting("LHand Y", this, -0.08, -2.0, 2.0, false).inCategory(leftHandCategory);
    public final NumberSetting lHandZ = new NumberSetting("LHand Z", this, -0.67, -2.0, 2.0, false).inCategory(leftHandCategory);
    public final NumberSetting lHandYaw = new NumberSetting("LHand Yaw", this, 0, -100, 100, true).inCategory(leftHandCategory);
    public final NumberSetting lHandPitch = new NumberSetting("LHand Pitch", this, 0, -100, 100, true).inCategory(leftHandCategory);
    public final NumberSetting lHandRoll = new NumberSetting("LHand Roll", this, 0, -100, 100, true).inCategory(leftHandCategory);
    public final NumberSetting lHandScale = new NumberSetting("LHand Scale", this, 1, 0.05, 4, false).inCategory(leftHandCategory);

    public final CategorySetting rightHandCategory = new CategorySetting("Right Hand", this);
    public final NumberSetting rHandX = new NumberSetting("RHand X", this, 0.63, -2.0, 2.0, false).inCategory(rightHandCategory);
    public final NumberSetting rHandY = new NumberSetting("RHand Y", this, -0.08, -2.0, 2.0, false).inCategory(rightHandCategory);
    public final NumberSetting rHandZ = new NumberSetting("RHand Z", this, -0.67, -2.0, 2.0, false).inCategory(rightHandCategory);
    public final NumberSetting rHandYaw = new NumberSetting("RHand Yaw", this, 0, -100, 100, true).inCategory(rightHandCategory);
    public final NumberSetting rHandPitch = new NumberSetting("RHand Pitch", this, 0, -100, 100, true).inCategory(rightHandCategory);
    public final NumberSetting rHandRoll = new NumberSetting("RHand Roll", this, 0, -100, 100, true).inCategory(rightHandCategory);
    public final NumberSetting rHandScale = new NumberSetting("RHand Scale", this, 1, 0.05, 4, false).inCategory(rightHandCategory);

    public final BooleanSetting noEatAnim = new BooleanSetting("No Eat Anim", this, false);
    public final BooleanSetting noBob = new BooleanSetting("No Bob", this, false);
    public final NumberSetting handAnimStep = new NumberSetting("Hand Anim. Step", this, 0.5, 0.3, 1, false);


    @EventSubscriber
    public void onTransformSideFirstPerson(TransformFirstPersonEvent.Pre e) {
        if (noEatAnim.getValue())
            if (e.transformType == TransformFirstPersonEvent.TransformType.EAT)
                    e.setCancelled(true);
        if (e.arm == Arm.LEFT) {
            e.matrices.translate(lHandX.getValue(), lHandY.getValue(), lHandZ.getValue());
        } else if (e.arm == Arm.RIGHT) {
            e.matrices.translate(rHandX.getValue(), rHandY.getValue(), rHandZ.getValue());
        }
    }

    @EventSubscriber
    public void onTransFormPost(TransformFirstPersonEvent.Post e) {
        Matrix4f matrix = e.matrices.peek().getPositionMatrix();
        if (e.arm == Arm.LEFT) {
            matrix.rotate((float) Math.toRadians(lHandYaw.getValue()),0,1,0);
            matrix.rotate((float) Math.toRadians(lHandPitch.getValue()),1,0,0);
            matrix.rotate((float) Math.toRadians(lHandRoll.getValue()),0,0,1);
            matrix.scale(lHandScale.getValue().floatValue());
        } else if (e.arm == Arm.RIGHT) {
            matrix.rotate((float) Math.toRadians(rHandYaw.getValue()),0,1,0);
            matrix.rotate((float) Math.toRadians(rHandPitch.getValue()),1,0,0);
            matrix.rotate((float) Math.toRadians(rHandRoll.getValue()),0,0,1);
            matrix.scale(rHandScale.getValue().floatValue());
        }
    }
}
