package com.ferra13671.BThack.impl.hud.onetext;

import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.api.utils.PlayerUtils;
import com.ferra13671.BThack.impl.hud.AbstractOneTextComponent;
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
