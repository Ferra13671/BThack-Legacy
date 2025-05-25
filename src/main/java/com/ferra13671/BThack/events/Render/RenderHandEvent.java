package com.ferra13671.BThack.events.Render;

import com.ferra13671.MegaEvents.Base.Event;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Hand;

public class RenderHandEvent extends Event {
    public final Type type;
    public final Hand hand;
    public final MatrixStack matrix;

    public RenderHandEvent(Type type, Hand hand, MatrixStack matrix) {
        this.type = type;
        this.hand = hand;
        this.matrix = matrix;
    }

    public enum Type {
        HELD_ITEM,
        ARM
    }
}
