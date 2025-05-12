package com.ferra13671.BThack.impl.HudComponents.OneTextComponents;

import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.impl.HudComponents.AbstractOneTextComponent;
import net.minecraft.util.Formatting;

@ModuleInfo(name = "FPS", category = "HUD", autoEnabled = true)
public class FPSComponent extends AbstractOneTextComponent {

    public FPSComponent() {
        super(5, 47);
    }

    @Override
    public String getText() {
        return "FPS: " + Formatting.WHITE + mc.getCurrentFps();
    }
}
