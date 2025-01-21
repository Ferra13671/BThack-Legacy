package com.ferra13671.BThack.api.Gui.MainMenu;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Animation.Animation;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.api.Utils.System.BThackScreen;
import com.ferra13671.BThack.api.Utils.System.BThackScreens;
import com.ferra13671.BThack.api.Utils.System.buttons.Button;
import com.ferra13671.BThack.api.Utils.TextureStorage;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class LanguageSelectorScreen extends BThackScreen {

    public LanguageSelectorScreen() {
        super(Text.of("LanguageSelector"));
    }

    @Override
    protected void init() {
        buttons.clear();

        buttons.add(new Button(1, mc.getWindow().getScaledWidth() / 2 - 44, (int) (mc.getWindow().getScaledHeight() / 1.7), 20, 10, "EN")
                .withAction(buttonClickInfo -> {
                    ModuleList.clientSettings.language.setValue("EN");
                    ModuleList.clientSettings.language.setIndex(0);
                    actionAfterClicking(() -> changeScreen(BThackScreens.BTHACK_MAIN_MENU, new Animation(Easing.LINEAR, 1000)));
                }));
        buttons.add(new Button(1, mc.getWindow().getScaledWidth() / 2, (int) (mc.getWindow().getScaledHeight() / 1.7), 20, 10, "RU")
                .withAction(buttonClickInfo -> {
                    ModuleList.clientSettings.language.setValue("RU");
                    ModuleList.clientSettings.language.setIndex(1);
                    actionAfterClicking(() -> changeScreen(BThackScreens.BTHACK_MAIN_MENU, new Animation(Easing.LINEAR, 1000)));
                }));
        buttons.add(new Button(1, mc.getWindow().getScaledWidth() / 2 + 44, (int) (mc.getWindow().getScaledHeight() / 1.7), 20, 10, "PL")
                .withAction(buttonClickInfo -> {
                    ModuleList.clientSettings.language.setValue("PL");
                    ModuleList.clientSettings.language.setIndex(2);
                    actionAfterClicking(() -> changeScreen(BThackScreens.BTHACK_MAIN_MENU, new Animation(Easing.LINEAR, 1000)));
                }));
    }

    private final int blackColor = ColorUtils.fastRGBA(0,0,0, 240);

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        drawMainMenuWallpaper(mouseX, mouseY);
        BThackRender.drawVerticalGradientRect(0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), ColorUtils.TRANSPARENT, blackColor);

        float yUp = (float) (mc.getWindow().getScaledHeight() / 1.7) - 36;
        float yDown = (float) (mc.getWindow().getScaledHeight() / 1.7) - 14;
        float center = mc.getWindow().getScaledWidth() / 2f;

        BThackRender.drawTextureRect(TextureStorage.EN_FLAG, center - 64, yUp, center - 24, yDown);
        BThackRender.drawTextureRect(TextureStorage.RU_FLAG, center - 20, yUp, center + 20, yDown);
        BThackRender.drawTextureRect(TextureStorage.PL_FLAG, center + 24, yUp, center + 64, yDown);

        BThackRender.drawCenteredString("Welcome! Please select the language to be used.", center, yUp - 20, -1);

        super.render(context, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
