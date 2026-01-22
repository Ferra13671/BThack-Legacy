package com.ferra13671.BThack.impl.modules.render;

import com.ferra13671.BThack.events.render.RenderHandEvent;
import com.ferra13671.BThack.managers.impl.setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.impl.setting.Settings.CategorySetting;
import com.ferra13671.BThack.managers.impl.setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;

@ModuleInfo(name = "HandTweaks", description = "lang.module.HandTweaks", category = "RENDER")
public class HandTweaks extends Module {

    public final CategorySetting mainHandCategory = new CategorySetting("Main Hand", this);
    public final BooleanSetting armAlso = new BooleanSetting("Arm Also", this, true).inCategory(mainHandCategory);
    public final NumberSetting mainPosX = new NumberSetting("Pos X", this, 0.71, -2.0, 2.0, false).inCategory(mainHandCategory);
    public final NumberSetting mainPosY = new NumberSetting("Pos Y", this, -0.08, -2.0, 2.0, false).inCategory(mainHandCategory);
    public final NumberSetting mainPosZ = new NumberSetting("Pos Z", this, -0.67, -2.0, 2.0, false).inCategory(mainHandCategory);
    public final NumberSetting mainRotX = new NumberSetting("Rot X", this, 0, -100, 100, true).inCategory(mainHandCategory);
    public final NumberSetting mainRotY = new NumberSetting("Rot Y", this, 0, -100, 100, true).inCategory(mainHandCategory);
    public final NumberSetting mainRotZ = new NumberSetting("Rot Z", this, 0, -100, 100, true).inCategory(mainHandCategory);
    public final NumberSetting mainScaleX = new NumberSetting("Scale X", this, 1, 0.05, 4, false).inCategory(mainHandCategory);
    public final NumberSetting mainScaleY = new NumberSetting("Scale Y", this, 1, 0.05, 4, false).inCategory(mainHandCategory);
    public final NumberSetting mainScaleZ = new NumberSetting("Scale Z", this, 1, 0.05, 4, false).inCategory(mainHandCategory);

    public final CategorySetting offHandCategory = new CategorySetting("Off Hand", this);
    public final NumberSetting offPosX = new NumberSetting("Pos X", this, -0.71, -2.0, 2.0, false).inCategory(offHandCategory);
    public final NumberSetting offPosY = new NumberSetting("Pos Y", this, -0.08, -2.0, 2.0, false).inCategory(offHandCategory);
    public final NumberSetting offPosZ = new NumberSetting("Pos Z", this, -0.67, -2.0, 2.0, false).inCategory(offHandCategory);
    public final NumberSetting offRotX = new NumberSetting("Rot X", this, 0, -100, 100, true).inCategory(offHandCategory);
    public final NumberSetting offRotY = new NumberSetting("Rot Y", this, 0, -100, 100, true).inCategory(offHandCategory);
    public final NumberSetting offRotZ = new NumberSetting("Rot Z", this, 0, -100, 100, true).inCategory(offHandCategory);
    public final NumberSetting offScaleX = new NumberSetting("Scale X", this, 1, 0.05, 4, false).inCategory(offHandCategory);
    public final NumberSetting offScaleY = new NumberSetting("Scale Y", this, 1, 0.05, 4, false).inCategory(offHandCategory);
    public final NumberSetting offScaleZ = new NumberSetting("Scale Z", this, 1, 0.05, 4, false).inCategory(offHandCategory);

    public final BooleanSetting noEatAnim = new BooleanSetting("No Eat Anim", this, false);
    public final BooleanSetting noBob = new BooleanSetting("No Bob", this, false);
    public final NumberSetting handMoveStep = new NumberSetting("Hand Move Step", this, 0.5, 0.3, 1, false);

    public final BooleanSetting customSwingSpeed = new BooleanSetting("Custom Swing Speed", this, true);
    public final NumberSetting swingSpeed = new NumberSetting("Swing Speed", this, 15, 1, 35, true, customSwingSpeed::getValue);

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onRenderHand(RenderHandEvent e) {
        if (e.hand == Hand.MAIN_HAND) {
            if (!armAlso.getValue() && e.type == RenderHandEvent.Type.ARM) return;

            e.matrix.translate(mainPosX.getValue(), mainPosY.getValue(), mainPosZ.getValue());
            e.matrix.multiply(RotationAxis.POSITIVE_X.rotationDegrees(mainRotX.getValue().floatValue()));
            e.matrix.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(mainRotY.getValue().floatValue()));
            e.matrix.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(mainRotZ.getValue().floatValue()));
            e.matrix.scale(mainScaleX.getValue().floatValue(), mainScaleY.getValue().floatValue(), mainScaleZ.getValue().floatValue());
        } else {
            e.matrix.translate(offPosX.getValue(), offPosY.getValue(), offPosZ.getValue());
            e.matrix.multiply(RotationAxis.POSITIVE_X.rotationDegrees(offRotX.getValue().floatValue()));
            e.matrix.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(offRotY.getValue().floatValue()));
            e.matrix.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(offRotZ.getValue().floatValue()));
            e.matrix.scale(offScaleX.getValue().floatValue(), offScaleY.getValue().floatValue(), offScaleZ.getValue().floatValue());
        }
    }
}
