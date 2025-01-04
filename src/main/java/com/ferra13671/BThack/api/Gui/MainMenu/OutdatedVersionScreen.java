package com.ferra13671.BThack.api.Gui.MainMenu;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Utils.DesktopUtils;
import com.ferra13671.BThack.api.Utils.System.BThackScreen;
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
        buttons.add(Button.of(1, mc.getWindow().getScaledWidth() / 2, mc.getWindow().getScaledHeight() / 2, 120, 10, "lang.screen.ExitAndUpdate"));
        buttons.add(Button.of(2, mc.getWindow().getScaledWidth() / 2, mc.getWindow().getScaledHeight() / 2 + 25, 120, 10, "lang.screen.Don'tShowUntilNext"));
        buttons.add(Button.of(3, mc.getWindow().getScaledWidth() / 2, mc.getWindow().getScaledHeight() / 2 + 50, 120, 10, "lang.screen.Don'tShowAll"));

        buttons.forEach(button -> button.outline = true);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        BThackMainMenuScreen.drawWallpaper(mouseX, mouseY);
        BThackRender.drawVerticalGradientRect(0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), ColorUtils.TRANSPARENT, ColorUtils.fastRGBA(0,0,0, 240));

        BThackRender.drawCenteredString(String.format(LanguageSystem.translate("lang.screen.OutdatedVersion.message1"), BThack.instance.VERSION, BThack.instance.versionInfo.getNewVersion()), mc.getWindow().getScaledWidth() / 2f, mc.getWindow().getScaledHeight() / 2f - 50, -1);
        BThackRender.drawCenteredString(LanguageSystem.translate("lang.screen.OutdatedVersion.message2"), mc.getWindow().getScaledWidth() / 2f, mc.getWindow().getScaledHeight() / 2f - 50 + mc.textRenderer.fontHeight + 5, -1);

        super.render(context, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        switch (activeButton.getId()) {
            case 1 -> {
                DesktopUtils.openURI("https://github.com/Ferra13671/BThack/releases/download/" + BThack.instance.versionInfo.getNewVersion() + "/BThack-" + BThack.instance.MC_VERSION + "-fabric" + BThack.instance.versionInfo.getNewVersion().replace(BThack.instance.MC_VERSION, "") + ".jar");
                mc.stop();
            }
            case 2 -> {
                BThack.instance.versionInfo.setNeedShowAgainOneRelease(false);
                mc.setScreen(null);
            }
            case 3 -> {
                BThack.instance.versionInfo.setNeedShowAgainAllReleases(false);
                mc.setScreen(null);
            }
        }
        return false;
    }
}
