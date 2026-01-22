package com.ferra13671.BThack.shaders;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.events.DisconnectEvent;
import com.ferra13671.BThack.api.utils.Mc;
import com.ferra13671.BThack.shaders.mainmenu.MainMenuBThackShader;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

public class CoreShaders implements Mc {
    public static final ShaderTicker shaderTicker = new ShaderTicker();
    static {
        BThack.EVENT_BUS.register(new CoreShaders());
    }

    public static final BThackShaderProgram POSITION = BThackShaderProgram.of("render/position");

    public static final BThackShaderProgram ROUNDED_RECT = BThackShaderProgram.of("render/rounded_rect");
    public static final BThackShaderProgram ROUNDED_RECT_WITH_OUTLINE = BThackShaderProgram.of("render/rounded_rect_with_outline");
    public static final BThackShaderProgram XY_GRADIENT_ROUNDED_RECT_WITH_OUTLINE = new BThackShaderProgram(CoreShaderLoader.getShaderKey("render/xy_gradient_rounded_rect_with_outline")) {
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
    public static final BThackShaderProgram X_RAINBOW = new BThackShaderProgram(CoreShaderLoader.getShaderKey("render/x_rainbow")) {
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
    public static final BThackShaderProgram XY_GRADIENT = new BThackShaderProgram(CoreShaderLoader.getShaderKey("render/xy_gradient")) {
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
    public static final MainMenuBThackShader SNOW = MainMenuBThackShader.of("render/snow");

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onDisconnect(DisconnectEvent e) {
        shaderTicker.reset();
    }

    public static void updateTime() {
        shaderTicker.update(1);
    }
}
