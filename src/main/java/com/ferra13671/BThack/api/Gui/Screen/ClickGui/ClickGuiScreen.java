package com.ferra13671.BThack.api.Gui.Screen.ClickGui;

import com.ferra13671.BThack.Constants;
import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigSystem;
import com.ferra13671.BThack.Core.Render.BThackMatrix;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Font.FontUtils;
import com.ferra13671.BThack.Core.Render.Utils.BThackRenderUtils;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Category.Categories;
import com.ferra13671.BThack.api.Category.Category;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.Component;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.Frame;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.SearchModuleButton;
import com.ferra13671.BThack.api.Gui.Screen.ClickGui.component.components.setting.settings.Slider;
import com.ferra13671.BThack.api.Gui.Widget.Config.ConfigsWidget;
import com.ferra13671.BThack.api.GuiSystem.buttons.Button;
import com.ferra13671.BThack.api.GuiSystem.buttons.ImageButton;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Shader.ShaderTicker;
import com.ferra13671.BThack.api.Shader.Shaders;
import com.ferra13671.BThack.api.Utils.Data;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.GuiSystem.Screen.BThackScreen;
import com.ferra13671.BThack.api.GuiSystem.buttons.SliderButton;
import com.ferra13671.BThack.api.Utils.Textures;
import com.ferra13671.BThack.api.Utils.Ticker;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class ClickGuiScreen extends BThackScreen implements Mc {

    private final ArrayList<Frame> frames = new ArrayList<>();
    private SliderButton guiScaleSlider;
    private final Data<Slider> writingSlider = new Data<>();
    private final ArrayList<DescriptionBar> descriptions = new ArrayList<>();
    private final Ticker ticker = new Ticker();
    public final ShaderTicker snowTicker = new ShaderTicker();

    public SearchModuleButton searchModuleButton;

    public ClickGuiScreen() {
        super(Text.literal("ClickGui"));
        int tempX = 20;
        int tempY = 20;
        int tempId = 0;
        for (Category category : Categories.getCategories()) {
            Frame frame = new Frame(category, writingSlider);
            if (frame.getName().equals("CLIENT")) {
                searchModuleButton = new SearchModuleButton(frame, 0);
                frame.buttons.addFirst(searchModuleButton);
            }
            frame.id = tempId;
            tempId++;
            frame.setPosition(tempX, tempY);
            frames.add(frame);
            tempX += Constants.CLICKGUI_FRAME_WIDTH + 5;
            frame.refresh();
        }
        ticker.reset();
    }

    @Override
    public void onDisplayed() {
        super.onDisplayed();
        for (Frame frame : frames) frame.resetAnimation();
        snowTicker.reset();
        ModuleList.clickGui.openAction();
    }

    @Override
    public void init() {
        super.init();
        buttons.clear();

        int scWidth = mc.getWindow().getScaledWidth();
        int scHeight = mc.getWindow().getScaledHeight();

        buttons.add(new ImageButton(1, scWidth - 24, scHeight - 34, 20, 30, Textures.CONFIGS)
                .withAction(buttonClickInfo -> widgetManage.addWidget(new ConfigsWidget())));

        guiScaleSlider = new SliderButton(10, scWidth / 2, scHeight - 15, 50, 10, "Gui Scale", ModuleList.clickGui.guiScale.getValue(), 0.5, 1.5);
        buttons.add(guiScaleSlider);
    }

    @Override
    public void render(DrawContext guiGraphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.enableDepthTest();
        if (Module.nullCheck()) drawMainMenuWallpaper(mouseX, mouseY);

        if (ModuleList.clickGui.blur.getValue())
            ClickGui.renderBlur(partialTicks);

        if (Client.clientInfo.isWinter() && ModuleList.clickGui.snow.getValue()) {
            BThackRenderUtils.applyBlend();
            snowTicker.update(ModuleList.clickGui.snowSpeed.getValue().floatValue());
            Shaders.INSTANCE.SNOW.setParameters(mouseX, mouseY, mc.getWindow().getWidth(), mc.getWindow().getHeight(), snowTicker.getPassedTime() / 1000f);
            BThackRender.drawShader(Shaders.INSTANCE.SNOW, 0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight());
        }

        if (ticker.passed(50)) {
            for (Frame frame : frames) frame.tick();
            ticker.reset();
        }
        for (Button button : buttons) {
            if (!button.isHided()) {
                if (widgetManage.widgets.isEmpty())
                    button.updateButton(mouseX, mouseY);

                button.renderButton();
            }
        }

        boolean continueUpdate = true;
        for (Frame frame : frames) {
            for (ModuleButton moduleButton : frame.buttons)
                moduleButton.updateAnim();
            if (continueUpdate && frame.isMouseOnFrame(mouseX, mouseY)) {
                continueUpdate = false;
                frame.updateButtons(mouseX, mouseY);
            } else if (frame.buttonHovered) frame.resetHovered();
        }

        if (writingSlider.get() != null && writingSlider.get().writing)
            BThackRender.drawString("New Value: " + writingSlider.get().textBuilder, (int) ((mc.getWindow().getScaledWidth() / 2f) - (FontUtils.getTextWidth("New Value: " + writingSlider.get().textBuilder) / 2)), (mc.getWindow().getScaledHeight() - 45), ColorUtils.WHITE);

        BThackMatrix.push();
        BThackMatrix.scale(ModuleList.clickGui.guiScale.getValue().floatValue(), ModuleList.clickGui.guiScale.getValue().floatValue(), 1);

        for (int i = frames.size() - 1; i > -1; i--) {
            Frame frame = frames.get(i);
            frame.renderFrame();
            frame.updatePosition((int) (mouseX / ModuleList.clickGui.guiScale.getValue()), (int) (mouseY / ModuleList.clickGui.guiScale.getValue()));
        }

        descriptions.removeIf(DescriptionBar::needRemove);
        descriptions.forEach(descriptionBar -> {
            BThackMatrix.translate(0, 0, 3);
            descriptionBar.render();
        });

        BThackMatrix.pop();
        if (!widgetManage.widgets.isEmpty()) {
            BThackMatrix.push();
            BThackMatrix.translate(0, 0, 200);
            widgetManage.render(guiGraphics, mouseX, mouseY, partialTicks);
            BThackMatrix.pop();
        }
    }

    @Override
    public void tick() {
        super.tick();
        ModuleList.clickGui.guiScale.setValue(guiScaleSlider.value);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
        if (!widgetManage.widgets.isEmpty()) return;
        Module m = null;
        for (Frame frame : frames) {
            Module module = frame.getDescriptionModule(mouseX, mouseY);
            if (module != null) {
                m = module;
                if (descriptions.isEmpty() || descriptions.getFirst().getModule() != module) {
                    if (!descriptions.isEmpty() && !descriptions.getFirst().isClosing()) descriptions.getFirst().close();
                    descriptions.addFirst(new DescriptionBar(module));
                }
            }
        }
        if (m == null && !descriptions.isEmpty() && !descriptions.getFirst().isClosing()) descriptions.getFirst().close();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (widgetManage.widgets.isEmpty()) {
            for (Frame frame : frames) {
                if (!frame.updateClick(mouseX, mouseY, mouseButton)) {
                    Frame temp = frames.getFirst();
                    if (!frame.getName().equals("CLIENT") || !searchModuleButton.isMouseOnButton2((int) mouseX, (int) mouseY))
                        searchModuleButton.resetSelected();
                    frames.set(0, frame);
                    frames.set(frame.id, temp);
                    temp.id = frame.id;
                    frame.id = 0;
                    checkCloseAfterClicking();
                    return false;
                }
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);

        checkCloseAfterClicking();
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        if (!widgetManage.widgets.isEmpty()) return false;
        for (Frame frame : frames)
            frame.moveFrame(horizontalAmount, verticalAmount);
        return false;
    }

    public boolean firstIgnore = true;



    @Override
    public boolean keyPressed(int keyCode, int scanCode, int shift) {
        super.keyPressed(keyCode, scanCode, shift);
        if (!widgetManage.widgets.isEmpty()) return false;

        for(Frame frame : frames) {
            if(frame.isOpen() && keyCode != 1) {
                if(!frame.getVisibleButtons().isEmpty()) {
                    for(Component component : frame.getVisibleButtons()) {
                        component.keyTyped(keyCode);
                    }
                }
            }
        }

        if (keyCode == ModuleList.clickGui.getKey() || keyCode == KeyboardUtils.KEY_ESCAPE) {
            if (!firstIgnore) {
                ConfigSystem.saveConfigThreaded();
                mc.setScreen(null);

                return true;
            } else {
                firstIgnore = false;
            }
        }

        switch (keyCode) {
            case KeyboardUtils.KEY_LEFT, KeyboardUtils.KEY_RIGHT, KeyboardUtils.KEY_UP, KeyboardUtils.KEY_DOWN -> {
                for(Frame frame : frames) {
                    frame.moveFrame(keyCode);
                }
            }
        }

        return false;
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (widgetManage.widgets.isEmpty())
            searchModuleButton.charTyped(chr);
        else super.charTyped(chr, modifiers);
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (!widgetManage.widgets.isEmpty()) return false;

        for(Frame frame : frames) {
            frame.setDrag(false);
            frame.updateRelease((int) mouseX, (int) mouseY, state);
        }

        return false;
    }

    @Override
    public void removed() {
        super.removed();
        searchModuleButton.reset();
        for (Frame frame : frames) {
            for (ModuleButton component : frame.buttons) {
                component.open = false;
                component.parent.refresh();
            }
            frame.close();
        }
    }

    @Override
    public boolean shouldPause() {
        return ModuleList.clickGui.shouldPause.getValue();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
