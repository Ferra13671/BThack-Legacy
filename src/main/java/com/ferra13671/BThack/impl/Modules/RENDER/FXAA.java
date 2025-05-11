package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import org.ladysnake.satin.api.event.ShaderEffectRenderCallback;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.ladysnake.satin.api.managed.ShaderEffectManager;
import net.minecraft.util.Identifier;

import java.util.Arrays;

public class FXAA extends Module {

    public final ModeSetting level = new ModeSetting("Level", this, Arrays.asList("1x", "2x", "3x", "4x", "5x"));

    public FXAA() {
        super("FXAA",
                "lang.module.FXAA",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        ShaderEffectRenderCallback.EVENT.register(tickDelta -> {
            if (this.isEnabled()) {
                int _level = Integer.parseInt(level.getValue().replace("x", ""));
                for (int i = 0; i < _level; i++)
                    fxaa.render(tickDelta);
            }
        });
    }

    ManagedShaderEffect fxaa = ShaderEffectManager.getInstance().manage(Identifier.of("shaders/post/fxaa.json"));

}
