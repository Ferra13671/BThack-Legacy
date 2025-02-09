package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.managers.Thread.BThackThread;
import com.ferra13671.BThack.api.Managers.managers.Thread.IThread;
import com.ferra13671.BThack.api.Managers.managers.Thread.ThreadManager;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class CameraRotator extends Module {
    public final NumberSetting speed = new NumberSetting("Speed", this, 1,0.1,4,false);
    public final BooleanSetting inversion = new BooleanSetting("Inversion", this, false);

    public CameraRotator() {
        super("CameraRotator",
                "lang.module.CameraRotator",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );

        initSettings(
                speed,
                inversion
        );
    }

    @Override
    public void onEnable() {
        ThreadManager.startNewThread(new IThread() {
            double a = 0;
            float b = 0;

            @Override
            public void start(BThackThread thread) {
                while (ModuleList.cameraRotator.isEnabled()) {
                    if (!nullCheck()) {
                        a = speed.getValue();
                        b = (float) (1080 * a);
                        b = b / 950;
                        if (!inversion.getValue()) {
                            mc.player.yaw = mc.player.yaw + b;
                        } else {
                            mc.player.yaw = mc.player.yaw - b;
                        }
                        thread.sleepThread(1);
                    } else Thread.yield();
                }
            }
        });
    }
}
