package com.ferra13671.BThack.impl.HudComponents.OneTextComponents;

import com.ferra13671.BThack.impl.HudComponents.AbstractOneTextComponent;
import net.minecraft.util.Formatting;

public class FPSComponent extends AbstractOneTextComponent {

    public FPSComponent() {
        super("FPS",
                5,
                47,
                true
        );
    }

    @Override
    public String getText() {
        return "FPS: " + Formatting.WHITE + mc.getCurrentFps();
    }
}
