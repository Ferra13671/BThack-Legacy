package com.ferra13671.BThack.api.GuiSystem.buttons;

import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.TextureUtils.GLTexture;

public class ImageButton extends Button {
    private final GLTexture texture;

    public ImageButton(int id, int x, int y, int width, int height, GLTexture texture) {
        super(id, x, y, width, height, "");
        this.texture = texture;
    }

    @Override
    public void renderButton() {
        float animationDelta = getAnimationDelta();
        drawPlate(animationDelta);
        BThackRender.drawTextureRect(texture, getCenterX() - getWidth() + 2, getCenterY() - getHeight() + 2, getCenterX() + getWidth() - 2, getCenterY() + getHeight() - 2);
    }
}
