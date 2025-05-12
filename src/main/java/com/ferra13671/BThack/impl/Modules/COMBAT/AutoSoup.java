package com.ferra13671.BThack.impl.Modules.COMBAT;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

import java.util.Arrays;

@ModuleInfo(name = "AutoSoup", description = "lang.module.AutoSoup", category = "COMBAT")
public class AutoSoup extends Module {

    public final ModeSetting swap = new ModeSetting("Swap", this, Arrays.asList("Client", "Packet"));
    public final BooleanSetting swingHand = new BooleanSetting("Swing Hand", this, false);

    public final NumberSetting minHealth = new NumberSetting("Min Health", this, 14, 1, 20, false);


    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        if (mc.player.isAlive()) {
            if (mc.player.getHealth() > minHealth.getValue()) return;

            autoSoupAction();
        }
    }

    public void autoSoupAction() {
        int slot = InventoryUtils.findItem(Items.MUSHROOM_STEW);
        if (slot == -1) return;
        int oldSlot = mc.player.getInventory().selectedSlot;

        InventoryUtils.swapAction(oldSlot, slot, false, swap.getValue());
        ItemUtils.useItem(Hand.MAIN_HAND, swingHand.getValue(), mc.player.getYaw(), mc.player.getPitch());
        InventoryUtils.swapAction(oldSlot, slot, true, swap.getValue());
    }
}
