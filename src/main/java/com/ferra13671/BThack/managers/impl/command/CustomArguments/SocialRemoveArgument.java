package com.ferra13671.BThack.managers.impl.command.CustomArguments;

import com.ferra13671.BThack.managers.impl.SocialManager;
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

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SocialRemoveArgument implements ArgumentType<String> {
    private static final Collection<String> examples = List.of("bebra_tyan", "player123");

    private final SocialManager socialManager;

    public SocialRemoveArgument(SocialManager manager) {
        socialManager = manager;
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        String name = reader.readString();

        String player = socialManager.getPlayers().stream()
                .filter(name::equals)
                .findFirst()
                .orElse(null);
        if (player == null)
            throw new DynamicCommandExceptionType(
                    n -> Text.literal(String.format(LanguageSystem.translate("lang.argument.SocialRemove.exception"), name))
            ).create(reader.readString());
        return player;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(socialManager.getPlayers(), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return examples;
    }
}
