package com.ferra13671.BThack.impl.Modules.MOVEMENT;


import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class NoRotate extends Module {

    public static BooleanSetting blockPitch;

    public NoRotate() {
        super("NoRotate",
                "lang.module.NoRotate",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );

        blockPitch = new BooleanSetting("BlockPitchRotate", this, true);

        initSettings(
                blockPitch
        );
    }

    /*
    @EventSubscriber
    public void onUpdate(TickEvent.ClientTickEvent e) {
        if (nullCheck()) return;

        mc.player.yaw = NoRotateMathUtil.RotateYawMath(mc.player);
        if (getCheckbox(this.name, "BlockPitchRotate"))
            mc.player.pitch = NoRotateMathUtil.RotatePitchMath(mc.player);
    }

     */
}
