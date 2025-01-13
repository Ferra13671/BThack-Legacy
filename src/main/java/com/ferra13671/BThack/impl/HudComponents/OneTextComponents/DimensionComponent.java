package com.ferra13671.BThack.impl.HudComponents.OneTextComponents;

import com.ferra13671.BThack.impl.HudComponents.AbstractOneTextComponent;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

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
        String text;
        if (mc.world.getRegistryKey() == World.NETHER) {
            text = "Dimension: " + Formatting.WHITE + "Nether";
        } else if (mc.world.getRegistryKey() == World.OVERWORLD) {
            text = "Dimension: " + Formatting.WHITE + "Overworld";
        } else {
            text = "Dimension: " + Formatting.WHITE + "End";
        }
        return text;
    }
}
