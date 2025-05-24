package com.ferra13671.BThack.impl.Modules.Movement;

import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Module.OneActionModule;

@ModuleInfo(name = "Flip", description = "lang.module.Flip", category = "MOVEMENT")
public class Flip extends OneActionModule {

    public final BooleanSetting saveSpeed = new BooleanSetting("Save Speed", this, true);

    @Override
    @SuppressWarnings("DataFlowIssue")
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        mc.player.setYaw(mc.player.getYaw() - 180);
        if (saveSpeed.getValue()) {
            mc.player.velocity.x *= -1;
            mc.player.velocity.z *= -1;
        }
    }
}
