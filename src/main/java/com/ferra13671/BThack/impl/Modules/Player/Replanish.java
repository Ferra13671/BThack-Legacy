package com.ferra13671.BThack.impl.Modules.Player;

import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Utils.Ticker;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

import java.util.ArrayList;
import java.util.Arrays;

@ModuleInfo(name = "Replanish", description = "lang.module.Replanish", category = "PLAYER")
public class Replanish extends Module {

    public final NumberSetting count = new NumberSetting("Item threshold", this, 32,1,63, false);
    public final ModeSetting delayMode = new ModeSetting("Delay Mode", this, Arrays.asList("None", "Ms"));
    public final NumberSetting delay = new NumberSetting("Delay", this, 500, 100, 1000, true, () -> delayMode.getValue().equals("Ms"));


    private final Ticker delayTicker = new Ticker();
    private final ArrayList<ItemInfo> itemInfos = new ArrayList<>();

    @Override
    public void onEnable() {
        super.onEnable();
        delayTicker.reset();
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        if (delayMode.getValue().equals("Ms")) {
            if (delayTicker.passed(delay.getValue())) {
                findAction();
                action();
                delayTicker.reset();
            }
        } else {
            findAction();
            action();
        }
    }

    @SuppressWarnings("DataFlowIssue")
    public void action() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.isEmpty() || !stack.isStackable()) continue;
            if (stack.getCount() == 1 || stack.getCount() <= count.getValue()) {
                for (ItemInfo itemInfo : itemInfos) {
                    if (itemInfo.item == stack.getItem() && stack.getName().getString().equals(itemInfo.stackName) && itemInfo.slot != i) {
                        int slotId = itemInfo.slot < 9 ? itemInfo.slot + 36 : itemInfo.slot;
                        mc.interactionManager.clickSlot(0, slotId, 0, SlotActionType.PICKUP, mc.player);
                        mc.interactionManager.tick();
                        mc.interactionManager.clickSlot(0, i + 36, 0, SlotActionType.PICKUP, mc.player);
                        mc.interactionManager.tick();
                        mc.interactionManager.clickSlot(0, slotId, 0, SlotActionType.PICKUP, mc.player);
                        mc.interactionManager.tick();
                        return;
                    }
                }
            }
        }
    }

    @SuppressWarnings("DataFlowIssue")
    public void findAction() {
        itemInfos.clear();
        for (int i = 9; i < 36; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack != null && !stack.isEmpty())
                itemInfos.add(new ItemInfo(stack.getName().getString(), stack.getItem(), i));
            else
                itemInfos.add(new ItemInfo("", Items.AIR, i));
        }
    }

    private record ItemInfo(String stackName, Item item, int slot) {}
}
