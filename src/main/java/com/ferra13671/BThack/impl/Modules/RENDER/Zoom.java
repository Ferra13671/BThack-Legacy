package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.api.Animation.Animation;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import net.minecraft.util.math.MathHelper;

public class Zoom extends Module {

    public final NumberSetting zoom = new NumberSetting("Zoom", this, 0.3, 0.01, 0.9, false);
    public final NumberSetting scrollStep = new NumberSetting("Scroll Step", this, 0.3, 0.1, 0.65, false);
    public final BooleanSetting hideHud = new BooleanSetting("Hide Hud", this, true);

    public Zoom() {
        super("Zoom",
                "lang.module.Zoom",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        initSettings(
                zoom,
                scrollStep,
                hideHud
        );
    }
    private final Animation animation = new Animation(Easing.CUBIC_OUT, 750);
    private double prevMouseSensitivity;
    private boolean prevHudHidden;
    private float needZoomValue;
    private float currentZoomValue;

    @Override
    public void onChangeSetting(Setting<?> setting) {
        if (isEnabled() && setting == hideHud) {
            if (hideHud.getValue()) {
                prevHudHidden = mc.options.hudHidden;
                mc.options.hudHidden = true;
            } else mc.options.hudHidden = prevHudHidden;
        }
    }

    @Override
    public void onEnable() {
        currentZoomValue = 1;
        animation.reset();
        needZoomValue = 1 / zoom.getValue().floatValue();
        prevMouseSensitivity = mc.options.getMouseSensitivity().getValue();
        prevHudHidden = mc.options.hudHidden;
        if (hideHud.getValue()) mc.options.hudHidden = true;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.options.getMouseSensitivity().setValue(prevMouseSensitivity);
        if (hideHud.getValue()) mc.options.hudHidden = prevHudHidden;
    }

    public void mouseScroll(float step) {
        needZoomValue = Math.max(1, needZoomValue + (step * scrollStep.getValue().floatValue() * needZoomValue));
        animation.reset();
        mc.options.getMouseSensitivity().setValue(prevMouseSensitivity / needZoomValue);
    }

    public double getFov(double original) {
        currentZoomValue = (float) MathHelper.lerp(animation.getEase(), currentZoomValue, needZoomValue);
        return original / currentZoomValue;
    }
}
