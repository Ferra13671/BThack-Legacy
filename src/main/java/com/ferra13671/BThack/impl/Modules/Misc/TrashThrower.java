package com.ferra13671.BThack.impl.Modules.Misc;

import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.core.Client.Client;
import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.DataList.DataLists;
import com.ferra13671.BThack.api.Utils.DataList.ItemList;
import com.ferra13671.BThack.api.Utils.Ticker;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Formatting;

@ModuleInfo(name = "TrashThrower", description = "lang.module.TrashThrower", category = "MISC")
public class TrashThrower extends Module {

    public final NumberSetting delay = new NumberSetting("Delay", this, 200, 50, 1000, true);


    private boolean firstOpened = true;
    private final Ticker ticker = new Ticker();

    @Override
    public void onEnable() {
        super.onEnable();
        ticker.reset();
        if (!nullCheck()) {
            if (firstOpened) {
                ChatUtils.sendMessage(Formatting.GRAY + "Use: " + Client.clientInfo.getChatPrefix() + DataLists.get("TrashThrower", ItemList.class).editDataListCommand.getAliases()[0]);
                firstOpened = false;
            }
        }
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        if (ticker.passed(delay.getValue())) {
            for (int i = 0; i < 36; i++) {
                if (DataLists.get("TrashThrower", ItemList.class).values.contains(mc.player.getInventory().getStack(i).getItem())) {
                    mc.interactionManager.clickSlot(0, (i < 9 ? i + 36 : i), 0, SlotActionType.PICKUP, mc.player);
                    mc.interactionManager.clickSlot(0, -999, 0, SlotActionType.PICKUP, mc.player);
                }
            }
            ticker.reset();
        }
    }
}
