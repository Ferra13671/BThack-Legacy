package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.api.Events.Camera.PositionCameraEvent;
import com.ferra13671.BThack.api.Events.Camera.RotateCameraEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class ModifyCamera extends Module {

    public static BooleanSetting rewriteDistance;
    public static NumberSetting distance;

    public static BooleanSetting rewritePosition;
    public static NumberSetting posX;
    public static NumberSetting posY;
    public static NumberSetting posZ;

    public static BooleanSetting rewriteRotation;
    public static NumberSetting yaw;
    public static NumberSetting pitch;

    public ModifyCamera() {
        super("ModifyCamera",
                "lang.module.ModifyCamera",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        rewriteDistance = new BooleanSetting("Rewrite Distance", this, true);
        distance = new NumberSetting("Distance", this, 7, 2.5, 40, false, rewriteDistance::getValue);

        rewritePosition = new BooleanSetting("Rewrite Position", this, false);
        posX = new NumberSetting("Position X", this, 0, -30, 30, false, rewritePosition::getValue);
        posY = new NumberSetting("Position Y", this, 0, -30, 30, false, rewritePosition::getValue);
        posZ = new NumberSetting("Position Z", this, 0, -30, 30, false, rewritePosition::getValue);

        rewriteRotation = new BooleanSetting("Rewrite Rotation", this, false);
        yaw = new NumberSetting("Camera Yaw", this, 0, -180, 180, false, rewriteRotation::getValue);
        pitch = new NumberSetting("Camera Pitch", this, 0, -90, 90, false);

        initSettings(
                rewriteDistance,
                distance,

                rewritePosition,
                posX,
                posY,
                posZ,

                rewriteRotation,
                yaw,
                pitch
        );
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onCameraPos(PositionCameraEvent e) {
        if (rewritePosition.getValue()) {
            e.setPosition(new Vec3d(e.getX() + posX.getValue(), e.getY() + posY.getValue(), e.getZ() + posZ.getValue()));
        }
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onCameraRotation(RotateCameraEvent e) {
        if (rewriteRotation.getValue()) {
            e.setRotation(new Vec2f((float) yaw.getValue(), (float) pitch.getValue()));
        }
    }
}
