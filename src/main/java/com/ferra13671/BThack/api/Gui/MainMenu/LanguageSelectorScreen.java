package com.ferra13671.BThack.api.Gui.MainMenu;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Font.FontRenderManager;
import com.ferra13671.BThack.Core.Render.Font.FontUtils;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Animation.Animation;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.api.Utils.System.BThackScreen;
import com.ferra13671.BThack.api.Utils.System.BThackScreens;
import com.ferra13671.BThack.api.Utils.System.buttons.Button;
import com.ferra13671.BThack.api.Utils.Textures;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class LanguageSelectorScreen extends BThackScreen {

    public LanguageSelectorScreen() {
        super(Text.of("LanguageSelector"));
    }

    @Override
    protected void init() {
        buttons.clear();

        buttons.add(Button.of(1, (mc.getWindow().getScaledWidth() / 2) + 16, (mc.getWindow().getScaledHeight() / 2) + 30, 20, 10, "EN")
                .withAction(buttonClickInfo -> {
                    ModuleList.clientSettings.language.setValue("EN");
                    ModuleList.clientSettings.language.setIndex(0);
                    actionAfterClicking(() -> changeScreen(BThackScreens.BTHACK_MAIN_MENU, new Animation(Easing.LINEAR, 1000)));
                })
        );
        buttons.add(Button.of(2, (mc.getWindow().getScaledWidth() / 2) + 60, (mc.getWindow().getScaledHeight() / 2) + 30, 20, 10, "RU")
                .withAction(buttonClickInfo -> {
                    ModuleList.clientSettings.language.setValue("RU");
                    ModuleList.clientSettings.language.setIndex(1);
                    actionAfterClicking(() -> changeScreen(BThackScreens.BTHACK_MAIN_MENU, new Animation(Easing.LINEAR, 1000)));
                })
        );
        buttons.add(Button.of(3, (mc.getWindow().getScaledWidth() / 2) + 104, (mc.getWindow().getScaledHeight() / 2) + 30, 20, 10, "PL")
                .withAction(buttonClickInfo -> {
                    ModuleList.clientSettings.language.setValue("PL");
                    ModuleList.clientSettings.language.setIndex(2);
                    actionAfterClicking(() -> changeScreen(BThackScreens.BTHACK_MAIN_MENU, new Animation(Easing.LINEAR, 1000)));
                })
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        drawMainMenuWallpaper(mouseX, mouseY);
        BThackRender.drawVerticalGradientRect(0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), ColorUtils.TRANSPARENT, ColorUtils.fastRGBA(0,0,0, 180));

        BThackRender.drawRect((mc.getWindow().getScaledWidth() / 2f) - 155, (mc.getWindow().getScaledHeight() / 2f) - 75, (mc.getWindow().getScaledWidth() / 2f) + 155, (mc.getWindow().getScaledHeight() / 2f) + 75, ColorUtils.fastRGBA(0, 0, 0, 150));
        BThackRender.drawVerticalGradientOutlineRect((mc.getWindow().getScaledWidth() / 2f) - 155, (mc.getWindow().getScaledHeight() / 2f) - 75, (mc.getWindow().getScaledWidth() / 2f) + 155, (mc.getWindow().getScaledHeight() / 2f) + 75, 1.5f, ColorUtils.WHITE, ColorUtils.fastRGBA(150, 150, 150, 255));
        BThackRender.drawTextureRect(Textures.CHIBI2, (mc.getWindow().getScaledWidth() / 2f) - 155, (mc.getWindow().getScaledHeight() / 2f) - 75, (mc.getWindow().getScaledWidth() / 2f) - 5, (mc.getWindow().getScaledHeight() / 2f) + 75);

        final float x = (mc.getWindow().getScaledWidth() / 2f) + 60;
        float y = (mc.getWindow().getScaledHeight() / 2f) - 70;

        BThackRender.drawCenteredString("Welcome!", x, y, -1, FontRenderManager.DrawMode.NORMAL_BOLD);
        y += FontUtils.getTextHeight("Welcome!", FontRenderManager.DrawMode.NORMAL_BOLD) + 4;

        String text = "Select the language to be used";
        BThackRender.drawCenteredString(text, x, y, -1, FontRenderManager.DrawMode.NORMAL_BOLD);

        float x2 = (mc.getWindow().getScaledWidth() / 2f) + 60;
        float y2 = (mc.getWindow().getScaledHeight() / 2f) + 5;

        BThackRender.drawTextureRect(Textures.EN_FLAG, x2 - 64, y2 - 11, x2 - 24, y2 + 11);
        BThackRender.drawTextureRect(Textures.RU_FLAG, x2 - 20, y2 - 11, x2 + 20, y2 + 11);
        BThackRender.drawTextureRect(Textures.PL_FLAG, x2 + 24, y2 - 11, x2 + 64, y2 + 11);

        super.render(context, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
