package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.Core.Render.Utils.RainbowUtils;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import net.minecraft.util.math.ColorHelper;

public class EnchantColor extends Module {

    public final NumberSetting enchantSpeed = new NumberSetting("Ench. Speed", this, 1, 0, 2, false);
    public final NumberSetting enchantSize = new NumberSetting("Ench. Size", this, 1, 0.1, 5, false);

    public final BooleanSetting rainbow = new BooleanSetting("Rainbow", this, false);
    public final NumberSetting rainbowSpeed = new NumberSetting("Rainbow Speed", this, 2, 1, 4, true, rainbow::getValue);

    public final NumberSetting alphaColor = new NumberSetting("Alpha", this, 180, 0, 255, true);
    public final NumberSetting redColor = new NumberSetting("Red", this, 255, 0, 255, true, () -> !rainbow.getValue());
    public final NumberSetting greenColor = new NumberSetting("Green", this, 255, 0, 255, true, () -> !rainbow.getValue());
    public final NumberSetting blueColor = new NumberSetting("Blue", this, 255, 0, 255, true, () -> !rainbow.getValue());

    public EnchantColor() {
        super("EnchantColor",
                "lang.module.EnchantColor",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        initSettings(
                enchantSpeed,
                enchantSize,

                alphaColor,

                redColor,
                greenColor,
                blueColor,

                rainbow,
                rainbowSpeed
        );
    }

    public static float[] getEnchantColor() {
        float red;
        float green;
        float blue;
        float alpha = (float) (ModuleList.enchantColor.alphaColor.getValue() / 255d);

        if (!ModuleList.enchantColor.rainbow.getValue()) {
            red = (float) (ModuleList.enchantColor.redColor.getValue() / 255d);
            green = (float) (ModuleList.enchantColor.greenColor.getValue() / 255d);
            blue = (float) (ModuleList.enchantColor.blueColor.getValue() / 255d);
        } else {
            int rainbowType = (int) ModuleList.enchantColor.rainbowSpeed.getValue();
            float speed = RainbowUtils.getRainbowRectSpeed(rainbowType)[0];
            int delay = (int) RainbowUtils.getRainbowRectSpeed(rainbowType)[1];
            int argb = ColorUtils.rainbow(delay, speed);
            red = ColorHelper.Argb.getRed(argb) / 255f;
            green = ColorHelper.Argb.getGreen(argb) / 255f;
            blue = ColorHelper.Argb.getBlue(argb) / 255f;
        }
        return new float[]{red, green, blue, alpha};
    }
}
