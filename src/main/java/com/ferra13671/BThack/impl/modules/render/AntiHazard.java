package com.ferra13671.BThack.impl.modules.render;

import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.effect.StatusEffects;

@ModuleInfo(name = "AntiHazard", description = "lang.module.AntiHazard", category = "RENDER")
public class AntiHazard extends Module {

    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        if (mc.player.hasStatusEffect(StatusEffects.BLINDNESS))
            mc.player.removeStatusEffect(StatusEffects.BLINDNESS);
        if (mc.player.hasStatusEffect(StatusEffects.NAUSEA))
            mc.player.removeStatusEffect(StatusEffects.NAUSEA);
    }
}
