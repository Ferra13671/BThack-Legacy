package com.ferra13671.BThack.impl.Modules.Misc;

import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.MathUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

@ModuleInfo(name = "ItemRandomizer", description = "lang.module.ItemRandomizer", category = "MISC")
public class ItemRandomizer extends Module {

    @EventSubscriber
    public void onClientTick(ClientTickEvent e) {
        if (nullCheck()) return;

        InventoryUtils.swapItem(MathUtils.randomInt(0, 8));
    }
}
