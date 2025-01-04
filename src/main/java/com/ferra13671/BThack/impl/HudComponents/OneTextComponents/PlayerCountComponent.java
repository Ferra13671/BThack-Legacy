package com.ferra13671.BThack.impl.HudComponents.OneTextComponents;

import com.ferra13671.BThack.impl.HudComponents.AbstractOneTextComponent;
import net.minecraft.util.Formatting;

public class PlayerCountComponent extends AbstractOneTextComponent {

    public PlayerCountComponent() {
        super("PlayerCount",
                5,
                135,
                true
        );
    }

    @Override
    public String getText() {
        return "Players " + Formatting.WHITE + mc.player.networkHandler.getPlayerList().size();
    }
}
