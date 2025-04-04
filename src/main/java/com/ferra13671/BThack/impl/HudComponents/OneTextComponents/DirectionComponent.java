package com.ferra13671.BThack.impl.HudComponents.OneTextComponents;

import com.ferra13671.BThack.api.Utils.RotateUtils;
import com.ferra13671.BThack.impl.HudComponents.AbstractOneTextComponent;
import net.minecraft.util.Formatting;

public class DirectionComponent extends AbstractOneTextComponent {

    public DirectionComponent() {
        super("Direction",
                5,
                100,
                true
        );
    }

    @Override
    public String getText() {
        return "Direction: " + Formatting.WHITE + RotateUtils.getDirection(mc.player);
    }
}
