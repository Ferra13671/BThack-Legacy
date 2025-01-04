package com.ferra13671.BThack.impl.Commands.Social.Clans;

import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import com.ferra13671.BThack.api.Social.Clans.ClansUtils;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.math.NumberUtils;

public class ClansCommand extends AbstractCommand {

    public ClansCommand() {
        super("lang.command.Clans.description", "clans/clan [ [add] [clanName] [ [red] [green] [blue] ] / [ red/orange/yellow/green/blue/violet ] ] / [ [remove] [clanName] ]", "clans", "clan"
        );
    }

    @Override
    public void execute(String[] args) {
        if (args.length > 1 && (args[0].equals("add") || args[0].equals("remove"))) {
            switch (args[0]) {
                case "add":
                    if (args.length > 3) {
                        if (args.length > 4) {
                            if (NumberUtils.isNumber(args[2]) && NumberUtils.isNumber(args[3]) && NumberUtils.isNumber(args[4])) {
                                if (Integer.parseInt(args[2]) < 256 && Integer.parseInt(args[3]) < 256 && Integer.parseInt(args[4]) < 256) {
                                    if (ClansUtils.addClan(args[1], (float) Integer.parseInt(args[2]) / 255, (float) Integer.parseInt(args[3]) / 255, (float) Integer.parseInt(args[4]) / 255)) {
                                        ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Clans.clanAdded"));
                                    } else {
                                        ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.Clans.clanExists"));
                                    }
                                } else
                                    invalidArgumentError();
                            } else
                                invalidArgumentError();
                        } else
                            invalidArgumentError();
                    } else {
                        if (args.length > 2) {
                            if (ClansUtils.addClan(args[1], args[2])) {
                                ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Clans.clanAdded"));
                            } else {
                                ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.Clans.clanExists"));
                            }
                        } else {
                            invalidArgumentError();
                        }
                    }
                    break;
                case "remove":
                    if (ClansUtils.removeClan(args[1])) {
                        ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Clans.clanRemoved"));
                    } else {
                        ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.Clans.clanDoesn'tExists"));
                    }
                    break;
            }
        } else
            invalidArgumentError();
    }
}
