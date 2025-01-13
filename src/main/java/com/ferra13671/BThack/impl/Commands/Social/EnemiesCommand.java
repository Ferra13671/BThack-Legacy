package com.ferra13671.BThack.impl.Commands.Social;

import com.ferra13671.BThack.api.Managers.Command.AbstractCommand;
import com.ferra13671.BThack.api.Managers.Command.Arguments;
import com.ferra13671.BThack.api.Social.SocialManagers;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

public class EnemiesCommand extends AbstractCommand {
    public EnemiesCommand() {
        super("lang.command.Enemies.description", "enemies");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("add").then(arg("enemy", Arguments.SOCIAL_ADD(SocialManagers.ENEMIES)).executes(context -> {
            SocialManagers.ENEMIES.add(context.getArgument("enemy", String.class));
            sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Enemies.enemyAdded"));
            return SUCCESFUL;
        })));
        builder.then(literal("remove").then(arg("enemy", Arguments.SOCIAL_REMOVE(SocialManagers.ENEMIES)).executes(context -> {
            SocialManagers.ENEMIES.remove(context.getArgument("enemy", String.class));
            sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Enemies.enemyRemoved"));
            return SUCCESFUL;
        })));
    }
}
