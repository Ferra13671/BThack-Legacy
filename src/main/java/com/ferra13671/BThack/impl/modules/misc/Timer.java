package com.ferra13671.BThack.impl.modules.misc;

import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.core.client.ModuleList;
import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.impl.setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

@ModuleInfo(name = "Timer", description = "lang.module.Timer", category = "MISC")
public class Timer extends Module {

    public final NumberSetting tickSpeed = new NumberSetting("Tick speed", this, 1,0.1,3,false);

    @Override
    public void onEnable() {
        super.onEnable();
        ModuleList.elytraFlight.setEnabled(false);
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        Managers.TICK_MANAGER.applyTickModifier((50f / tickSpeed.getValue().floatValue()) / 50);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (ModuleList.elytraFlight.isEnabled() && ModuleList.elytraFlight.mode.getValue().equals("Timer")) return;
        Managers.TICK_MANAGER.applyTickModifier(1);
    }
}
