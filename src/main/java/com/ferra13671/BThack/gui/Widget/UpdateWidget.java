package com.ferra13671.BThack.gui.Widget;

import com.ferra13671.BThack.core.BThackUpdater.BThackUpdater;
import com.ferra13671.BThack.core.BThackUpdater.UpdateState;
import com.ferra13671.BThack.core.BThackUpdater.UpdaterThread;
import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.core.Render.Font.FontRenderManager;
import com.ferra13671.BThack.core.Render.Font.FontUtils;
import com.ferra13671.BThack.api.Animation.Animation;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.api.GuiSystem.BThackScreens;
import com.ferra13671.BThack.api.GuiSystem.ScreenWidget;
import com.ferra13671.BThack.api.GuiSystem.buttons.Button;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.client.gui.DrawContext;

public class UpdateWidget extends ScreenWidget {
    private Runnable textRunnable;
    private UpdaterThread updaterThread;
    private UpdateState lastUpdateState = UpdateState.STARTED;

    public UpdateWidget() {
        super(300, 50, 1);
    }

    public void startUpdate() {
        if (updaterThread != null && updaterThread.isAlive()) return;
        try {
            updaterThread = BThackUpdater.startUpdate();
        } catch (Exception e) {
            buttons.clear();
            initFailed();
        }
    }

    @Override
    public void init() {
        super.init();

        buttons.clear();
        if (updaterThread != null) {
            switch (updaterThread.getUpdateState()) {
                case STARTED -> initStarted();
                case SUCCESSFUL -> initSuccessful();
                case FAILED -> initFailed();
            }
        }
    }

    public void initStarted() {
        textRunnable = () -> {
            float x = xLeft + 150;
            float y = yUp + 5;

            String text1 = LanguageSystem.translate("lang.widget.Update.Started.text1");
            BThackRender.drawCenteredString(text1, x, y, -1, FontRenderManager.DrawMode.NORMAL_BOLD);
            y += FontUtils.getTextHeight(text1, FontRenderManager.DrawMode.NORMAL_BOLD) + 4;

            String text2 = LanguageSystem.translate("lang.widget.Update.Started.text2");
            BThackRender.drawCenteredString(text2, x, y, -1, FontRenderManager.DrawMode.NORMAL_BOLD);
            y += FontUtils.getTextHeight(text2, FontRenderManager.DrawMode.NORMAL_BOLD) + 4;

            BThackRender.drawCenteredString(LanguageSystem.translate("lang.widget.Update.Started.text3"), x, y, -1, FontRenderManager.DrawMode.SMALL);

            BThackRender.drawCenteredString(String.format(LanguageSystem.translate("lang.widget.Update.Started.text4") + " %sMb.", updaterThread.getDownloadedMBytes()), x, yDown - 10, -1, FontRenderManager.DrawMode.NORMAL_BOLD);
        };
    }

    public void initSuccessful() {
        textRunnable = () -> {
            float x = xLeft + 150;
            float y = yUp + 5;

            String text1 = LanguageSystem.translate("lang.widget.Update.Successful.text1");
            BThackRender.drawCenteredString(text1, x, y, -1, FontRenderManager.DrawMode.NORMAL_BOLD);
            y += FontUtils.getTextHeight(text1, FontRenderManager.DrawMode.NORMAL_BOLD) + 4;

            BThackRender.drawCenteredString(LanguageSystem.translate("lang.widget.Update.Successful.text2"), x, y, -1, FontRenderManager.DrawMode.NORMAL_BOLD);
        };
        buttons.add(Button.of(1, (int) xLeft + 150, (int) yDown - 15, 70, 10, "lang.screen.Mainmenu.Quit")
                .withAction(buttonClickInfo -> actionAfterClicking(() -> changeScreen(() -> BThackScreens.EXIT, new Animation(Easing.LINEAR, 1000))))
        );
    }

    public void initFailed() {
        textRunnable = () -> {
            float x = xLeft + 150;
            float y = yUp + 5;

            String text1 = LanguageSystem.translate("lang.widget.Update.Failed.text1");
            BThackRender.drawCenteredString(text1, x, y, -1, FontRenderManager.DrawMode.NORMAL_BOLD);
            y += FontUtils.getTextHeight(text1, FontRenderManager.DrawMode.NORMAL_BOLD) + 4;

            BThackRender.drawCenteredString(LanguageSystem.translate("lang.widget.Update.Failed.text2"), x, y, -1, FontRenderManager.DrawMode.NORMAL_BOLD);
        };
        buttons.add(Button.of(1, (int) xLeft + 73, (int) yDown - 15, 70, 10, "lang.widget.Update.Failed.tryAgain")
                .withAction(buttonClickInfo -> actionAfterClicking(() -> {
                    startUpdate();
                    init();
                }))
        );
        buttons.add(Button.of(1, (int) xRight - 73, (int) yDown - 15, 70, 10, "lang.widget.Update.Failed.close")
                .withAction(buttonClickInfo -> actionAfterClicking(this::close))
        );
    }

    @Override
    public void tick() {
        super.tick();
        if (lastUpdateState != updaterThread.getUpdateState()) {
            lastUpdateState = updaterThread.getUpdateState();
            init();
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        drawPlate();

        if (textRunnable != null) textRunnable.run();

        super.render(context, mouseX, mouseY, partialTicks);
    }
}
