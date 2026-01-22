package com.ferra13671.BThack.impl.hud.onetext;

import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.impl.hud.AbstractOneTextComponent;
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
