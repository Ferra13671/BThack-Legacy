package com.ferra13671.BThack.api.Events.Render;

import com.ferra13671.MegaEvents.Base.Event;
import net.minecraft.client.util.math.MatrixStack;

public class RenderWorldLastEvent extends Event {
    public final MatrixStack matrixStack;

    public RenderWorldLastEvent(MatrixStack matrixStack) {
        this.matrixStack = matrixStack;
    }
}
