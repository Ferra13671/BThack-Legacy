package com.ferra13671.BThack.api.Gui.ClickGui.component.components;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Animation.Animation;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.api.Gui.ClickGui.component.Component;
import com.ferra13671.BThack.api.Gui.ClickGui.component.Frame;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.settings.*;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.settings.Checkbox;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.Setting.Settings.*;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;

import java.awt.*;
import java.util.ArrayList;

public class ModuleButton extends Component implements Mc {
	public static final int BUTTON_HEIGHT = 12;

	public Module module;
	public Frame parent;
	public int offset;

	public boolean open = false;
	public boolean renderOpen = false;

	private boolean isHovered;
	private final ArrayList<AbstractSetting> settings = new ArrayList<>();
	private float alphaDelta = 1;
	private boolean alphaDeltaInverse = true;
	private Animation settingAnimation = new Animation(Easing.CIRC_OUT, 500);
	private final Animation toggleAnimation = new Animation(Easing.LINEAR, 250);
	private double lastAnimFactor = 0;
	private int animatedSettingsHeight = 0;

	public ModuleButton(Module module, Frame parent, int offset) {
		this.module = module;
		this.parent = parent;
		this.offset = offset;
		int opY = offset + BUTTON_HEIGHT;
		AbstractSetting setting;
		if(Managers.SETTINGS_MANAGER.getSettingsByMod(module) != null) {
			for(Setting s : Managers.SETTINGS_MANAGER.getSettingsByMod(module)){
				setting = s instanceof ModeSetting set ? new ModeButton(set, this, opY, set.getIndex(), module) :
					    	s instanceof NumberSetting set ? new Slider(set, this, opY, module) :
								  s instanceof BooleanSetting set ? new Checkbox(set, this, opY, module) :
										s instanceof KeyCodeSetting set ? new KeyCode(this, opY, set, module) :
												s instanceof GuiButtonSetting set ? new OpenGuiButton(set, this, opY, module) :
														s instanceof ColorSetting set ? new ColorPicker(set, this, opY, module) :
																null;
				settings.add(setting);
				opY += setting.getHeight();
			}
		}

		int h = 0;
		if (module.allowRemapVisible) {
			setting = new Visible(this, opY, module);
			h = setting.getHeight();
			settings.add(setting);
		}
		if (module.allowRemapKeyCode) {
			if (module.allowRemapVisible)
				opY += h;
			settings.add(new Keybind(this, opY));
		}
	}

	@Override
	public void setOff(int newOff) {
		setOffInternal(newOff);
	}

	@Override
	public void updateDependencies(int offset) {
		for (AbstractSetting comp : settings) {
			comp.updateDependencies(0);
		}

		setOffInternal(offset);
	}

	private void setOffInternal(int offset) {
		this.offset = offset;
		int opY = offset + BUTTON_HEIGHT;
		for(AbstractSetting comp : settings) {
			comp.setOff(opY);
			if (comp.getVisible())
				opY += comp.getHeight();
		}
	}

	@Override
	public void renderComponent() {
		if (settingAnimation.getPassedMillis() <= settingAnimation.getMillis() + 50) {
			lastAnimFactor = settingAnimation.getEase();
			parent.refresh();
		} else if (lastAnimFactor != 1) {
			lastAnimFactor = 1;
			parent.refresh();
		}

		int alpha = (int) (255 * ModuleList.clickGui.opacity.getValue());
		if (!module.isEnabled() || toggleAnimation.getEase() < 1) drawNormalBackground(alpha);
		if (module.isEnabled() || toggleAnimation.getEase() < 1) drawEnabledBackground(alpha);

		BThackRender.drawString(module.getName(), (parent.getX() + 5), (parent.getY() + offset + 2), getModuleTextColor());

		if (!settings.isEmpty()) {
			BThackRender.drawString(open ? "-" : "+", (parent.getX() + parent.getWidth() - 10), (parent.getY() + offset + 2), new Color(Client.clientInfo.getColorTheme().moduleDisabledColor()).hashCode());
		}
		if(renderOpen || open) {
			if(!settings.isEmpty()) {
				BThackRender.enableScissor(ClickGui.applyGuiScale(parent.getX()), ClickGui.applyGuiScale(parent.getY() + offset), ClickGui.applyGuiScale(parent.getWidth()), ClickGui.applyGuiScale(animatedSettingsHeight + BUTTON_HEIGHT));
				//BThackRender.guiGraphics.getMatrices().translate(0, 0, -1);
				for(AbstractSetting set : settings) {
					if (set.getVisible()) {
						set.renderComponent();
					}
				}
				//BThackRender.guiGraphics.getMatrices().translate(0, 0, 1);
				BThackRender.disableScissor();
				if (ModuleList.clickGui.settingsOutline.getValue()) {
					//BThackRender.guiGraphics.getMatrices().translate(0, 0, 4);
					BThackRender.drawOutlineRect(parent.getX(), parent.getY() + offset, parent.getX() + parent.getWidth(), parent.getY() + offset + animatedSettingsHeight + BUTTON_HEIGHT, 1, ColorUtils.fastRGBA(255, 255, 255, Math.max(1, (int) (alphaDelta * 255))));
					//BThackRender.guiGraphics.getMatrices().translate(0, 0, -4);
				}
			}
		}
	}

