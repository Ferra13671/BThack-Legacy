package com.ferra13671.BThack.impl.Modules.WORLD;

import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ColorSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class FogColor extends Module {

    public final BooleanSetting rainbow = new BooleanSetting("Rainbow", this, false);
    public final ColorSetting fogColor = new ColorSetting("Fog Color", this, new Color(255, 255, 255), () -> !rainbow.getValue()).withBlockedAlpha();

    public final BooleanSetting overworld = new BooleanSetting("Overworld", this, true);
    public final BooleanSetting nether = new BooleanSetting("Nether", this, true);
    public final BooleanSetting end = new BooleanSetting("End", this, true);

    public FogColor() {
        super("FogColor",
                "lang.module.FogColor",
                KeyboardUtils.RELEASE,
                MCategory.WORLD,
                false
        );

        initSettings(
                fogColor,
                rainbow,
                overworld,
                nether,
                end
        );
    }

    public Vec3d getFogColor() {
        if (rainbow.getValue()) {
            Color color = new Color(ColorUtils.rainbow());
            return new Vec3d(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
        } else
            return new Vec3d(fogColor.getValue().getRed() / 255d, fogColor.getValue().getGreen() / 255d, fogColor.getValue().getBlue() / 255d);
    }
}
