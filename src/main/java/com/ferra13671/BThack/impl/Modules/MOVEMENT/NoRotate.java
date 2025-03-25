package com.ferra13671.BThack.impl.Modules.MOVEMENT;


import com.ferra13671.BThack.api.Events.Player.SetPlayerPitchEvent;
import com.ferra13671.BThack.api.Events.Player.SetPlayerYawEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.NoRotateMathUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

public class NoRotate extends Module {

    public final BooleanSetting blockPitch = new BooleanSetting("BlockPitchRotate", this, true);

    public NoRotate() {
        super("NoRotate",
                "lang.module.NoRotate",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );

        initSettings(
                blockPitch
        );
    }

    @EventSubscriber
    public void onSetYaw(SetPlayerYawEvent e) {
        e.setYaw(NoRotateMathUtils.getNearestYawAxis(mc.player));
    }

    @EventSubscriber
    public void onSetPitch(SetPlayerPitchEvent e) {
        e.setPitch(NoRotateMathUtils.getNearestPitchAxis(mc.player));
    }
}
