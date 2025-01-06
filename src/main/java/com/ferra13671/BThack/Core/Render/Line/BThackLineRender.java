package com.ferra13671.BThack.Core.Render.Line;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.BThackRenderUtils;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.RegionPos;
import com.ferra13671.BThack.api.Utils.RotateUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.List;

import static com.ferra13671.BThack.Core.Render.Utils.BThackRenderUtils.getCameraPos;

public final class BThackLineRender implements Mc {

    public void prepareLineRenderer() {
        BThackRender.worldRenderContext.matrixStack().push();

        RegionPos region = BThackRenderUtils.getCameraRegion();
        BThackRender.applyRegionalRenderOffset(BThackRender.worldRenderContext.matrixStack(), region);

        BThackRenderUtils.applyBlend();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    public void renderLines(List<RenderLine> lines) {
        Matrix4f matrix = BThackRender.worldRenderContext.matrixStack().peek().getPositionMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        Vec3d regionVec = BThackRenderUtils.getCameraRegion().toVec3d();

        Vec3d start = RotateUtils.getClientLookVec(mc.getTickDelta()).add(getCameraPos()).subtract(regionVec);

        for (RenderLine line : lines) {
            BThackRender.trace(line.vec3d, matrix, start, line.red, line.green, line.blue, line.alpha, bufferBuilder, regionVec);
        }
        BThackRenderUtils.resetShader();
    }

    public void stopLineRenderer() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderSystem.setShaderColor(1,1,1,1);

        BThackRender.worldRenderContext.matrixStack().pop();
    }
}
