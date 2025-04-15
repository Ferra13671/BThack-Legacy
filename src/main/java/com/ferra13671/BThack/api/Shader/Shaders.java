package com.ferra13671.BThack.api.Shader;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Events.DisconnectEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Shader.MainMenu.MainMenuShader;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public class Shaders implements Mc {
    public static Shaders INSTANCE;
    public final ShaderTicker shaderTicker = new ShaderTicker();
    {
        BThack.EVENT_BUS.register(this);
    }

    public final ShaderProgram POSITION = ShaderProgram.of("render/position", VertexFormats.POSITION);

    public final ShaderProgram ROUNDED_RECT = ShaderProgram.of("render/rounded_rect", VertexFormats.POSITION);
    public final ShaderProgram ROUNDED_RECT_WITH_OUTLINE = ShaderProgram.of("render/rounded_rect_with_outline", VertexFormats.POSITION);
    public final ShaderProgram X_RAINBOW = new ShaderProgram(Identifier.of("bthack", "render/x_rainbow"), VertexFormats.POSITION) {
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
    public final ShaderProgram XY_GRADIENT = new ShaderProgram(Identifier.of("bthack", "render/xy_gradient"), VertexFormats.POSITION) {
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
    public final MainMenuShader SNOW = MainMenuShader.of("render/snow");

    @EventSubscriber
    public void onDisconnect(DisconnectEvent e) {
        shaderTicker.reset();
    }

    public void updateTime() {
        shaderTicker.update(1);
    }
}
