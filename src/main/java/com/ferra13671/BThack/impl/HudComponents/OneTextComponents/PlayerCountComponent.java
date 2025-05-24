package com.ferra13671.BThack.impl.HudComponents.OneTextComponents;

import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.impl.HudComponents.AbstractOneTextComponent;
import net.minecraft.util.Formatting;

@ModuleInfo(name = "PlayerCount", category = "HUD", autoEnabled = true)
public class PlayerCountComponent extends AbstractOneTextComponent {

    public PlayerCountComponent() {
        super(5, 175);
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public String getText() {
        return "Players " + Formatting.WHITE + mc.player.networkHandler.getPlayerList().size();
    }
}
