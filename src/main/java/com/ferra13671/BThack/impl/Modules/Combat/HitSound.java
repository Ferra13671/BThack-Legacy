package com.ferra13671.BThack.impl.Modules.Combat;

import com.ferra13671.BThack.events.Entity.AttackEntityEvent;
import com.ferra13671.BThack.managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.sound.SoundEvents;

import java.util.ArrayList;
import java.util.Arrays;

@ModuleInfo(name = "HitSound", description = "lang.module.HitSound", category = "COMBAT")
public class HitSound extends Module {

    public final ModeSetting sound = new ModeSetting("Sound", this, new ArrayList<>(Arrays.asList("Ding","Meow","Villager","Enderman","EnderDragon","Blaze","Chicken","Cow")));
    public final NumberSetting volume = new NumberSetting("Volume", this, 1, 0.5, 3, false);


    @EventSubscriber
    public void onUpdate(AttackEntityEvent e) {
        if (nullCheck()) return;

        if (e.getPlayer() == mc.player) {
            if (e.getEntity() instanceof EndCrystalEntity) return;

            float volume1 = volume.getValue().floatValue();

            switch (sound.getValue()) {
                case "Ding" -> mc.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, volume1, 1);
                case "Meow" -> mc.player.playSound(SoundEvents.ENTITY_CAT_AMBIENT, volume1, 1);
                case "Villager" -> mc.player.playSound(SoundEvents.ENTITY_VILLAGER_HURT, volume1, 1);
                case "Enderman" -> mc.player.playSound(SoundEvents.ENTITY_ENDERMAN_HURT, volume1, 1);
                case "EnderDragon" -> mc.player.playSound(SoundEvents.ENTITY_ENDER_DRAGON_HURT, volume1, 1);
                case "Blaze" -> mc.player.playSound(SoundEvents.ENTITY_BLAZE_HURT, volume1, 1);
                case "Chicken" -> mc.player.playSound(SoundEvents.ENTITY_CHICKEN_HURT, volume1, 1);
                case "Cow" -> mc.player.playSound(SoundEvents.ENTITY_COW_HURT, volume1, 1);
            }
        }
    }
}