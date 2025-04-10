package com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting.settings;


import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Shader.Shaders;
import com.ferra13671.BThack.api.SoundSystem.SoundSystem;
import com.ferra13671.BThack.api.SoundSystem.Sounds;
import com.ferra13671.BThack.Constants;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Ticker;
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

	private double renderWidth = -1;
	private final Ticker soundTicker = new Ticker();

	public Slider(NumberSetting option, ModuleButton button, int offset, Module module) {
		super(offset, button, module, option);
		set = option;
		x = button.parent.getX() + Constants.CLICKGUI_FRAME_WIDTH;
		y = button.parent.getY() + button.offset;
	}

	@Override
	public void renderComponent() {
		BThackRender.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + Constants.CLICKGUI_FRAME_WIDTH, parent.parent.getY() + offset + 15, ColorUtils.integrateAlpha(ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().backgroundColor()), (int) (255 * Math.min(1, ModuleList.clickGui.opacity.getValue() + 0.13))));

		//BThackRender.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX(), parent.parent.getY() + offset + 15, new Color(Client.clientInfo.getColorTheme().getBackgroundFontColour()).hashCode());
		BThackRender.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset + 11, parent.parent.getX() + 100 - 2, parent.parent.getY() + offset + 15, Color.GRAY.darker().darker().darker().getRGB());

		if (ModuleList.clickGui.rainbow.getValue()) {
			ModuleList.clickGui.prepareRainbowShader();
			BThackRender.drawShader(Shaders.INSTANCE.X_RAINBOW, parent.parent.getX() + 2, parent.parent.getY() + offset + 11, parent.parent.getX() + 2 + (int) renderWidth, parent.parent.getY() + offset + 15);
		} else
			BThackRender.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset + 11, parent.parent.getX() + 2 + (int) renderWidth, parent.parent.getY() + offset + 15, ClickGui.getClickGuiColor(false));

		BThackRender.drawString(op.getName() + ": " + set.getValue(), parent.parent.getX() + 2, (parent.parent.getY() + offset + 1), ColorUtils.fastRGBA(Client.clientInfo.getColorTheme().moduleDisabledColor()));
	}

	@Override
	public boolean updateComponent(int mouseX, int mouseY) {
		if (!getVisible() || !parent.open) return true;

		y = parent.parent.getY() + offset;
		x = parent.parent.getX();

		double min = set.getMinValue();
		double max = set.getMaxValue();

		double diff = ((mouseX - 1 - ClickGui.applyGuiScale(x)) / (getWidth() * 2)) * 100;
		diff = Math.max(0, Math.min(100, diff));

		double prevRenderWidth = renderWidth;

		renderWidth = (100 - 4) * ((set.getValue() - min) / (max - min));

		if (prevRenderWidth != renderWidth && prevRenderWidth != -1 && soundTicker.passed(50)) {
			SoundSystem.playSound(renderWidth > prevRenderWidth ? Sounds.GUI_SLIDER_UP : Sounds.GUI_SLIDER_DOWN);
			soundTicker.reset();
		}

		if (dragging) {
			if (diff == 0) {
				set.setValue(min);
				set.module.onChangeSetting(set);
			} else {
				set.setValue(roundToPlace(((diff / 100) * (max - min) + min)));
				set.module.onChangeSetting(set);
			}
		}

		return !dragging;
	}

	private float getWidth() {
		return ClickGui.applyGuiScale(50 - 2);
	}

	public static double roundToPlace(double value) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(Constants.CLICKGUI_SLIDER_ROUND_TO_PLACE_VALUE, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int button) {

		if (!getVisible()) {
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
		return x > ClickGui.applyGuiScale(this.x) && x < ClickGui.applyGuiScale(this.x + (Constants.CLICKGUI_FRAME_WIDTH / 2f + 1)) &&
				y > ClickGui.applyGuiScale(this.y) && y < ClickGui.applyGuiScale(this.y + 15);
	}

	private boolean isMouseOnButtonI(int x, int y) {
		return x > ClickGui.applyGuiScale(this.x + Constants.CLICKGUI_FRAME_WIDTH / 2f) && x < ClickGui.applyGuiScale(this.x + Constants.CLICKGUI_FRAME_WIDTH) &&
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
