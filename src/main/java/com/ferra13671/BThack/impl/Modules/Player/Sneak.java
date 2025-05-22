package com.ferra13671.BThack.impl.Modules.Player;

import com.ferra13671.BThack.events.Entity.UpdateInputEvent;
import com.ferra13671.BThack.managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Utils.InputUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

import java.util.Arrays;

@ModuleInfo(name = "Sneak", description = "lang.module.Sneak", category = "PLAYER")
public class Sneak extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", this, Arrays.asList("Always", "Only Motion"));


    @EventSubscriber
    public void onInputUpdate(UpdateInputEvent e) {
        if (mode.getValue().equals("Always"))
            InputUtils.setSneaking(true);
        else {
            if (mc.player.input.movementForward != 0 || mc.player.input.movementSideways != 0)
                InputUtils.setSneaking(true);
        }
    }
}
