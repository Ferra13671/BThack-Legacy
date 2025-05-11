package com.ferra13671.BThack.core.Client.Systems;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.core.Client.Client;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class FirstLaunchWelcomer implements Mc {

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (!Module.nullCheck()) {
            mc.player.sendMessage(Text.literal("Welcome to " + Formatting.BLUE + "BThack" + Formatting.RESET + "!"));
            mc.player.sendMessage(Text.literal("The ClickGui bind is " + Formatting.AQUA + KeyboardUtils.getKeyName(ModuleList.clickGui.getKey())));
            mc.player.sendMessage(Text.literal("The command prefix is " + Formatting.AQUA + Client.clientInfo.getChatPrefix()));
            mc.player.sendMessage(Text.literal("Type " + Formatting.AQUA + "$help" + Formatting.RESET + " to get a list of available commands"));

            BThack.EVENT_BUS.unregister(this);
        }
    }
}
