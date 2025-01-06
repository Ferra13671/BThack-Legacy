package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.minecraft.util.Identifier;

public class MotionBlur extends Module {

    public static NumberSetting blur;

    public MotionBlur() {
        super("MotionBlur",
                "lang.module.MotionBlur",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        blur = new NumberSetting("Blur", this, 50, 0, 99, false);

        initSettings(
                blur
        );

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

    private final ManagedShaderEffect motionBlur = ShaderEffectManager.getInstance().manage(new Identifier("bthack", "shaders/post/motion_blur.json"),
            shader -> shader.setUniformValue("BlendFactor", getBlur()));

    private float getBlur() {
        return (float) (blur.getValue() / 100);
    }

}
