package com.ferra13671.BThack.impl.HudComponents.OneTextComponents;

import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Utils.PlayerUtils;
import com.ferra13671.BThack.impl.HudComponents.AbstractOneTextComponent;
import net.minecraft.util.Formatting;

@ModuleInfo(name = "Dimension", category = "HUD")
public class DimensionComponent extends AbstractOneTextComponent {

    public DimensionComponent() {
        super(5, 205);
    }

    @Override
    public String getText() {
        return "Dimension: " + Formatting.WHITE + PlayerUtils.getDimension();
    }
}
