package com.ferra13671.BThack.api.Gui.Widget.Account;

import com.ferra13671.BTbot.api.Utils.Generate.StringGenerator;
import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.core.Render.Drawers.Drawers;
import com.ferra13671.BThack.core.Render.Font.FontRenderManager;
import com.ferra13671.BThack.core.Render.Font.FontUtils;
import com.ferra13671.BThack.api.Animation.Animation;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.api.GuiSystem.ScreenWidget;
import com.ferra13671.BThack.api.GuiSystem.buttons.Button;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Account.Account;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class AccountsWidget extends ScreenWidget {

    private final List<Button> accounts = new ArrayList<>();

    private double maxYScroll;

    private Animation accountButtonsAnimation;
    private boolean closing = false;

    public AccountsWidget() {
        super(330, 230, 1);
    }

    @Override
    public void onDisplayed() {
        super.onDisplayed();
        refreshAccounts();
        accountButtonsAnimation = new Animation(Easing.BACK_IN_OUT, 1000);
        closing = false;
    }

    @Override
    public void close() {
        super.close();
        accountButtonsAnimation = new Animation(Easing.BACK_IN_OUT, 1100);
        closing = true;
    }

    @Override
    public void init() {
        super.init();
        buttons.clear();

        buttons.add(Button.of(1, (int) xRight - 55, (int) yDown - 15, 50, 10, "Add Account")
                .withAction(buttonClickInfo -> actionAfterClicking(() -> {
                    close();
                    parent.widgetManage.addWidget(new AddAccountWidget());
                })));
        buttons.add(Button.of(2, (int) xLeft + 55, (int) yDown - 15, 50, 10, "Random Account")
                .withAction(buttonClickInfo -> actionAfterClicking(this::createRandomAccount)));
        buttons.add(Button.of(3, (int) xLeft + 165, (int) yDown - 15, 50, 10, "Done")
                .withAction(buttonClickInfo -> actionAfterClicking(this::close)));

        refreshAccounts();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        drawPlate();

        String text = "Current account: " + Formatting.AQUA + mc.getSession().getUsername();
        BThackRender.drawCenteredString(text, xLeft + (getWidth() / 2), yUp - 5 - FontUtils.getTextHeight(text, FontRenderManager.DrawMode.NORMAL_BOLD), -1, FontRenderManager.DrawMode.NORMAL_BOLD);

        Drawers.RECT.begin(-1);
        Drawers.RECT.draw(xLeft, yDown - 31.5f, xRight, yDown - 30);
        Drawers.RECT.end();
        double yOffset = ((closing ? accountButtonsAnimation.getEase() : 1 - accountButtonsAnimation.getEase()) * ((mc.getWindow().getScaledHeight() / 2d) + getHeight()));
        BThackRender.enableScissor((int) xLeft + 1, (int) (yUp + 1 + yOffset), (int) getWidth() - 2, (int) (getHeight() - 31.5f));
        for (Button button : accounts) {
            if (mouseY < yDown - 31.5f)
                button.updateButton(mouseX, mouseY);
            else button.setHovered(false);
            button.renderButton();
        }
        BThackRender.disableScissor();
        super.render(context, mouseX, mouseY, partialTicks);
    }

    public void deleteAccount(Account account) {
        Managers.ACCOUNT_MANAGER.removeAccount(account.name());
        init();
    }

    public void createRandomAccount() {
        Managers.ACCOUNT_MANAGER.addAccount(new Account(StringGenerator.generateNextString(10, true, false, false), "", ""));
        init();
    }

    public void refreshAccounts() {
        accounts.clear();

        int yOffset = 0;

        for (Account account : Managers.ACCOUNT_MANAGER.getAccounts()) {
            Button button = new AccountButton(-1, (int) xLeft + 165, (int) yUp + 35 + (35 * yOffset), account, this) {
                @Override
                public boolean equals(Object obj) {
                    if (!(obj instanceof Button b)) return false;
                    return b.getText().equals(this.getText());
                }
            };
            accounts.add(button);
            yOffset++;
        }

        if (!accounts.isEmpty())
            maxYScroll = accounts.getLast().getCenterY();
        else
            maxYScroll = 0;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (mouseY < yDown - 31.5f) {
            for (Button button : accounts) {
                if (button.isMouseOnButton((int) mouseX, (int) mouseY))
                    button.mouseClicked((int) mouseX, (int) mouseY, mouseButton);
            }
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        Button button = accounts.getLast();
        if ((button.getCenterY() + (verticalAmount * 10)) > maxYScroll)
            return false;

        for (Button button1 : accounts)
            button1.setCenterY((int) (button1.getCenterY() + (verticalAmount * 10)));

        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int shift) {
        if (keyCode == KeyboardUtils.KEY_ESCAPE) {
            close();
            return false;
        }
        return super.keyPressed(keyCode, scanCode, shift);
    }
}
