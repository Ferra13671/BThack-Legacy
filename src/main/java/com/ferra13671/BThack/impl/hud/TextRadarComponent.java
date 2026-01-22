package com.ferra13671.BThack.impl.hud;

import com.ferra13671.BThack.Constants;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.core.render.BThackRender;
import com.ferra13671.BThack.core.render.font.FontRenderManager;
import com.ferra13671.BThack.core.render.font.FontUtils;
import com.ferra13671.BThack.api.module.HudComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;

@ModuleInfo(name = "TextRadar", category = "HUD", autoEnabled = true)
public class TextRadarComponent extends HudComponent {

    public TextRadarComponent() {
        super(250, 5);
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public void render() {
        float y = 0;
        float maxWidth = 0;
        int count = 0;

        if (width > 0 && height > 0)
            BThackRender.drawHudPlate(getX(), getY(), getX() + width, getY() + height);

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player.getDisplayName().getString().equals(mc.player.getDisplayName().getString())) continue;
            String text = player.getDisplayName().getString() + " " + Formatting.GRAY + "[" + Formatting.WHITE + Constants.DECIMAL_FORMAT.format(player.distanceTo(mc.player)) + "m." + Formatting.GRAY + "]";

            BThackRender.drawString(text, (int) getX() + 3, (int) (getY() + y + 3), ArrayListComponent.INSTANCE.getArrayColor(count), true, FontRenderManager.DrawMode.SMALL_BOLD);


            float textWidth = FontUtils.getTextWidth(text, FontRenderManager.DrawMode.SMALL_BOLD);
            if (textWidth > maxWidth) {
                maxWidth = textWidth;
            }
            y += FontUtils.getTextHeight(text, FontRenderManager.DrawMode.SMALL_BOLD) + 5;
            count++;
        }

        width = maxWidth > 0 ? (maxWidth + 6) : 0;
        height = y;
    }
}
