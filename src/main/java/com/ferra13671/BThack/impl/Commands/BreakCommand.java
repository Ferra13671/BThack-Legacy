package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import com.ferra13671.BThack.api.Managers.Destroy.DestroyManager;
import com.ferra13671.BThack.api.Managers.Destroy.DestroyThread3D;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.MathUtils;
import com.ferra13671.BThack.api.Utils.ModifyBlockPos;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;

public class BreakCommand extends AbstractCommand {

    public BreakCommand() {
        super("lang.command.BreakCommand.description", "break [x] [y] [z]", "break"
        );
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 3) {
            invalidArgumentError();
            return;
        }
        if (!MathUtils.isDouble(args[0]) || !MathUtils.isDouble(args[1]) || !MathUtils.isDouble(args[2])) {
            invalidArgumentError();
            return;
        }
        if (DestroyManager.isDestroying) {
            ChatUtils.sendMessage(Formatting.RED + LanguageSystem.translate("lang.command.BreakCommand.alreadyDestroying"));
            return;
        }

        DestroyThread3D thread3D = new DestroyThread3D();
        thread3D.set3DSchematic(new ArrayList<>(Arrays.asList(new Vec3d(Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2])))), new ModifyBlockPos(mc.player.getX(), mc.player.getY(), mc.player.getZ()));
        thread3D.start();
    }
}
