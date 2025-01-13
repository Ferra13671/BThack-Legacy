package com.ferra13671.BThack.impl.HudComponents.OneTextComponents;

import com.ferra13671.BThack.impl.HudComponents.AbstractOneTextComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;

public class MinecraftTimeComponent extends AbstractOneTextComponent {

    public MinecraftTimeComponent() {
        super("MinecraftTime",
                MinecraftClient.getInstance().getWindow().getScaledWidth() / 2f,
                20,
                true
        );
    }

    @Override
    public String getText() {
        int hours = (int) (mc.world.getTimeOfDay() / 1000d);
        if (hours > 24) {
            while ((hours - 24) > 0) {
                hours -= 24;
            }
        }
        int minutes = (int) (((mc.world.getTimeOfDay() / 1000d) - hours) * 10);
        if (minutes > 59) {
            if (minutes == 60)
                minutes = 0;
            while ((minutes - 60) >= 0)
                minutes -= 60;
        }
        return "Minecraft Time: " + Formatting.WHITE + hours + ":" + (minutes > 9 ? minutes : "0" + minutes);
    }
}
