package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.Core.Render.Utils.RainbowUtils;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.api.Managers.ColourTheme.ColorTheme;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.OneActionModule;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClickGui extends OneActionModule {

    public static ModeSetting activeTheme;
    public static NumberSetting redColor;
    public static NumberSetting greenColor;
    public static NumberSetting blueColor;
    public static BooleanSetting customColor;
    public static BooleanSetting rainbow;
    public static NumberSetting rainbowSpeed;

    public static BooleanSetting frameOutline;
    public static BooleanSetting moduleOutline;
    public static BooleanSetting settingsOutline;

    public static NumberSetting opacity;

    public static NumberSetting animationTime;
    public static ModeSetting easing;

    public static NumberSetting guiScale;

    public ClickGui() {
        super("ClickGui",
                "lang.module.ClickGui",
                KeyboardUtils.KEY_RSHIFT,
                MCategory.CLIENT,
                false
        );

        ArrayList<String> options = new ArrayList<>();

        for (ColorTheme theme : Managers.COLOR_THEME_MANAGER.getColorThemes()) {
            options.add(theme.getName());
        }

        activeTheme = new ModeSetting("Theme", this, options);

        redColor = new NumberSetting("Red", this, 25,0,255,true, () -> customColor.getValue());
        greenColor = new NumberSetting("Green", this, 28,0,255,true, () -> customColor.getValue());
        blueColor = new NumberSetting("Blue", this, 255,0,255,true, () -> customColor.getValue());
        customColor = new BooleanSetting("Custom Color", this, false, () -> !rainbow.getValue());
        rainbow = new BooleanSetting("Rainbow", this, false);
        rainbowSpeed = new NumberSetting("Rainbow speed", this, 2, 1, 4, true, () -> rainbow.getValue());

        frameOutline = new BooleanSetting("Frame Outline", this, true);
        moduleOutline = new BooleanSetting("Module Outline", this, true);
        settingsOutline = new BooleanSetting("Settings Outline", this, true);

        opacity = new NumberSetting("Opacity", this, 0.76, 0.1, 1, false);

        animationTime = new NumberSetting("Anim Time", this, 400, 250, 1500, true);
        List<String> easingList = new ArrayList<>();
        for (Easing eas : Easing.values()) {
            easingList.add(eas.name());
        }
        easing = new ModeSetting("Easing", this, easingList);
        easing.setValue("CIRC_OUT");
        easing.setIndex(17);

        guiScale = new NumberSetting("Gui Scale", this, 1, 0.5, 1.5, false, () -> false);

        initSettings(
                activeTheme,
                redColor,
                greenColor,
                blueColor,
                customColor,
                rainbow,
                rainbowSpeed,

                frameOutline,
                moduleOutline,
                settingsOutline,

                opacity,

                animationTime,
                easing,

                guiScale
        );
    }

    @Override
    public void onChangeSetting(Setting setting) {
        updateColorTheme();
    }

    @Override
    public void playOnSound() {
        //No action
    }

    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        if (mc.currentScreen == null) {
            BThack.instance.clickGui.firstIgnore = true;
            mc.setScreen(BThack.instance.clickGui);
        }

        toggle();
    }

    public void updateColorTheme() {
        for (ColorTheme theme : Managers.COLOR_THEME_MANAGER.getColorThemes()) {
            if (Objects.equals(ClickGui.activeTheme.getValue(), theme.getName())) {
                Client.clientInfo.setColorTheme(theme);
            }
        }
    }

    public static Easing getCurrentEasing() {
        return Easing.valueOf(easing.getValue());
    }

    public static int getClickGuiColor(boolean allowRainbow) {
        if (rainbow.getValue() && allowRainbow) {
            int rainbowType = (int) rainbowSpeed.getValue();
            float speed = RainbowUtils.getRainbowRectSpeed(rainbowType)[0];
            int delay = (int) RainbowUtils.getRainbowRectSpeed(rainbowType)[1];

            return ColorUtils.rainbow(delay, speed);
        } else if (customColor.getValue()) {
            return new Color((int) redColor.getValue(), (int) greenColor.getValue(), (int) blueColor.getValue()).getRGB();
        } else {
            return new Color(Client.clientInfo.getColorTheme().getModuleEnabledColour()).hashCode();
        }
    }

    public static float applyGuiScale(float cord) {
        return (float) (cord * guiScale.getValue());
    }

    public static int applyGuiScale(int cord) {
        return (int) (cord * guiScale.getValue());
    }
}
