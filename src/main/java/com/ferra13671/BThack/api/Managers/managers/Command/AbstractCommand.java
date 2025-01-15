package com.ferra13671.BThack.api.Managers.managers.Command;

import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

import java.util.function.Supplier;

public abstract class AbstractCommand {
    public static final MinecraftClient mc = MinecraftClient.getInstance();
    public static final int SUCCESFUL = 1;

    private final String[] aliases;
    private final String description;

    public AbstractCommand(String description, String... aliases) {
        this.aliases = aliases;
        this.description = description;
    }

    public abstract void compile(LiteralArgumentBuilder<CommandSource> builder);

    public void sendMessage(String text) {
        ChatUtils.sendMessage(text);
    }

    public void error(String text) {
        ChatUtils.sendMessage(Formatting.RED + text);
    }

    protected static <T> RequiredArgumentBuilder<CommandSource, T> arg(String name, Supplier<ArgumentType<T>> type) {
        return RequiredArgumentBuilder.argument(name, type.get());
    }

    protected static LiteralArgumentBuilder<CommandSource> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    public void register(CommandDispatcher<CommandSource> dispatcher) {
        for (String alias : aliases) {
            LiteralArgumentBuilder<CommandSource> builder = LiteralArgumentBuilder.literal(alias);
            compile(builder);
            dispatcher.register(builder);
        }
    }

    public String[] getAliases() {
        return aliases;
    }

    public String getFormatedAliases() {
        StringBuilder text = new StringBuilder(aliases[0]);
        if (aliases.length > 1) {
            for (int i = 1; i < aliases.length; i++)
                text.append(" / ").append(aliases[i]);
        }
        return text.toString();
    }

    public String getDescription() {
        return description.startsWith("lang.") ? LanguageSystem.translate(description) : description;
    }
}
