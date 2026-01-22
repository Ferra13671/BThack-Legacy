package com.ferra13671.BThack.gui.screen.hudeditor;

import com.ferra13671.BThack.gui.screen.clickgui.component.components.setting.settings.Slider;
import com.ferra13671.BThack.core.client.Client;
import com.ferra13671.BThack.core.client.ModuleList;
import com.ferra13671.BThack.core.client.systems.config.SubConfigs;
import com.ferra13671.BThack.core.render.BThackMatrix;
import com.ferra13671.BThack.core.render.BThackRender;
import com.ferra13671.BThack.core.render.font.FontUtils;
import com.ferra13671.BThack.core.render.utils.BThackRenderUtils;
import com.ferra13671.BThack.core.render.utils.ColorUtils;
import com.ferra13671.BThack.api.category.Categories;
import com.ferra13671.BThack.gui.screen.clickgui.component.Component;
import com.ferra13671.BThack.gui.screen.clickgui.component.Frame;
import com.ferra13671.BThack.gui.screen.clickgui.component.components.ModuleButton;
import com.ferra13671.BThack.api.module.HudComponent;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.shaders.CoreShaders;
import com.ferra13671.BThack.Constants;
import com.ferra13671.BThack.api.utils.Data;
import com.ferra13671.BThack.api.utils.KeyboardUtils;
import com.ferra13671.BThack.core.client.systems.gui.Screen.BThackScreen;
import com.ferra13671.BThack.core.client.systems.gui.BThackScreens;
import com.ferra13671.BThack.api.utils.Ticker;
import com.ferra13671.BThack.impl.modules.client.ClickGui;
import com.google.common.collect.Sets;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.Set;

public class HudEditorScreen extends BThackScreen {

    private final Frame frame;
    public final Data<Slider> writingSlider = new Data<>();
    public final Ticker ticker = new Ticker();

    public HudEditorScreen() {
        super(Text.literal("Hud Mover"));

        frame = new Frame(Categories.HUD, writingSlider);
        frame.setPosition(300, 50);
        ticker.reset();
    }

    private final Set<HudComponentButton> hudComponentButtons = Sets.newHashSet();

    @Override
    public void onDisplayed() {
        frame.resetAnimation();
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
        if (ModuleList.clickGui.blur.getValue())
            ClickGui.renderBlur();

        if (Client.clientInfo.isWinter() && ModuleList.clickGui.snow.getValue()) {
            BThackRenderUtils.applyBlend();
            BThackScreens.CLICK_GUI.snowTicker.update(ModuleList.clickGui.snowSpeed.getValue().floatValue());
            CoreShaders.SNOW.setParameters(mouseX, mouseY, mc.getWindow().getWidth(), mc.getWindow().getHeight(), BThackScreens.CLICK_GUI.snowTicker.getPassedTime() / 1000f);
            BThackRender.drawShader(CoreShaders.SNOW, 0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight());
        }

        BThackRender.draw4ColorRect( 0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), Constants.SCREEN_BACKGROUND_TABLE_COLOR, Constants.SCREEN_BACKGROUND_TABLE_COLOR, ColorUtils.fastRGBA(161,0, 255, 128), ColorUtils.fastRGBA(255, 0, 0, 128));

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

        if (writingSlider.get() != null && writingSlider.get().writing)
            BThackRender.drawString("New Value: " + writingSlider.get().textBuilder, (int) ((mc.getWindow().getScaledWidth() / 2f) - (FontUtils.getTextWidth("New Value: " + writingSlider.get().textBuilder) / 2)), (mc.getWindow().getScaledHeight() - 45), ColorUtils.WHITE);

        for (ModuleButton moduleButton : frame.buttons)
            moduleButton.updateAnim();
        frame.updateButtons(mouseX, mouseY);

        BThackMatrix.push();
        BThackMatrix.scale(ModuleList.clickGui.guiScale.getValue().floatValue(), ModuleList.clickGui.guiScale.getValue().floatValue(), 1);
        frame.renderFrame();
        frame.updatePosition((int) (mouseX / ModuleList.clickGui.guiScale.getValue()), (int) (mouseY / ModuleList.clickGui.guiScale.getValue()));
        BThackMatrix.pop();
    }

    @Override
    public void tick() {
        for (HudComponentButton button : hudComponentButtons)
            button.hudComponent.tick();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (!frame.updateClick(mouseX, mouseY, mouseButton)) return false;

        for (HudComponentButton button : hudComponentButtons)
            if (button.hudComponent.isEnabled())
                button.mouseClicked((int) mouseX, (int) mouseY, mouseButton);

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
        if(frame.isOpen() && keyCode != 1 && !frame.getButtons().isEmpty())
            for(Component component : frame.getButtons())
                component.keyTyped(keyCode);

        switch (keyCode) {
            case KeyboardUtils.KEY_ESCAPE -> mc.setScreen(null);
            case KeyboardUtils.KEY_LEFT, KeyboardUtils.KEY_RIGHT, KeyboardUtils.KEY_UP, KeyboardUtils.KEY_DOWN -> frame.moveFrame(keyCode);
        }

        return super.keyPressed(keyCode, scanCode, shift);
    }

    @Override
    public void removed() {
        SubConfigs.HUD_COMPONENTS.save();
        for (ModuleButton component : frame.buttons) {
            component.open = false;
            component.parent.refresh();
        }
        frame.close();
    }
}
