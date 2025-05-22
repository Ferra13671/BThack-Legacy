package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.core.Client.Client;
import com.ferra13671.BThack.core.Client.Systems.ConfigSystem.SubConfigs;
import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.managers.managers.Command.AbstractCommand;
import com.ferra13671.BThack.managers.managers.Command.Arguments;
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
