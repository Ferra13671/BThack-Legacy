package com.ferra13671.BThack.api.Utils.List.ItemList;

import com.ferra13671.BThack.api.Plugin.Plugin;
import com.ferra13671.BThack.api.Plugin.PluginSystem;
import com.ferra13671.BThack.api.Utils.RunnableWithObject;

import java.util.HashMap;
import java.util.function.Supplier;

public class ItemLists {
    private static final Supplier<ItemList> TRASH_THROWER = () -> new ItemList("TrashThrower", "trashThrower", "TrashThrowerItems");
    private static final Supplier<ItemList> CHEST_STEALER = () -> new ItemList("ChestStealer", "chestStealer", "ChestStealerItems");

    private static final HashMap<String , ItemList> itemLists = new HashMap<>();
    private static boolean inited = false;

    public static void init() {
        if (inited) return;

        add(TRASH_THROWER.get());
        add(CHEST_STEALER.get());

        PluginSystem.getLoadedPlugins().forEach(Plugin::onInitItemLists);

        inited = true;
    }


    public static ItemList get(String name) {
        return itemLists.get(name);
    }

    public static void add(ItemList itemList) {
        add(itemList.editBlockListCommand.descName, itemList);
    }

    public static void add(String name, ItemList itemList) {
        itemLists.put(name, itemList);
    }

    public static void forEach(RunnableWithObject<ItemList> runnable) {
        itemLists.forEach((name, blockList) -> runnable.run(blockList));
    }
}
