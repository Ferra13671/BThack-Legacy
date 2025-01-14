package com.ferra13671.BThack.api.Utils.DataList;

import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigUtils;
import com.ferra13671.BThack.api.Managers.Command.Arguments;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.DataList.Commands.AbstractDataListCommand;
import com.ferra13671.BThack.api.Utils.DataList.Commands.EditDataListCommand;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.item.Item;
import net.minecraft.util.Formatting;

import java.io.IOException;

public class ItemList extends DataList<Item, Item> {

    public ItemList(String descName, String alias, String txtName) {
        super(descName, txtName);
        initEditDataListCommand(new EditItemListCommand("lang.command.ListItem.description", descName, alias, this));
        initAbstractDataListCommand(new AbstractDataListCommand("lang.command.ListItem.description", "lang.command.ItemList.message", descName, alias) {
            @Override
            public void sendAllList() {
                ItemList.this.sendAllList();
            }
        });
    }

    public void saveInFile() throws IOException {
        ConfigUtils.saveInTxt(txtName, editDataListCommand.descName, writer -> {
            for (String itemName : valueNames) {
                try {
                    writer.write(itemName + System.lineSeparator());
                } catch (IOException ignored) {}
            }
        });
    }

    public void loadFromFile() throws IOException {
        ConfigUtils.loadFromTxt(txtName, editDataListCommand.descName, line -> {
            Item item = ItemUtils.getItemFromName(line);
            if (item != null) {
                values.add(item);
                valueNames.add(line);
            }
        });
    }

    public void addToList(Item item) {
        if (!values.contains(item)) {
            values.add(item);
            valueNames.add(ItemUtils.getItemName(item));
            try {
                saveInFile();
            } catch (IOException ignored) {}
            ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.List.itemAdded"));
        } else {
            ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.List.itemAlreadyAdded"));
        }
    }

    public void removeFromList(Item item) {
        if (values.contains(item)) {
            values.remove(item);
            valueNames.remove(ItemUtils.getItemName(item));
            try {
                saveInFile();
            } catch (IOException ignored) {}
            ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.List.itemRemoved"));
        } else {
            ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.List.itemAlreadyRemoved"));
        }
    }

    public void clearList() {
        values.clear();
        valueNames.clear();
        try {
            saveInFile();
        } catch (IOException ignored) {}
        ChatUtils.sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.List.listCleared"), editDataListCommand.descName));
    }

    public void sendAllList() {
        for (String itemName : valueNames) {
            ChatUtils.sendMessage(itemName);
        }
    }


    public static class EditItemListCommand extends EditDataListCommand<Item, Item> {

        public EditItemListCommand(String descriptionKey, String descName, String alias, DataList<Item, Item> dataList) {
            super(descriptionKey, descName, alias, dataList);
        }

        @Override
        public void compile(LiteralArgumentBuilder<CommandSource> builder) {
            builder.then(literal("add").then(arg("item", Arguments.ITEM).executes(context -> {
                Item item = context.getArgument("item", Item.class);

                dataList.addToList(item);
                dataList.postAction();

                return SUCCESFUL;
            })));
            builder.then(literal("remove").then(arg("item", Arguments.ITEM).executes(context -> {
                Item item = context.getArgument("item", Item.class);

                dataList.removeFromList(item);
                dataList.postAction();

                return SUCCESFUL;
            })));
            builder.then(literal("clear").executes(context -> {
                dataList.clearList();
                dataList.postAction();

                return SUCCESFUL;
            }));
        }
    }
}
