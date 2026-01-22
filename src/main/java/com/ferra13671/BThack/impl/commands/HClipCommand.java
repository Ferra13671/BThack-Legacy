package com.ferra13671.BThack.impl.commands;

import com.ferra13671.BThack.managers.impl.command.AbstractCommand;
import com.ferra13671.BThack.managers.impl.command.Arguments;
import com.ferra13671.BThack.api.utils.ChatUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

public class HClipCommand extends AbstractCommand {
    public HClipCommand() {
        super("lang.command.HClipCommand.description", "hclip");
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(arg("x Delta", Arguments.DOUBLE).then(arg("z Delta", Arguments.DOUBLE).executes(context -> {
            double xDelta = context.getArgument("x Delta", Double.class);
            double zDelta = context.getArgument("z Delta", Double.class);
            ChatUtils.sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.HClipCommand.message"), Formatting.GRAY + "X: " + Formatting.WHITE + xDelta + Formatting.GRAY + "  Z: " + Formatting.WHITE + zDelta + Formatting.AQUA));

            mc.player.setPosition(mc.player.getX() + xDelta, mc.player.getY(), mc.player.getZ() + zDelta);
            return SUCCESFUL;
        })));
    }
}
