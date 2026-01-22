package com.ferra13671.BThack.impl.hud.onetext;

import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.impl.hud.AbstractOneTextComponent;
import net.minecraft.util.Formatting;

@ModuleInfo(name = "TPS", category = "HUD", autoEnabled = true)
public class TPSComponent extends AbstractOneTextComponent {

    public TPSComponent() {
        super(5, 160);
    }

    @Override
    public String getText() {
        return String.format("TPS%s %.2f", Formatting.WHITE, Managers.TPS_MANAGER.getTickRate());
    }
}
