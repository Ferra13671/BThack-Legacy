package com.ferra13671.BThack.impl.HudComponents.OneTextComponents;

import com.ferra13671.BThack.Constants;
import com.ferra13671.BThack.api.Utils.SpeedMathThread;
import com.ferra13671.BThack.impl.HudComponents.AbstractOneTextComponent;
import net.minecraft.util.Formatting;

public class SpeedComponent extends AbstractOneTextComponent {

    public SpeedComponent() {
        super("Speed",
                5,
                130,
                true
        );
    }

    @Override
    public String getText() {
        return "Speed: " + Formatting.WHITE + Constants.DECIMAL_FORMAT.format(SpeedMathThread.speed) + "b/s";
    }
}
