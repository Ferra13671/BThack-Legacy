package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import org.ladysnake.satin.api.event.ShaderEffectRenderCallback;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.ladysnake.satin.api.managed.ShaderEffectManager;
import net.minecraft.util.Identifier;

@ModuleInfo(name = "MotionBlur", description = "lang.module.MotionBlur", category = "RENDER")
public class MotionBlur extends Module {

    public final NumberSetting blur = new NumberSetting("Blur", this, 50, 0, 99, false);

    public MotionBlur() {
        ShaderEffectRenderCallback.EVENT.register(tickDelta -> {
            if (this.isEnabled()) {
                if (getBlur() != 0) {
                    if (prevBlur != getBlur()) {
                        motionBlur.setUniformValue("BlendFactor", getBlur());
                        prevBlur = getBlur();
                    }
                    motionBlur.render(tickDelta);
                }
            }
        });
    }

    private float prevBlur = 0;

    private final ManagedShaderEffect motionBlur = ShaderEffectManager.getInstance().manage(Identifier.of("bthack", "shaders/post/motion_blur.json"),
            shader -> shader.setUniformValue("BlendFactor", getBlur()));

    private float getBlur() {
        return (float) (blur.getValue() / 100);
    }

}
