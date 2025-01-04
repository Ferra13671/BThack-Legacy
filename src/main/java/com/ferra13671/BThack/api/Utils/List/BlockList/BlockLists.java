package com.ferra13671.BThack.api.Utils.List.BlockList;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigUtils;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Plugin.Plugin;
import com.ferra13671.BThack.api.Plugin.PluginSystem;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.RunnableWithObject;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.block.Block;
import net.minecraft.util.Formatting;

import java.io.IOException;
import java.util.HashMap;
import java.util.function.Supplier;

public final class BlockLists implements Mc {
    private static final Supplier<BlockList> SEARCH = () -> new BlockList("Search", "search", "SearchBlocks") {
        @Override
        public void saveInFile() throws IOException {
            ConfigUtils.saveInTxt("SearchBlocks", "Search", writer -> {
                for (String blockName : get("Search").blockNames) {
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
                    get("Search").blockNames.add(line);
                }
            });
        }

        @Override
        public void addToList(String arg) {
            if (!Managers.BLOCK_SEARCH_MANAGER.getSearchBlocks().contains(block)) {
                Managers.BLOCK_SEARCH_MANAGER.addBlockToSearch(block);
                get("Search").blockNames.add(arg);
                try {
                    get("Search").saveInFile();
                } catch (IOException ignored) {}
                ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.List.blockAdded"));
            } else {
                ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.List.blockAlreadyAdded"));
            }
        }

        @Override
        public void removeFromList(String arg) {
            if (Managers.BLOCK_SEARCH_MANAGER.getSearchBlocks().contains(block)) {
                Managers.BLOCK_SEARCH_MANAGER.removeBlockToSearch(block);
                get("Search").blockNames.remove(arg);
                try {
                    get("Search").saveInFile();
                } catch (IOException ignored) {}
                ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.List.blockRemoved"));
            } else {
                ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.List.blockAlreadyRemoved"));
            }
        }

        @Override
        public void clearList() {
            Managers.BLOCK_SEARCH_MANAGER.clearSearchBlocks();
            get("Search").blockNames.clear();
            try {
                get("Search").saveInFile();
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





    private static final HashMap<String , BlockList> blockLists = new HashMap<>();
    private static boolean inited = false;

    public static void init() {
        if (inited) return;

        add(SEARCH.get());
        add(BREAKER.get());
        add(XRAY.get());
        add(AUTOMINE.get());

        PluginSystem.getLoadedPlugins().forEach(Plugin::onInitBlockLists);

        inited = true;
    }


    public static BlockList get(String name) {
        return blockLists.get(name);
    }

    public static void add(BlockList blockList) {
        add(blockList.editBlockListCommand.descName, blockList);
    }

    public static void add(String name, BlockList blockList) {
        blockLists.put(name, blockList);
    }

    public static void forEach(RunnableWithObject<BlockList> runnable) {
        blockLists.forEach((name, blockList) -> runnable.run(blockList));
    }
}
