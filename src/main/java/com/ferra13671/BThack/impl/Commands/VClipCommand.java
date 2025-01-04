package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.MathUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.util.Formatting;

public class VClipCommand extends AbstractCommand {

    public VClipCommand() {
        super("lang.command.VClipCommand.description", "vclip [y]", "vclip"
        );
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            invalidArgumentError();
            return;
        }

        if (!MathUtils.isDouble(args[0])) {
            invalidArgumentError();
            return;
        }

        ChatUtils.sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.VClipCommand.message"), Formatting.WHITE + args[0] + Formatting.AQUA));

        mc.player.setPosition(mc.player.getX(), mc.player.getY() + Double.parseDouble(args[0]), mc.player.getZ());
    }
}
