package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.entity.Entity;
import net.minecraft.util.Formatting;

public class EntitiesNearbyCommand extends AbstractCommand {

    public EntitiesNearbyCommand() {
        super("lang.command.EntitiesNearby.description", "entitiesNearby", "entitiesNearby"
        );
    }

    @Override
    public void execute(String[] args) {
        int a = 0;
        for (Entity entity : mc.world.getEntities()) {
            if (entity != mc.player)
                a++;
        }
        ChatUtils.sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.EntitiesNearby.message"), a));
    }
}
