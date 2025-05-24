package com.ferra13671.BThack.impl.Modules.Combat;

import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Utils.Ticker;
import com.ferra13671.BThack.mixins.accessor.IMinecraftClient;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.item.SwordItem;

import java.util.Arrays;

@ModuleInfo(name = "AutoClicker", description = "lang.module.AutoClicker", category = "COMBAT")
public class AutoClicker extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", this, Arrays.asList("Cooldown", "Delay"));
    public final NumberSetting delay = new NumberSetting("Delay", this, 500, 100, 3000, true, () -> mode.getValue().equals("Delay"));

    public final BooleanSetting onlySword = new BooleanSetting("Only Sword", this, false);
    public final BooleanSetting ifPressing = new BooleanSetting("If Pressing", this, true);


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
        return mode.getValue().equals("Cooldown") ? mc.player.getAttackCooldownProgress(0) >= 1.0 : ticker.passed(delay.getValue());
    }

    public boolean check() {
        return !onlySword.getValue() || mc.player.getMainHandStack().getItem() instanceof SwordItem;
    }

    public boolean checkPressing() {
        return !ifPressing.getValue() || mc.options.attackKey.isPressed();
    }
}
