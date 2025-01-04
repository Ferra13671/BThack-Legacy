package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.MathUtils;
import com.ferra13671.BThack.api.Utils.Modules.StrafeUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.util.Formatting;

public class HClipCommand extends AbstractCommand {

    public HClipCommand() {
        super("lang.command.HClipCommand.description", "hclip [ [x] [z] ] / [length]", "hclip"
        );
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            invalidArgumentError();
            return;
        }
        if (args.length == 1) {
            if (!MathUtils.isDouble(args[0])) {
                invalidArgumentError();
                return;
            }

            double[] moveF = StrafeUtils.getMoveFactors(Double.parseDouble(args[0]));

            ChatUtils.sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.HClipCommand.message"), Formatting.WHITE + args[0] + Formatting.AQUA));

            mc.player.setPosition(mc.player.getX() + moveF[0], mc.player.getY(), mc.player.getZ() + moveF[1]);
        } else {
            if (!MathUtils.isDouble(args[0]) || !MathUtils.isDouble(args[1])) {
                invalidArgumentError();
                return;
            }

            ChatUtils.sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.HClipCommand.message"), Formatting.GRAY + "X: " + Formatting.WHITE + args[0] + Formatting.GRAY + "  Z: " + Formatting.WHITE + args[1] + Formatting.AQUA));

            mc.player.setPosition(mc.player.getX() + Double.parseDouble(args[0]), mc.player.getY(), mc.player.getZ() + Double.parseDouble(args[1]));
        }
    }
}
