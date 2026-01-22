package com.ferra13671.BThack.impl.commands;

import com.ferra13671.BThack.core.client.Client;
import com.ferra13671.BThack.core.client.systems.config.SubConfigs;
import com.ferra13671.BThack.core.render.BThackRender;
import com.ferra13671.BThack.managers.impl.command.AbstractCommand;
import com.ferra13671.BThack.managers.impl.command.Arguments;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;

public class CustomFontCommand extends AbstractCommand {
    public CustomFontCommand() {
        super("lang.command.CustomFont.description", "customFont");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("setDefault").executes(context -> {
            Client.clientInfo.setFont("default");
            SubConfigs.CLIENT_INFO.save();
            try {
                BThackRender.reloadFontRenderManager();
            } catch (Exception ignored) {}
            return SUCCESFUL;
        }));
        builder.then(literal("set").then(arg("font", Arguments.FONT_FILE("BThack/Fonts/")).executes(context -> {
            Client.clientInfo.setFont(context.getArgument("font", String.class));
            SubConfigs.CLIENT_INFO.save();
            try {
                BThackRender.reloadFontRenderManager();
            } catch (Exception ignored) {}
            return SUCCESFUL;
        })));
    }
}
