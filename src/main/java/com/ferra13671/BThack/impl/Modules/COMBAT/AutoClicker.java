package com.ferra13671.BThack.impl.Modules.COMBAT;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Ticker;
import com.ferra13671.BThack.mixins.accessor.IMinecraftClient;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.item.SwordItem;

import java.util.Arrays;

public class AutoClicker extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", this, Arrays.asList("Cooldown", "Delay"));
    public final NumberSetting delay = new NumberSetting("Delay", this, 500, 100, 3000, true, () -> mode.getValue().equals("Delay"));

    public final BooleanSetting onlySword = new BooleanSetting("Only Sword", this, false);
    public final BooleanSetting ifPressing = new BooleanSetting("If Pressing", this, true);

    public AutoClicker() {
        super("AutoClicker",
                "lang.module.AutoClicker",
                KeyboardUtils.RELEASE,
                MCategory.COMBAT,
                false
        );
    }

    private final Ticker ticker = new Ticker();

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
            return ticker.passed(delay.getValue());
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
