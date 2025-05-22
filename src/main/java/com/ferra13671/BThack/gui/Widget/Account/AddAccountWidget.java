package com.ferra13671.BThack.gui.Widget.Account;

import com.ferra13671.BThack.api.GuiSystem.ScreenWidget;
import com.ferra13671.BThack.api.GuiSystem.buttons.Button;
import com.ferra13671.BThack.api.GuiSystem.buttons.TextFrameButton;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.managers.Account.Account;
import com.ferra13671.BThack.api.SoundSystem.Sounds;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import net.minecraft.client.gui.DrawContext;

public class AddAccountWidget extends ScreenWidget {
    private Account editableAccount = null;

    public AddAccountWidget() {
        super(140, 105, 1);
    }

    public AddAccountWidget(Account editableAccount) {
        this();
        this.editableAccount = editableAccount;
    }

    @Override
    public void init() {
        super.init();

        buttons.clear();
        buttons.add(new TextFrameButton(1, (int) xLeft + 70, (int) yUp + 15, 65, 10, "NickName"));
        buttons.add(new TextFrameButton(2, (int) xLeft + 70, (int) yUp + 40, 65, 10, "AutoAuth (optionally)"));
        buttons.add(new TextFrameButton(3, (int) xLeft + 70, (int) yUp + 65, 65, 10, "TwoFA (optionally)"));
        Button button = Button.of(4, (int) xLeft + 70, (int) yUp + 90, 65, 10, "Confirm")
                .withAction(buttonClickInfo -> {
                    Account account = new Account(getButtonFromId(1).getText(), getButtonFromId(2).getText(), getButtonFromId(3).getText());
                    if (editableAccount != null) Managers.ACCOUNT_MANAGER.replaceAccount(editableAccount, account);
                    else Managers.ACCOUNT_MANAGER.addAccount(account);
                    close();
                });
        button.setClickSound(Sounds.CONFIG_SAVED_OR_LOADED);
        buttons.add(button);

        if (editableAccount != null) {
            getButtonFromId(1).setText(editableAccount.name());
            getButtonFromId(2).setText(editableAccount.autoAuth());
            getButtonFromId(3).setText(editableAccount.twoFA());
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        drawPlate();
        super.render(context, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int shift) {
        if (keyCode == KeyboardUtils.KEY_ESCAPE) close();
        return super.keyPressed(keyCode, scanCode, shift);
    }

    @Override
    public void close() {
        super.close();
        parent.widgetManage.addWidget(new AccountsWidget());
    }
}
