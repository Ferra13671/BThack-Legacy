package com.ferra13671.BThack.api.Utils.DataList;

import com.ferra13671.BThack.core.Client.Systems.FileSystem.JsonUtils;
import com.ferra13671.BThack.api.Managers.managers.Command.Arguments;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.DataList.Commands.AbstractDataListCommand;
import com.ferra13671.BThack.api.Utils.DataList.Commands.EditDataListCommand;
import com.ferra13671.BThack.api.Utils.Lists;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.Formatting;

import java.io.IOException;

public class PacketList extends DataList<Class<? extends Packet<?>>, String> {

    public PacketList(String descName, String alias, String txtName) {
        super(txtName);
        initEditDataListCommand(new EditPacketListCommand("lang.command.PacketList.description", descName, alias, this));
        initAbstractDataListCommand(new AbstractDataListCommand("lang.command.PacketList.description", "lang.command.PacketList.message", descName, alias) {
            @Override
            public void sendAllList() {
                PacketList.this.sendAllList();
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
                if (Lists.PACKETS.containsKey(value)) {
                    values.add(Lists.PACKETS.get(value));
                    valueNames.add(value);
                }
            });
        }
    }

    @Override
    public void addToList(String packet) {
        if (!valueNames.contains(packet)) {
            values.add(Lists.PACKETS.get(packet));
            valueNames.add(packet);
            try {
                saveInFile();
            } catch (IOException ignored) {}
            ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.List.packetAdded"));
        } else {
            ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.List.packetAlreadyAdded"));
        }
    }

    @Override
    public void removeFromList(String packet) {
        if (valueNames.contains(packet)) {
            values.remove(Lists.PACKETS.get(packet));
            valueNames.remove(packet);
            try {
                saveInFile();
            } catch (IOException ignored) {}
            ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.List.packetRemoved"));
        } else {
            ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.List.packetAlreadyRemoved"));
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
        for (String packetName : valueNames) {
            ChatUtils.sendMessage(packetName);
        }
    }

    public static class EditPacketListCommand extends EditDataListCommand<Class<? extends Packet<?>>, String> {

        public EditPacketListCommand(String descriptionKey, String descName, String alias, DataList<Class<? extends Packet<?>>, String> dataList) {
            super(descriptionKey, descName, alias, dataList);
        }

        @Override
        public void compile(LiteralArgumentBuilder<CommandSource> builder) {
            builder.then(literal("add").then(literal("client->server").then(arg("packet", Arguments.CLIENT_PACKET).executes(context -> {
                dataList.addToList(context.getArgument("packet", String.class));
                dataList.postAction();

                return SUCCESFUL;
            }))).then(literal("server->client").then(arg("packet", Arguments.SERVER_PACKET).executes(context -> {
                dataList.addToList(context.getArgument("packet", String.class));
                dataList.postAction();

                return SUCCESFUL;
            }))));
            builder.then(literal("remove").then(arg("packet", Arguments.PACKET_LIST_PACKET((PacketList) dataList)).executes(context -> {
                dataList.removeFromList(context.getArgument("packet", String.class));
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
