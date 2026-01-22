package com.ferra13671.BThack.impl.commands.otherlist;

import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.impl.command.AbstractCommand;
import com.ferra13671.BThack.impl.modules.client.ClientSettings;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

public class FriendListCommand extends AbstractCommand {
    public FriendListCommand() {
        super("lang.command.FriendList.description", "friendList");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            sendMessage(Formatting.AQUA + "|$#> " + ClientSettings.getFriendColor() + "Friends" + Formatting.AQUA + " <#&|");
            Managers.FRIENDS_MANAGER.getPlayers().forEach(this::sendMessage);

            return SUCCESFUL;
        });
    }
}
