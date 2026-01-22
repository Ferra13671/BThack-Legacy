package com.ferra13671.BThack.impl.modules.render;

import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.managers.impl.setting.Settings.ModeSetting;
import com.ferra13671.BThack.managers.impl.setting.Settings.Setting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

import java.util.Arrays;

@ModuleInfo(name = "FullBright", description = "lang.module.FullBright", category = "RENDER")
public class FullBright extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", this, Arrays.asList("Gamma", "Potion"));


    private boolean hasAppliedNightVision = false;

    @Override
    public void onChangeSetting(Setting<?> setting) {
        arrayListInfo = mode.getValue();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        hasAppliedNightVision = false;

        arrayListInfo = mode.getValue();
    }

    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;
        if (mode.getValue().equals("Gamma")) return;
        if (!mc.player.hasStatusEffect(StatusEffects.NIGHT_VISION)) {
            hasAppliedNightVision = true;
            mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 1000000000, 0));
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (hasAppliedNightVision && mc.player != null) {
            mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
            hasAppliedNightVision = false;
        }
    }
}
