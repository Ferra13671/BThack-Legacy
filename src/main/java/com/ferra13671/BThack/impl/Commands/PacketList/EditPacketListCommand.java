package com.ferra13671.BThack.impl.Commands.PacketList;

import com.ferra13671.BThack.api.Managers.Command.AbstractCommand;
import com.ferra13671.BThack.api.Managers.Command.Arguments;
import com.ferra13671.BThack.api.Utils.List.PacketList.PacketList;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;

public class EditPacketListCommand extends AbstractCommand {
    public final String descName;
    public final PacketList packetList;

    public EditPacketListCommand(String descName, String alias, PacketList packetList) {
        super("lang.command.PacketList.description", alias);
        this.descName = descName;
        this.packetList = packetList;
    }

    @Override
    public String getDescription() {
        return String.format(super.getDescription(), descName);
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("add").then(literal("client->server").then(arg("packet", Arguments.CLIENT_PACKET).executes(context -> {
            packetList.addToList(context.getArgument("packet", String.class));
            packetList.postAction();

            return SUCCESFUL;
        }))).then(literal("server->client").then(arg("packet", Arguments.SERVER_PACKET).executes(context -> {
            packetList.addToList(context.getArgument("packet", String.class));
            packetList.postAction();

            return SUCCESFUL;
        }))));
        builder.then(literal("remove").then(arg("packet", Arguments.PACKET_LIST_PACKET(packetList)).executes(context -> {
            packetList.removeFromList(context.getArgument("packet", String.class));
            packetList.postAction();

            return SUCCESFUL;
        })));
        builder.then(literal("clear").executes(context -> {
            packetList.clearList();
            packetList.postAction();

            return SUCCESFUL;
        }));
    }
}
