package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.Render.RenderWorldLastEvent;
import com.ferra13671.BThack.api.IMixin.ModifyWorldRenderer;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.*;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Ticker;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.client.render.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.awt.*;
import java.util.Arrays;

@ModuleInfo(name = "Ambience", description = "lang.module.Ambience", category = "RENDER")
public class Ambience extends Module {

    public final CategorySetting fogCategory = new CategorySetting("Fog", this);
    public final BooleanSetting customFogDistance = new BooleanSetting("Custom Distance", this, true).inCategory(fogCategory);
    public final NumberSetting fogStart = new NumberSetting("Fog Start", this, 0, 0, 600, false, customFogDistance::getValue).inCategory(fogCategory);
    public final NumberSetting fogEnd = new NumberSetting("Fog End", this, 150, 0, 600, false, customFogDistance::getValue).inCategory(fogCategory);
    public final BooleanSetting customFogColor = new BooleanSetting("Custom Color", this, true).inCategory(fogCategory);
    public final ColorSetting fogColor = new ColorSetting("Fog Color", this, new Color(195, 85, 251), customFogColor::getValue).withBlockedAlpha().inCategory(fogCategory);
    public final BooleanSetting overworld = new BooleanSetting("Overworld", this, true, customFogColor::getValue).inCategory(fogCategory);
    public final BooleanSetting nether = new BooleanSetting("Nether", this, true, customFogColor::getValue).inCategory(fogCategory);
    public final BooleanSetting end = new BooleanSetting("End", this, true, customFogColor::getValue).inCategory(fogCategory);

    public final CategorySetting skyCategory = new CategorySetting("Sky", this);
    public final BooleanSetting customSkyColor = new BooleanSetting("Custom Color", this, true).inCategory(skyCategory);
    public final ColorSetting skyColor = new ColorSetting("Sky Color", this, new Color(94, 0, 155), customSkyColor::getValue).withBlockedAlpha().inCategory(skyCategory);

    public final CategorySetting cloudsCategory = new CategorySetting("Clouds", this);
    public final BooleanSetting customCloudsColor = new BooleanSetting("Custom Color", this, true).inCategory(cloudsCategory);
    public final ColorSetting cloudsColor = new ColorSetting("Clouds Color", this, new Color(213, 142, 253), customCloudsColor::getValue).withBlockedAlpha().inCategory(cloudsCategory);

    public final CategorySetting worldTimeCategory = new CategorySetting("World Time", this);
    public final BooleanSetting customWorldTime = new BooleanSetting("Custom Time", this, true).inCategory(worldTimeCategory);
    public final ModeSetting worldTimeMode = new ModeSetting("Mode", this, Arrays.asList("Day", "Night", "Morning", "Sunset", "Spin", "Custom"), customWorldTime::getValue).defaultValue("Night").inCategory(worldTimeCategory);
    public final NumberSetting customTime = new NumberSetting("Time", this, 10000, 1, 24000, true, () -> customWorldTime.getValue() && worldTimeMode.getValue().equals("Custom")).inCategory(worldTimeCategory);
    public final NumberSetting spinSpeed = new NumberSetting("Spin Speed", this, 1, 0.5, 5, false, () -> customWorldTime.getValue() && worldTimeMode.getValue().equals("Spin")).inCategory(worldTimeCategory);

    public final CategorySetting starsCategory = new CategorySetting("Stars", this);
    public final BooleanSetting customStars = new BooleanSetting("Custom Stars", this, false).inCategory(starsCategory);
    public final BooleanSetting customBrightness = new BooleanSetting("Custom Brightness", this, true, customStars::getValue).inCategory(starsCategory);
    public final NumberSetting starsBrightness = new NumberSetting("Brightness", this, 0.5, 0, 1, false, () -> customBrightness.getValue() && customStars.getValue()).inCategory(starsCategory);
    public final NumberSetting stars = new NumberSetting("Stars", this, 3000, 200, 10000, true, customStars::getValue).inCategory(starsCategory);
    public final NumberSetting starsSeed = new NumberSetting("Stars Seed", this, 10842, 1, 20000, true, customStars::getValue).inCategory(starsCategory);

