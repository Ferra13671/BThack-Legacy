package com.ferra13671.BThack.impl.hud.onetext;

import com.ferra13671.BThack.Constants;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.api.utils.SpeedMathThread;
import com.ferra13671.BThack.impl.hud.AbstractOneTextComponent;
import net.minecraft.util.Formatting;

@ModuleInfo(name = "Speed", category = "HUD", autoEnabled = true)
public class SpeedComponent extends AbstractOneTextComponent {

    public SpeedComponent() {
        super(5, 130);
    }

    @Override
    public String getText() {
        return "Speed: " + Formatting.WHITE + Constants.DECIMAL_FORMAT.format(SpeedMathThread.speed) + "b/s";
    }
}
