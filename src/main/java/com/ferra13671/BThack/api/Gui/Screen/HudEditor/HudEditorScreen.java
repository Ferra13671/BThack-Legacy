package com.ferra13671.BThack.api.Gui.Screen.HudEditor;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigSystem;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Font.FontUtils;
import com.ferra13671.BThack.Core.Render.Utils.BThackRenderUtils;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Category.Categories;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.Component;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.Frame;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting.settings.Slider;
import com.ferra13671.BThack.api.Gui.Screen.HudEditor.Utils.HudComponentButton;
import com.ferra13671.BThack.api.Module.HudComponent;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Shader.Shaders;
import com.ferra13671.BThack.api.Utils.Data;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.GuiSystem.Screen.BThackScreen;
import com.ferra13671.BThack.api.GuiSystem.BThackScreens;
import com.ferra13671.BThack.api.Utils.Ticker;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;
import com.google.common.collect.Sets;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.io.IOException;
import java.util.Set;

public class HudEditorScreen extends BThackScreen {

    private final Frame frame;
    public final Data<Slider> writingSlider = new Data<>();
    public final Ticker ticker = new Ticker();

    public HudEditorScreen() {
        super(Text.literal("Hud Mover"));

        frame = new Frame(Categories.HUD, writingSlider);
        frame.setX(300);
        frame.setY(50);
        ticker.reset();
    }

    private final Set<HudComponentButton> hudComponentButtons = Sets.newHashSet();

    @Override
    public void onDisplayed() {
        ModuleList.clickGui.updateColorTheme();
        frame.resetFrameAnimation();
        BThackScreens.CLICK_GUI.snowTicker.reset();
    }

    @Override
    public void init() {
        super.init();
        for (Module module : Client.getModulesInCategory(Categories.HUD))
            hudComponentButtons.add(new HudComponentButton(0, (HudComponent) module));
    }

    @Override
    public void render(DrawContext guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (ModuleList.clickGui.blur.getValue()) {
            ClickGui.renderBlur(partialTicks);
        }

        if (Client.clientInfo.isWinter() && ModuleList.clickGui.snow.getValue()) {
            BThackRenderUtils.applyBlend();
            BThackScreens.CLICK_GUI.snowTicker.update((float) ModuleList.clickGui.snowSpeed.getValue());
            Shaders.INSTANCE.SNOW.setParameters(mouseX, mouseY, mc.getWindow().getWidth(), mc.getWindow().getHeight(), BThackScreens.CLICK_GUI.snowTicker.getPassedTime() / 1000f);
            BThackRender.drawShader(Shaders.INSTANCE.SNOW, 0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight());
        }

        BThackRender.draw4ColorRect( 0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), BACKGROUND_TABLE_COLOR, BACKGROUND_TABLE_COLOR, ColorUtils.fastRGBA(161,0, 255, 128), ColorUtils.fastRGBA(255, 0, 0, 128));

        if (ticker.passed(50)) {
            frame.tick();
            ticker.reset();
        }

        for (HudComponentButton button : hudComponentButtons) {
            if (button.hudComponent.isEnabled()) {
                button.updateButton(mouseX, mouseY);
                button.renderButton();
            }
        }

        if (writingSlider.get() != null) {
            if (writingSlider.get().writing) {
                BThackRender.drawString("New Value: " + writingSlider.get().textBuilder, (int) ((mc.getWindow().getScaledWidth() / 2f) - (FontUtils.getTextWidth("New Value: " + writingSlider.get().textBuilder) / 2)), (mc.getWindow().getScaledHeight() - 45), ColorUtils.WHITE);
            }
        }

        frame.updateButtons(mouseX, mouseY);

        BThackRender.guiGraphics.getMatrices().push();
        BThackRender.guiGraphics.getMatrices().scale((float) ModuleList.clickGui.guiScale.getValue(), (float) ModuleList.clickGui.guiScale.getValue(), 1);
        frame.renderFrame();
        frame.updatePosition((int) (mouseX / ModuleList.clickGui.guiScale.getValue()), (int) (mouseY / ModuleList.clickGui.guiScale.getValue()));
        BThackRender.guiGraphics.getMatrices().pop();
    }

    @Override
    public void tick() {
        for (HudComponentButton button : hudComponentButtons) {
            button.hudComponent.tick();
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (!frame.updateClick(mouseX, mouseY, mouseButton)) return false;

        for (HudComponentButton button : hudComponentButtons) {
            if (button.hudComponent.isEnabled())
                button.mouseClicked((int) mouseX, (int) mouseY, mouseButton);
        }
        checkCloseAfterClicking();
        return super.mouseClicked(mouseX,mouseY,mouseButton);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        frame.moveFrame(horizontalAmount, verticalAmount);
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        frame.setDrag(false);
        frame.updateRelease((int) mouseX, (int) mouseY, mouseButton);

        hudComponentButtons.forEach(button -> button.mouseReleased((int) mouseX, (int) mouseY, mouseButton));

        return super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int shift) {
        if(frame.isOpen() && keyCode != 1) {
            if(!frame.getButtons().isEmpty()) {
                for(Component component : frame.getButtons()) {
                    component.keyTyped(keyCode);
                }
            }
        }

        switch (keyCode) {
            case KeyboardUtils.KEY_ESCAPE -> mc.setScreen(null);
            case KeyboardUtils.KEY_LEFT, KeyboardUtils.KEY_RIGHT, KeyboardUtils.KEY_UP, KeyboardUtils.KEY_DOWN -> frame.moveFrame(keyCode);
        }

        return super.keyPressed(keyCode, scanCode, shift);
    }

    @Override
    public void removed() {
        try {
            ConfigSystem.saveHudComponents();
        } catch (IOException ignored) {}
        for (ModuleButton component : frame.buttons) {
            component.open = false;
            component.parent.refresh();
        }
        frame.close();
    }
}
