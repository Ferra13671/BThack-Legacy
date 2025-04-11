package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.api.Module.HudComponent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Utils.Textures;

import java.util.Arrays;

public class CompanionComponent extends HudComponent {

    public final ModeSetting mode = new ModeSetting("Mode", this, Arrays.asList("Caipirinha", "Cutie1", "Cutie2"));
    public final NumberSetting size = new NumberSetting("Size", this, 40, 20, 100, true);
    public final NumberSetting speed = new NumberSetting("Speed", this, 1, 0.5, 3, false);

    public CompanionComponent() {
        super("Companion",
                mc.getWindow().getScaledWidth() / 1.8f,
                mc.getWindow().getScaledHeight() / 1.8f,
                false
        );

        initSettings(
                mode,
                size,
                speed
        );
    }

    @Override
    public void onChangeSetting(Setting<?> setting) {
        Textures.CAIPIRINHA.setUpdateDelayMillis((int) (150 / speed.getValue()));
        Textures.CUTIE1.setUpdateDelayMillis((int) (50 / speed.getValue()));
    }

    @Override
    public void render() {
        switch (mode.getValue()) {
            case "Caipirinha" -> {
                Textures.CAIPIRINHA.update();
                float w = (float) ((Textures.CAIPIRINHA.getWidth() / 560d) * size.getValue());
                float h = (float) ((Textures.CAIPIRINHA.getHeight() / 560d) * size.getValue());
                float startX = getX() + ((size.getValue().floatValue() - w) / 2);
                float startY = getY() + ((size.getValue().floatValue() - h) / 2);
                BThackRender.drawTextureRect(
                        Textures.CAIPIRINHA,
                        startX,
                        startY,
                        startX + w,
                        startY + h
                );
            }
            case "Cutie1" -> {
                Textures.CUTIE1.update();
                BThackRender.drawTextureRect(Textures.CUTIE1, getX(), getY(), getX() + size.getValue().floatValue(), getY() + size.getValue().floatValue());
            }
            case "Cutie2" -> {
                Textures.CUTIE2.update();
                BThackRender.drawTextureRect(Textures.CUTIE2, getX(), getY(), getX() + size.getValue().floatValue(), getY() + size.getValue().floatValue());
            }
        }
    }

    @Override
    public void tick() {
        this.width = size.getValue().floatValue();
        this.height = size.getValue().floatValue();
    }
}
