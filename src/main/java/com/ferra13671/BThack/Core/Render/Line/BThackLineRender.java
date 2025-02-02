package com.ferra13671.BThack.Core.Render.Line;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.BThackRenderUtils;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.RegionPos;
import com.ferra13671.BThack.api.Utils.RotateUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.util.List;

import static com.ferra13671.BThack.Core.Render.Utils.BThackRenderUtils.getCameraPos;

public final class BThackLineRender implements Mc {

    public void prepareLineRenderer() {
        BThackRender.worldMatrixStack.push();

        RegionPos region = BThackRenderUtils.getCameraRegion();
        BThackRender.applyRegionalRenderOffset(BThackRender.worldMatrixStack, region);

        BThackRenderUtils.applyBlend();
        RenderSystem.disableDepthTest();
    }

    public void renderLines(List<RenderLine> lines) {
        Matrix4f matrix = BThackRender.worldMatrixStack.peek().getPositionMatrix();
        Vec3d regionVec = BThackRenderUtils.getCameraRegion().toVec3d();

        Vec3d start = RotateUtils.getClientLookVec().add(getCameraPos()).subtract(regionVec);

        for (RenderLine line : lines) {
            BThackRender.trace(line.vec3d, matrix, start, line.red, line.green, line.blue, line.alpha, regionVec);
        }
        BThackRenderUtils.resetShader();
    }

    public void stopLineRenderer() {
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1,1,1,1);

        BThackRender.worldMatrixStack.pop();
    }
}
