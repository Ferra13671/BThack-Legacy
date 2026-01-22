package com.ferra13671.BThack.managers.impl.command.CustomArguments;

import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModeArgument implements ArgumentType<String> {
    private static final Collection<String> examples = List.of("Mode1", "Mode2", "Mode3");

    private final String[] modes;

    public ModeArgument(String... modes) {
        this.modes = modes;
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        String name = reader.readString();

        String mode = Arrays.stream(modes)
                .filter(name::equals)
                .findFirst()
                .orElse(null);
        if (mode == null)
            throw new DynamicCommandExceptionType(
                    n -> Text.literal(LanguageSystem.translate("lang.argument.Mode.exception"))
            ).create(reader.readString());
        return name;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(modes, builder);
    }

    @Override
    public Collection<String> getExamples() {
        return examples;
    }
}
