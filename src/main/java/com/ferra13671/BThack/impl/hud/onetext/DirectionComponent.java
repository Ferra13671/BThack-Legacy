package com.ferra13671.BThack.impl.hud.onetext;

import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.api.utils.rotate.RotateUtils;
import com.ferra13671.BThack.impl.hud.AbstractOneTextComponent;
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
