package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.api.Events.SoundPlayEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.google.common.collect.Sets;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.util.HashSet;
import java.util.Set;

public class NoSoundLag extends Module {

    public static BooleanSetting armorEquip;
    public static BooleanSetting explode;
    public static BooleanSetting attack;

    public NoSoundLag() {
        super("NoSoundLag",
                "lang.module.NoSoundLag",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        armorEquip = new BooleanSetting("ArmorEquip", this, true);
        explode = new BooleanSetting("Explode", this, true);
        attack = new BooleanSetting("Attack", this, true);

        initSettings(
                armorEquip,
                explode,
                attack
        );
    }

    private final Set<SoundEvent> armorSounds = new HashSet<>(Sets.newHashSet(
            SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
            SoundEvents.ITEM_ARMOR_EQUIP_TURTLE,
            SoundEvents.ITEM_ARMOR_EQUIP_CHAIN,
            SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA,
            SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND,
            SoundEvents.ITEM_ARMOR_EQUIP_GOLD,
            SoundEvents.ITEM_ARMOR_EQUIP_IRON,
            SoundEvents.ITEM_ARMOR_EQUIP_LEATHER,
            SoundEvents.ITEM_ARMOR_EQUIP_GENERIC
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
