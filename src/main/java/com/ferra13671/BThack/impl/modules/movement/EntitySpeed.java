package com.ferra13671.BThack.impl.modules.movement;

import com.ferra13671.BThack.events.player.PlayerTravelEvent;
import com.ferra13671.BThack.managers.impl.setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.vehicle.BoatEntity;

@ModuleInfo(name = "EntitySpeed", description = "lang.module.EntitySpeed", category = "MOVEMENT")
public class EntitySpeed extends Module {

    public final NumberSetting boatSpeed = new NumberSetting("BoatSpeed", this, 0.3, 0.1, 1.2, false);


    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
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
