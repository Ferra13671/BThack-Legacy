package com.ferra13671.BThack.core.Render.Line;

import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.core.Render.Utils.BThackRenderUtils;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.Rotate.RotateUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.math.Vec3d;

import java.util.List;

import static com.ferra13671.BThack.core.Render.Utils.BThackRenderUtils.getCameraPos;

public final class BThackLineRender implements Mc {

    public void prepareLineRenderer() {
        BThackRender.worldMatrixStack.push();

        BThackRenderUtils.applyBlend();
        RenderSystem.disableDepthTest();
    }

    public void renderLines(List<RenderLine> lines) {
        Vec3d offset = getCameraPos().negate();
        Vec3d start = RotateUtils.getClientLookVec().multiply(10);

        for (RenderLine line : lines) {
            BThackRender.trace(BThackRender.worldMatrixStack, start, line.vec3d.add(offset), line.red, line.green, line.blue, line.alpha);
        }
        BThackRenderUtils.resetShader();
    }

    public void stopLineRenderer() {
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1,1,1,1);

        BThackRender.worldMatrixStack.pop();
    }
}
