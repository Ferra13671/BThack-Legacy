package com.ferra13671.BThack.api.Utils.DataList;

import com.ferra13671.BThack.core.Client.Systems.FileSystem.JsonUtils;
import com.ferra13671.BThack.managers.managers.Command.Arguments;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.DataList.Commands.AbstractDataListCommand;
import com.ferra13671.BThack.api.Utils.DataList.Commands.EditDataListCommand;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.item.Item;
import net.minecraft.util.Formatting;

import java.io.IOException;

public class ItemList extends DataList<Item, Item> {

    public ItemList(String descName, String alias, String txtName) {
        super(txtName);
        initEditDataListCommand(new EditItemListCommand("lang.command.ListItem.description", descName, alias, this));
        initAbstractDataListCommand(new AbstractDataListCommand("lang.command.ListItem.description", "lang.command.ItemList.message", descName, alias) {
            @Override
            public void sendAllList() {
                ItemList.this.sendAllList();
            }
        });
    }

    @Override
    protected void save(JsonObject jsonObject) {
        JsonArray jsonList = new JsonArray();
        valueNames.forEach(value -> jsonList.add(new JsonPrimitive(value)));
        jsonObject.add("values", jsonList);
    }

    @Override
    protected void load(JsonObject jsonObject) {
        if (!JsonUtils._null(jsonObject, "values")) {
            JsonArray jsonList = jsonObject.get("values").getAsJsonArray();
            jsonList.asList().forEach(jsonElement -> {
                String value = jsonElement.getAsString();
                Item item = ItemUtils.getItemFromName(value);
                if (item != null) {
                    values.add(item);
                    valueNames.add(value);
                }
            });
        }
    }

    @Override
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

    @Override
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

    @Override
    public void clearList() {
        values.clear();
        valueNames.clear();
        try {
            saveInFile();
        } catch (IOException ignored) {}
        ChatUtils.sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.List.listCleared"), editDataListCommand.descName));
    }

    @Override
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
