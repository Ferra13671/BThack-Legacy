package com.ferra13671.BThack.api.Utils.DataList;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigUtils;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Plugin.Plugin;
import com.ferra13671.BThack.api.Plugin.PluginSystem;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.block.Block;
import net.minecraft.util.Formatting;

import java.io.IOException;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DataLists implements Mc {
    //Block Lists
    private static final Supplier<BlockList> SEARCH = () -> new BlockList("Search", "search", "SearchBlocks") {
        @Override
        public void saveInFile() throws IOException {
            ConfigUtils.saveInTxt("SearchBlocks", "Search", writer -> {
                for (String blockName : get("Search", BlockList.class).valueNames) {
                    try {
                        writer.write(blockName + System.lineSeparator());
                    } catch (IOException ignored) {}
                }
            });
        }

        @Override
        public void loadFromFile() throws IOException {
            ConfigUtils.loadFromTxt("SearchBlocks", "Search", line -> {
                Block block = BlockUtils.getBlockFromNameOrID(line);
                if (block != null) {
                    Managers.BLOCK_SEARCH_MANAGER.addBlockToSearch(block);
                    get("Search", BlockList.class).valueNames.add(line);
                }
            });
        }

        @Override
        public void addToList(Block block) {
            if (!Managers.BLOCK_SEARCH_MANAGER.getSearchBlocks().contains(block)) {
                Managers.BLOCK_SEARCH_MANAGER.addBlockToSearch(block);
                get("Search", BlockList.class).valueNames.add(BlockUtils.getBlockName(block));
                try {
                    get("Search", BlockList.class).saveInFile();
                } catch (IOException ignored) {}
                ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.List.blockAdded"));
            } else {
                ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.List.blockAlreadyAdded"));
            }
        }

        @Override
        public void removeFromList(Block block) {
            if (Managers.BLOCK_SEARCH_MANAGER.getSearchBlocks().contains(block)) {
                Managers.BLOCK_SEARCH_MANAGER.removeBlockToSearch(block);
                get("Search", BlockList.class).valueNames.remove(BlockUtils.getBlockName(block));
                try {
                    get("Search", BlockList.class).saveInFile();
                } catch (IOException ignored) {}
                ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.List.blockRemoved"));
            } else {
                ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.List.blockAlreadyRemoved"));
            }
        }

        @Override
        public void clearList() {
            Managers.BLOCK_SEARCH_MANAGER.clearSearchBlocks();
            get("Search", BlockList.class).valueNames.clear();
            try {
                get("Search", BlockList.class).saveInFile();
            } catch (IOException ignored) {}
            ChatUtils.sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.List.listCleared"), "Search"));
        }
    };
    private static final Supplier<BlockList> BREAKER = () -> new BlockList("Breaker", "breaker", "BreakerBlocks");
    private static final Supplier<BlockList> XRAY = () -> new BlockList("Xray", "xray", "XrayBlocks") {
        @Override
        public void postAction() {
            if (ModuleList.xray.isEnabled())
                mc.worldRenderer.reload();
        }
    };
    private static final Supplier<BlockList> AUTOMINE = () -> new BlockList("AutoMine", "autoMine", "AutoMineBlocks");
    private static final Supplier<BlockList> CUSTOM_FRICTION = () -> new BlockList("CustomFriction", "customFriction", "CustomFrictionBlocks");

    //Item Lists
    private static final Supplier<ItemList> TRASH_THROWER = () -> new ItemList("TrashThrower", "trashThrower", "TrashThrowerItems");
    private static final Supplier<ItemList> CHEST_STEALER = () -> new ItemList("ChestStealer", "chestStealer", "ChestStealerItems");

    //Packet Lists
    private static final Supplier<PacketList> PACKET_CANCELLER = () -> new PacketList("PacketCanceller", "packetCanceller", "PacketCancellerPackets");


    private static final HashMap<String, DataList<?, ?>> dataLists = new HashMap<>();
    private static boolean inited = false;

    public static void init() {
        if (inited) return;

        add(SEARCH.get());
        add(BREAKER.get());
        add(XRAY.get());
        add(AUTOMINE.get());
        add(CUSTOM_FRICTION.get());
        add(TRASH_THROWER.get());
        add(CHEST_STEALER.get());
        add(PACKET_CANCELLER.get());

        PluginSystem.getLoadedPlugins().forEach(Plugin::onInitDataLists);

        inited = true;
    }

    public static <T extends DataList<?, ?>> T get(String key, Class<T> clazz) {
        Object dataList = dataLists.get(key);
        return (T) dataList; //Ignore the warning from IntelliJ IDEA
    }

    public static void add(DataList<?, ?> dataList) {
        add(dataList.editDataListCommand.descName, dataList);
    }

    public static void add(String name, DataList<?, ?> dataList) {
        dataLists.put(name, dataList);
    }

    public static void forEach(Consumer<DataList<?, ?>> consumer) {
        dataLists.forEach((name, dataList) -> consumer.accept(dataList));
    }
}
