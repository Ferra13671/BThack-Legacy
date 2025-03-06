package com.ferra13671.BThack.Core.Render.Drawers;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.BThackRenderUtils;
import com.ferra13671.BThack.api.Shader.ShaderProgram;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import org.joml.Matrix4f;

import static com.ferra13671.BThack.Core.Render.Utils.BThackRenderUtils.prepareToDraw;

public class ShaderDrawer extends Drawer {
    private ShaderProgram shaderProgram;

    public void begin(ShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;
        shaderProgram.use();
        Tessellator tessellator = prepareToDraw();
        buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
    }

    public void draw(float x1, float y1, float x2, float y2, Matrix4f matrix4f) {
        buffer.vertex(matrix4f, x1, y1, 0);
        buffer.vertex(matrix4f, x1, y2, 0);
        buffer.vertex(matrix4f, x2, y2, 0);
        buffer.vertex(matrix4f, x2, y1, 0);
    }

    public void draw(float x1, float y1, float x2, float y2) {
        draw(x1, y1, x2, y2, BThackRender.guiGraphics.getMatrices().peek().getPositionMatrix());
    }

    public void vertex(Matrix4f matrix4f, float x, float y, float z) {
        buffer.vertex(matrix4f, x, y, z);
    }

    public void end() {
        BThackRenderUtils.draw(buffer.end());
        shaderProgram.release();
        buffer = null;
    }
}
