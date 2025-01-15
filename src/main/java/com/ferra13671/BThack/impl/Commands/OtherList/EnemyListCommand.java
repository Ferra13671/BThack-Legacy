package com.ferra13671.BThack.impl.Commands.OtherList;

import com.ferra13671.BThack.api.Managers.managers.Command.AbstractCommand;
import com.ferra13671.BThack.api.Social.SocialManagers;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClientSettings;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

public class EnemyListCommand extends AbstractCommand {
    public EnemyListCommand() {
        super("lang.command.EnemyList.description", "enemyList");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            sendMessage(Formatting.AQUA + "|$#> " + ClientSettings.getEnemyColor() + "Enemies" + Formatting.AQUA + " <#&|");
            for (String name : SocialManagers.ENEMIES.getPlayers()) {
                sendMessage(name);
            }

            return SUCCESFUL;
        });
    }
}
