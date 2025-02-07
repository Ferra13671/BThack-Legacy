package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Font.FontUtils;
import com.ferra13671.BThack.api.HudComponent.HudComponent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Utils.Textures;

import java.util.Arrays;

public class WatermarkComponent extends HudComponent {

    private final ModeSetting logoType;

    public WatermarkComponent() {
        super("Watermark",
                5,
                5,
                true
        );

        logoType = new ModeSetting("Logo Type", this, Arrays.asList("Logo", "Text"));

        initSettings(
                logoType
        );
    }

    @Override
    public void render() {
        if (nullCheck()) return;

        if (logoType.getValue().equals("Text")) {
            drawText(Client.clientInfo.getCName(), (int) this.getX(), (int) this.getY());
        } else {
            BThackRender.drawTextureRect(Textures.BTHACK_LOGO, getX(), getY() - 18, getX() + 138, getY() + 54);
        }
    }

    @Override
    public void tick() {
        if (nullCheck()) return;

        if (logoType.getValue().equals("Text")) {
            this.width = FontUtils.getTextWidth(Client.clientInfo.getCName());
            this.height = FontUtils.getTextHeight(Client.clientInfo.getCName());
        } else {
            this.width = 138;
            this.height = 42;
        }
    }
}
