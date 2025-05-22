package com.ferra13671.BThack.shaders;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.events.DisconnectEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.shaders.MainMenu.MainMenuBThackShader;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

public class Shaders implements Mc {
    public static Shaders INSTANCE;
    public final ShaderTicker shaderTicker = new ShaderTicker();
    {
        BThack.EVENT_BUS.register(this);
    }

    public final BThackShaderProgram POSITION = BThackShaderProgram.of("position");

    public final BThackShaderProgram ROUNDED_RECT = BThackShaderProgram.of("rounded_rect");
    public final BThackShaderProgram ROUNDED_RECT_WITH_OUTLINE = BThackShaderProgram.of("rounded_rect_with_outline");
    public final BThackShaderProgram XY_GRADIENT_ROUNDED_RECT_WITH_OUTLINE = new BThackShaderProgram(CoreShaderLoader.getShaderKeys().get("xy_gradient_rounded_rect_with_outline")) {
        @Override
        public void use() {
            this.setUniformValue("time", shaderTicker.getPassedTime() / 1000f);
            this.setUniformValue("resolution", (float) mc.getWindow().getWidth(), mc.getWindow().getHeight());
            super.use();
        }

        @Override
        public void release() {
            this.setUniformValue("scale", 1f);
            this.setUniformValue("speed", 1f);
            super.release();
        }
    };
    public final BThackShaderProgram X_RAINBOW = new BThackShaderProgram(CoreShaderLoader.getShaderKeys().get("x_rainbow")) {
        @Override
        public void use() {
            this.setUniformValue("time", shaderTicker.getPassedTime() / 1000f);
            this.setUniformValue("resolution", (float) mc.getWindow().getWidth(), mc.getWindow().getHeight());
            super.use();
        }

        @Override
        public void release() {
            this.setUniformValue("alpha", 1f);
            this.setUniformValue("brightness", 1f);
            this.setUniformValue("scale", 1f);
            this.setUniformValue("speed", 1f);
            super.release();
        }
    };
    public final BThackShaderProgram XY_GRADIENT = new BThackShaderProgram(CoreShaderLoader.getShaderKeys().get("xy_gradient")) {
        @Override
        public void use() {
            this.setUniformValue("time", shaderTicker.getPassedTime() / 1000f);
            this.setUniformValue("resolution", (float) mc.getWindow().getWidth(), mc.getWindow().getHeight());
            super.use();
        }

        @Override
        public void release() {
            this.setUniformValue("brightness", 1f);
            this.setUniformValue("scale", 1f);
            this.setUniformValue("speed", 1f);
            super.release();
        }
    };
    public final MainMenuBThackShader SNOW = MainMenuBThackShader.of("snow");

    @EventSubscriber
    public void onDisconnect(DisconnectEvent e) {
        shaderTicker.reset();
    }

    public void updateTime() {
        shaderTicker.update(1);
    }
}
