package com.ferra13671.BThack.impl.modules.misc;

import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.api.utils.InventoryUtils;
import com.ferra13671.BThack.api.utils.MathUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

@ModuleInfo(name = "ItemRandomizer", description = "lang.module.ItemRandomizer", category = "MISC")
public class ItemRandomizer extends Module {

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onClientTick(ClientTickEvent e) {
        if (nullCheck()) return;

        InventoryUtils.swapItem(MathUtils.randomInt(0, 8));
    }
}
