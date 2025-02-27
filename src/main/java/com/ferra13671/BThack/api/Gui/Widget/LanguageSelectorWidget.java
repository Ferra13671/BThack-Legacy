package com.ferra13671.BThack.api.Gui.Widget;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Font.FontRenderManager;
import com.ferra13671.BThack.Core.Render.Font.FontUtils;
import com.ferra13671.BThack.api.GuiSystem.ScreenWidget;
import com.ferra13671.BThack.api.GuiSystem.buttons.Button;
import com.ferra13671.BThack.api.Utils.Textures;
import net.minecraft.client.gui.DrawContext;

public class LanguageSelectorWidget extends ScreenWidget {

    public LanguageSelectorWidget() {
        super(310, 150, 1);
    }

    @Override
    public void init() {
        super.init();

        buttons.clear();

        buttons.add(Button.of(1, (int) xRight - 139, (int) yDown - 45, 20, 10, "EN")
                .withAction(buttonClickInfo -> {
                    ModuleList.clientSettings.language.setValue("EN");
                    ModuleList.clientSettings.language.setIndex(0);
                    close();
                })
        );
        buttons.add(Button.of(2, (int) xRight - 95, (int) yDown - 45, 20, 10, "RU")
                .withAction(buttonClickInfo -> {
                    ModuleList.clientSettings.language.setValue("RU");
                    ModuleList.clientSettings.language.setIndex(1);
                    close();
                })
        );
        buttons.add(Button.of(3, (int) xRight - 51, (int) yDown - 45, 20, 10, "PL")
                .withAction(buttonClickInfo -> {
                    ModuleList.clientSettings.language.setValue("PL");
                    ModuleList.clientSettings.language.setIndex(2);
                    close();
                })
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        drawPlate();

        BThackRender.drawTextureRect(Textures.CHIBI2, xLeft, yUp, xLeft + 150, yDown);

        final float x = xRight - 95;
        float y = yUp + 5;

        BThackRender.drawCenteredString("Welcome!", x, y, -1, FontRenderManager.DrawMode.NORMAL_BOLD);
        y += FontUtils.getTextHeight("Welcome!", FontRenderManager.DrawMode.NORMAL_BOLD) + 4;

        String text = "Select the language to be used";
        BThackRender.drawCenteredString(text, x, y, -1, FontRenderManager.DrawMode.NORMAL_BOLD);

        float x2 = xRight - 95;
        float y2 = yDown - 70;

        BThackRender.drawTextureRect(Textures.EN_FLAG, x2 - 64, y2 - 11, x2 - 24, y2 + 11);
        BThackRender.drawTextureRect(Textures.RU_FLAG, x2 - 20, y2 - 11, x2 + 20, y2 + 11);
        BThackRender.drawTextureRect(Textures.PL_FLAG, x2 + 24, y2 - 11, x2 + 64, y2 + 11);

        super.render(context, mouseX, mouseY, partialTicks);
    }
}
