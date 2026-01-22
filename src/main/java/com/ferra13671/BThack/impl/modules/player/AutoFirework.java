package com.ferra13671.BThack.impl.modules.player;


import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.core.client.ModuleList;
import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.impl.setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.impl.setting.Settings.ModeSetting;
import com.ferra13671.BThack.managers.impl.setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.core.client.systems.sound.SoundSystem;
import com.ferra13671.BThack.core.client.systems.sound.Sounds;
import com.ferra13671.BThack.api.utils.*;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;

import java.util.Arrays;

@ModuleInfo(name = "AutoFirework", description = "lang.module.AutoFirework", category = "PLAYER")
public class AutoFirework extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", this, Arrays.asList("One", "Always"));
    public final NumberSetting delay = new NumberSetting("Delay", this, 0, 0, 5000, true, () -> mode.getValue().equals("Always"));
    public final BooleanSetting swingHand = new BooleanSetting("Swing Hand", this, true);

    private final Ticker ticker = new Ticker();

    @Override
    public void playOffSound() {
        if (mode.getValue().equals("Always"))
            if (ModuleList.clientSettings.moduleToggleSound.getValue())
                SoundSystem.playSound(Sounds.MODULE_OFF, ModuleList.clientSettings.soundVolume.getValue().floatValue());
    }

    @Override
    public void sendToggleMessage() {
        if (ModuleList.chatNotifications.isEnabled() && ModuleList.chatNotifications.moduleToggle.getValue()) {
            if (mode.getValue().equals("Always")) {
                if (enabled)
                    ChatUtils.sendMessage(this.getName() + ": " + Formatting.GREEN + "Enabled");
                else
                    ChatUtils.sendMessage(this.getName() + ": " + Formatting.RED + "Disabled");
            } else if (enabled) ChatUtils.sendMessage(this.getName() + ": " + Formatting.YELLOW + "Toggled");
        }
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
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;
        if (!mc.player.isGliding()) return;
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
        if (mc.player != null && mc.player.isGliding()) {
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
