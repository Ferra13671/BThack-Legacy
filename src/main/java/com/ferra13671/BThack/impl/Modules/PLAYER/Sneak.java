package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.api.Events.Entity.UpdateInputEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

import java.util.Arrays;

public class Sneak extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", this, Arrays.asList("Always", "Only Motion"));

    public Sneak() {
        super("Sneak",
                "lang.module.Sneak",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );

        initSettings(
                mode
        );
    }

    @EventSubscriber
    public void onInputUpdate(UpdateInputEvent e) {
        if (mode.getValue().equals("Always"))
            mc.player.input.sneaking = true;
        else {
            if (mc.player.input.movementForward != 0 || mc.player.input.movementSideways != 0)
                mc.player.input.sneaking = true;
        }
    }
}
