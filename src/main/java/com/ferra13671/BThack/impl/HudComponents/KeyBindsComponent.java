package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Font.FontRenderManager;
import com.ferra13671.BThack.Core.Render.Font.FontUtils;
import com.ferra13671.BThack.api.Module.HudComponent;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.impl.Modules.CLIENT.HUD;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;

public class KeyBindsComponent extends HudComponent {

    public KeyBindsComponent() {
        super("KeyBinds",
                MinecraftClient.getInstance().getWindow().getScaledWidth() / 2f,
                MinecraftClient.getInstance().getWindow().getScaledHeight() / 2f,
                false
        );
    }

    @Override
    public void render() {
        BThackRender.drawHudPlate(getX(), getY(), getX() + width, getY() + height);

        float textHeight = FontUtils.getTextHeight("KeyBinds", FontRenderManager.DrawMode.NORMAL_BOLD);
        BThackRender.drawCenteredString("KeyBinds", getX() + (width / 2), getY() + 2 + (textHeight / 2), -1, FontRenderManager.DrawMode.NORMAL_BOLD);
        BThackRender.drawRect(getX(), getY() + 10 + textHeight, getX() + width, getY() + 11 + textHeight, HUD.getHUDColor());

        float maxWidth = FontUtils.getTextWidth("KeyBinds", FontRenderManager.DrawMode.NORMAL_BOLD);

        float y = 14 + textHeight;
        for (Module module : Client.getAllModules()) {
            if (!module.isEnabled() || module.getKey() == KeyboardUtils.RELEASE) continue;

            String text = module.getName() + Formatting.GRAY + " | " + Formatting.AQUA + KeyboardUtils.getKeyName(module.getKey());
            float tempWidth = FontUtils.getTextWidth(text, FontRenderManager.DrawMode.NORMAL);
            if (tempWidth > maxWidth)
                maxWidth = tempWidth;

            BThackRender.drawString(text, getX() + 3, getY() + y, -1);
            y += FontUtils.getTextHeight(text) + 3;
        }

        width = maxWidth + 6;
        height = y + 3;
    }
}
