package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Events.GuiOpenEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Managers.managers.Thread.ThreadManager;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.DataList.DataLists;
import com.ferra13671.BThack.api.Utils.DataList.ItemList;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Formatting;

import java.util.Arrays;

public class ChestStealer extends Module {

    public final NumberSetting stealDelay = new NumberSetting("Steal Delay", this, 100,0,1000,true);

    public final ModeSetting steal = new ModeSetting("Steal", this, Arrays.asList("All", "Select"));
    public final ModeSetting mode = new ModeSetting("Mode", this, Arrays.asList("WhiteList", "BlackList"), () -> steal.getValue().equals("Select"));

    public final BooleanSetting autoClose = new BooleanSetting("Auto Close", this, true);

    public ChestStealer() {
        super("ChestStealer",
                "lang.module.ChestStealer",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );

        initSettings(
                stealDelay,

                steal,
                mode,

                autoClose
        );
    }

    public static boolean active = false;

    @Override
    public void onChangeSetting(Setting setting) {
        if (setting == steal) {
            if (steal.getValue().equals("Select"))
                ChatUtils.sendMessage(Formatting.GRAY + "Use: " + Client.clientInfo.getChatPrefix() + DataLists.get("ChestStealer", ItemList.class).editDataListCommand.getAliases()[0]);
        }
    }

    @EventSubscriber
    public void onSetScreen(GuiOpenEvent e) {
        if (!(mc.currentScreen instanceof GenericContainerScreen)) active = false;
    }

    @EventSubscriber
    public void onUpdate(ClientTickEvent e) {
        if (nullCheck()) return;

        if (mc.player.currentScreenHandler instanceof GenericContainerScreenHandler) {
            if (!active) {
                ThreadManager.startNewThread(thread -> {
                    if (mc.player.currentScreenHandler instanceof GenericContainerScreenHandler container) {
                        if (container.getInventory().isEmpty() || checkFullInventory()) {
                            while (mc.currentScreen instanceof GenericContainerScreen) {
                                thread.sleepThread(100);
                            }
                            ChestStealer.active = false;
                            thread.stopOnException();
                        }
                        for (int index = 0; index < container.slots.size() - 36; ++index) {
                            if (mc.player.currentScreenHandler instanceof GenericContainerScreenHandler && ModuleList.chestStealer.isEnabled()) {
                                if (checkFullInventory()) break;
                                if (filterStack(container.getInventory().getStack(index))) {
                                    pc.clickSlot(container.syncId, index, 0, SlotActionType.QUICK_MOVE);
                                    thread.sleepThread((long) stealDelay.getValue());
                                }

                                if (container.getInventory().isEmpty()) {
                                    if (autoClose.getValue()) {
                                        pc.closeScreen();
                                        ChestStealer.active = false;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                });
                active = true;
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        active = false;
    }

    public boolean filterStack(ItemStack stack) {
        if (steal.getValue().equals("All")) return stack.getItem() != Items.AIR;
        else {
            if (mode.getValue().equals("WhiteList")) return DataLists.get("ChestStealer", ItemList.class).values.contains(stack.getItem());
            else return !DataLists.get("ChestStealer", ItemList.class).values.contains(stack.getItem());
        }
    }

    public boolean checkFullInventory() {
        for (int i = 0; i < 36; i++) {
            if (mc.player.getInventory().getStack(i).getItem() == Items.AIR) return false;
        }
        return true;
    }
}
