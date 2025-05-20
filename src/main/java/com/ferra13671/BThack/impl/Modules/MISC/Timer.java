package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

@ModuleInfo(name = "Timer", description = "lang.module.Timer", category = "MISC")
public class Timer extends Module {

    public final NumberSetting tickSpeed = new NumberSetting("Tick speed", this, 1,0.1,3,false);

    @Override
    public void onEnable() {
        super.onEnable();
        ModuleList.elytraFlight.setToggled(false);
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        Managers.TICK_MANAGER.applyTickModifier((50f / tickSpeed.getValue().floatValue()) / 50);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (ModuleList.elytraFlight.isEnabled())
            if (ModuleList.elytraFlight.mode.equals("Timer")) return;
        Managers.TICK_MANAGER.applyTickModifier(1);
    }
}
