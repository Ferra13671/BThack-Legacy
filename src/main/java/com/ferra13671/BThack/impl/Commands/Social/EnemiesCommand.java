package com.ferra13671.BThack.impl.Commands.Social;

import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import com.ferra13671.BThack.api.Social.SocialManagers;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.util.Formatting;

public class EnemiesCommand extends AbstractCommand {

    public EnemiesCommand() {
        super("lang.command.Enemies.description", "enemies/enemy [add/remove] [enemyName]", "enemies", "enemy"
        );
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 0 && args.length != 1 && (args[0].equals("add") || args[0].equals("remove"))) {
            if (args[0].equals("add")) {
                if (SocialManagers.ENEMIES.add(args[1])) {
                    ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Enemies.enemyAdded"));
                } else {
                    ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.Enemies.enemyExists"));
                }
            }

            if (args[0].equals("remove")) {
                if (SocialManagers.ENEMIES.remove(args[1])) {
                    ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Enemies.enemyRemoved"));
                } else {
                    ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.Enemies.enemyDoesn'tExists"));
                }
            }

        } else {
            invalidArgumentError();
        }
    }
}
