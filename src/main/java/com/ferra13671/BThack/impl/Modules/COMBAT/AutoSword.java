package com.ferra13671.BThack.impl.Modules.COMBAT;

import com.ferra13671.BThack.api.Events.Entity.AttackEntityEvent;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.SwordItem;

@ModuleInfo(name = "AutoSword", description = "lang.module.AutoSword", category = "COMBAT")
public class AutoSword extends Module {

    @EventSubscriber
    public void onPacket(AttackEntityEvent e) {
        if (e.getEntity() instanceof EndCrystalEntity || e.getPlayer() != mc.player) return;

        int inventorySlot = InventoryUtils.findItem(SwordItem.class);
        if (inventorySlot != -1) {
            InventoryUtils.swapItem(inventorySlot);
        }
    }
}
