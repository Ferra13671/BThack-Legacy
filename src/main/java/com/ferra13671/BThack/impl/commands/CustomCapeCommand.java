package com.ferra13671.BThack.impl.commands;

import com.ferra13671.BThack.core.client.Client;
import com.ferra13671.BThack.core.client.ClientInfo;
import com.ferra13671.BThack.core.client.systems.config.SubConfigs;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.impl.cape.Cape;
import com.ferra13671.BThack.managers.impl.cape.CapeManager;
import com.ferra13671.BThack.managers.impl.command.AbstractCommand;
import com.ferra13671.BThack.managers.impl.command.Arguments;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CustomCapeCommand extends AbstractCommand {

    public CustomCapeCommand() {
        super("lang.command.CustomCape.description", "customCape");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("openFolder").executes(context -> {
            Util.getOperatingSystem().open(Paths.get("BThack/CustomCapes"));
            return SUCCESFUL;
        }));
        builder.then(literal("set")
                .then(literal("default").executes(context -> {
                    Managers.CAPE_MANAGER.setCape(Cape.fromIdentifier(Identifier.of("bthack", "textures/bthack_cape.png")));
                    Client.clientInfo.setCapeInfo(CapeManager.DEFAULT_CAPE_INFO);
                    SubConfigs.CLIENT_INFO.save();
                    return SUCCESFUL;
                }))
                .then(literal("file").then(arg("cape", Arguments.CAPE_FILE).executes(context -> {
                    try {
                        Path path = Paths.get("BThack/CustomCapes/" + context.getArgument("cape", String.class));
                        Managers.CAPE_MANAGER.setCape(Cape.fromInputStream(Files.newInputStream(path)));
                        Client.clientInfo.setCapeInfo(new ClientInfo.CapeInfo("BThack/CustomCapes/" + context.getArgument("cape", String.class), ClientInfo.CapeDataType.FILE));
                    } catch (IOException e) {
                        error(e.getMessage());
                    }
                    SubConfigs.CLIENT_INFO.save();
                    return SUCCESFUL;
                })))
                .then(literal("url").then(arg("link", Arguments.GREEDY_STRING).executes(context -> {
                    try {
                        Managers.CAPE_MANAGER.setCape(Cape.fromURL(new URI(context.getArgument("link", String.class)).toURL()));
                        Client.clientInfo.setCapeInfo(new ClientInfo.CapeInfo(context.getArgument("link", String.class), ClientInfo.CapeDataType.URL));
                    } catch (IOException | URISyntaxException e) {
                        error(e.getMessage());
                    }
                    SubConfigs.CLIENT_INFO.save();
                    return SUCCESFUL;
                })))
        );
    }
}
