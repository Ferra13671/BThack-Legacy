package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.OneActionModule;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class Flip extends OneActionModule {

    public static BooleanSetting saveSpeed;

    public Flip() {
        super("Flip",
                "lang.module.Flip",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );

        saveSpeed = new BooleanSetting("Save Speed", this, true);

        initSettings(
                saveSpeed
        );
    }

    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        double mX = mc.player.velocity.x;
        double mZ = mc.player.velocity.z;

        mc.player.setYaw(mc.player.getYaw() - 180);
        if (saveSpeed.getValue()) {
            mc.player.velocity.x = -mX;
            mc.player.velocity.z = -mZ;
        }
    }
}
