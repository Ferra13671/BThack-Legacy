package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.MathUtils;
import com.ferra13671.BThack.api.Utils.Ticker;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.entity.mob.ZombieHorseEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.util.Hand;

public class AutoMount extends Module {

    public static NumberSetting range;
    public static NumberSetting delay;

    public static BooleanSetting boats;
    public static BooleanSetting horses;
    public static BooleanSetting skeletonHorses;
    public static BooleanSetting zombieHorses;
    public static BooleanSetting donkeys;
    public static BooleanSetting pigs;
    public static BooleanSetting llamas;


    public AutoMount() {
        super("AutoMount",
                "lang.module.AutoMount",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );

        range = new NumberSetting("Range", this, 4.3, 2, 7, false);
        delay = new NumberSetting("DelayTicks", this, 1, 1, 5, true);

        boats = new BooleanSetting("Boats", this, true);
        horses = new BooleanSetting("Horses", this, true);
        skeletonHorses = new BooleanSetting("Skeleton Horses", this, true, horses::getValue);
        zombieHorses = new BooleanSetting("Zombie Horses", this, true, horses::getValue);
        donkeys = new BooleanSetting("Donkeys", this, true);
        pigs = new BooleanSetting("Pigs", this, true);
        llamas = new BooleanSetting("Llamas", this, true);

        initSettings(
                range,
                delay,

                boats,
                horses,
                skeletonHorses,
                zombieHorses,
                donkeys,
                pigs,
                llamas
        );
    }

    private final Ticker delayTicker = new Ticker();

    @Override
    public void onEnable() {
        super.onEnable();

        delayTicker.reset();
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;
        if (mc.player.getControllingVehicle() != null) return;

        if (!delayTicker.passed(delay.getValue() * mc.renderTickCounter.tickTime)) return;

        for (Entity entity : mc.world.getEntities()) {
            if (entity == mc.player || MathUtils.getDistance(mc.player.getPos(), entity.getPos()) > range.getValue()) continue;

            if (canMount(entity)) {
                mc.interactionManager.interactEntity(mc.player, entity, Hand.MAIN_HAND);
            }
        }
    }

    public boolean canMount(Entity entity) {
        if (entity instanceof AbstractHorseEntity horse) {
            if (horse.isBaby())
                return false;
        }

        if ((entity instanceof BoatEntity || entity instanceof ChestBoatEntity) && boats.getValue())
            return true;

        if (horses.getValue()) {
            if (entity instanceof HorseEntity) return true;
            if (entity instanceof SkeletonHorseEntity && skeletonHorses.getValue()) return true;
            if (entity instanceof ZombieHorseEntity && zombieHorses.getValue()) return true;
        }

        if (entity instanceof DonkeyEntity && donkeys.getValue())
            return true;

        if (entity instanceof PigEntity pig && pigs.getValue()) {
            return pig.isSaddled();
        }

        if (entity instanceof LlamaEntity llama && llamas.getValue()) {
            return !llama.isBaby();
        }

        return false;
    }
}
