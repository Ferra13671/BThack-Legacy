package com.ferra13671.BThack.api.Utils.System;


import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.System.buttons.Button;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class BThackScreen extends Screen implements Mc {
    public static final int BACKGROUND_TABLE_COLOR = ColorUtils.fastRGBA(0,0,0,40);

    public ArrayList<Button> buttons = new ArrayList<>();

    public Button activeButton = Button.of(Integer.MIN_VALUE, -100, -100, 1, 1, "nullButton");

    public boolean closeAfterClicking = false;
    public Runnable runnable;

    protected BThackScreen(Text title) {
        super(title);
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        for (Button button : buttons) {
            if (!button.hided) {
                button.updateButton(mouseX, mouseY);

                button.renderButton();
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        activeButton = Button.of(Integer.MIN_VALUE, -100, -100, 1, 1, "nullButton");

        for (Button button : buttons) {
            if (!button.hided) {
                if (button.isMouseOnButton((int) mouseX, (int) mouseY)) {
                    button.mouseClicked((int) mouseX, (int) mouseY, mouseButton);
                    if (mouseButton == 0) {
                        activeButton = button;
                        //MusicStorage.playSound(MusicStorage.buttonClicked, mc.options.getSoundVolume(SoundCategory.MASTER) / 2.5f);
                    }
                }
            }
        }
        checkCloseAfterClicking();
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void checkCloseAfterClicking() {
        if (closeAfterClicking) {
            closeAfterClicking = false;
            runnable.run();
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        for (Button button : buttons) {
            button.mouseReleased((int) mouseX, (int) mouseY, mouseButton);
        }
        return super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        for (Button button : buttons) {
            if (!button.hided)
                button.charTyped(chr);
        }

        return super.charTyped(chr, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int shift) {
        for (Button button : buttons) {
            if (!button.hided)
                button.keyTyped(keyCode);
        }

        return super.keyPressed(keyCode, scanCode, shift);
    }

    public Button getButtonFromId(int id) {
        for (Button button : buttons) {
            if (button.getId() == id)
                return button;
        }
        return null;
    }

    public void closeAfterClicking(Runnable action) {
        closeAfterClicking = true;
        runnable = action;
    }

    public void drawBackGround() {
        BThackRender.draw4ColorRect( 0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), BACKGROUND_TABLE_COLOR, BACKGROUND_TABLE_COLOR, ColorUtils.fastRGBA(161,0, 255, 255), ColorUtils.fastRGBA(255, 0, 0, 255));
    }

    public int getX100P() {
        return mc.getWindow().getScaledWidth() / 100;
    }
}
