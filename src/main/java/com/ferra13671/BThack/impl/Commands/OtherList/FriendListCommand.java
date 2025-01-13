package com.ferra13671.BThack.impl.Commands.OtherList;

import com.ferra13671.BThack.api.Managers.Command.AbstractCommand;
import com.ferra13671.BThack.api.Social.SocialManagers;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClientSettings;
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
            for (String name : SocialManagers.FRIENDS.getPlayers()) {
                sendMessage(name);
            }

            return SUCCESFUL;
        });
    }
}
