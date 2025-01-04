package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.api.Events.Player.PlayerTravelEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.vehicle.BoatEntity;

public class EntitySpeed extends Module {

    public final NumberSetting boatSpeed = new NumberSetting("BoatSpeed", this, 0.3, 0.1, 1.2, false);

    public EntitySpeed() {
        super("EntitySpeed",
                "lang.module.EntitySpeed",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );

        initSettings(
                boatSpeed
        );
    }

    @EventSubscriber
    public void onTravel(PlayerTravelEvent e) {
        if (mc.player.getControllingVehicle() != null) {
            double speed = boatSpeed.getValue();

            if (mc.player.getControllingVehicle() instanceof BoatEntity boatEntity) {
                if (mc.options.forwardKey.isPressed()) {
                    double yaw = Math.toRadians(boatEntity.yaw);
                    boatEntity.velocity.x = -Math.sin(yaw) * speed;
                    boatEntity.velocity.z = Math.cos(yaw) * speed;
                }
            }
        }
    }
}
