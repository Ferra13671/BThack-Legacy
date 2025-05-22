package com.ferra13671.BThack.impl.Commands.Social;

import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.managers.Command.AbstractCommand;
import com.ferra13671.BThack.managers.managers.Command.Arguments;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

public class FriendsCommand extends AbstractCommand {
    public FriendsCommand() {
        super("lang.command.Friends.description", "friends");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("add").then(arg("friend", Arguments.SOCIAL_ADD(Managers.FRIENDS_MANAGER)).executes(context -> {
            Managers.FRIENDS_MANAGER.add(context.getArgument("friend", String.class));
            sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Friends.friendAdded"));
            return SUCCESFUL;
        })));
        builder.then(literal("remove").then(arg("friend", Arguments.SOCIAL_REMOVE(Managers.FRIENDS_MANAGER)).executes(context -> {
            Managers.FRIENDS_MANAGER.remove(context.getArgument("friend", String.class));
            sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Friends.friendRemoved"));
            return SUCCESFUL;
        })));
    }
}
