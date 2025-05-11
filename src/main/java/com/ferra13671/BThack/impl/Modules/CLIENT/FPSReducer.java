package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

public class FPSReducer extends Module {

    public final NumberSetting fpsLimit = new NumberSetting("FPS Limit", this, 10, 1, 60, true);
    public final NumberSetting delay = new NumberSetting("Delay", this, 100, 0, 10000, true);

    public FPSReducer() {
        super("FPSReducer",
                "lang.module.FPSReducer",
                KeyboardUtils.RELEASE,
                MCategory.CLIENT,
                false
        );
    }

    public int lastFocusTicks = 0;

    @Override
    public void onChangeSetting(Setting<?> setting) {
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
            lastFocusTicks = delay.getValue().intValue();
        else {
            if (lastFocusTicks > 0)
                lastFocusTicks--;
        }
    }
}
