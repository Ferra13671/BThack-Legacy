package com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting.settings;


import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackMatrix;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.SoundSystem.SoundSystem;
import com.ferra13671.BThack.api.SoundSystem.Sounds;
import com.ferra13671.BThack.Constants;

public class ModeButton extends AbstractSetting<ModeSetting> {

	public ModeButton(ModeSetting setting, ModuleButton button, int offset, int modeIndex, Module module) {
		super(offset, button, module, setting);
		x = button.parent.getX() + Constants.CLICKGUI_FRAME_WIDTH;
		y = button.parent.getY() + button.offset;

		setting.setValue(setting.getOptions().get(modeIndex));
	}
	
	@Override
	public void renderComponent() {
		BThackRender.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + Constants.CLICKGUI_FRAME_WIDTH, parent.parent.getY() + offset + 15, this.hovered ? ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().backgroundHoveredColor()), (int) (255 * Math.min(1, ModuleList.clickGui.opacity.getValue() + 0.13))) : ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().backgroundColor()), (int) (255 * Math.min(1, ModuleList.clickGui.opacity.getValue() + 0.13))));

		String text = getModeString();
		float scale = getTextScale(text);

		if (scale != 1) {
			BThackMatrix.push();
			BThackMatrix.scale(scale, scale, 1);
		}
		BThackRender.drawString(text, (parent.parent.getX() + 2) / scale, (parent.parent.getY() + offset + 4) / scale, ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().moduleDisabledColor()));
		if (scale != 1)
			BThackMatrix.pop();
	}

	private float getTextScale(String text) {
		if (text.length() > 17) return 0.9f;
		else return 1;
	}

	private String getModeString() {
		return this.setting.getName() + ": " + (!setting.getOptions().contains(setting.getValue()) ? "NULL" : (setting.getOptions().size() < setting.getIndex() ? setting.getValue() : setting.getOptions().get(setting.getIndex())));
	}

	@Override
	public boolean updateComponent(int mouseX, int mouseY) {
		if (!getVisible() || !parent.open) return true;

		hovered = isMouseOnButton(mouseX, mouseY);
		y = parent.parent.getY() + offset;
		x = parent.parent.getX();

		return true;
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int button) {
		if (!getVisible()) return false;

		if (isMouseOnButton(mouseX, mouseY) && button == 0) {
			int maxIndex = setting.getOptions().size();

			if (setting.getIndex() + 1 >= maxIndex) {
				setting.setIndex(0);
			} else {
				int currentIndex = setting.getIndex();
				setting.setIndex(currentIndex + 1);
			}

			setting.setValue(setting.getOptions().get(setting.getIndex()));
			setting.module.onChangeSetting(setting);
			SoundSystem.playSound(Sounds.GUI_CHECKBOX_ENABLE);
		}

		return isMouseOnButton(mouseX, mouseY);
	}
}
