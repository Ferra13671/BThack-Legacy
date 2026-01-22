package com.ferra13671.BThack.impl.modules.misc;

import com.ferra13671.BThack.events.SoundPlayEvent;
import com.ferra13671.BThack.managers.impl.setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.google.common.collect.Sets;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.util.HashSet;
import java.util.Set;

@ModuleInfo(name = "NoSoundLag", description = "lang.module.NoSoundLag", category = "MISC")
public class NoSoundLag extends Module {

    public final BooleanSetting armorEquip = new BooleanSetting("ArmorEquip", this, true);
    public final BooleanSetting explode = new BooleanSetting("Explode", this, true);
    public final BooleanSetting attack = new BooleanSetting("Attack", this, true);

    private final Set<SoundEvent> armorSounds = new HashSet<>(Sets.newHashSet(
            SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE.value(),
            SoundEvents.ITEM_ARMOR_EQUIP_TURTLE.value(),
            SoundEvents.ITEM_ARMOR_EQUIP_CHAIN.value(),
            SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA.value(),
            SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND.value(),
            SoundEvents.ITEM_ARMOR_EQUIP_GOLD.value(),
            SoundEvents.ITEM_ARMOR_EQUIP_IRON.value(),
            SoundEvents.ITEM_ARMOR_EQUIP_LEATHER.value(),
            SoundEvents.ITEM_ARMOR_EQUIP_GENERIC.value()
    ));

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onSound(SoundPlayEvent e) {
        if (armorEquip.getValue() && armorSounds.contains(e.soundEvent))
            e.setCancelled(true);
        if (explode.getValue() && e.soundEvent.equals(SoundEvents.ENTITY_GENERIC_EXPLODE.value()))
            e.setCancelled(true);
        if (attack.getValue() && e.soundEvent.equals(SoundEvents.ENTITY_PLAYER_ATTACK_WEAK) || e.soundEvent.equals(SoundEvents.ENTITY_PLAYER_ATTACK_STRONG))
            e.setCancelled(true);
    }
}
