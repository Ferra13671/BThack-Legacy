package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.api.Events.Render.RenderWorldLastEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Utils.Ticker;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

@ModuleInfo(name = "ShiftSpam", description = "lang.module.ShiftSpam", category = "MOVEMENT")
public class ShiftSpam extends Module {

    public final NumberSetting activeDelay = new NumberSetting("Delay Active", this, 0.1,0.05,1,false);
    public final NumberSetting deActiveDelay = new NumberSetting("Delay deActive", this, 0.1,0.05,1,false);


    private final Ticker ticker = new Ticker();
    private boolean sneaked = false;

    @Override
    public void onEnable() {
        super.onEnable();
        ticker.reset();
        sneaked = false;
    }

    @EventSubscriber
    public void onTick(RenderWorldLastEvent e) {
        if (nullCheck()) return;

        if (ticker.passed(sneaked ? (activeDelay.getValue() * 1000) : (deActiveDelay.getValue() * 1000))) {
            mc.options.sneakKey.setPressed(!mc.options.sneakKey.isPressed());

            sneaked = !sneaked;
            ticker.reset();
        }
    }
}
