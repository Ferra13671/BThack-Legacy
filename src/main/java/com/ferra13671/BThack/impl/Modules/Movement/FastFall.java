package com.ferra13671.BThack.impl.Modules.Movement;

import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

import java.util.Arrays;

@ModuleInfo(name = "FastFall", description = "lang.module.FastFall", category = "MOVEMENT")
public class FastFall extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", this, Arrays.asList("Timer", "Velocity", "Pos"));
    public final NumberSetting fallDistance = new NumberSetting("Fall Dist.", this, 1, 0.1, 5, false);
    public final NumberSetting downSpeed = new NumberSetting("Down Speed", this, 0.4, 0.35, 1.5, false, () -> mode.getValue().equals("Velocity"));
    public final NumberSetting timerSpeed = new NumberSetting("Timer Speed", this, 2, 1.1, 5, false, () -> mode.getValue().equals("Timer"));


    @Override
    public void onEnable() {
        super.onEnable();

        ModuleList.elytraFlight.setEnabled(false);
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        if (!mode.getValue().equals("Timer") && Managers.TICK_MANAGER.getTickModifier() != 1)
            Managers.TICK_MANAGER.applyTickModifier(1);

        switch (mode.getValue()) {
            case "Timer" -> timerAction();
            case "Velocity" -> velocityAction();
            case "Pos" -> posAction();
        }
    }

    @SuppressWarnings("DataFlowIssue")
    public void timerAction() {
        if (ModuleList.timer.isEnabled())
            ModuleList.timer.setEnabled(false);

        if (mc.player.verticalCollision)
            Managers.TICK_MANAGER.applyTickModifier(1);
        else
            if (Managers.FALL_DISTANCE_MANAGER.getFallDistance() >= fallDistance.getValue())
                Managers.TICK_MANAGER.applyTickModifierWithFactor(timerSpeed.getValue());
    }

    @SuppressWarnings("DataFlowIssue")
    public void velocityAction() {
        if (Managers.FALL_DISTANCE_MANAGER.getFallDistance() >= fallDistance.getValue())
            mc.player.velocity.y = -downSpeed.getValue();
    }

    @SuppressWarnings("DataFlowIssue")
    public void posAction() {
        if (Managers.FALL_DISTANCE_MANAGER.getFallDistance() >= fallDistance.getValue())
            mc.player.setPosition(mc.player.getX(), mc.player.getY() - Managers.FALL_DISTANCE_MANAGER.getFallDistance(), mc.player.getZ());

    }
}
