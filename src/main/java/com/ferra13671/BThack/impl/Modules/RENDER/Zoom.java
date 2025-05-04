package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.api.Animation.Animation;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.api.Events.InputEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.*;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.math.MathHelper;

import java.util.Arrays;

public class Zoom extends Module {

    public final NumberSetting zoom = new NumberSetting("Zoom", this, 0.3, 0.01, 0.9, false);
    public final NumberSetting scrollStep = new NumberSetting("Scroll Step", this, 0.3, 0.1, 0.65, false);
    public final BooleanSetting hideHud = new BooleanSetting("Hide Hud", this, true);

    public final ModeSetting toggleMode = new ModeSetting("Toggle Mode", this, Arrays.asList("Toggle", "Hold"));
    public final KeyCodeSetting holdKey = new KeyCodeSetting("Hold Key", this, () -> toggleMode.getValue().equals("Hold"));

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
                hideHud,

                toggleMode,
                holdKey
        );
    }
    private final Animation animation = new Animation(Easing.CUBIC_OUT, 750);
    private double prevMouseSensitivity;
    private boolean prevHudHidden;
    private float needZoomValue;
    private float currentZoomValue;

    private boolean holdEnabled = false;

    @Override
    public void onChangeSetting(Setting<?> setting) {
        if (isEnabled()) {
            if (setting == hideHud) {
                if (hideHud.getValue()) {
                    prevHudHidden = mc.options.hudHidden;
                    mc.options.hudHidden = true;
                } else mc.options.hudHidden = prevHudHidden;
            }
            if (setting == toggleMode) {
                if (toggleMode.getValue().equals("Toggle"))
                    onEnable();
                else onDisableInternal();
            }
        }
    }

    @Override
    public void onEnable() {
        if (needZoom()) {
            currentZoomValue = 1;
            animation.reset();
            needZoomValue = 1 / zoom.getValue().floatValue();
            prevMouseSensitivity = mc.options.getMouseSensitivity().getValue();
            prevHudHidden = mc.options.hudHidden;
            if (hideHud.getValue()) mc.options.hudHidden = true;
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        onDisableInternal();
    }

    private void onDisableInternal() {
        mc.options.getMouseSensitivity().setValue(prevMouseSensitivity);
        if (hideHud.getValue()) mc.options.hudHidden = prevHudHidden;
    }

    @EventSubscriber
    public void onKey(InputEvent.KeyInputEvent e) {
        if (e.getKeyCode() == holdKey.getValue() && toggleMode.getValue().equals("Hold")) {
            if (e.getAction() == InputEvent.KeyInputEvent.Action.PRESS) {
                holdEnabled = true;
                onEnable();
            } else if (e.getAction() == InputEvent.KeyInputEvent.Action.RELEASE) {
                holdEnabled = false;
                onDisableInternal();
            }
        }
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

    public boolean needZoom() {
        return toggleMode.getValue().equals("Toggle") || holdEnabled;
    }
}
