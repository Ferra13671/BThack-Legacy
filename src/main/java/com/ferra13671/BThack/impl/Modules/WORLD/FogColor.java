package com.ferra13671.BThack.impl.Modules.WORLD;

import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ColorSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Shader.ShaderTicker;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class FogColor extends Module {
    private final ShaderTicker ticker = new ShaderTicker();

    public final BooleanSetting rainbow = new BooleanSetting("Rainbow", this, false);
    public final NumberSetting rainbowSpeed = new NumberSetting("Rainbow Speed", this, 1, 0.3, 5, false, rainbow::getValue);
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
                rainbow,
                rainbowSpeed,

                fogColor,

                overworld,
                nether,
                end
        );
    }

    @Override
    public void onEnable() {
        super.onEnable();
        ticker.reset();
    }

    public Vec3d getFogColor() {
        ticker.update((float) rainbowSpeed.getValue());
        Color color = rainbow.getValue() ? new Color(ColorUtils.rainbow(ticker.getPassedTime())) : fogColor.getValue();
        return new Vec3d(color.getRed() / 255d, color.getGreen() / 255d, color.getBlue() / 255d);
    }
}