    public final CategorySetting moonPhaseCategory = new CategorySetting("Moon Phase", this);
    public final BooleanSetting customMoonPhase = new BooleanSetting("Custom Moon Phase", this, false).inCategory(moonPhaseCategory);
    public final NumberSetting moonPhase = new NumberSetting("Moon Phase", this, 5, 0, 7, true, customMoonPhase::getValue).inCategory(moonPhaseCategory);

    public final CategorySetting weatherCategory = new CategorySetting("Weather", this);
    public final BooleanSetting customWeather = new BooleanSetting("Custom Weather", this, false).inCategory(weatherCategory);
    public final ModeSetting weather = new ModeSetting("Weather", this, Arrays.asList("Clear", "Rain", "Thunder", "Ash", "Snow"), customWeather::getValue).inCategory(weatherCategory);
    public final NumberSetting ashProbability = new NumberSetting("Ash Probability", this, 1, 0.1, 10, false, () -> customWeather.getValue() && weather.getValue().equals("Ash")).inCategory(weatherCategory);


    private final Ticker ticker = new Ticker();

    @Override
    public void onChangeSetting(Setting<?> setting) {
        if (setting == worldTimeMode) ticker.reset();
        if (isEnabled()) {
            if (setting == stars || setting == starsSeed || setting == customStars) ((ModifyWorldRenderer) mc.worldRenderer).generateStarsMap();
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        ticker.reset();
        ((ModifyWorldRenderer) mc.worldRenderer).generateStarsMap();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        ((ModifyWorldRenderer) mc.worldRenderer).generateStarsMap();
    }

    public Vec3d getFogColor() {
        Color color = fogColor.getValue();
        return new Vec3d(color.getRed() / 255d, color.getGreen() / 255d, color.getBlue() / 255d);
    }

    public Vec3d getSkyColor() {
        Color color = skyColor.getValue();
        return new Vec3d(color.getRed() / 255d, color.getGreen() / 255d, color.getBlue() / 255d);
    }

    public Vec3d getCloudsColor() {
        Color color = cloudsColor.getValue();
        return new Vec3d(color.getRed() / 255d, color.getGreen() / 255d, color.getBlue() / 255d);
    }

    public long getWorldTime() {
        return switch (worldTimeMode.getValue()) {
            case "Day" -> 5000L;
            case "Night" -> 17000L;
            case "Morning" -> 0L;
            case "Sunset" -> 13000L;
            case "Custom" -> customTime.getValue().longValue();
            case "Spin" -> (long) (((ticker.getPassedTime() % 10000f) / 10000f) *spinSpeed.getValue().floatValue() * 24000);
            default -> 0L;
        };
    }

    @EventSubscriber
    public void onRender(RenderWorldLastEvent e) {
        if (customWorldTime.getValue() && worldTimeMode.getValue().equals("Spin") && !nullCheck())
            mc.world.setTimeOfDay(0);
    }

    public BuiltBuffer buildStarsBuffer() {
        Random random = Random.create(starsSeed.getValue().longValue());
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

    public boolean isParticleWeather() {
        return ModuleList.ambience.weather.getValue().equals("Ash");
    }

    public float getRainGradient(float value) {
        if (ModuleList.ambience.isEnabled() && ModuleList.ambience.customWeather.getValue() && (!ModuleList.ambience.weather.getValue().equals("Clear") && !ModuleList.ambience.isParticleWeather()))
            return ModuleList.ambience.weather.getValue().equals("Thunder") ? 2f : 1f;
        else return value;
    }

    public float getThunderGradient(float value) {
        if (ModuleList.ambience.isEnabled() && ModuleList.ambience.customWeather.getValue() && ModuleList.ambience.weather.getValue().equals("Thunder"))
            return 1f;
        else return value;
    }
}
