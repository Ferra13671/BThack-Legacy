package com.ferra13671.BThack.api.Gui.MainMenu;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Font.FontUtils;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Utils.DesktopUtils;
import com.ferra13671.BThack.api.Utils.System.BThackScreen;
import com.ferra13671.BThack.api.Utils.System.BThackScreens;
import com.ferra13671.BThack.api.Utils.System.buttons.Button;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class OutdatedVersionScreen extends BThackScreen {

    public OutdatedVersionScreen() {
        super(Text.of("OutdatedVersion"));
    }

    @Override
    protected void init() {
        super.init();

        buttons.clear();
        buttons.add(Button.of(1, mc.getWindow().getScaledWidth() / 2, mc.getWindow().getScaledHeight() / 2, 120, 10, "lang.screen.ExitAndUpdate")
                .withAction(buttonClickInfo -> {
                    DesktopUtils.openURI("https://github.com/Ferra13671/BThack/releases/download/" + BThack.instance.versionInfo.getNewVersion() + "/BThack-" + BThack.instance.MC_VERSION + "-fabric" + BThack.instance.versionInfo.getNewVersion().replace(BThack.instance.MC_VERSION, "") + ".jar");
                    mc.stop();
                }));
        buttons.add(Button.of(2, mc.getWindow().getScaledWidth() / 2, mc.getWindow().getScaledHeight() / 2 + 25, 120, 10, "lang.screen.Don'tShowUntilNext")
                .withAction(buttonClickInfo -> {
                    BThack.instance.versionInfo.setNeedShowAgainOneRelease(false);
                    actionAfterClicking(() -> changeScreen(BThack.instance.versionInfo.isFirstLaunched() ? BThackScreens.LANGUAGE_SELECTOR : BThackScreens.BTHACK_MAIN_MENU));
                }));
        buttons.add(Button.of(3, mc.getWindow().getScaledWidth() / 2, mc.getWindow().getScaledHeight() / 2 + 50, 120, 10, "lang.screen.Don'tShowAll")
                .withAction(buttonClickInfo -> {
                    BThack.instance.versionInfo.setNeedShowAgainAllReleases(false);
                    actionAfterClicking(() -> changeScreen(BThack.instance.versionInfo.isFirstLaunched() ? BThackScreens.LANGUAGE_SELECTOR : BThackScreens.BTHACK_MAIN_MENU));
                }));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        drawMainMenuWallpaper(mouseX, mouseY);
        BThackRender.drawVerticalGradientRect(0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), ColorUtils.TRANSPARENT, ColorUtils.fastRGBA(0,0,0, 240));

        BThackRender.drawCenteredString(String.format(LanguageSystem.translate("lang.screen.OutdatedVersion.message1"), BThack.instance.VERSION, BThack.instance.versionInfo.getNewVersion()), mc.getWindow().getScaledWidth() / 2f, mc.getWindow().getScaledHeight() / 2f - 50, -1);
        BThackRender.drawCenteredString(LanguageSystem.translate("lang.screen.OutdatedVersion.message2"), mc.getWindow().getScaledWidth() / 2f, mc.getWindow().getScaledHeight() / 2f - 50 + FontUtils.getTextHeight(LanguageSystem.translate("lang.screen.OutdatedVersion.message1")) + 5, -1);

        super.render(context, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
