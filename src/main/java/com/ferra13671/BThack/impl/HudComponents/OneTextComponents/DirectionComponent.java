package com.ferra13671.BThack.impl.HudComponents.OneTextComponents;

import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Utils.Rotate.RotateUtils;
import com.ferra13671.BThack.impl.HudComponents.AbstractOneTextComponent;
import net.minecraft.util.Formatting;

@ModuleInfo(name = "Direction", category = "HUD", autoEnabled = true)
public class DirectionComponent extends AbstractOneTextComponent {

    public DirectionComponent() {
        super(5, 100);
    }

    @Override
    public String getText() {
        return "Direction: " + Formatting.WHITE + RotateUtils.getDirection(mc.player);
    }
}
