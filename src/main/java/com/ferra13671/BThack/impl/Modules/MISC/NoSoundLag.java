package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.api.Events.SoundPlayEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.google.common.collect.Sets;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.util.HashSet;
import java.util.Set;

public class NoSoundLag extends Module {

    public final BooleanSetting armorEquip = new BooleanSetting("ArmorEquip", this, true);
    public final BooleanSetting explode = new BooleanSetting("Explode", this, true);
    public final BooleanSetting attack = new BooleanSetting("Attack", this, true);

    public NoSoundLag() {
        super("NoSoundLag",
                "lang.module.NoSoundLag",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );
    }

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
    public void onSound(SoundPlayEvent e) {
        if (armorEquip.getValue())
            if (armorSounds.contains(e.soundEvent))
                e.setCancelled(true);
        if (explode.getValue())
            if (e.soundEvent.equals(SoundEvents.ENTITY_GENERIC_EXPLODE))
                e.setCancelled(true);
        if (attack.getValue())
            if (e.soundEvent.equals(SoundEvents.ENTITY_PLAYER_ATTACK_WEAK) || e.soundEvent.equals(SoundEvents.ENTITY_PLAYER_ATTACK_STRONG))
                e.setCancelled(true);
    }
}
