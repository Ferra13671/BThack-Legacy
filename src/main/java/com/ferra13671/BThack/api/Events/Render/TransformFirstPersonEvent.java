package com.ferra13671.BThack.api.Events.Render;

import com.ferra13671.MegaEvents.Base.Event;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;

public class TransformFirstPersonEvent extends Event {

    public final Arm arm;
    public final MatrixStack matrices;
    public final TransformType transformType;

    private TransformFirstPersonEvent(Arm arm, MatrixStack matrices, TransformType transformType) {
        this.arm = arm;
        this.matrices = matrices;
        this.transformType = transformType;
    }

    public static class Pre extends TransformFirstPersonEvent {
        public Pre(Arm arm, MatrixStack matrices, TransformType transformType) {
            super(arm, matrices, transformType);
        }
    }
    public static class Post extends TransformFirstPersonEvent {
        public Post(Arm arm, MatrixStack matrices, TransformType transformType) {
            super(arm, matrices, transformType);
        }
    }

    public enum TransformType {
        SWING,
        EQUIP,
        EAT,
        BRUSH,
        ARM
    }
}
