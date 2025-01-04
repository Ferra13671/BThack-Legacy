package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Events.Entity.UpdateInputEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.BaritoneUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

import java.util.Arrays;

public class AutoWalk extends Module {

    public final BooleanSetting disableOnDisconnect = new BooleanSetting("Disable On Disconnect", this, true);
    public final ModeSetting mode = new ModeSetting("Mode", this, Arrays.asList("Forward", "Baritone"));


    public AutoWalk() {
        super("AutoWalk",
                "lang.module.AutoWalk",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );

        initSettings(
                disableOnDisconnect,
                mode
        );
    }

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
