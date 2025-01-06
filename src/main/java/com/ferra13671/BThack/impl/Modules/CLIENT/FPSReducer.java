package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

public class FPSReducer extends Module {

    public static int lastFocusTicks = 0;

    public static NumberSetting fpsLimit;
    public static NumberSetting delay;

    public FPSReducer() {
        super("FPSReducer",
                "lang.module.FPSReducer",
                KeyboardUtils.RELEASE,
                MCategory.CLIENT,
                false
        );

        fpsLimit = new NumberSetting("FPS Limit", this, 10, 1, 60, true);
        delay = new NumberSetting("Delay", this, 100, 0, 10000, true);

        initSettings(
                fpsLimit,
                delay
        );
    }

    @Override
    public void onChangeSetting(Setting setting) {
        arrayListInfo = fpsLimit.getValue() + "";
    }

    @Override
    public void onEnable() {
        super.onEnable();
        arrayListInfo = fpsLimit.getValue() + "";
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (mc.isWindowFocused())
            lastFocusTicks = (int) delay.getValue();
        else {
            if (lastFocusTicks > 0)
                lastFocusTicks--;
        }
    }
}
