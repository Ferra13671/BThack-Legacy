package com.ferra13671.BThack.impl.HudComponents.OneTextComponents;

import com.ferra13671.BThack.api.Utils.PlayerUtils;
import com.ferra13671.BThack.impl.HudComponents.AbstractOneTextComponent;
import net.minecraft.util.Formatting;

public class DimensionComponent extends AbstractOneTextComponent {

    public DimensionComponent() {
        super("Dimension",
                5,
                205,
                false
        );
    }

    @Override
    public String getText() {
        return "Dimension: " + Formatting.WHITE + PlayerUtils.getDimension();
    }
}
