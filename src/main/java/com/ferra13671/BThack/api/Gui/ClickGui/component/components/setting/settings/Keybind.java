package com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.settings;


import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class Keybind extends AbstractSetting {

	private boolean binding;
	
	public Keybind(ModuleButton button, int offset) {
		super(offset, button, null, null);


		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
	}
	
	@Override
	public void renderComponent() {
		BThackRender.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + 15, this.hovered ? ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().backgroundFontHoveredColor()), (int) (255 * Math.min(1, ModuleList.clickGui.opacity.getValue() + 0.13))) : ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().backgroundFontColor()), (int) (255 * Math.min(1, ModuleList.clickGui.opacity.getValue() + 0.13))));

		BThackRender.drawString(binding ? "< PRESS KEY >" : ("Key: " + KeyboardUtils.getKeyName(this.parent.module.getKey())), parent.parent.getX() + 2, parent.parent.getY() + offset + 4, Client.clientInfo.getColorTheme().moduleDisabledColor());
	}

	@Override
	public void updateDependencies(int offset) {
		//no action
	}
	
	@Override
	public boolean updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButton(mouseX, mouseY);
		this.y = parent.parent.getY() + offset;
		this.x = parent.parent.getX();

		return true;
	}
	
	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
			this.binding = !this.binding;
		}

		return isMouseOnButton(mouseX, mouseY);
	}
	
	@Override
	public void keyTyped(int key) {
		if (this.binding) {
			if (key == KeyboardUtils.KEY_DELETE) {
				this.parent.module.setKey(0);
				this.binding = false;
			} else if (key != KeyboardUtils.KEY_ESCAPE) {
				this.parent.module.setKey(key);
				this.binding = false;
			}
		}
	}
}
