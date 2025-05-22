package com.ferra13671.BThack.impl.Modules.Movement;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.events.Entity.UpdateInputEvent;
import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Utils.BaritoneUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

import java.util.Arrays;

@ModuleInfo(name = "AutoWalk", description = "lang.module.AutoWalk", category = "MOVEMENT")
public class AutoWalk extends Module {

    public final BooleanSetting disableOnDisconnect = new BooleanSetting("Disable On Disconnect", this, true);
    public final ModeSetting mode = new ModeSetting("Mode", this, Arrays.asList("Forward", "Baritone"));


    @Override
    public void onDisable() {
        super.onDisable();

        if (mode.getValue().equals("Baritone"))
            if (BThack.isBaritonePresent()) BaritoneUtils.cancelAllPathing();
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) {
            if (disableOnDisconnect.getValue()) toggle();
            return;
        }

        if (mode.getValue().equals("Baritone"))  baritoneAction();
    }

    @EventSubscriber
    public void onInputUpdate(UpdateInputEvent e) {
        if (mode.getValue().equals("Forward")) forwardAction();
    }

    public void forwardAction() {
        mc.player.input.movementForward = 1;
    }

    public void baritoneAction() {
        if (!BThack.isBaritonePresent()) {
            toggle();
            return;
        }

        if (!BaritoneUtils.isActive())
            BaritoneUtils.autoWalkUpdate();
    }
}
