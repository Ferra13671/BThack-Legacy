package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.api.Managers.managers.ColourTheme.ColorTheme;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.*;
import com.ferra13671.BThack.api.Module.OneActionModule;
import com.ferra13671.BThack.api.Shader.ShaderProgram;
import com.ferra13671.BThack.api.Shader.Shaders;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.GuiSystem.BThackScreens;
import com.ferra13671.BThack.mixins.accessor.IGameRenderer;
import net.minecraft.client.gl.PostEffectProcessor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ClickGui extends OneActionModule {

    public final ModeSetting activeTheme = new ModeSetting("Theme", this, getActiveThemeList());

    //rainbow and gradient
    public BooleanSetting rainbow;

    public BooleanSetting gradient;
    public ColorSetting color1;
    public ColorSetting color2;

    public NumberSetting scale;
    public NumberSetting speed;
    //

    public final BooleanSetting customColor = new BooleanSetting("Custom Color", this, false, () -> !rainbow.getValue());
    public final ColorSetting color = new ColorSetting("ClickGui Color", this, new Color(25, 28, 255), () -> customColor.getValue() && !rainbow.getValue()).withBlockedAlpha();

    public final BooleanSetting arrows = new BooleanSetting("Arrows", this, true);

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

    public final BooleanSetting snow = new BooleanSetting("Snow", this, true, Client.clientInfo::isWinter);
    public final NumberSetting snowSpeed = new NumberSetting("Snow Speed", this, 1, 0.3, 2, false, () -> Client.clientInfo.isWinter() && snow.getValue());

    public final BooleanSetting shouldPause = new BooleanSetting("Should Pause", this, true);

    public final NumberSetting guiScale = new NumberSetting("Gui Scale", this, 1, 0.5, 1.5, false, () -> false);

    public ClickGui() {
        super("ClickGui",
                "lang.module.ClickGui",
                KeyboardUtils.KEY_RSHIFT,
                MCategory.CLIENT,
                false
        );

        rainbow = new BooleanSetting("Rainbow", this, false, () -> !gradient.getValue());
        gradient = new BooleanSetting("Gradient", this, true, () -> !(rainbow.getValue() && !this.gradient.getValue()));

        color1 = new ColorSetting("Color1", this, new Color(195, 85, 251), gradient::getValue).withBlockedAlpha();
        color2 = new ColorSetting("Color2", this, new Color(105, 0, 166), gradient::getValue).withBlockedAlpha();

        scale = new NumberSetting("Scale", this, 1, 0.3, 4, false, () -> rainbow.getValue() || gradient.getValue());
        speed = new NumberSetting("Speed", this, 1, 0.3, 4, false, () -> rainbow.getValue() || gradient.getValue());

        initSettings(
                activeTheme,

                color,
                customColor,

                rainbow,

                gradient,
                color1,
                color2,

                scale,
                speed,

                arrows,

                frameOutline,
                moduleOutline,
                settingsOutline,

                opacity,

                animationTime,
                easing,

                blur,
                blurStrength,

                snow,
                snowSpeed,

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

    public boolean isShaderEnabled() {
        return rainbow.getValue() || gradient.getValue();
    }

    public void prepareCurrentShader(float alpha, float brightness) {
        if (gradient.getValue()) {
            Shaders.INSTANCE.XY_GRADIENT.setUniformValue("scale", scale.getValue().floatValue());
            Shaders.INSTANCE.XY_GRADIENT.setUniformValue("speed", speed.getValue().floatValue());
            Shaders.INSTANCE.XY_GRADIENT.setUniformValue("brightness", brightness);

            Shaders.INSTANCE.XY_GRADIENT.setUniformValue("color1", color1.getValue().getRed() / 255f, color1.getValue().getGreen() / 255f, color1.getValue().getBlue() / 255f, alpha);
            Shaders.INSTANCE.XY_GRADIENT.setUniformValue("color2", color2.getValue().getRed() / 255f, color2.getValue().getGreen() / 255f, color2.getValue().getBlue() / 255f, alpha);
        } else if (rainbow.getValue()) {
            Shaders.INSTANCE.X_RAINBOW.setUniformValue("alpha", alpha);
            Shaders.INSTANCE.X_RAINBOW.setUniformValue("brightness", brightness);
            Shaders.INSTANCE.X_RAINBOW.setUniformValue("scale", scale.getValue().floatValue());
            Shaders.INSTANCE.X_RAINBOW.setUniformValue("speed", speed.getValue().floatValue());
        }
    }

    public ShaderProgram getCurrentShader() {
        if (gradient.getValue()) return Shaders.INSTANCE.XY_GRADIENT;
        else return Shaders.INSTANCE.X_RAINBOW;
    }

    @Override
    public void onChangeSetting(Setting<?> setting) {
        Managers.COLOR_THEME_MANAGER.updateColorTheme();
    }

    @Override
    public void playOnSound() {
        //No action
    }

    @Override
    public void onEnable() {
        if (nullCheck()) return;

        if (mc.currentScreen == null) {
            BThackScreens.CLICK_GUI.firstIgnore = true;
            mc.setScreen(BThackScreens.CLICK_GUI);
        }
    }

    public static void renderBlur(float tickDelta) {
        PostEffectProcessor blurProcessor = ((IGameRenderer) mc.gameRenderer).getBlurPostProcessor();
        if (blurProcessor != null) {
            blurProcessor.setUniforms("Radius", ModuleList.clickGui.blurStrength.getValue().floatValue());
            blurProcessor.render(tickDelta);
        }
        mc.getFramebuffer().beginWrite(false);
    }

    public static Easing getCurrentEasing() {
        return Easing.valueOf(ModuleList.clickGui.easing.getValue());
    }

    public static int getClickGuiColor(boolean allowRainbow) {
        if (ModuleList.clickGui.rainbow.getValue() && allowRainbow) return ColorUtils.rainbow();
        else return (ModuleList.clickGui.customColor.getValue() ?
                new Color(ModuleList.clickGui.color.getValue().getRed(), ModuleList.clickGui.color.getValue().getGreen(), ModuleList.clickGui.color.getValue().getBlue()) :
                new Color(Client.clientInfo.getColorTheme().moduleEnabledColor())).getRGB();
    }

    public static float applyGuiScale(float cord) {
        return (float) (cord * ModuleList.clickGui.guiScale.getValue());
    }

    public static int applyGuiScale(int cord) {
        return (int) (cord * ModuleList.clickGui.guiScale.getValue());
    }
}
