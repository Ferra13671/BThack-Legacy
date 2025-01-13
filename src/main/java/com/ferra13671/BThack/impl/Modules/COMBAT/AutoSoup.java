package com.ferra13671.BThack.impl.Modules.COMBAT;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

import java.util.Arrays;

public class AutoSoup extends Module {

    public final ModeSetting swap = new ModeSetting("Swap", this, Arrays.asList("Client", "Packet"));
    public final BooleanSetting swingHand = new BooleanSetting("Swing Hand", this, false);

    public final NumberSetting minHealth = new NumberSetting("Min Health", this, 14, 1, 20, false);

    public AutoSoup() {
        super("AutoSoup",
                "lang.module.AutoSoup",
                KeyboardUtils.RELEASE,
                MCategory.COMBAT,
                false
        );

        initSettings(
                swap,
                swingHand,

                minHealth
        );
    }

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
