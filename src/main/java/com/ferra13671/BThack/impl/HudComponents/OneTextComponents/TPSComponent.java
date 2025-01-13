package com.ferra13671.BThack.impl.HudComponents.OneTextComponents;

import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.impl.HudComponents.AbstractOneTextComponent;
import net.minecraft.util.Formatting;

public class TPSComponent extends AbstractOneTextComponent {

    public TPSComponent() {
        super("TPS",
                5,
                125,
                true
        );
    }

    @Override
    public String getText() {
        return String.format("TPS%s %.2f", Formatting.WHITE, Managers.TPS_MANAGER.getTickRate());
    }
}
