package com.ferra13671.BThack.api.Utils.List.ItemList;

import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigUtils;
import com.ferra13671.BThack.Core.FileSystem.FileSystem;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import com.ferra13671.BThack.impl.Commands.ItemList.AbstractItemListCommand;
import com.ferra13671.BThack.impl.Commands.ItemList.EditItemListCommand;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.item.Item;
import net.minecraft.util.Formatting;

import java.io.IOException;
import java.util.ArrayList;

public class ItemList {

    public final String txtName;

    public final EditItemListCommand editItemListCommand;
    public final AbstractItemListCommand itemListCommand;

    public final ArrayList<Item> items = new ArrayList<>();
    public final ArrayList<String> itemNames = new ArrayList<>();

    public ItemList(String descName, String alias, String txtName) {
        editItemListCommand = new EditItemListCommand(descName, alias, this);
        itemListCommand = new AbstractItemListCommand(descName, alias + "list") {
            @Override
            public void sendAllList() {
                ItemList.this.sendAllList();
            }
        };
        this.txtName = txtName;
        try {
            FileSystem.registerFolder(descName, "");
            FileSystem.registerFile(txtName, descName, "txt");
        } catch (Exception ignored) {}
    }

    public void saveInFile() throws IOException {
        ConfigUtils.saveInTxt(txtName, editItemListCommand.descName, writer -> {
            for (String itemName : itemNames) {
                try {
                    writer.write(itemName + System.lineSeparator());
                } catch (IOException ignored) {}
            }
        });
    }

    public void loadFromFile() throws IOException {
        ConfigUtils.loadFromTxt(txtName, editItemListCommand.descName, line -> {
            Item item = ItemUtils.getItemFromName(line);
            if (item != null) {
                items.add(item);
                itemNames.add(line);
            }
        });
    }

    public void addToList(Item item) {
        if (!items.contains(item)) {
            items.add(item);
            itemNames.add(ItemUtils.getItemName(item));
            try {
                saveInFile();
            } catch (IOException ignored) {}
            ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.List.itemAdded"));
        } else {
            ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.List.itemAlreadyAdded"));
        }
    }

    public void removeFromList(Item item) {
        if (items.contains(item)) {
            items.remove(item);
            itemNames.remove(ItemUtils.getItemName(item));
            try {
                saveInFile();
            } catch (IOException ignored) {}
            ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.List.itemRemoved"));
        } else {
            ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.List.itemAlreadyRemoved"));
        }
    }

    public void clearList() {
        items.clear();
        itemNames.clear();
        try {
            saveInFile();
        } catch (IOException ignored) {}
        ChatUtils.sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.List.listCleared"), editItemListCommand.descName));
    }

    public void postAction() {
    }

    public void sendAllList() {
        for (String itemName : itemNames) {
            ChatUtils.sendMessage(itemName);
        }
    }
}
