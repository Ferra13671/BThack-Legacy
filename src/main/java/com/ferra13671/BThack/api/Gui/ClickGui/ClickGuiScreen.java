package com.ferra13671.BThack.api.Gui.ClickGui;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigSystem;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Font.FontUtils;
import com.ferra13671.BThack.Core.Render.Utils.BThackRenderUtils;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Animation.Animation;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.api.Category.Categories;
import com.ferra13671.BThack.api.Category.Category;
import com.ferra13671.BThack.api.Gui.ClickGui.component.Component;
import com.ferra13671.BThack.api.Gui.ClickGui.component.Frame;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.settings.Slider;
import com.ferra13671.BThack.api.Gui.Config.LoadConfigScreen;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Shader.ShaderTicker;
import com.ferra13671.BThack.api.Shader.Shaders;
import com.ferra13671.BThack.api.Utils.Data;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.System.BThackScreen;
import com.ferra13671.BThack.api.Utils.System.buttons.Button;
import com.ferra13671.BThack.api.Utils.System.buttons.SliderButton;
import com.ferra13671.BThack.api.Utils.System.buttons.TextFrameButton;
import com.ferra13671.BThack.api.Utils.Ticker;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.io.IOException;
import java.util.ArrayList;

public class ClickGuiScreen extends BThackScreen implements Mc {
    public static int descriptionY;

    private final ArrayList<Frame> frames = new ArrayList<>();
    private boolean startSaving = false;
    private SliderButton guiScaleSlider;
    private final Data<Module> descriptionModule = new Data<>();
    private final Animation descriptionAnimation = new Animation(Easing.LINEAR, 500);
    private final Data<Slider> writingSlider = new Data<>();
    private final Ticker ticker = new Ticker();
    private final ShaderTicker snowTicker = new ShaderTicker();

    public ClickGuiScreen() {
        super(Text.of("ClickGui"));
        int tempX = 0;
        int tempY = 0;
        int tempId = 0;
        for (Category category : Categories.getCategories()) {
            Frame frame = new Frame(category, writingSlider);
            frame.id = tempId;
            tempId++;
            frame.setY(tempY);
            frame.setX(tempX);
            frames.add(frame);
            tempX += 100;
            frame.refresh();
            frame.updateDependencies();
        }
        ticker.reset();
    }

    @Override
    public void onDisplayed() {
        ModuleList.clickGui.updateColorTheme();
        for (Frame frame : frames) frame.resetFrameAnimation();
        snowTicker.reset();
    }

    @Override
    public void init() {
        buttons.clear();

        int scWidth = mc.getWindow().getScaledWidth();
        int scHeight = mc.getWindow().getScaledHeight();

        buttons.add(Button.of(0,
                scWidth - 50, scHeight - 15, 40, 10, "Load Config")
                .withAction(buttonClickInfo -> {
                    startSaving = false;
                    mc.setScreen(new LoadConfigScreen());
                }));
        buttons.add(Button.of(1,
                scWidth - 50, scHeight - 40, 40, 10, "Save Config")
                .withAction(buttonClickInfo -> {
                    startSaving = true;
                }));
        buttons.add(new TextFrameButton(8,
                scWidth - 70, scHeight - 65, 60, 10));
        buttons.add(Button.of(9
                , scWidth - 150, scHeight - 40, 40, 10, "Confirm")
                .withAction(buttonClickInfo -> {
                    TextFrameButton button = (TextFrameButton) getButtonFromId(8);

                    try {
                        ConfigSystem.saveConfigFile(button.getText());
                    } catch (IOException ignored) {}
                    startSaving = false;
                    button.setText("");
                }));

        guiScaleSlider = new SliderButton(10, scWidth / 2, scHeight - 15, 50, 10, "Gui Scale", ModuleList.clickGui.guiScale.getValue(), 0.5, 1.5);
        buttons.add(guiScaleSlider);

        getButtonFromId(8).setHided(!startSaving);
        getButtonFromId(9).setHided(!startSaving);
    }

