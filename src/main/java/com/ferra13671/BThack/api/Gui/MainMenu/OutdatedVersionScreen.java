package com.ferra13671.BThack.api.Gui.MainMenu;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Font.FontRenderManager;
import com.ferra13671.BThack.Core.Render.Font.FontUtils;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Utils.DesktopUtils;
import com.ferra13671.BThack.api.Utils.System.BThackScreen;
import com.ferra13671.BThack.api.Utils.System.BThackScreens;
import com.ferra13671.BThack.api.Utils.System.buttons.Button;
import com.ferra13671.BThack.api.Utils.Textures;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class OutdatedVersionScreen extends BThackScreen {

    public OutdatedVersionScreen() {
        super(Text.of("OutdatedVersion"));
    }

    @Override
    protected void init() {
        super.init();

        buttons.clear();
        buttons.add(Button.of(1, (mc.getWindow().getScaledWidth() / 2) + 85, mc.getWindow().getScaledHeight() / 2, 65, 10, "lang.screen.OutdatedVersion.Yes")
                .withAction(buttonClickInfo -> {
                    DesktopUtils.openURI("https://github.com/Ferra13671/BThack/releases/download/" + BThack.instance.versionInfo.getNewVersion() + "/BThack-" + BThack.instance.MC_VERSION + "-fabric" + BThack.instance.versionInfo.getNewVersion().replace(BThack.instance.MC_VERSION, "") + ".jar");
                    mc.stop();
                })
        );
        buttons.add(Button.of(2, (mc.getWindow().getScaledWidth() / 2) + 85, mc.getWindow().getScaledHeight() / 2 + 22, 65, 10, "lang.screen.OutdatedVersion.No")
                .withAction(buttonClickInfo -> {
                    BThack.instance.versionInfo.setNeedShowAgainOneRelease(false);
                    actionAfterClicking(() -> changeScreen(BThack.instance.versionInfo.isFirstLaunched() ? BThackScreens.LANGUAGE_SELECTOR : BThackScreens.BTHACK_MAIN_MENU));
                })
        );
        buttons.add(Button.of(3, (mc.getWindow().getScaledWidth() / 2) + 85, mc.getWindow().getScaledHeight() / 2 + 44, 65, 10, "lang.screen.OutdatedVersion.AlwaysNo")
                .withAction(buttonClickInfo -> {
                    BThack.instance.versionInfo.setNeedShowAgainAllReleases(false);
                    actionAfterClicking(() -> changeScreen(BThack.instance.versionInfo.isFirstLaunched() ? BThackScreens.LANGUAGE_SELECTOR : BThackScreens.BTHACK_MAIN_MENU));
                })
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        drawMainMenuWallpaper(mouseX, mouseY);
        BThackRender.drawVerticalGradientRect(0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), ColorUtils.TRANSPARENT, ColorUtils.fastRGBA(0,0,0, 180));

        BThackRender.drawRect((mc.getWindow().getScaledWidth() / 2f) - 155, (mc.getWindow().getScaledHeight() / 2f) - 100, (mc.getWindow().getScaledWidth() / 2f) + 155, (mc.getWindow().getScaledHeight() / 2f) + 100, ColorUtils.fastRGBA(0, 0, 0, 150));
        BThackRender.drawVerticalGradientOutlineRect((mc.getWindow().getScaledWidth() / 2f) - 155, (mc.getWindow().getScaledHeight() / 2f) - 100, (mc.getWindow().getScaledWidth() / 2f) + 155, (mc.getWindow().getScaledHeight() / 2f) + 100, 1.5f, ColorUtils.WHITE, ColorUtils.fastRGBA(150, 150, 150, 255));
        BThackRender.drawTextureRect(Textures.CHIBI1, (mc.getWindow().getScaledWidth() / 2f) - 155, (mc.getWindow().getScaledHeight() / 2f) - 50, (mc.getWindow().getScaledWidth() / 2f) - 5, (mc.getWindow().getScaledHeight() / 2f) + 100);

        float y = (mc.getWindow().getScaledHeight() / 2f) - 95;
        final float x = (mc.getWindow().getScaledWidth() / 2f) - 150;
        BThackRender.drawString(LanguageSystem.translate("lang.screen.OutdatedVersion.helloMessage"), x, y, -1, true, FontRenderManager.DrawMode.NORMAL_BOLD);
        y += FontUtils.getTextHeight(LanguageSystem.translate("lang.screen.OutdatedVersion.helloMessage"), FontRenderManager.DrawMode.NORMAL_BOLD) + 15;

        String currentText = String.format(LanguageSystem.translate("lang.screen.OutdatedVersion.Current"), Formatting.GRAY + BThack.instance.VERSION);
        BThackRender.drawString(currentText, x, y, -1, true, FontRenderManager.DrawMode.NORMAL_BOLD);
        y += FontUtils.getTextHeight(currentText, FontRenderManager.DrawMode.NORMAL_BOLD) + 5;

        String newText = String.format(LanguageSystem.translate("lang.screen.OutdatedVersion.New"), Formatting.GREEN + BThack.instance.versionInfo.getNewVersion());
        BThackRender.drawString(newText, x, y, -1, true, FontRenderManager.DrawMode.NORMAL_BOLD);

        String askText = LanguageSystem.translate("lang.screen.OutdatedVersion.UpdateMessage");
        BThackRender.drawString(askText, (mc.getWindow().getScaledWidth() / 2f) + 150 - FontUtils.getTextWidth(askText, FontRenderManager.DrawMode.NORMAL_BOLD), mc.getWindow().getScaledHeight() / 2f - 60, -1, true, FontRenderManager.DrawMode.NORMAL_BOLD);


        super.render(context, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
