package com.ferra13671.BThack.api.Events.Render;

import com.ferra13671.BThack.Core.Render.Utils.BThackWorldRenderContext;
import com.ferra13671.MegaEvents.Base.Event;

public class RenderWorldEvent extends Event {
    private final BThackWorldRenderContext renderContext;

    private RenderWorldEvent(BThackWorldRenderContext renderContext) {
        this.renderContext = renderContext;
    }

    public static class BeforeEntities extends RenderWorldEvent {
        public BeforeEntities(BThackWorldRenderContext renderContext) {
            super(renderContext);
        }
    }

    public static class AfterEntities extends RenderWorldEvent {
        public AfterEntities(BThackWorldRenderContext renderContext) {
            super(renderContext);
        }
    }

    public static class BlockOutline extends RenderWorldEvent {
        private final BThackWorldRenderContext blockOutlineContext;

        public BlockOutline(BThackWorldRenderContext renderContext, BThackWorldRenderContext blockOutlineContext) {
            super(renderContext);
            this.blockOutlineContext = blockOutlineContext;
        }

        public BThackWorldRenderContext getBlockOutlineContext() {
            return this.blockOutlineContext;
        }
    }

    public static class Last extends RenderWorldEvent {
        public Last(BThackWorldRenderContext renderContext) {
            super(renderContext);
        }
    }

    public static class End extends RenderWorldEvent {
        public End(BThackWorldRenderContext renderContext) {
            super(renderContext);
        }
    }

    public BThackWorldRenderContext getWorldRenderContext() {
        return this.renderContext;
    }
}
