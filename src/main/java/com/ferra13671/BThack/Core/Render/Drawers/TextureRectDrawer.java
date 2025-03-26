package com.ferra13671.BThack.Core.Render.Drawers;

import com.ferra13671.BThack.Core.Render.BThackMatrix;
import com.ferra13671.BThack.Core.Render.Utils.BThackRenderUtils;
import com.ferra13671.TextureUtils.GlTex;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

public class TextureRectDrawer extends Drawer {
    protected TextureRectDrawer() {}

    @Deprecated
    public void begin(Identifier texture) {
        RenderSystem.setShaderTexture(0, texture);
        buffer = BThackRenderUtils.prepareToDraw(GameRenderer::getPositionTexProgram).begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
    }

    public void begin(GlTex texture) {
        RenderSystem.setShaderTexture(0, texture.getTexId());
        buffer = BThackRenderUtils.prepareToDraw(GameRenderer::getPositionTexProgram).begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
    }

    public void draw(float x1, float y1, float x2, float y2, Matrix4f matrix4f) {
        buffer.vertex(matrix4f, x1, y2, 0.0f).texture(0, 1);
        buffer.vertex(matrix4f, x2, y2, 0.0f).texture(1, 1);
        buffer.vertex(matrix4f, x2, y1, 0.0f).texture(1, 0);
        buffer.vertex(matrix4f, x1, y1, 0.0f).texture(0, 0);
    }

    public void draw(float x1, float y1, float x2, float y2) {
        draw(x1, y1, x2, y2, BThackMatrix.peek().getPositionMatrix());
    }

    public void end() {
        BThackRenderUtils.draw(buffer.end());
        buffer = null;
    }
}
