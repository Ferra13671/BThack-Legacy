package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.api.Managers.managers.Command.AbstractCommand;
import com.ferra13671.BThack.api.Managers.managers.Command.Arguments;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

public class VClipCommand extends AbstractCommand {
    public VClipCommand() {
        super("lang.command.VClipCommand.description", "vclip");
    }

    @Override
    public void compile(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(arg("y delta", Arguments.DOUBLE).executes(context -> {
            double yDelta = context.getArgument("y delta", Double.class);
            ChatUtils.sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.VClipCommand.message"), "" + Formatting.WHITE + yDelta + Formatting.AQUA));

            mc.player.setPosition(mc.player.getX(), mc.player.getY() + yDelta, mc.player.getZ());

            return SUCCESFUL;
        }));
    }
}
