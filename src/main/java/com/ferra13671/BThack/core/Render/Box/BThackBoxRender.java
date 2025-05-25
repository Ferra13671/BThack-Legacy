package com.ferra13671.BThack.core.Render.Box;

import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.core.Render.Utils.BThackRenderUtils;
import com.ferra13671.BThack.shaders.CoreShaders;
import com.ferra13671.BThack.api.Utils.RegionPos;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.GlUsage;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.joml.Matrix4f;

import java.util.List;

public class BThackBoxRender {
    public VertexBuffer solidBox;
    public VertexBuffer outlinedBox;

    public void init() {
        solidBox = new VertexBuffer(GlUsage.STATIC_WRITE);
        outlinedBox = new VertexBuffer(GlUsage.STATIC_WRITE);

        Box box = new Box(BlockPos.ORIGIN);
        drawSolidBox(box, solidBox);
        drawOutlinedBox(box, outlinedBox);
    }

    public void prepareBoxRender() {
        BThackRenderUtils.applyBlend();
        RenderSystem.enableCull();
        RenderSystem.disableDepthTest();

        BThackRender.worldMatrixStack.push();
        BThackRender.applyRegionalRenderOffset(BThackRender.worldMatrixStack);

        RenderSystem.lineWidth(1);
    }

    public void stopBoxRender() {
        BThackRender.worldMatrixStack.pop();

        // GL resets
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableDepthTest();
    }

    public void renderBoxes(List<RenderBox> boxes) {
        MatrixStack matrixStack =  BThackRender.worldMatrixStack;

        RegionPos region = BThackRenderUtils.getCameraRegion();
        CoreShaders.POSITION.use();

        ShaderProgram shader = RenderSystem.getShader();

        for(RenderBox box : boxes) {
            matrixStack.push();

            matrixStack.translate(box.box.minX - region.x(), box.box.minY,
                    box.box.minZ - region.z());

            matrixStack.scale((float)(box.box.maxX - box.box.minX),
                    (float)(box.box.maxY - box.box.minY), (float)(box.box.maxZ - box.box.minZ));

            Matrix4f viewMatrix = matrixStack.peek().getPositionMatrix();
            Matrix4f projMatrix = RenderSystem.getProjectionMatrix();

            renderSolidBox(box, viewMatrix, projMatrix, shader);

            renderOutlineBox(box, viewMatrix, projMatrix, shader);

            matrixStack.pop();
        }
        BThackRenderUtils.resetShader();
    }

    public void renderSolidBox(RenderBox box, Matrix4f viewMatrix, Matrix4f projMatrix, ShaderProgram shader) {
        CoreShaders.POSITION.setUniformValue("color", box.boxRed, box.boxGreen, box.boxBlue, box.boxAlpha);
        solidBox.bind();
        solidBox.draw(viewMatrix, projMatrix, shader);
        VertexBuffer.unbind();
    }

    public void renderOutlineBox(RenderBox box, Matrix4f viewMatrix, Matrix4f projMatrix, ShaderProgram shader) {
        CoreShaders.POSITION.setUniformValue("color", box.linesRed, box.linesGreen, box.linesBlue, box.linesAlpha);
        outlinedBox.bind();
        outlinedBox.draw(viewMatrix, projMatrix, shader);
        VertexBuffer.unbind();
    }



