package com.ferra13671.BThack.impl.modules.combat;

import com.ferra13671.BThack.events.entity.AttackEntityEvent;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.api.utils.InventoryUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.SwordItem;

@ModuleInfo(name = "AutoSword", description = "lang.module.AutoSword", category = "COMBAT")
public class AutoSword extends Module {

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onPacket(AttackEntityEvent e) {
        if (e.getEntity() instanceof EndCrystalEntity || e.getPlayer() != mc.player) return;

        int inventorySlot = InventoryUtils.findItem(SwordItem.class);
        if (inventorySlot != -1) {
            InventoryUtils.swapItem(inventorySlot);
        }
    }
}
