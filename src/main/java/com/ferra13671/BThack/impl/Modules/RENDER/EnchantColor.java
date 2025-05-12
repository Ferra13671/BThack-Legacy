package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ColorSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import net.minecraft.util.math.ColorHelper;

import java.awt.*;

@ModuleInfo(name = "EnchantColor", description = "lang.module.EnchantColor", category = "RENDER")
public class EnchantColor extends Module {

    public final NumberSetting enchantSpeed = new NumberSetting("Ench. Speed", this, 1, 0, 2, false);
    public final NumberSetting enchantSize = new NumberSetting("Ench. Size", this, 1, 0.1, 5, false);

    public final BooleanSetting rainbow = new BooleanSetting("Rainbow", this, false);
    public final NumberSetting speed = new NumberSetting("Speed", this, 1, 0.1, 10, false, rainbow::getValue);

    public final NumberSetting alphaColor = new NumberSetting("Alpha", this, 180, 0, 255, true);
    public final ColorSetting colorSet = new ColorSetting("Color", this, new Color(255, 255, 255), () -> !rainbow.getValue()).withBlockedAlpha();


    public static float[] getEnchantColor() {
        float red;
        float green;
        float blue;
        float alpha = (float) (ModuleList.enchantColor.alphaColor.getValue() / 255d);

        if (!ModuleList.enchantColor.rainbow.getValue()) {
            red = (float) (ModuleList.enchantColor.colorSet.getValue().getRed() / 255d);
            green = (float) (ModuleList.enchantColor.colorSet.getValue().getGreen() / 255d);
            blue = (float) (ModuleList.enchantColor.colorSet.getValue().getBlue() / 255d);
        } else {

            int argb = ColorUtils.rainbow(1, ModuleList.enchantColor.speed.getValue().floatValue());
            red = ColorHelper.Argb.getRed(argb) / 255f;
            green = ColorHelper.Argb.getGreen(argb) / 255f;
            blue = ColorHelper.Argb.getBlue(argb) / 255f;
        }
        return new float[]{red, green, blue, alpha};
    }
}
