package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.api.HudComponent.HudComponent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Utils.Textures;

import java.util.Arrays;

public class CompanionComponent extends HudComponent {

    public final ModeSetting mode = new ModeSetting("Mode", this, Arrays.asList("Caipirinha", "Cutie"));
    public final NumberSetting size = new NumberSetting("Size", this, 40, 20, 100, true);

    public CompanionComponent() {
        super("Companion",
                mc.getWindow().getScaledWidth() / 1.8f,
                mc.getWindow().getScaledHeight() / 1.8f,
                false
        );

        initSettings(
                mode,
                size
        );
    }

    @Override
    public void render() {
        if (mode.getValue().equals("Caipirinha")) {
            Textures.CAIPIRINHA.update();
            float w = (float) ((Textures.CAIPIRINHA.getWidth() / 560d) * size.getValue());
            float h = (float) ((Textures.CAIPIRINHA.getHeight() / 560d) * size.getValue());
            float startX = getX() + (((float) size.getValue() - w) / 2);
            float startY = getY() + (((float) size.getValue() - h) / 2);
            BThackRender.drawTextureRect(
                    Textures.CAIPIRINHA,
                    startX,
                    startY,
                    startX + w,
                    startY + h
            );
        } else {
            Textures.CUTIE.update();
            BThackRender.drawTextureRect(Textures.CUTIE, getX(), getY(), getX() + (float) size.getValue(), getY() + (float) size.getValue());
        }
    }

    @Override
    public void tick() {
        this.width = (float) size.getValue();
        this.height = (float) size.getValue();
    }
}
