package com.ferra13671.BThack.api.Gui.Widget;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Font.FontRenderManager;
import com.ferra13671.BThack.Core.Render.Font.FontUtils;
import com.ferra13671.BThack.api.GuiSystem.BThackWidgets;
import com.ferra13671.BThack.api.GuiSystem.ScreenWidget;
import com.ferra13671.BThack.api.GuiSystem.buttons.Button;
import com.ferra13671.BThack.api.Utils.Textures;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Formatting;

public class OutdatedVersionWidget extends ScreenWidget {

    public OutdatedVersionWidget() {
        super(310, 200, 1);
    }

    @Override
    public void init() {
        super.init();

        buttons.clear();
        buttons.add(Button.of(1, (int) xRight - 70, (int) yUp + 100, 65, 10, "lang.widget.OutdatedVersion.Yes")
                .withAction(buttonClickInfo -> {
                    //DesktopUtils.openURI("https://github.com/Ferra13671/BThack/releases/download/" + BThack.instance.versionInfo.getNewVersion() + "/BThack-" + BThack.instance.MC_VERSION + "-fabric" + BThack.instance.versionInfo.getNewVersion().replace(BThack.instance.MC_VERSION, "") + ".jar");
                    //mc.stop();
                    close();
                    ((UpdateWidget) BThackWidgets.UPDATE).startUpdate();
                    parent.widgetManage.addWidget(BThackWidgets.UPDATE);
                })
        );
        buttons.add(Button.of(2, (int) xRight - 70, (int) yUp + 100 + 22, 65, 10, "lang.widget.OutdatedVersion.No")
                .withAction(buttonClickInfo -> {
                    BThack.instance.versionInfo.setNeedShowAgainOneRelease(false);
                    close();
                })
        );
        buttons.add(Button.of(3, (int) xRight - 70, (int) yUp + 100 + 44, 65, 10, "lang.widget.OutdatedVersion.AlwaysNo")
                .withAction(buttonClickInfo -> {
                    BThack.instance.versionInfo.setNeedShowAgainAllReleases(false);
                    close();
                })
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        drawPlate();

        BThackRender.drawTextureRect(Textures.CHIBI1, xLeft, yUp + 50, xLeft + 150, yDown);

        float y = yUp + 5;
        final float x = xLeft + 5;
        BThackRender.drawString(LanguageSystem.translate("lang.widget.OutdatedVersion.helloMessage"), x, y, -1, true, FontRenderManager.DrawMode.NORMAL_BOLD);
        y += FontUtils.getTextHeight(LanguageSystem.translate("lang.widget.OutdatedVersion.helloMessage"), FontRenderManager.DrawMode.NORMAL_BOLD) + 15;

        String currentText = String.format(LanguageSystem.translate("lang.widget.OutdatedVersion.Current"), Formatting.GRAY + BThack.instance.VERSION);
        BThackRender.drawString(currentText, x, y, -1, true, FontRenderManager.DrawMode.NORMAL_BOLD);
        y += FontUtils.getTextHeight(currentText, FontRenderManager.DrawMode.NORMAL_BOLD) + 5;

        String newText = String.format(LanguageSystem.translate("lang.widget.OutdatedVersion.New"), Formatting.GREEN + BThack.instance.versionInfo.getNewVersion());
        BThackRender.drawString(newText, x, y, -1, true, FontRenderManager.DrawMode.NORMAL_BOLD);

        String askText = LanguageSystem.translate("lang.widget.OutdatedVersion.UpdateMessage");
        BThackRender.drawString(askText, xRight - 5 - FontUtils.getTextWidth(askText, FontRenderManager.DrawMode.NORMAL_BOLD), yUp + 40, -1, true, FontRenderManager.DrawMode.NORMAL_BOLD);

        super.render(context, mouseX, mouseY, partialTicks);
    }
}
