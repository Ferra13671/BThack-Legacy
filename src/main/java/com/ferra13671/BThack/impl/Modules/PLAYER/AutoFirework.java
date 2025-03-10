package com.ferra13671.BThack.impl.Modules.PLAYER;


import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.SoundSystem.SoundSystem;
import com.ferra13671.BThack.api.SoundSystem.Sounds;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Ticker;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

import java.util.Arrays;

public class AutoFirework extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", this, Arrays.asList("One", "Always"));
    public final NumberSetting delay = new NumberSetting("Delay", this, 0, 0, 5000, true, () -> mode.getValue().equals("Always"));
    public final BooleanSetting swingHand = new BooleanSetting("Swing Hand", this, true);

    public AutoFirework() {
        super("AutoFirework",
                "lang.module.AutoFirework",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );

        initSettings(
                mode,

                delay,

                swingHand
        );
    }

    private final Ticker ticker = new Ticker();

    @Override
    public void playOffSound() {
        if (mode.getValue().equals("Always"))
            if (ModuleList.clientSettings.moduleToggleSound.getValue())
                SoundSystem.playSound(Sounds.MODULE_OFF, (float) ModuleList.clientSettings.soundVolume.getValue());
    }

    @Override
    protected void addToArrayList() {
        if (mode.getValue().equals("Always"))
            super.addToArrayList();
    }

    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }
        ticker.reset();

        if (mode.getValue().equals("One")) {
            useFirework(swingHand.getValue());
            toggle();
        } else super.onEnable();
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;
        if (!mc.player.isFallFlying()) return;
        if (mode.getValue().equals("One")) {
            toggle();
            return;
        }

        if (!Managers.FIREWORK_MANAGER.isUsingFireWork()) {
            if (ticker.passed(delay.getValue())) {
                useFirework(swingHand.getValue());
            }
        } else ticker.reset();
    }

    public static void useFirework(boolean swing) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null && mc.player.isFallFlying()) {
            int oldSlot = mc.player.getInventory().selectedSlot;
            int inventorySlot = InventoryUtils.findItem(Items.FIREWORK_ROCKET);
            if (inventorySlot != -1) {
                if (inventorySlot < 9)
                    InventoryUtils.swapItem(inventorySlot);
                else
                    InventoryUtils.swapItemOnInventory(oldSlot, inventorySlot);
                ItemUtils.useItem(Hand.MAIN_HAND, swing, mc.player.getYaw(), mc.player.getPitch());
                if (inventorySlot < 9)
                    InventoryUtils.swapItem(oldSlot);
                else
                    InventoryUtils.swapItemOnInventory(oldSlot, inventorySlot);

                Managers.FIREWORK_MANAGER.resetLastClientUseFireworkTicker();
            }
        }
    }
}
