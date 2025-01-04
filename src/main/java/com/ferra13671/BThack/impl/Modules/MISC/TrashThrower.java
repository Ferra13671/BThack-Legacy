package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.List.ItemList.ItemLists;
import com.ferra13671.BThack.api.Utils.Ticker;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Formatting;

public class TrashThrower extends Module {

    public final NumberSetting delay = new NumberSetting("Delay", this, 200, 50, 1000, true);

    public TrashThrower() {
        super("TrashThrower",
                "lang.module.TrashThrower",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        initSettings(
                delay
        );
    }
    private boolean firstOpened = true;
    private final Ticker ticker = new Ticker();

    @Override
    public void onEnable() {
        super.onEnable();
        ticker.reset();
        if (!nullCheck()) {
            if (firstOpened) {
                ChatUtils.sendMessage(Formatting.GRAY + "Use: " + Client.clientInfo.getChatPrefix() + ItemLists.get("TrashThrower").editBlockListCommand.getUsage());
                firstOpened = false;
            }
        }
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        if (ticker.passed(delay.getValue())) {
            for (int i = 0; i < 36; i++) {
                if (ItemLists.get("TrashThrower").items.contains(mc.player.getInventory().getStack(i).getItem())) {
                    pc.clickSlot(0, (i < 9 ? i + 36 : i), 0, SlotActionType.PICKUP);
                    pc.clickSlot(0, -999, 0, SlotActionType.PICKUP);
                }
            }
            ticker.reset();
        }
    }
}
