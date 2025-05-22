package com.ferra13671.BThack.gui.Screen.ClickGui.component;

import com.ferra13671.BThack.gui.Screen.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.gui.Screen.ClickGui.component.components.setting.settings.Slider;
import com.ferra13671.BThack.core.Client.Client;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.core.Render.BThackMatrix;
import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.core.Render.Font.FontRenderManager;
import com.ferra13671.BThack.core.Render.Font.FontUtils;
import com.ferra13671.BThack.api.Animation.Animation;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.api.Category.Category;
import com.ferra13671.BThack.api.GuiSystem.BThackScreens;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.Constants;
import com.ferra13671.BThack.api.Utils.Data;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.impl.Modules.Client.ClickGui;
import org.joml.Vector2i;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Frame implements Mc, Closeable {
	private static final List<Frame> GLOBAL_FRAMES = new ArrayList<>();

	public int id;
	public final String name;
	private boolean open = true;
	private final Vector2i position = new Vector2i(0, 0);
	private boolean isDragging = false;
	private final Vector2i dragPosition = new Vector2i(0, 0);
	public final ArrayList<ModuleButton> buttons = new ArrayList<>();
	public final CopyOnWriteArrayList<ModuleButton> visibleButtons = new CopyOnWriteArrayList<>();
	public int height;
	public float renderHeight;
	public boolean buttonHovered = false;
	private final Animation frameAnimation = new Animation(Easing.CUBIC_OUT, 500);

	public final Data<Slider> writingSlider;

	public Frame(String name, List<Module> modules, Data<Slider> writingSlider) {
		GLOBAL_FRAMES.add(this);

		this.writingSlider = writingSlider;

		this.name = name;
		int tY = Constants.CLICKGUI_BAR_HEIGHT;

		for(Module mod : modules) {
			ModuleButton button = new ModuleButton(mod, this, tY);
			buttons.add(button);
			tY += Constants.CLICKGUI_BUTTON_HEIGHT;
		}
	}
	
	public Frame(Category cat, Data<Slider> writingSlider) {
		this(cat.name(), Client.getModulesInCategory(cat), writingSlider);
	}
	
	public ArrayList<ModuleButton> getButtons() {
		return buttons;
	}

	public CopyOnWriteArrayList<ModuleButton> getVisibleButtons() {
		return visibleButtons;
	}

	public void setPosition(int x, int y) {
		position.set(x, y);
	}
	
	public void setDrag(boolean drag) {
		isDragging = drag;
	}
	
	public boolean isOpen() {
		return open;
	}
	
	public void setOpen(boolean open) {
		this.open = open;
	}

	public String getName() {
		return this.name;
	}

	public void updateButtons(int mouseX, int mouseY) {
		for (ModuleButton button : getVisibleButtons())
			button.updateComponent(mouseX, mouseY);
	}

	public void resetHovered() {
		for (ModuleButton button : getButtons())
			button.resetHovered();
		buttonHovered = false;
	}

	public void resetAnimationIgnoreOpen() {
		frameAnimation.reset();
	}

	public void resetAnimation() {
		if (isOpen())
			frameAnimation.reset();
	}

	/**
	 * @return - whether to continue the cycle
	 */
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public boolean updateClick(double mouseX, double mouseY, int mouseButton) {
		if(isWithinHeader((int) mouseX, (int) mouseY) && mouseButton == 0) {
			setDrag(true);
			dragPosition.set((int) (mouseX / ModuleList.clickGui.guiScale.getValue()) - getX(), (int) (mouseY / ModuleList.clickGui.guiScale.getValue()) - getY());
			return false;
		}

		if(isOpen() && !getVisibleButtons().isEmpty())
			for(Component component : getVisibleButtons())
				component.mouseClicked((int) mouseX, (int) mouseY, mouseButton);

		if(isWithinHeader((int) mouseX, (int) mouseY) && mouseButton == 1) {
			setOpen(!isOpen());
			resetAnimationIgnoreOpen();
			if (isOpen()) refresh();
			return false;
		}
        return !isMouseOnFrame((int) mouseX, (int) mouseY);
    }

	public void updateRelease(int mouseX, int mouseY, int mouseButton) {
		if(isOpen() && !getVisibleButtons().isEmpty())
			for(Component component : getVisibleButtons())
				component.mouseReleased(mouseX, mouseY, mouseButton);
	}

	public Module getDescriptionModule(double mouseX, double mouseY) {
		if (isOpen())
			for (ModuleButton button : getVisibleButtons())
				if (button.isMouseOnButton((int) mouseX, (int) mouseY))
					return button.module;
		return null;
	}
	
	public void renderFrame() {
		BThackMatrix.translate(0,0, 1);

		boolean needScissor = frameAnimation.getEase() < 1;
		renderHeight = needScissor ? (float) (height * (open ? frameAnimation.getEase() : 1 - frameAnimation.getEase())) : open ? height : 0;

		if (ModuleList.clickGui.isShaderEnabled()) {
			ModuleList.clickGui.prepareCurrentShader(1, 1);
			BThackRender.drawShader(ModuleList.clickGui.getCurrentShader(), getX(), getY(), getX() + Constants.CLICKGUI_FRAME_WIDTH, getY() + Constants.CLICKGUI_BAR_HEIGHT);
		} else
			BThackRender.drawRect(getX(), getY(), getX() + Constants.CLICKGUI_FRAME_WIDTH, getY() + Constants.CLICKGUI_BAR_HEIGHT, ModuleList.clickGui.color.getValue().hashCode());
		if (ModuleList.clickGui.frameOutline.getValue()) {
			if (ModuleList.clickGui.isShaderEnabled()) {
				ModuleList.clickGui.prepareCurrentShader(1, 1);
				BThackRender.drawShaderOutlineRect(ModuleList.clickGui.getCurrentShader(), getX() - 1, getY() - 1, getX() + Constants.CLICKGUI_FRAME_WIDTH + 1, getY() + renderHeight + Constants.CLICKGUI_BAR_HEIGHT + 1, 1);
			} else
				BThackRender.drawOutlineRect(getX() - 1, getY() - 1, getX() + Constants.CLICKGUI_FRAME_WIDTH + 1, getY() + renderHeight + Constants.CLICKGUI_BAR_HEIGHT + 1, 1, ClickGui.getClickGuiColor(true));
		}

		BThackRender.drawString(name, getX() + (Constants.CLICKGUI_FRAME_WIDTH / 2f) - (FontUtils.getTextWidth(name) / 2f), getY() + (Constants.CLICKGUI_BAR_HEIGHT / 2f) - (FontUtils.getTextHeight(name) / 2f), ModuleList.clickGui.textColor.getValue().hashCode(), true, FontRenderManager.DrawMode.NORMAL_BOLD);

		if((open || frameAnimation.getEase() < 1) && !getVisibleButtons().isEmpty()) {
			if (needScissor)
				BThackRender.enableScissor(ClickGui.applyGuiScale(getX()), ClickGui.applyGuiScale(getY() + Constants.CLICKGUI_BAR_HEIGHT), ClickGui.applyGuiScale(Constants.CLICKGUI_FRAME_WIDTH), (int) ClickGui.applyGuiScale(renderHeight));
			for(Component component : getVisibleButtons())
				component.renderComponent();
			BThackMatrix.translate(0, 0, -1);
			if (needScissor)
				BThackRender.disableScissor();
		}
	}
	
	public void refresh() {
		visibleButtons.clear();
		int off = Constants.CLICKGUI_BAR_HEIGHT;
		String searchText = BThackScreens.CLICK_GUI != null ? BThackScreens.CLICK_GUI.searchModuleButton.getSearchText().toUpperCase() : "";
		for(ModuleButton moduleButton : buttons) {
			if (!searchText.isEmpty())
				if (!moduleButton.getName().toUpperCase().contains(searchText)) continue;
			moduleButton.refresh(off);
			off += moduleButton.getHeight();
			visibleButtons.add(moduleButton);
		}
		height = off - Constants.CLICKGUI_BAR_HEIGHT;
	}

	public void tick() {
		for (ModuleButton button : getVisibleButtons())
			button.tick();
	}

	@Override
	public void close() {
		for (Component comp : getVisibleButtons())
			comp.mouseReleased(0, 0, 0);
	}

	public int getX() {
		return position.x;
	}
	
	public int getY() {
		return position.y;
	}
	
	public void updatePosition(int mouseX, int mouseY) {
		if(isDragging)
			setPosition(mouseX - dragPosition.x, mouseY - dragPosition.y);
	}

	public void moveFrame(int keyCode) {
		int x = position.x;
		int y = position.y;
		switch (keyCode) {
			case KeyboardUtils.KEY_LEFT -> x -= Constants.CLICKGUI_FRAME_MOVE_STEP;
			case KeyboardUtils.KEY_RIGHT -> x += Constants.CLICKGUI_FRAME_MOVE_STEP;
			case KeyboardUtils.KEY_UP -> y -= Constants.CLICKGUI_FRAME_MOVE_STEP;
			case KeyboardUtils.KEY_DOWN -> y += Constants.CLICKGUI_FRAME_MOVE_STEP;
		}
		setPosition(x, y);
	}

	public void moveFrame(double deltaX, double deltaY) {
		setPosition(position.x + (int) (deltaX * 7), position.y + (int) (deltaY * 7));
	}
	
	public boolean isWithinHeader(int mouseX, int mouseY) {
        return mouseX >= ClickGui.applyGuiScale(getX()) && mouseX <= ClickGui.applyGuiScale(getX() + Constants.CLICKGUI_FRAME_WIDTH) &&
				mouseY >= ClickGui.applyGuiScale(getY()) && mouseY <= ClickGui.applyGuiScale(getY() + Constants.CLICKGUI_BAR_HEIGHT);
    }

	public boolean isMouseOnFrame(int mouseX, int mouseY) {
		return mouseX >= ClickGui.applyGuiScale(getX()) && mouseX <= ClickGui.applyGuiScale(getX() + Constants.CLICKGUI_FRAME_WIDTH) &&
				mouseY >= ClickGui.applyGuiScale(getY()) && mouseY <= ClickGui.applyGuiScale(getY() + renderHeight + Constants.CLICKGUI_BAR_HEIGHT);
	}

	public static List<Frame> getGlobalFrames() {
		return new ArrayList<>(GLOBAL_FRAMES);
	}
	
}
