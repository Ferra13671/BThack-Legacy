package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

import java.util.Arrays;

public class AutoJump extends Module {

    public static ModeSetting mode;

    public AutoJump() {
        super("AutoJump",
                "lang.module.AutoJump",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );

        mode = new ModeSetting("Mode", this, Arrays.asList("OnlyPress", "PressRelease"));

        initSettings(
                mode
        );
    }

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
