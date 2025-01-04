package com.ferra13671.BThack.impl.Commands.Social;

import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import com.ferra13671.BThack.api.Social.SocialManagers;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.util.Formatting;

public class FriendsCommand extends AbstractCommand {

    public FriendsCommand() {
        super("lang.command.Friends.description", "friends/friend [add/remove] [friendName]", "friends", "friend"
        );
    }


    @Override
    public void execute(String[] args) {
        if (args.length != 0 && args.length != 1 && (args[0].equals("add") || args[0].equals("remove"))) {
            if (args[0].equals("add")) {
                if (SocialManagers.FRIENDS.add(args[1])) {
                    ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Friends.friendAdded"));
                } else {
                    ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.Friends.friendExists"));
                }
            }

            if (args[0].equals("remove")) {
                if (SocialManagers.FRIENDS.remove(args[1])) {
                    ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Friends.friendRemoved"));
                } else {
                    ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.Friends.friendDoesn'tExists"));
                }
            }

        } else {
            invalidArgumentError();
        }
    }
}
