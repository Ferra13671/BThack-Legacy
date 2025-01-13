package com.ferra13671.BThack.impl.Modules.MOVEMENT;


import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.OneActionModule;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import net.minecraft.util.math.Vec3d;

public class Impulse extends OneActionModule {

    public final BooleanSetting considerY = new BooleanSetting("Consider Y", this, true);
    public final NumberSetting factor = new NumberSetting("Impulse factor", this, 0.1, 0.1, 100, false);

    public Impulse() {
        super("Impulse",
                "lang.module.Impulse",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );

        initSettings(
                considerY,
                factor
        );
    }

    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        double f = factor.getValue();

        Vec3d viewVec3 = mc.player.getRotationVector();

        Vec3d factor;

        factor = f < 1 ? new Vec3d( viewVec3.x * f, considerY.getValue() ? viewVec3.y * f : 0, viewVec3.z * f) : new Vec3d((viewVec3.x * f) - viewVec3.x, considerY.getValue() ? (viewVec3.y * f) - viewVec3.y : 0, (viewVec3.z * f) - viewVec3.z);

        mc.player.setVelocity(viewVec3.add(factor));
    }
}
