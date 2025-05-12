package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BTbot.api.Utils.Generate.NumberGenerator;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

@ModuleInfo(name = "ItemRandomizer", description = "lang.module.ItemRandomizer", category = "MISC")
public class ItemRandomizer extends Module {

    @EventSubscriber
    public void onClientTick(ClientTickEvent e) {
        if (nullCheck()) return;


        InventoryUtils.swapItem(NumberGenerator.generateInt(0, 8));
    }
}
