package com.ferra13671.BThack.api.Utils.List.PacketList;

import com.ferra13671.BThack.api.Plugin.Plugin;
import com.ferra13671.BThack.api.Plugin.PluginSystem;
import com.ferra13671.BThack.api.Utils.RunnableWithObject;

import java.util.HashMap;
import java.util.function.Supplier;

/**
 * @Deprecated BlockLists, ItemLists and PacketLists will be merged into
 * one common class in the next update, for easier use and then
 * adding more Lists in the future.
 *
 * @see com.ferra13671.BThack.api.Utils.List.BlockList.BlockLists
 * @see com.ferra13671.BThack.api.Utils.List.ItemList.ItemLists
 */
@Deprecated(forRemoval = true)
public class PacketLists {
    private static final Supplier<PacketList> PACKET_CANCELLER = () -> new PacketList("PacketCanceller", "packetCanceller", "PacketCancellerPackets");

    private static final HashMap<String , PacketList> packetLists = new HashMap<>();
    private static boolean inited = false;

    public static void init() {
        if (inited) return;

        add(PACKET_CANCELLER.get());

        PluginSystem.getLoadedPlugins().forEach(Plugin::onInitPacketLists);

        inited = true;
    }

    public static PacketList get(String name) {
        return packetLists.get(name);
    }

    public static void add(PacketList packetList) {
        add(packetList.editPacketListCommand.descName, packetList);
    }

    public static void add(String name, PacketList packetList) {
        packetLists.put(name, packetList);
    }

    public static void forEach(RunnableWithObject<PacketList> runnable) {
        packetLists.forEach((name, blockList) -> runnable.run(blockList));
    }
}
