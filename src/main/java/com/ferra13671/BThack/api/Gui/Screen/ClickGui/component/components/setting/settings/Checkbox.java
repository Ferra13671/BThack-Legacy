package com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting.settings;

import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Animation.Animation;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.SoundSystem.SoundSystem;
import com.ferra13671.BThack.api.SoundSystem.Sounds;
import com.ferra13671.BThack.Constants;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;

public class Checkbox extends AbstractSetting<BooleanSetting> {
	protected final Animation animation = new Animation(Easing.LINEAR, 250);
	
	public Checkbox(BooleanSetting setting, ModuleButton button, int offset, Module module) {
		super(offset, button, module, setting);

		animation.reset();
	}

	@Override
	public void renderComponent() {
		super.renderComponent();

		BThackRender.drawRect(getX(), getY(), getX() + Constants.CLICKGUI_FRAME_WIDTH, getY() + getHeight(), hovered ? ClickGui.BACKGROUND_HOVERED_COLOR : ClickGui.BACKGROUND_COLOR);

		if (needRenderPlate()) {
			int alpha = getAlpha();
			if (ModuleList.clickGui.isShaderEnabled()) {
				ModuleList.clickGui.prepareCurrentShader(alpha / 255f, 0.7f);
				BThackRender.drawShader(ModuleList.clickGui.getCurrentShader(), getX() + 1, getY(), getX() + Constants.CLICKGUI_FRAME_WIDTH - 1, getY() + getHeight());
			} else
				BThackRender.drawRect(getX() + 1, getY(), getX() + Constants.CLICKGUI_FRAME_WIDTH - 1, getY() + getHeight(), ColorUtils.integrateAlpha(ClickGui.getClickGuiColor(true), alpha));

			BThackRender.drawOutlineRect(getX() + 1, getY(), getX() + Constants.CLICKGUI_FRAME_WIDTH - 1, getY() + getHeight(), 1, Constants.CLICKGUI_BUTTON_OUTLINE_COLOR);
		}

		BThackRender.drawString(getText(), getX() + 7, getY() + 4, ModuleList.clickGui.textColor.getValue().hashCode());
	}

	protected int getAlpha() {
		return (int) (255 * (setting.getValue() ? animation.getEase() : 1 - animation.getEase()));
	}

	protected boolean needRenderPlate() {
		return setting.getValue() || animation.getEase() < 1;
	}

	protected String getText() {
		return setting.getName();
	}
	
	@Override
	public boolean updateComponent(int mouseX, int mouseY) {
		if (!getVisible()) return true;

		hovered = isMouseOnButton(mouseX, mouseY);
		return true;
	}
	
	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int button) {
		if (!getVisible()) return false;

		if (isMouseOnButton(mouseX, mouseY) && button == 0) {
			setting.setValue(!setting.getValue());
			animation.reset();
			SoundSystem.playSound(setting.getValue() ? Sounds.GUI_CHECKBOX_ENABLE : Sounds.GUI_CHECKBOX_DISABLE);
		}

		return isMouseOnButton(mouseX, mouseY);
	}
}
