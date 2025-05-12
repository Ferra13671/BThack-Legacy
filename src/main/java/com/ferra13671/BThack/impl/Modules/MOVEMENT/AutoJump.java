package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

import java.util.Arrays;

@ModuleInfo(name = "AutoJump", description = "lang.module.AutoJump", category = "MOVEMENT")
public class AutoJump extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", this, Arrays.asList("OnlyPress", "PressRelease"));


    private boolean needRelease = false;

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        switch (mode.getValue()) {
            case "OnlyPress" -> mc.options.jumpKey.setPressed(true);
            case "PressRelease" -> {
                if (needRelease) {
                    mc.options.jumpKey.setPressed(false);
                    needRelease = false;
                } else {
                    if (mc.player.verticalCollision) {
                        mc.options.jumpKey.setPressed(true);
                        needRelease = true;
                    }
                }
            }
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        needRelease = false;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.options.jumpKey.setPressed(false);
    }
}