    @Override
    public void render(DrawContext guiGraphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.enableDepthTest();
        if (Module.nullCheck()) drawMainMenuWallpaper(mouseX, mouseY);

        if (ModuleList.clickGui.blur.getValue()) {
            ClickGui.renderBlur(partialTicks);
        }

        if (Client.clientInfo.isWinter() && ModuleList.clickGui.snow.getValue()) {
            BThackRenderUtils.applyBlend();
            snowTicker.update((float) ModuleList.clickGui.snowSpeed.getValue());
            Shaders.INSTANCE.SNOW.setParameters(mouseX, mouseY, mc.getWindow().getWidth(), mc.getWindow().getHeight(), snowTicker.getPassedTime() / 1000f);
            BThackRender.drawShader(Shaders.INSTANCE.SNOW, 0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight());
        }

        if (ticker.passed(50)) {
            for (Frame frame : frames) frame.tick();
            ticker.reset();
        }
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        boolean continueUpdate = true;
        for (Frame frame : frames) {
            if (continueUpdate) {
                if (frame.isMouseOnFrame(mouseX, mouseY)) {
                    continueUpdate = false;
                    frame.updateButtons(mouseX, mouseY);
                } else if (frame.buttonHovered) frame.resetHovered();
            } else {
                if (frame.buttonHovered) frame.resetHovered();
            }
        }

        if (writingSlider.get() != null) {
            if (writingSlider.get().writing) {
                BThackRender.drawString("New Value: " + writingSlider.get().textBuilder, (int) ((mc.getWindow().getScaledWidth() / 2f) - (FontUtils.getTextWidth("New Value: " + writingSlider.get().textBuilder) / 2)), (mc.getWindow().getScaledHeight() - 45), ColorUtils.WHITE);
            }
        }
        BThackRender.guiGraphics.getMatrices().push();
        BThackRender.guiGraphics.getMatrices().scale((float) ModuleList.clickGui.guiScale.getValue(), (float) ModuleList.clickGui.guiScale.getValue(), 1);
        BThackRender.guiGraphics.getMatrices().translate(0, 0, 1);

        ClickGuiRenderer.drawDescriptionBar(descriptionModule, descriptionAnimation);

        BThackRender.guiGraphics.getMatrices().translate(0, 0, 1);

        for (int i = frames.size() - 1; i > -1; i--) {
            Frame frame = frames.get(i);
            frame.renderFrame();
            frame.updatePosition((int) (mouseX / ModuleList.clickGui.guiScale.getValue()), (int) (mouseY / ModuleList.clickGui.guiScale.getValue()));
        }
        BThackRender.guiGraphics.getMatrices().pop();

        descriptionY = (int) ((height - (height / 40)) / ModuleList.clickGui.guiScale.getValue());
    }

    @Override
    public void tick() {
        ModuleList.clickGui.guiScale.setValue(guiScaleSlider.value);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        for (Frame frame : frames) {
            Module module = frame.getDescriptionModule(mouseX, mouseY);
            if (module != null) {
                if (descriptionModule.get() == null) descriptionAnimation.reset();
                descriptionModule.set(module);
                return;
            }
        }
        descriptionModule.set(null);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        for(Frame frame : frames) {
            if (!frame.updateClick(mouseX, mouseY, mouseButton)) {
                Frame temp = frames.get(0);
                frames.set(0, frame);
                frames.set(frame.id, temp);
                temp.id = frame.id;
                frame.id = 0;
                checkCloseAfterClicking();
                return false;
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (activeButton.getId() == 1 || activeButton.getId() == 9) init();

        checkCloseAfterClicking();
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        for (Frame frame : frames)
            frame.moveFrame(horizontalAmount, verticalAmount);
        return false;
    }

    public boolean firstIgnore = true;



    @Override
    public boolean keyPressed(int keyCode, int scanCode, int shift) {
        for(Frame frame : frames) {
            if(frame.isOpen() && keyCode != 1) {
                if(!frame.getButtons().isEmpty()) {
                    for(Component component : frame.getButtons()) {
                        component.keyTyped(keyCode);
                    }
                }
            }
        }

        if (keyCode == ModuleList.clickGui.getKey()) {
            if (!firstIgnore) {
                ConfigSystem.saveConfig();
                mc.setScreen(null);

                return true;
            } else {
                firstIgnore = false;
            }
        }

        switch (keyCode) {
            case KeyboardUtils.KEY_ESCAPE:
                mc.setScreen(null);
                break;
            case KeyboardUtils.KEY_LEFT:
            case KeyboardUtils.KEY_RIGHT:
            case KeyboardUtils.KEY_UP:
            case KeyboardUtils.KEY_DOWN:
                for(Frame frame : frames) {
                    frame.moveFrame(keyCode);
                }
        }

        return super.keyPressed(keyCode, scanCode, shift);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int state) {
        for(Frame frame : frames) {
            frame.setDrag(false);
        }
        for(Frame frame : frames) {
            frame.updateRelease((int) mouseX, (int) mouseY, state);
        }

        return super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void removed() {
        ConfigSystem.saveConfig();
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
}
