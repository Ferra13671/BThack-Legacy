package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.api.HudComponent.HudComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;

public class TextRadarComponent extends HudComponent {

    public TextRadarComponent() {
        super("TextRadar",
                250,
                5,
                true
        );
    }

    @Override
    public void render() {
        int y = 0;
        int maxWidth = 0;
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player.getDisplayName().getString().equals(mc.player.getDisplayName().getString())) continue;
            String text = player.getDisplayName().getString() + " " + Formatting.GRAY + "[" + Formatting.WHITE + decimal.format(player.distanceTo(mc.player)) + "m." + Formatting.GRAY + "]";

            drawText(text, (int) getX(), (int) getY() + y);


            if (maxWidth < mc.textRenderer.getWidth(text)) {
                maxWidth = mc.textRenderer.fontHeight;
            }
            y += mc.textRenderer.fontHeight + 1;
        }

        this.width = maxWidth;
        this.height = y;
    }
}
