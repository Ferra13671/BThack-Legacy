package com.ferra13671.BThack.api.Gui.Widget.Config;

import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigSystem;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Drawers.Drawers;
import com.ferra13671.BThack.Core.Render.Font.FontRenderManager;
import com.ferra13671.BThack.Core.Render.Font.FontUtils;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Animation.Animation;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.api.GuiSystem.ScreenWidget;
import com.ferra13671.BThack.api.GuiSystem.buttons.Button;
import com.ferra13671.BThack.api.SoundSystem.SoundSystem;
import com.ferra13671.BThack.api.SoundSystem.Sounds;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Textures;
import net.minecraft.client.gui.DrawContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ConfigsWidget extends ScreenWidget {

    private final List<Button> configs = new ArrayList<>();
    private Button selectedConfig;

    private double maxYScroll;

    private Animation configButtonsAnimation;
    private boolean closing = false;

    public ConfigsWidget() {
        super(330, 230, 1);
    }

    @Override
    public void onDisplayed() {
        super.onDisplayed();
        refreshConfigs();
        configButtonsAnimation = new Animation(Easing.BACK_IN_OUT, 1050);
        closing = false;
    }

    @Override
    public void close() {
        super.close();
        configButtonsAnimation = new Animation(Easing.BACK_IN_OUT, 1100);
        closing = true;
    }

    @Override
    public void init() {
        super.init();
        buttons.clear();

        refreshConfigs();

        Button confirmButton = new Button(1, (int) xRight - 83, (int) yDown - 15, 78, 10, "Load Config")
                .withAction(buttonClickInfo -> loadCurrentConfig());
        confirmButton.setClickSound(Sounds.CONFIG_SAVED_OR_LOADED);
        Button deleteButton = Button.of(4, (int) xRight - 83, (int) yDown - 40, 78, 10, "Delete Config")
                .withAction(buttonClickInfo -> actionAfterClicking(this::deleteCurrentConfig));
        confirmButton.setHided(selectedConfig == null);
        deleteButton.setHided(selectedConfig == null);
        buttons.add(confirmButton);
        buttons.add(deleteButton);
        buttons.add(Button.of(3, (int) xLeft + 83, (int) yDown - 40, 78, 10, "Create Config")
                .withAction(buttonClickInfo -> {
                    close();
                    parent.widgetManage.addWidget(new SaveConfigWidget());
                }));
        buttons.add(Button.of(2, (int) xLeft + 83, (int) yDown - 15, 78, 10, "Refresh")
                .withAction(buttonClickInfo -> {
                    selectedConfig = null;
                    init();
                }));
    }

    public void refreshConfigs() {
        configs.clear();

        int yOffset = 0;
        int xOffset = 0;

        for (String string : ConfigSystem.getAllConfigs()) {
            Button button = new ConfigButton(-1, (int) xLeft + 35 + (65 * xOffset), (int) yUp + 35 + (65 * yOffset), 30, 30, string, 18) {
                @Override
                public boolean equals(Object obj) {
                    if (!(obj instanceof Button b)) return false;
                    return b.getText().equals(this.getText());
                }
            };
            configs.add(button);
            xOffset++;
            if (xOffset > 4) {
                xOffset = 0;
                yOffset++;
            }
            if (button.equals(selectedConfig)) button.setSelected(true);
        }

        if (!configs.isEmpty())
            maxYScroll = configs.getLast().getCenterY();
        else
            maxYScroll = 0;

        if (!configs.contains(selectedConfig)) selectedConfig = null;
    }

    public void deleteCurrentConfig() {
        try {
            Files.deleteIfExists(Paths.get("BThack/Configs/" + selectedConfig.getText() + ".json"));
        } catch (IOException ignored) {}
        selectedConfig = null;
        init();
    }

    public void loadCurrentConfig() {
        try {
            ConfigSystem.loadConfigFile(selectedConfig.getText());
        } catch (IOException ignored) {}
        close();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        BThackRender.drawRect(xLeft, yUp, xRight, yDown, ColorUtils.fastRGBA(0, 0, 0, 150));
        BThackRender.drawOutlineRect(xLeft, yUp, xRight, yDown, 1.5f, ColorUtils.WHITE);
        Drawers.RECT.begin(-1);
        Drawers.RECT.draw(xLeft, yDown - 56.5f, xRight, yDown - 55);
        Drawers.RECT.end();
        double yOffset = ((closing ? configButtonsAnimation.getEase() : 1 - configButtonsAnimation.getEase()) * ((mc.getWindow().getScaledHeight() / 2d) + getHeight()));
        BThackRender.enableScissor((int) xLeft + 1, (int) (yUp + 1 + yOffset), (int) getWidth() - 2, (int) (getHeight() - 58.5f));
        for (Button button : configs) {
            button.updateButton(mouseX, mouseY);
            button.renderButton();
        }
        BThackRender.disableScissor();
        super.render(context, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        for (Button button : configs) {
            if (button.isMouseOnButton((int) mouseX, (int) mouseY)) {
                button.mouseClicked((int) mouseX, (int) mouseY, mouseButton);
                button.setSelected(true);
                selectedConfig = button;
            } else button.setSelected(false);
        }

        if (selectedConfig != null) {
            init();
        }

        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        Button button = configs.getLast();
        if ((button.getCenterY() + (verticalAmount * 10)) > maxYScroll)
            return false;

        for (Button button1 : configs)
            button1.setCenterY((int) (button1.getCenterY() + (verticalAmount * 10)));

        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int shift) {
        switch (keyCode) {
            case KeyboardUtils.KEY_ESCAPE -> close();
            case KeyboardUtils.KEY_DELETE -> {
                deleteCurrentConfig();
                SoundSystem.playSound(Sounds.BUTTON_CLICK);
            }
            case KeyboardUtils.KEY_ENTER -> {
                loadCurrentConfig();
                SoundSystem.playSound(Sounds.CONFIG_SAVED_OR_LOADED);
            }
        }
        if (keyCode == KeyboardUtils.KEY_ESCAPE) close();
        return super.keyPressed(keyCode, scanCode, shift);
    }

    public static class ConfigButton extends Button {
        private final int textureSize;

        public ConfigButton(int id, int x, int y, int width, int height, String text, int textureSize) {
            super(id, x, y, width, height, text);
            this.textureSize = textureSize;
        }

        @Override
        public void renderButton() {
            float animationDelta = getAnimationDelta();
            if (!hovered && hoveredAnimation.getEase() >= 1)
                BThackRender.drawRect(getCenterX() - width, getCenterY() - height, getCenterX() + width, getCenterY() + height, RECT_COLOR);
            else
                BThackRender.drawRect(getCenterX() - width - 1, getCenterY() - height - animationDelta, getCenterX() + width + animationDelta, getCenterY() + height + 1, RECT_COLOR);

            if (outline && !selected)
                BThackRender.drawOutlineRect(getCenterX() - getWidth() - (animationDelta * 2), getCenterY() - getHeight() - (animationDelta * 2), getCenterX() + getWidth() + (animationDelta * 2), getCenterY() + getHeight() + (animationDelta * 2), 1, -1);
            BThackRender.drawTextureRect(Textures.CONFIG_FILE, getCenterX() - textureSize, getCenterY() - getHeight() + 3, getCenterX() + textureSize + 3, getCenterY() + textureSize);
            BThackRender.drawString(getText(), getCenterX() - (FontUtils.getTextWidth(getText()) / 2f), getCenterY() + (getHeight() - 3 - FontUtils.getTextHeight(getText())), -1, true, FontRenderManager.DrawMode.NORMAL_BOLD);

            if (selected)
                BThackRender.drawOutlineRect(getCenterX() - getWidth(), getCenterY() - getHeight(), getCenterX() + getWidth(), getCenterY() + getHeight(), 1, ColorUtils.rainbow());
        }
    }
}
