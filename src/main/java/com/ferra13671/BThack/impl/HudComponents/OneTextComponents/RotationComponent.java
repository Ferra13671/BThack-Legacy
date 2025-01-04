package com.ferra13671.BThack.impl.HudComponents.OneTextComponents;

import com.ferra13671.BThack.impl.HudComponents.AbstractOneTextComponent;
import net.minecraft.util.Formatting;

public class RotationComponent extends AbstractOneTextComponent {

    public RotationComponent() {
        super("Rotation",
                5,
                75,
                true
        );
    }

    @Override
    public String getText() {
        double preYaw;

        preYaw = mc.player.getYaw() / 360;
        if (preYaw < 0) {
            preYaw = -preYaw;
            preYaw = (-(preYaw - ((int) preYaw))) * 360;
        } else {
            preYaw = (preYaw - ((int) preYaw)) * 360;
        }


        return "Yaw: " + Formatting.WHITE + decimal.format(preYaw) + Formatting.RESET + " " +
                "Pitch: " + Formatting.WHITE + decimal.format(mc.player.getPitch());
    }
}
