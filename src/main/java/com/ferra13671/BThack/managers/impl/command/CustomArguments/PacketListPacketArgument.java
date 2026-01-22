package com.ferra13671.BThack.managers.impl.command.CustomArguments;

import com.ferra13671.BThack.api.utils.datalist.PacketList;
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

public class PacketListPacketArgument implements ArgumentType<String> {
    private static final Collection<String> examples = List.of("PlayerInteractEntityC2SPacket", "ChatMessageS2CPacket");

    private final PacketList packetList;

    public PacketListPacketArgument(PacketList packetList) {
        this.packetList = packetList;
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        String name = reader.readString();

        String packetName = packetList.valueNames.stream()
                .filter(name::equals)
                .findFirst()
                .orElse(null);
        if (packetName == null)
            throw new DynamicCommandExceptionType(
                    n -> Text.literal(String.format(LanguageSystem.translate("lang.argument.PacketListPacket.exception"), name))
            ).create(reader.readString());
        return name;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(packetList.valueNames, builder);
    }

    @Override
    public Collection<String> getExamples() {
        return examples;
    }
}
