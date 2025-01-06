package com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.settings;


import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;
import com.google.common.collect.Sets;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

public class Slider extends AbstractSetting implements Mc {

	private final NumberSetting set;

	private boolean dragging = false;

	public boolean writing = false;
	public StringBuilder textBuilder = new StringBuilder();

	private final Set<Integer> keys = Sets.newHashSet(
			KeyboardUtils.KEY_0,
			KeyboardUtils.KEY_1,
			KeyboardUtils.KEY_2,
			KeyboardUtils.KEY_3,
			KeyboardUtils.KEY_4,
			KeyboardUtils.KEY_5,
			KeyboardUtils.KEY_6,
			KeyboardUtils.KEY_7,
			KeyboardUtils.KEY_8,
			KeyboardUtils.KEY_9
	);

	private double renderWidth;

	public Slider(NumberSetting option, ModuleButton button, int offset, Module module) {
		super(offset, button, module, option);
		set = option;
		x = button.parent.getX() + button.parent.getWidth();
		y = button.parent.getY() + button.offset;
	}

	@Override
	public void renderComponent() {
		BThackRender.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + 15, ColorUtils.integrateAlpha(new Color(Client.clientInfo.getColorTheme().getBackgroundFontColour()).hashCode(), (int) (255 * Math.min(1, ClickGui.opacity.getValue() + 0.13))));

		//BThackRender.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX(), parent.parent.getY() + offset + 15, new Color(Client.clientInfo.getColorTheme().getBackgroundFontColour()).hashCode());
		BThackRender.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset + 11, parent.parent.getX() + 100 - 2, parent.parent.getY() + offset + 15, Color.GRAY.darker().darker().darker().getRGB());

		if (ClickGui.rainbow.getValue()) {
			int type = (int) ClickGui.rainbowSpeed.getValue();
			BThackRender.drawHorizontalRainbowRect(parent.parent.getX() + 2, parent.parent.getY() + offset + 11, parent.parent.getX() + 2 + (int) renderWidth, parent.parent.getY() + offset + 15, type);
		} else {
			BThackRender.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset + 11, parent.parent.getX() + 2 + (int) renderWidth, parent.parent.getY() + offset + 15, ClickGui.getClickGuiColor(false));
		}

		BThackRender.drawString(op.getName() + ": " + set.getValue(), parent.parent.getX() + 2, (parent.parent.getY() + offset + 1), Client.clientInfo.getColorTheme().getModuleDisabledColour());


		/*
		if (writing) {
			int scaleWidth = mc.getWindow().getScaledWidth();

			BThackRender.guiGraphics.getMatrices().pop();
			BThackRender.drawString("New Value: " + textBuilder, (int) ((scaleWidth / 2f) - (mc.textRenderer.getWidth("New Value: " + textBuilder) / 2)), (mc.getWindow().getScaledHeight() - 45), Color.white.hashCode());
			BThackRender.guiGraphics.getMatrices().push();
			BThackRender.guiGraphics.getMatrices().scale((float) ClickGui.guiScale.getValue(), (float) ClickGui.guiScale.getValue(), 1);
		}

		 */
	}

	@Override
	public boolean updateComponent(int mouseX, int mouseY) {
		if (!getVisible() || !parent.open) return true;

		y = parent.parent.getY() + offset;
		x = parent.parent.getX();

		//double diff = Math.min(ClickGui.applyGuiScale(100), Math.max(0, mouseX - ClickGui.applyGuiScale(this.x)));

		double min = set.getMinValue();
		double max = set.getMaxValue();

		//renderWidth = (100) * (set.getValue() - min) / (max - min);

		//if (dragging) {
		//	if (diff == 0) {
		//		set.setValue(set.getMinValue());
		//	} else {
		//		double newValue = roundToPlace(((diff / 100) * (max - min) + min), 2);
		//		set.setValue(newValue);
		//	}
		//}



		double diff = ((mouseX - 1 - ClickGui.applyGuiScale(x)) / (getWidth() * 2)) * 100;
		diff = Math.max(0, Math.min(100, diff));

		renderWidth = (100 - 4) * ((set.getValue() - min) / (max - min));

		if (dragging) {
			if (diff == 0) {
				set.setValue(min);
				set.module.onChangeSetting(set);
			} else {
				set.setValue(roundToPlace(((diff / 100) * (max - min) + min), 2));
				set.module.onChangeSetting(set);
			}
		}

		return !dragging;
	}

	private float getWidth() {
		return ClickGui.applyGuiScale(50 - 2);
	}

	private static double roundToPlace(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int button) {

		if (!getVisible() || !parent.open) {
			writing = false;
			return false;
		}

		if (button != 2) {
			if (isMouseOnButton(mouseX, mouseY)) {
				dragging = true;
				writing = false;
			}
		} else {
			if (isMouseOnButton(mouseX, mouseY)) {
				writing = !writing;
				if (writing)
					parent.parent.writingSlider.set(this);
			}
		}

		return isMouseOnButton(mouseX, mouseY);
	}

	@Override
	public void keyTyped(int key) {
		if (!writing) return;

		if (key == KeyboardUtils.KEY_MINUS) {
			if (textBuilder.isEmpty())
				textBuilder.append((char) key);
		}

		if (keys.contains(key) || (char) key == '.') {
			textBuilder.append((char) key);
		}
		if (key == KeyboardUtils.KEY_BACKSPACE) {
			if (!textBuilder.isEmpty())
				textBuilder.deleteCharAt(textBuilder.length() - 1);
		}

		if (key == KeyboardUtils.KEY_ENTER) {
			double number = getNumber();
			textBuilder = new StringBuilder();

			writing = false;

			if (number < set.getMinValue())
				number = set.getMinValue();
			if (number > set.getMaxValue())
				number = set.getMaxValue();

			if (set.onlyInt) {
				set.setValue((int) number);
			} else
				set.setValue(number);
		}

		if (key == KeyboardUtils.KEY_ESCAPE || key == ModuleList.clickGui.getKey()) {
			writing = false;
			textBuilder = new StringBuilder();
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		dragging = false;
	}

	@Override
	public boolean isMouseOnButton(int x, int y) {
		return isMouseOnButtonD(x, y) || isMouseOnButtonI(x, y);
	}

	private boolean isMouseOnButtonD(int x, int y) {
		return x > ClickGui.applyGuiScale(this.x) && x < ClickGui.applyGuiScale(this.x + (parent.parent.getWidth() / 2f + 1)) &&
				y > ClickGui.applyGuiScale(this.y) && y < ClickGui.applyGuiScale(this.y + 15);
	}

	private boolean isMouseOnButtonI(int x, int y) {
		return x > ClickGui.applyGuiScale(this.x + parent.parent.getWidth() / 2f) && x < ClickGui.applyGuiScale(this.x + parent.parent.getWidth()) &&
				y > ClickGui.applyGuiScale(this.y) && y < ClickGui.applyGuiScale(this.y + 15);
	}

	public double getNumber() {
		if (!textBuilder.isEmpty()) {
			if (textBuilder.charAt(textBuilder.length() - 1) == '.') {
				textBuilder.append("0");
			}
		}
		if (textBuilder.isEmpty()) {
			return 0;
		}

		return Double.parseDouble(textBuilder.toString());
	}
}
