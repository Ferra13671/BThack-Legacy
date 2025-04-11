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


public class SkyColor extends Module {
    private final ShaderTicker ticker = new ShaderTicker();

    public final BooleanSetting rainbow = new BooleanSetting("Rainbow", this, false);
    public final NumberSetting rainbowSpeed = new NumberSetting("Rainbow Speed", this, 1, 0.3, 5, false, rainbow::getValue);

    public final ColorSetting skyColor = new ColorSetting("Sky Color", this, new Color(21, 191, 219), () -> !rainbow.getValue()).withBlockedAlpha();

    public SkyColor() {
        super("SkyColour",
                "lang.module.SkyColor",
                KeyboardUtils.RELEASE,
                MCategory.WORLD,
                false
        );

        initSettings(
                rainbow,
                rainbowSpeed,

                skyColor
        );
    }

    @Override
    public void onEnable() {
        super.onEnable();
        ticker.reset();
    }

    public Vec3d getSkyColor() {
        ticker.update(rainbowSpeed.getValue().floatValue());
        Color color = rainbow.getValue() ? new Color(ColorUtils.rainbow(ticker.getPassedTime())) : skyColor.getValue();
        return new Vec3d(color.getRed() / 255d, color.getGreen() / 255d, color.getBlue() / 255d);
    }
}
