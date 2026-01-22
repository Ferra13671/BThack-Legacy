package com.ferra13671.BThack.mixins.baritone;

import baritone.api.utils.Helper;
import com.ferra13671.BThack.core.client.ModuleList;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

import java.util.stream.Stream;

@Pseudo
@Mixin(targets = {"baritone.process.MineProcess"}, remap = false)
public abstract class MixinMineProcess implements Helper {

    @Override
    public void logDirect(String var1, Formatting var2, boolean var3) {
        if (var1.startsWith("Unable to find any path to ") && var1.contains(", canceling mine")) {
            if (ModuleList.autoMine != null)
                if (ModuleList.autoMine.isEnabled())
                    ModuleList.autoMine.setEnabled(false);
        }
        Stream.of(var1.split("\n")).forEach((var3x) -> {
            MutableText var4;
            MutableText var10000 = var4 = Text.literal(var3x.replace("\t", "    "));
            var10000.setStyle(var10000.getStyle().withColor(var2));
            this.logDirect(var3, var4);
        });
    }
}
