package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.Core.Render.Utils.RainbowUtils;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.api.Managers.managers.ColourTheme.ColorTheme;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.*;
import com.ferra13671.BThack.api.Module.OneActionModule;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.System.BThackScreens;
import com.ferra13671.BThack.mixins.accessor.IGameRenderer;
import net.minecraft.client.gl.PostEffectProcessor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClickGui extends OneActionModule {

    public final ModeSetting activeTheme = new ModeSetting("Theme", this, getActiveThemeList());
    public final BooleanSetting rainbow = new BooleanSetting("Rainbow", this, false);
    public final BooleanSetting customColor = new BooleanSetting("Custom Color", this, false, () -> !rainbow.getValue());
    public final ColorSetting color = new ColorSetting("ClickGui Color", this, new Color(25, 28, 255), () -> customColor.getValue() && !rainbow.getValue()).withBlockedAlpha();
    public final NumberSetting rainbowSpeed = new NumberSetting("Rainbow speed", this, 2, 1, 4, true, rainbow::getValue);

    public final BooleanSetting frameOutline = new BooleanSetting("Frame Outline", this, true);
    public final BooleanSetting moduleOutline = new BooleanSetting("Module Outline", this, true);
    public final BooleanSetting settingsOutline = new BooleanSetting("Settings Outline", this, true);

    public final NumberSetting opacity = new NumberSetting("Opacity", this, 0.76, 0.1, 1, false);

    public final NumberSetting animationTime = new NumberSetting("Anim Time", this, 400, 250, 1500, true);
    public final ModeSetting easing = new ModeSetting("Easing", this, getEasingList());
    {
        easing.setValue("CIRC_OUT");
        easing.setIndex(17);
    }

    public final BooleanSetting blur = new BooleanSetting("Blur", this, true);
    public final NumberSetting blurStrength = new NumberSetting("Blur Strength", this, 4, 1.1, 20, false, blur::getValue);

    public final BooleanSetting shouldPause = new BooleanSetting("Should Pause", this, true);

    public final NumberSetting guiScale = new NumberSetting("Gui Scale", this, 1, 0.5, 1.5, false, () -> false);

    public ClickGui() {
        super("ClickGui",
                "lang.module.ClickGui",
                KeyboardUtils.KEY_RSHIFT,
                MCategory.CLIENT,
                false
        );

        initSettings(
                activeTheme,
                color,
                customColor,
                rainbow,
                rainbowSpeed,

                frameOutline,
                moduleOutline,
                settingsOutline,

                opacity,

                animationTime,
                easing,

                blur,
                blurStrength,

                shouldPause,

                guiScale
        );
    }

    public List<String> getActiveThemeList() {
        ArrayList<String> options = new ArrayList<>();

        for (ColorTheme theme : Managers.COLOR_THEME_MANAGER.getColorThemes()) {
            options.add(theme.name());
        }
        return options;
    }

    public List<String> getEasingList() {
        List<String> easingList = new ArrayList<>();
        for (Easing eas : Easing.values()) {
            easingList.add(eas.name());
        }
        return easingList;
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
            BThackScreens.CLICK_GUI.firstIgnore = true;
            mc.setScreen(BThackScreens.CLICK_GUI);
        }

        toggle();
    }

    public void updateColorTheme() {
        for (ColorTheme theme : Managers.COLOR_THEME_MANAGER.getColorThemes()) {
            if (Objects.equals(activeTheme.getValue(), theme.name())) {
                Client.clientInfo.setColorTheme(theme);
            }
        }
    }

    public static void renderBlur(float tickDelta) {
        PostEffectProcessor blurProcessor = ((IGameRenderer) mc.gameRenderer).getBlurPostProcessor();
        if (blurProcessor != null) {
            blurProcessor.setUniforms("Radius", (float) ModuleList.clickGui.blurStrength.getValue());
            blurProcessor.render(tickDelta);
        }
        mc.getFramebuffer().beginWrite(false);
    }

    public static Easing getCurrentEasing() {
        return Easing.valueOf(ModuleList.clickGui.easing.getValue());
    }

    public static int getClickGuiColor(boolean allowRainbow) {
        if (ModuleList.clickGui.rainbow.getValue() && allowRainbow) {
            int rainbowType = (int) ModuleList.clickGui.rainbowSpeed.getValue();
            float speed = RainbowUtils.getRainbowRectSpeed(rainbowType)[0];
            int delay = (int) RainbowUtils.getRainbowRectSpeed(rainbowType)[1];

            return ColorUtils.rainbow(delay, speed);
        } else if (ModuleList.clickGui.customColor.getValue()) {
            return new Color(ModuleList.clickGui.color.getValue().getRed(), ModuleList.clickGui.color.getValue().getGreen(), ModuleList.clickGui.color.getValue().getBlue()).getRGB();
        } else {
            return new Color(Client.clientInfo.getColorTheme().moduleEnabledColor()).hashCode();
        }
    }

    public static float applyGuiScale(float cord) {
        return (float) (cord * ModuleList.clickGui.guiScale.getValue());
    }

    public static int applyGuiScale(int cord) {
        return (int) (cord * ModuleList.clickGui.guiScale.getValue());
    }
}
