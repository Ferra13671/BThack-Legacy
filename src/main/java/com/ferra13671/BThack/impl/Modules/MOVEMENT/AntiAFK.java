package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BTbot.impl.AntiAFK.Start.StartAntiAFK;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.Formatting;

public class AntiAFK extends Module {

    public final NumberSetting walkRadius = new NumberSetting("Walking radius", this, 3.0,1,10,false);
    public final NumberSetting delay = new NumberSetting("Delay(Seconds)", this, 3.0,0.5,10,false);
    public final NumberSetting messageSize = new NumberSetting("Message size", this, 15,5,30,true);

    public AntiAFK() {
        super("AntiAFK",
                "lang.module.AntiAFK",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );

        initSettings(
                walkRadius,
                delay,
                messageSize
        );
    }

    private boolean correct = false;

    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        if (!StartAntiAFK.active) {
            super.onEnable();
            correct = true;
            StartAntiAFK.start();
        } else {
            sendNotification(Formatting.YELLOW + "AntiAFK is already enabled. Please turn it off before using the module.");
            correct = false;
            setToggled(false);
        }
    }

    @EventSubscriber
    public void onClientTick(ClientTickEvent e) {
        if (correct) {
            StartAntiAFK.walkRadius = walkRadius.getValue();
            StartAntiAFK.messageSize = (int) messageSize.getValue();
            StartAntiAFK.delay = delay.getValue();
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (nullCheck()) return;
        if (correct) {
            if (StartAntiAFK.active) {
                StartAntiAFK.stop();
            }
        }
    }
}
