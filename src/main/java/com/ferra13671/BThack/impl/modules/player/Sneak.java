package com.ferra13671.BThack.impl.modules.player;

import com.ferra13671.BThack.events.entity.UpdateInputEvent;
import com.ferra13671.BThack.managers.impl.setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.api.utils.InputUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

import java.util.Arrays;

@ModuleInfo(name = "Sneak", description = "lang.module.Sneak", category = "PLAYER")
public class Sneak extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", this, Arrays.asList("Always", "Only Motion"));


    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onInputUpdate(UpdateInputEvent e) {
        if (mode.getValue().equals("Always"))
            InputUtils.setSneaking(true);
        else if (mc.player.input.movementForward != 0 || mc.player.input.movementSideways != 0)
                InputUtils.setSneaking(true);
    }
}
