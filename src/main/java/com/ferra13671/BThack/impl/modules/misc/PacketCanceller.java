package com.ferra13671.BThack.impl.modules.misc;

import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.core.client.Client;
import com.ferra13671.BThack.events.PacketEvent;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.utils.ChatUtils;
import com.ferra13671.BThack.api.utils.datalist.DataLists;
import com.ferra13671.BThack.api.utils.datalist.PacketList;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.Formatting;

@ModuleInfo(name = "PacketCanceller", description = "lang.module.PacketCanceller", category = "MISC")
@SuppressWarnings("SuspiciousMethodCalls")
public class PacketCanceller extends Module {

    @Override
    public void onEnable() {
        super.onEnable();
        if (!nullCheck())
            ChatUtils.sendMessage(Formatting.GRAY + "Use: " + Client.clientInfo.getChatPrefix() + DataLists.get("PacketCanceller", PacketList.class).editDataListCommand.getAliases()[0]);
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onPacketSend(PacketEvent.Send e) {
        if (DataLists.get("PacketCanceller", PacketList.class).values.contains(e.getPacket().getClass())) e.setCancelled(true);
    }

    @EventSubscriber
    public void onPacketReceive(PacketEvent.Receive e) {
        if (DataLists.get("PacketCanceller", PacketList.class).values.contains(e.getPacket().getClass())) e.setCancelled(true);
    }
}