    public void drawSolidBox(Box bb, VertexBuffer vertexBuffer) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.QUADS,
                getVertexFormat());

        drawSolidBoxInternal(bb, bufferBuilder);
        BuiltBuffer buffer = bufferBuilder.end();

        vertexBuffer.bind();
        vertexBuffer.upload(buffer);
        VertexBuffer.unbind();
    }

    public void drawSolidBoxInternal(Box bb, BufferBuilder bufferBuilder) {
        vertex(bufferBuilder, bb.minX, bb.minY, bb.minZ);
        vertex(bufferBuilder, bb.maxX, bb.minY, bb.minZ);
        vertex(bufferBuilder, bb.maxX, bb.minY, bb.maxZ);
        vertex(bufferBuilder, bb.minX, bb.minY, bb.maxZ);

        vertex(bufferBuilder, bb.minX, bb.maxY, bb.minZ);
        vertex(bufferBuilder, bb.minX, bb.maxY, bb.maxZ);
        vertex(bufferBuilder, bb.maxX, bb.maxY, bb.maxZ);
        vertex(bufferBuilder, bb.maxX, bb.maxY, bb.minZ);

        vertex(bufferBuilder, bb.minX, bb.minY, bb.minZ);
        vertex(bufferBuilder, bb.minX, bb.maxY, bb.minZ);
        vertex(bufferBuilder, bb.maxX, bb.maxY, bb.minZ);
        vertex(bufferBuilder, bb.maxX, bb.minY, bb.minZ);

        vertex(bufferBuilder, bb.maxX, bb.minY, bb.minZ);
        vertex(bufferBuilder, bb.maxX, bb.maxY, bb.minZ);
        vertex(bufferBuilder, bb.maxX, bb.maxY, bb.maxZ);
        vertex(bufferBuilder, bb.maxX, bb.minY, bb.maxZ);

        vertex(bufferBuilder, bb.minX, bb.minY, bb.maxZ);
        vertex(bufferBuilder, bb.maxX, bb.minY, bb.maxZ);
        vertex(bufferBuilder, bb.maxX, bb.maxY, bb.maxZ);
        vertex(bufferBuilder, bb.minX, bb.maxY, bb.maxZ);

        vertex(bufferBuilder, bb.minX, bb.minY, bb.minZ);
        vertex(bufferBuilder, bb.minX, bb.minY, bb.maxZ);
        vertex(bufferBuilder, bb.minX, bb.maxY, bb.maxZ);
        vertex(bufferBuilder, bb.minX, bb.maxY, bb.minZ);
    }

    public void drawOutlinedBox(Box bb, VertexBuffer vertexBuffer) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, getVertexFormat());

        drawOutlinedBoxInternal(bb, bufferBuilder);
        BuiltBuffer buffer = bufferBuilder.end();

        vertexBuffer.bind();
        vertexBuffer.upload(buffer);
        VertexBuffer.unbind();
    }

    public void drawOutlinedBoxInternal(Box bb, BufferBuilder bufferBuilder) {
        vertex(bufferBuilder, bb.minX, bb.minY, bb.minZ);
        vertex(bufferBuilder, bb.maxX, bb.minY, bb.minZ);

        vertex(bufferBuilder, bb.maxX, bb.minY, bb.minZ);
        vertex(bufferBuilder, bb.maxX, bb.minY, bb.maxZ);

        vertex(bufferBuilder, bb.maxX, bb.minY, bb.maxZ);
        vertex(bufferBuilder, bb.minX, bb.minY, bb.maxZ);

        vertex(bufferBuilder, bb.minX, bb.minY, bb.maxZ);
        vertex(bufferBuilder, bb.minX, bb.minY, bb.minZ);

        vertex(bufferBuilder, bb.minX, bb.minY, bb.minZ);
        vertex(bufferBuilder, bb.minX, bb.maxY, bb.minZ);

        vertex(bufferBuilder, bb.maxX, bb.minY, bb.minZ);
        vertex(bufferBuilder, bb.maxX, bb.maxY, bb.minZ);

        vertex(bufferBuilder, bb.maxX, bb.minY, bb.maxZ);
        vertex(bufferBuilder, bb.maxX, bb.maxY, bb.maxZ);

        vertex(bufferBuilder, bb.minX, bb.minY, bb.maxZ);
        vertex(bufferBuilder, bb.minX, bb.maxY, bb.maxZ);

        vertex(bufferBuilder, bb.minX, bb.maxY, bb.minZ);
        vertex(bufferBuilder, bb.maxX, bb.maxY, bb.minZ);

        vertex(bufferBuilder, bb.maxX, bb.maxY, bb.minZ);
        vertex(bufferBuilder, bb.maxX, bb.maxY, bb.maxZ);

        vertex(bufferBuilder, bb.maxX, bb.maxY, bb.maxZ);
        vertex(bufferBuilder, bb.minX, bb.maxY, bb.maxZ);

        vertex(bufferBuilder, bb.minX, bb.maxY, bb.maxZ);
        vertex(bufferBuilder, bb.minX, bb.maxY, bb.minZ);
    }

    public void vertex(BufferBuilder bufferBuilder, double x, double y, double z) {
        bufferBuilder.vertex((float) x, (float) y, (float) z);
    }

    public VertexFormat getVertexFormat() {
        return VertexFormats.POSITION;
    }
}