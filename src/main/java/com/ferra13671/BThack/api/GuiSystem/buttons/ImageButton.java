package com.ferra13671.BThack.api.GuiSystem.buttons;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
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
        if (outline && !selected)
            BThackRender.drawOutlineRect(getCenterX() - getWidth() - (animationDelta * 2), getCenterY() - getHeight() - (animationDelta * 2), getCenterX() + getWidth() + (animationDelta * 2), getCenterY() + getHeight() + (animationDelta * 2), 1, -1);

        if (selected)
            BThackRender.drawOutlineRect(getCenterX() - getWidth(), getCenterY() - getHeight(), getCenterX() + getWidth(), getCenterY() + getHeight(), 1, ColorUtils.rainbow());
        BThackRender.drawTextureRect(texture, getCenterX() - getWidth() + 2, getCenterY() - getHeight() + 2, getCenterX() + getWidth() - 2, getCenterY() + getHeight() - 2);
    }
}
