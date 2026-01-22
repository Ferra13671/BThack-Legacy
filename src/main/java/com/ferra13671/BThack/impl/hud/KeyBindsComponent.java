package com.ferra13671.BThack.impl.hud;

import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.core.client.Client;
import com.ferra13671.BThack.core.client.ModuleList;
import com.ferra13671.BThack.core.render.BThackRender;
import com.ferra13671.BThack.core.render.font.FontRenderManager;
import com.ferra13671.BThack.core.render.font.FontUtils;
import com.ferra13671.BThack.core.render.utils.BThackRenderUtils;
import com.ferra13671.BThack.core.render.utils.ColorUtils;
import com.ferra13671.BThack.api.module.HudComponent;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.shaders.CoreShaders;
import com.ferra13671.BThack.api.utils.KeyboardUtils;
import com.ferra13671.BThack.impl.modules.client.HUD;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;

@ModuleInfo(name = "KeyBinds", category = "HUD", autoEnabled = true)
public class KeyBindsComponent extends HudComponent {

    public KeyBindsComponent() {
        super(
                MinecraftClient.getInstance().getWindow().getScaledWidth() / 2f,
                MinecraftClient.getInstance().getWindow().getScaledHeight() / 2f
        );
    }

    @Override
    public void render() {
        BThackRender.drawHudPlate(getX(), getY(), getX() + width, getY() + height);

        float textHeight = FontUtils.getTextHeight("KeyBinds", FontRenderManager.DrawMode.NORMAL_BOLD);
        float textWidth = FontUtils.getTextWidth("KeyBinds", FontRenderManager.DrawMode.NORMAL_BOLD);
        drawText("KeyBinds", getX() + (width / 2) - (textWidth / 2), getY() + 2 + (textHeight / 2));

        if (ModuleList.HUD.gradient.getValue()) {
            float[] rgba1 = ColorUtils.hashCodeToRGBA(ModuleList.HUD.color1.getValue().hashCode());
            float[] rgba2 = ColorUtils.hashCodeToRGBA(ModuleList.HUD.color2.getValue().hashCode());
            CoreShaders.XY_GRADIENT.setUniformValue("color1", rgba1[0], rgba1[1], rgba1[2], rgba1[3]);
            CoreShaders.XY_GRADIENT.setUniformValue("color2", rgba2[0], rgba2[1], rgba2[2], rgba2[3]);
            CoreShaders.XY_GRADIENT.setUniformValue("scale", ModuleList.HUD.scale.getValue().floatValue() * 5);
            CoreShaders.XY_GRADIENT.setUniformValue("speed", ModuleList.HUD.speed.getValue().floatValue());
            BThackRender.drawShader(CoreShaders.XY_GRADIENT, getX(), getY() + 10 + textHeight, getX() + width, getY() + 10 + (2f / BThackRenderUtils.getGuiScale()) + textHeight);
        } else
            BThackRender.drawRect(getX(), getY() + 10 + textHeight, getX() + width, getY() + 11 + textHeight, HUD.getHUDColor());

        float maxWidth = FontUtils.getTextWidth("KeyBinds", FontRenderManager.DrawMode.NORMAL_BOLD);

        float y = 14 + textHeight;
        textWidth = 0;
        float bindWidth = 0;
        for (Module module : Client.getAllModules()) {
            if (!module.isEnabled() || module.getKey() == KeyboardUtils.RELEASE) continue;

            float tempTextWidth = FontUtils.getTextWidth(module.getName(), FontRenderManager.DrawMode.NORMAL) + (3 * BThackRenderUtils.getGuiScale());
            if (tempTextWidth > textWidth)
                textWidth = tempTextWidth;

            float tempBindWidth = FontUtils.getTextWidth(Formatting.GRAY + " | " + Formatting.AQUA + KeyboardUtils.getKeyName(module.getKey()), FontRenderManager.DrawMode.NORMAL_BOLD);
            if (tempBindWidth > bindWidth)
                bindWidth = tempBindWidth;
        }
        for (Module module : Client.getAllModules()) {
            if (!module.isEnabled() || module.getKey() == KeyboardUtils.RELEASE) continue;

            String keyBind = Formatting.GRAY + " | " + Formatting.AQUA + KeyboardUtils.getKeyName(module.getKey());

            drawText(module.getName(), getX() + 3, getY() + y);
            drawText(keyBind, getX() + 3 + textWidth, getY() + y);
            y += FontUtils.getTextHeight(module.getName() + keyBind, FontRenderManager.DrawMode.NORMAL_BOLD) + 3;
        }
        float fullKeyBindsWidth = textWidth + bindWidth;
        if (maxWidth < fullKeyBindsWidth)
            maxWidth = fullKeyBindsWidth;

        width = maxWidth + 6;
        height = y + 3;
    }
}
