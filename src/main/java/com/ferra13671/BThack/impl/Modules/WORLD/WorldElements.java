package com.ferra13671.BThack.impl.Modules.WORLD;

import com.ferra13671.BThack.api.IMixin.ModifyWorldRenderer;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import net.minecraft.client.render.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class WorldElements extends Module {

    public final BooleanSetting changeStars = new BooleanSetting("Change Stars", this, true);
    public final BooleanSetting starBrightness = new BooleanSetting("Star Brightness", this, true);
    public final NumberSetting starBright = new NumberSetting("Star Bright", this, 0.5,0,1, false, () -> changeStars.getValue() && starBrightness.getValue());
    public final NumberSetting stars = new NumberSetting("Stars", this, 3000, 200, 10000, true, changeStars::getValue);
    public final NumberSetting starsSeed = new NumberSetting("Stars Seed", this, 10842, 1, 20000, true, changeStars::getValue);

    public final BooleanSetting changeMoonPhase = new BooleanSetting("Ch. Moon Phase", this, false);
    public final NumberSetting moonPhase = new NumberSetting("Moon Phase", this, 5, 0, 7, true, changeMoonPhase::getValue);

    public WorldElements() {
        super("WorldElements",
                "lang.module.WorldElements",
                KeyboardUtils.RELEASE,
                MCategory.WORLD,
                false
        );

        initSettings(
                changeStars,
                starBrightness,
                starBright,
                stars,
                starsSeed,

                changeMoonPhase,
                moonPhase
        );
    }

    @Override
    public void onEnable() {
        if (changeStars.getValue()) ((ModifyWorldRenderer) mc.worldRenderer).generateStarsMap();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        ((ModifyWorldRenderer) mc.worldRenderer).generateStarsMap();
    }

    @Override
    public void onChangeSetting(Setting setting) {
        if (isEnabled()) {
            if (changeStars.getValue() && (setting == stars || setting == starsSeed)) ((ModifyWorldRenderer) mc.worldRenderer).generateStarsMap();
        }
    }

    public BuiltBuffer buildStarsBuffer() {
        Random random = Random.create((long) starsSeed.getValue());
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);

        for(int j = 0; j < stars.getValue(); ++j) {
            float g = random.nextFloat() * 2.0F - 1.0F;
            float h = random.nextFloat() * 2.0F - 1.0F;
            float k = random.nextFloat() * 2.0F - 1.0F;
            float l = 0.15F + random.nextFloat() * 0.1F;
            float m = MathHelper.magnitude(g, h, k);
            if (!(m <= 0.010000001F) && !(m >= 1.0F)) {
                Vector3f vector3f = (new Vector3f(g, h, k)).normalize(100.0F);
                float n = (float)(random.nextDouble() * 3.1415927410125732 * 2.0);
                Quaternionf quaternionf = (new Quaternionf()).rotateTo(new Vector3f(0.0F, 0.0F, -1.0F), vector3f).rotateZ(n);
                bufferBuilder.vertex(vector3f.add((new Vector3f(l, -l, 0.0F)).rotate(quaternionf)));
                bufferBuilder.vertex(vector3f.add((new Vector3f(l, l, 0.0F)).rotate(quaternionf)));
                bufferBuilder.vertex(vector3f.add((new Vector3f(-l, l, 0.0F)).rotate(quaternionf)));
                bufferBuilder.vertex(vector3f.add((new Vector3f(-l, -l, 0.0F)).rotate(quaternionf)));
            }
        }

        return bufferBuilder.end();
    }
}
