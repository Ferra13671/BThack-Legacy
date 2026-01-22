package com.ferra13671.BThack.impl.modules.movement;


import com.ferra13671.BThack.managers.impl.setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.impl.setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.api.module.OneActionModule;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(name = "Impulse", description = "lang.module.Impulse", category = "MOVEMENT")
public class Impulse extends OneActionModule {

    public final BooleanSetting considerY = new BooleanSetting("Consider Y", this, true);
    public final NumberSetting factor = new NumberSetting("Impulse factor", this, 0.1, 0.1, 100, false);

    @Override
    @SuppressWarnings("DataFlowIssue")
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
