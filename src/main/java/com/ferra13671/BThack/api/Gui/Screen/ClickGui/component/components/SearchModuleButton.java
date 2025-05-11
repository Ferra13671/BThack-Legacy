package com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components;

import com.ferra13671.BThack.Constants;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.core.Render.Font.FontRenderManager;
import com.ferra13671.BThack.core.Render.Font.FontUtils;
import com.ferra13671.BThack.core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.Frame;
import com.ferra13671.BThack.api.GuiSystem.buttons.TextFrameButton;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;
import net.minecraft.util.Formatting;

public class SearchModuleButton extends ModuleButton {
    private final TextFrameButton textFrameButton = new TextFrameButton(0, 0, 0, Constants.CLICKGUI_FRAME_WIDTH / 2, Constants.CLICKGUI_BUTTON_HEIGHT / 2, "Search") {
        @Override
        public void renderButton() {
            String text = this.textBuilder.toString();
            if (text.isEmpty() && !selected) text = Formatting.GRAY + nullText + "...";
            BThackRender.enableScissor(getCenterX() - getWidth() + 2, getCenterY() - getHeight(), (getWidth() * 2) - 2, getHeight() * 2);
            BThackRender.drawString(text + (insertAdd && selected ? "|" : ""), FontUtils.getTextWidth(text, FontRenderManager.DrawMode.NORMAL_BOLD) > (getWidth() * 2) - 10 ? getCenterX() + getWidth() - FontUtils.getTextWidth(text, FontRenderManager.DrawMode.NORMAL_BOLD) - 10 : (getCenterX() - getWidth() + 5), getCenterY() - (FontUtils.getTextHeight(getText(), FontRenderManager.DrawMode.NORMAL_BOLD) / 2f), ColorUtils.WHITE, true, FontRenderManager.DrawMode.NORMAL_BOLD);
            BThackRender.disableScissor();
        }
    };

    public SearchModuleButton(Frame parent, int offset) {
        super(new Module("", "", 0, Module.MCategory.CLIENT, false), parent, offset);
    }

    public void reset() {
        textFrameButton.setText("");
        resetSelected();
    }

    public void resetSelected() {
        textFrameButton.setSelected(false);
    }

    @Override
    public void refresh(int newOff) {
        super.refresh(newOff);
        textFrameButton.setCenterX(parent.getX() + (Constants.CLICKGUI_FRAME_WIDTH / 2));
        textFrameButton.setCenterY(parent.getY() + offset + (Constants.CLICKGUI_BUTTON_HEIGHT / 2));
    }

    @Override
    public String getName() {
        return textFrameButton.getText();
    }

    @Override
    public void renderComponent() {
        textFrameButton.setCenterX(parent.getX() + (Constants.CLICKGUI_FRAME_WIDTH / 2));
        textFrameButton.setCenterY(parent.getY() + offset + (Constants.CLICKGUI_BUTTON_HEIGHT / 2));
        drawNormalBackground();
        if (ModuleList.clickGui.isShaderEnabled()) {
            ModuleList.clickGui.prepareCurrentShader(1, 1);
            BThackRender.drawShaderOutlineRect(ModuleList.clickGui.getCurrentShader(), parent.getX(), parent.getY() + offset, parent.getX() + Constants.CLICKGUI_FRAME_WIDTH, parent.getY() + Constants.CLICKGUI_BUTTON_HEIGHT + offset, 1);
        } else
            BThackRender.drawOutlineRect(parent.getX(), parent.getY() + offset, parent.getX() + Constants.CLICKGUI_FRAME_WIDTH, parent.getY() + Constants.CLICKGUI_BUTTON_HEIGHT + offset, 1, ModuleList.clickGui.color.getValue().hashCode());
        textFrameButton.updateButton(0, 0);
        textFrameButton.renderButton();
    }

    @Override
    public boolean updateComponent(int mouseX, int mouseY) {
        return false;
    }

    @Override
    public int getHeight() {
        return Constants.CLICKGUI_BUTTON_HEIGHT;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        textFrameButton.mouseClicked(mouseX, mouseY, button);
        return false;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    @Override
    public void keyTyped(int key) {
        textFrameButton.keyTyped(key);
        Frame.getGlobalFrames().forEach(Frame::refresh);
    }

    public void charTyped(char _char) {
        textFrameButton.charTyped(_char);
        Frame.getGlobalFrames().forEach(Frame::refresh);
    }

    @Override
    public boolean isMouseOnButton(int x, int y) {
        return false;
    }

    public boolean isMouseOnButton2(int x, int y) {
        return x > ClickGui.applyGuiScale(parent.getX()) && x < ClickGui.applyGuiScale(parent.getX() + Constants.CLICKGUI_FRAME_WIDTH) &&
                y > ClickGui.applyGuiScale(parent.getY() + offset) && y < ClickGui.applyGuiScale(parent.getY() + Constants.CLICKGUI_BUTTON_HEIGHT + offset);
    }

    public String getSearchText() {
        return textFrameButton.getText();
    }
}
