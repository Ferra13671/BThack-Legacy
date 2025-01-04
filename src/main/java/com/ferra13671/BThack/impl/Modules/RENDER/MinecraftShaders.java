package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import org.ladysnake.satin.api.event.ShaderEffectRenderCallback;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.ladysnake.satin.api.managed.ShaderEffectManager;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.HashMap;

public class MinecraftShaders extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", this, Arrays.asList("One", "Some"));


    public final ModeSetting shader = new ModeSetting("Shader", this, Arrays.asList(
            "art",
            "bits",
            "blobs",
            "blobs2",
            "bumpy",
            "color_convolve",
            "creeper",
            "deconverge",
            "desaturate",
            "green",
            "notch",
            "ntsc",
            "pencil",
            "phosphor",
            "sobel",
            "spider",
            "wobble"
    ), () -> mode.getValue().equals("One"));


    public final BooleanSetting art = new BooleanSetting("art", this, false, () -> mode.getValue().equals("Some"));
    public final BooleanSetting bits = new BooleanSetting("bits", this, false, () -> mode.getValue().equals("Some"));
    public final BooleanSetting blobs = new BooleanSetting("blobs", this, false, () -> mode.getValue().equals("Some"));
    public final BooleanSetting blobs2 = new BooleanSetting("blobs2", this, false, () -> mode.getValue().equals("Some"));
    public final BooleanSetting bumpy = new BooleanSetting("bumpy", this, false, () -> mode.getValue().equals("Some"));
    public final BooleanSetting color_convolve = new BooleanSetting("color_convolve", this, false, () -> mode.getValue().equals("Some"));
    public final BooleanSetting creeper = new BooleanSetting("creeper", this, false, () -> mode.getValue().equals("Some"));
    public final BooleanSetting deconverge = new BooleanSetting("deconverge", this, false, () -> mode.getValue().equals("Some"));
    public final BooleanSetting desaturate = new BooleanSetting("desaturate", this, false, () -> mode.getValue().equals("Some"));
    public final BooleanSetting green = new BooleanSetting("green", this, false, () -> mode.getValue().equals("Some"));
    public final BooleanSetting notch = new BooleanSetting("notch", this, false, () -> mode.getValue().equals("Some"));
    public final BooleanSetting ntsc = new BooleanSetting("ntsc", this, false, () -> mode.getValue().equals("Some"));
    public final BooleanSetting pencil = new BooleanSetting("pencil", this, false, () -> mode.getValue().equals("Some"));
    public final BooleanSetting phosphor = new BooleanSetting("phosphor", this, false, () -> mode.getValue().equals("Some"));
    public final BooleanSetting sobel = new BooleanSetting("sobel", this, false, () -> mode.getValue().equals("Some"));
    public final BooleanSetting spider = new BooleanSetting("spider", this, false, () -> mode.getValue().equals("Some"));
    public final BooleanSetting wobble = new BooleanSetting("wobble", this, false, () -> mode.getValue().equals("Some"));

    public MinecraftShaders() {
        super("MinecraftShaders",
                "lang.module.MinecraftShaders",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        initSettings(
                mode,


                shader,


                art,
                bits,
                blobs,
                blobs2,
                bumpy,
                color_convolve,
                creeper,
                deconverge,
                desaturate,
                green,
                notch,
                ntsc,
                pencil,
                phosphor,
                sobel,
                spider,
                wobble
        );

        ShaderEffectRenderCallback.EVENT.register(tickDelta -> {
            if (this.isEnabled()) {
                if (mode.getValue().equals("One")) {
                    ManagedShaderEffect _shader = shaders.get(shader.getValue());
                    if (_shader != null)
                        _shader.render(tickDelta);
                } else {
                    checkAndRenderShader(art, tickDelta);
                    checkAndRenderShader(bits, tickDelta);
                    checkAndRenderShader(blobs, tickDelta);
                    checkAndRenderShader(blobs2, tickDelta);
                    checkAndRenderShader(bumpy, tickDelta);
                    checkAndRenderShader(color_convolve, tickDelta);
                    checkAndRenderShader(creeper, tickDelta);
                    checkAndRenderShader(deconverge, tickDelta);
                    checkAndRenderShader(desaturate, tickDelta);
                    checkAndRenderShader(green, tickDelta);
                    checkAndRenderShader(notch, tickDelta);
                    checkAndRenderShader(ntsc, tickDelta);
                    checkAndRenderShader(pencil, tickDelta);
                    checkAndRenderShader(phosphor, tickDelta);
                    checkAndRenderShader(sobel, tickDelta);
                    checkAndRenderShader(spider, tickDelta);
                    checkAndRenderShader(wobble, tickDelta);
                }
            }
        });

        shaders.put("art", getShader("art"));
        shaders.put("bits", getShader("bits"));
        shaders.put("blobs", getShader("blobs"));
        shaders.put("blobs2", getShader("blobs2"));
        shaders.put("bumpy", getShader("bumpy"));
        shaders.put("color_convolve", getShader("color_convolve"));
        shaders.put("creeper", getShader("creeper"));
        shaders.put("deconverge", getShader("deconverge"));
        shaders.put("desaturate", getShader("desaturate"));
        shaders.put("green", getShader("green"));
        shaders.put("invert", getShader("invert"));
        shaders.put("notch", getShader("notch"));
        shaders.put("ntsc", getShader("ntsc"));
        shaders.put("pencil", getShader("pencil"));
        shaders.put("phosphor", getShader("phosphor"));
        shaders.put("sobel", getShader("sobel"));
        shaders.put("spider", getShader("spider"));
        shaders.put("wobble", getShader("wobble"));
    }

    private final HashMap<String, ManagedShaderEffect> shaders = new HashMap<>();

    private ManagedShaderEffect getShader(String name) {
        return ShaderEffectManager.getInstance().manage(Identifier.of("shaders/post/" + name + ".json"));
    }

    private void checkAndRenderShader(BooleanSetting check, float tickDelta) {
        if (check.getValue())
            shaders.get(check.getName()).render(tickDelta);
    }
}
