package com.ferra13671.BThack.impl.HudComponents.OneTextComponents;

import com.ferra13671.BThack.Constants;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.impl.HudComponents.AbstractOneTextComponent;
import net.minecraft.util.Formatting;

@ModuleInfo(name = "Rotation", category = "HUD", autoEnabled = true)
public class RotationComponent extends AbstractOneTextComponent {

    public RotationComponent() {
        super(5, 85);
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


        return "Yaw: " + Formatting.WHITE + Constants.DECIMAL_FORMAT.format(preYaw) + Formatting.RESET + " " +
                "Pitch: " + Formatting.WHITE + Constants.DECIMAL_FORMAT.format(mc.player.getPitch());
    }
}
