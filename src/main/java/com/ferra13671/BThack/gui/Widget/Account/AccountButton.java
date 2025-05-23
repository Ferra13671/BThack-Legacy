package com.ferra13671.BThack.gui.Widget.Account;

import com.ferra13671.BThack.Constants;
import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.core.Render.Font.FontRenderManager;
import com.ferra13671.BThack.core.Render.Font.FontUtils;
import com.ferra13671.BThack.core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.GuiSystem.buttons.Button;
import com.ferra13671.BThack.managers.managers.Account.Account;
import com.ferra13671.BThack.api.SoundSystem.Sounds;
import com.ferra13671.BThack.api.Utils.Textures;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AccountButton extends Button {
    private final Account account;
    private final List<Button> subButtons = new ArrayList<>();
    private final AccountsWidget widget;


    public AccountButton(int id, int x, int y, Account account, AccountsWidget widget) {
        super(id, x, y, 110, 15, "");
        this.account = account;
        this.widget = widget;
        initSubButtons();
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        initSubButtons();
    }

    @Override
    public void setCenterX(int centerX) {
        super.setCenterX(centerX);
        initSubButtons();
    }

    @Override
    public void setCenterY(int centerY) {
        super.setCenterY(centerY);
        initSubButtons();
    }

    @Override
    public void setHovered(boolean hovered) {
        super.setHovered(hovered);
        subButtons.forEach(button -> button.setHovered(false));
    }

    @Override
    public String getText() {
        return account.name();
    }

    @Override
    public void renderButton() {
        float animationDelta = getAnimationDelta();
        drawPlate(animationDelta);
        BThackRender.drawTextureRect(Textures.PLAYER, getCenterX() - getWidth() + 2, getCenterY() - 10, getCenterX() - getWidth() + 22, getCenterY() + 10);
        BThackRender.drawString(getText(), getCenterX() - getWidth() + 24, (getCenterY() - (FontUtils.getTextHeight(getText()) / 2f)), -1, true, FontRenderManager.DrawMode.NORMAL_BOLD);
        subButtons.forEach(Button::renderButton);
    }

    @Override
    public void updateButton(int mouseX, int mouseY) {
        subButtons.forEach(button -> button.updateButton(mouseX, mouseY));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        subButtons.forEach(button -> {
            if (button.isMouseOnButton(mouseX, mouseY))
                button.mouseClicked(mouseX, mouseY, mouseButton);
        });
    }

    private void initSubButtons() {
        subButtons.clear();

        subButtons.add(new SubButton(1, getCenterX() + getWidth() - 20, getCenterY(), 15, 10, "Delete", Color.RED.hashCode()).withAction(
                buttonClickInfo -> widget.actionAfterClicking(() -> widget.deleteAccount(account))
        ));
        subButtons.add(new SubButton(1, getCenterX() + getWidth() - 55, getCenterY(), 15, 10, "Edit", Color.ORANGE.hashCode()).withAction(
                buttonClickInfo -> widget.actionAfterClicking(() -> {
                    widget.close();
                    widget.getParent().widgetManage.addWidget(new AddAccountWidget(account));
                })
        ));
        Button loadButton = new SubButton(3, getCenterX() + getWidth() - 90, getCenterY(), 15, 10, "Load", Color.GREEN.hashCode()).withAction(
                buttonClickInfo -> widget.actionAfterClicking(account::login)
        );
        loadButton.setClickSound(Sounds.CONFIG_SAVED_OR_LOADED);
        subButtons.add(loadButton);
    }

    private static class SubButton extends Button {
        private final int color;

        public SubButton(int id, int x, int y, int width, int height, String text, int color) {
            super(id, x, y, width, height, text);
            this.color = color;
        }

        @Override
        public void renderButton() {
            float animationDelta = getAnimationDelta();
            animationDelta *= 2;
            if (!hovered && hoveredAnimation.getEase() >= 1) {
                BThackRender.drawRoundedRectWithOutline(getCenterX() - width, getCenterY() - height, getCenterX() + width, getCenterY() + height, 10f, Constants.GUISYSTEM_BUTTON_RECT_COLOR, color, 1);
            } else {
                BThackRender.drawRoundedRectWithOutline(getCenterX() - width - animationDelta, getCenterY() - height - animationDelta, getCenterX() + width + animationDelta, getCenterY() + height + animationDelta, 10f, Constants.GUISYSTEM_BUTTON_RECT_COLOR, color, 1);

                animationDelta /= 2;
                BThackRender.drawHorizontalGradientRect((int)(getCenterX() - (width * 0.8 * animationDelta)), getCenterY() + height - 4, getCenterX(), getCenterY() + height - 2, ColorUtils.TRANSPARENT, ColorUtils.integrateAlpha(color, (int) (animationDelta * 255)));
                BThackRender.drawHorizontalGradientRect(getCenterX(), getCenterY() + height - 4, (int)(getCenterX() + (width * 0.8 * animationDelta)), getCenterY() + height - 2, ColorUtils.integrateAlpha(color, (int) (animationDelta * 255)), ColorUtils.TRANSPARENT);
            }
            BThackRender.drawString(getText(), (getCenterX() - (FontUtils.getTextWidth(getText(), FontRenderManager.DrawMode.SMALL_BOLD) / 2f)), (getCenterY() - (FontUtils.getTextHeight(getText(), FontRenderManager.DrawMode.SMALL_BOLD) / 2f)), color, true, FontRenderManager.DrawMode.SMALL_BOLD);
        }

        @Override
        public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
            super.mouseClicked(mouseX, mouseY, mouseButton);
            clickAction(mouseX, mouseY, mouseButton);
        }
    }
}