	private int getModuleTextColor() {
		if (ModuleList.clickGui.opacity.getValue() > 0.4) {
			return ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().moduleDisabledColor());
		} else
			return module.isEnabled() ? ClickGui.getClickGuiColor(true) : ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().moduleDisabledColor());
	}

	private void drawEnabledBackground(int alpha) {
		alpha = (int) (alpha * (module.isEnabled() ? toggleAnimation.getEase() : 1 - toggleAnimation.getEase()));
		BThackRender.drawRect(parent.getX(), parent.getY() + offset, parent.getX() + parent.getWidth(), parent.getY() + BUTTON_HEIGHT + offset,
				isHovered ?
						ColorUtils.integrateAlpha(
								new Color(ClickGui.getClickGuiColor(true)).darker().hashCode()
								, alpha
						)
						:
						ColorUtils.integrateAlpha(
								new Color(ClickGui.getClickGuiColor(true)).darker().darker().hashCode()
								, alpha
						)
		);
		if (ModuleList.clickGui.moduleOutline.getValue()) {
			BThackRender.drawOutlineRect(parent.getX(), parent.getY() + offset, parent.getX() + parent.getWidth(), parent.getY() + BUTTON_HEIGHT + offset, 1,
					isHovered ?
							ColorUtils.integrateAlpha(
									new Color(ClickGui.getClickGuiColor(true)).darker().darker().hashCode()
									, alpha
							)
							:
							ColorUtils.integrateAlpha(
									new Color(ClickGui.getClickGuiColor(true)).darker().darker().darker().hashCode()
									, alpha
							)
			);
		}
	}

	private void drawNormalBackground(int alpha) {
		if (isHovered)
			BThackRender.drawRect(parent.getX(), parent.getY() + offset, parent.getX() + parent.getWidth(), parent.getY() + BUTTON_HEIGHT + offset,
					ColorUtils.integrateAlpha(
							new Color(Client.clientInfo.getColorTheme().backgroundFontHoveredColor()).brighter().brighter().hashCode()
							, alpha
					)
			);
		/*
		BThackRender.drawRect(parent.getX(), parent.getY() + offset, parent.getX() + parent.getWidth(), parent.getY() + BUTTON_HEIGHT + offset,
				isHovered ?
						ColorUtils.integrateAlpha(
								ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().backgroundFontHoveredColor())
								, alpha
						)
						:
						ColorUtils.integrateAlpha(
								ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().backgroundFontColor())
								, alpha
						)
		);
		 */
		if (ModuleList.clickGui.moduleOutline.getValue()) {
			BThackRender.drawOutlineRect(parent.getX(), parent.getY() + offset, parent.getX() + parent.getWidth(), parent.getY() + BUTTON_HEIGHT + offset, 1,
					isHovered ?
							ColorUtils.integrateAlpha(
									new Color(Client.clientInfo.getColorTheme().backgroundFontHoveredColor()).darker().hashCode()
									, alpha
							)
							:
							ColorUtils.integrateAlpha(
									new Color(Client.clientInfo.getColorTheme().backgroundFontColor()).darker().hashCode()
									, alpha
							)
			);
		}
	}



	@Override
	public int getHeight() {
		if(renderOpen || open) {
			int height = 0;
			for (AbstractSetting component : settings) {
				if (component.getVisible()) {
					height += component.getHeight();
				}
			}
			height = open ? (int) (lastAnimFactor * height) : (int) (height - (lastAnimFactor * height));
			animatedSettingsHeight = height;
			height += BUTTON_HEIGHT;
			return height;
		}
		return BUTTON_HEIGHT;
	}

	@Override
	public boolean updateComponent(int mouseX, int mouseY) {
		if (isMouseOnButton(mouseX, mouseY) && !isHovered) {
			if (parent.buttonHovered) parent.resetHovered();
			isHovered = true;
			parent.buttonHovered = true;
		}
		if(!settings.isEmpty()) {
			for(Component comp : settings) {
				comp.updateComponent(mouseX, mouseY);
			}
		}
		return false;
	}

	public void tick() {
		if (settingAnimation.getPassedMillis() > settingAnimation.getMillis() + 50) if (renderOpen != open) renderOpen = open;
		if (renderOpen) {
			if (alphaDelta > 1) alphaDeltaInverse = true;
			if (alphaDelta <= 0.3) alphaDeltaInverse = false;
			alphaDelta += alphaDeltaInverse ? -0.03f : 0.03f;
		}
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int button) {
		if (isMouseOnButton(mouseX, mouseY)) {
			if (button == 0) {
				module.toggle();
				toggleAnimation.reset();
			}
			if (button == 1) {
				if (renderOpen == open) {
					settingAnimation = new Animation(ClickGui.getCurrentEasing(), (int) ModuleList.clickGui.animationTime.getValue());
					open = !open;
					settingAnimation.reset();
					parent.refresh();
				}
			}
		}
		for(Component comp : settings) {
			comp.mouseClicked(mouseX, mouseY, button);
		}
		if (open)
			parent.updateDependencies();

		return isMouseOnButton(mouseX, mouseY);
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		for(Component comp : settings) {
			comp.mouseReleased(mouseX, mouseY, mouseButton);
		}
		if (open)
			parent.updateDependencies();
	}

	@Override
	public void keyTyped(int key) {
		for(Component comp : this.settings) {
			comp.keyTyped(key);
		}
	}

	public boolean isMouseOnButton(int x, int y) {
        return x > ClickGui.applyGuiScale(parent.getX()) && x < ClickGui.applyGuiScale(parent.getX() + parent.getWidth()) &&
				y > ClickGui.applyGuiScale(parent.getY() + offset) && y < ClickGui.applyGuiScale(parent.getY() + BUTTON_HEIGHT + offset);
    }

	public void resetHovered() {
		isHovered = false;
	}
}
