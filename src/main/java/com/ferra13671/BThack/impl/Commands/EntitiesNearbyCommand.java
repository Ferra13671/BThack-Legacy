package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.api.Managers.Command.AbstractCommand;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.util.Formatting;

public class EntitiesNearbyCommand extends AbstractCommand {
    public EntitiesNearbyCommand() {
        super("lang.command.EntitiesNearby.description", "entitiesNearby");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            int a = 0;
            for (Entity entity : mc.world.getEntities()) {
                if (entity != mc.player)
                    a++;
            }
            sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.EntitiesNearby.message"), a));
            return SUCCESFUL;
        });
    }
}
