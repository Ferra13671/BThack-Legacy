package com.ferra13671.BThack.impl.Modules.Misc;

import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.core.Client.Client;
import com.ferra13671.BThack.events.PacketEvent;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.DataList.DataLists;
import com.ferra13671.BThack.api.Utils.DataList.PacketList;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.Formatting;

@ModuleInfo(name = "PacketCanceller", description = "lang.module.PacketCanceller", category = "MISC")
public class PacketCanceller extends Module {

    @Override
    public void onEnable() {
        super.onEnable();
        if (!nullCheck())
            ChatUtils.sendMessage(Formatting.GRAY + "Use: " + Client.clientInfo.getChatPrefix() + DataLists.get("PacketCanceller", PacketList.class).editDataListCommand.getAliases()[0]);
    }

    @EventSubscriber
    public void onPacketSend(PacketEvent.Send e) {
        if (DataLists.get("PacketCanceller", PacketList.class).values.contains(e.getPacket().getClass())) e.setCancelled(true);
    }

    @EventSubscriber
    public void onPacketReceive(PacketEvent.Receive e) {
        if (DataLists.get("PacketCanceller", PacketList.class).values.contains(e.getPacket().getClass())) e.setCancelled(true);
    }
}
