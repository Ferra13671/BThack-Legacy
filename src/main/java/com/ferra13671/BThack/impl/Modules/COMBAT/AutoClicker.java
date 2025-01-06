package com.ferra13671.BThack.impl.Modules.COMBAT;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Ticker;
import com.ferra13671.BThack.mixins.accessor.IMinecraftClient;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.item.SwordItem;

import java.util.Arrays;

public class AutoClicker extends Module {

    public static ModeSetting mode;
    public static NumberSetting delay;

    public static BooleanSetting onlySword;
    public static BooleanSetting ifPressing;

    public AutoClicker() {
        super("AutoClicker",
                "lang.module.AutoClicker",
                KeyboardUtils.RELEASE,
                MCategory.COMBAT,
                false
        );

        mode = new ModeSetting("Mode", this, Arrays.asList("Cooldown", "Delay"));
        delay = new NumberSetting("Delay", this, 500, 100, 3000, true, () -> mode.getValue().equals("Delay"));

        onlySword = new BooleanSetting("Only Sword", this, false);
        ifPressing = new BooleanSetting("If Pressing", this, true);

        initSettings(
                mode,
                delay,

                onlySword,
                ifPressing
        );
    }
    Ticker ticker = new Ticker();

    @Override
    public void onEnable() {
        super.onEnable();
        ticker.reset();
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        if (checkPressing() && check()) {
            if (delayPassed()) {
                ((IMinecraftClient) mc).attack();
                ticker.reset();
            }
        }
    }

    public boolean delayPassed() {
        if (mode.getValue().equals("Cooldown"))
            return mc.player.getAttackCooldownProgress(0) >= 1.0;
        else
            return ticker.passed((int) (delay.getValue()));
    }

    public boolean check() {
        if (onlySword.getValue())
            return mc.player.getMainHandStack().getItem() instanceof SwordItem;
        else
            return true;
    }

    public boolean checkPressing() {
        if (ifPressing.getValue())
            return mc.options.attackKey.isPressed();
        else return true;
    }
}
