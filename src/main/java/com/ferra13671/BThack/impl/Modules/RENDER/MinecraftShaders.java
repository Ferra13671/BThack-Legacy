package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.HashMap;

public class MinecraftShaders extends Module {

    public static ModeSetting mode;


    public static ModeSetting shader;


    public static BooleanSetting art;
    public static BooleanSetting bits;
    public static BooleanSetting blobs;
    public static BooleanSetting blobs2;
    public static BooleanSetting bumpy;
    public static BooleanSetting color_convolve;
    public static BooleanSetting creeper;
    public static BooleanSetting deconverge;
    public static BooleanSetting desaturate;
    public static BooleanSetting green;
    public static BooleanSetting notch;
    public static BooleanSetting ntsc;
    public static BooleanSetting pencil;
    public static BooleanSetting phosphor;
    public static BooleanSetting sobel;
    public static BooleanSetting spider;
    public static BooleanSetting wobble;

    public MinecraftShaders() {
        super("MinecraftShaders",
                "lang.module.MinecraftShaders",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        mode = new ModeSetting("Mode", this, Arrays.asList("One", "Some"));


        shader = new ModeSetting("Shader", this, Arrays.asList(
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

        art = new BooleanSetting("art", this, false, () -> mode.getValue().equals("Some"));
        bits = new BooleanSetting("bits", this, false, () -> mode.getValue().equals("Some"));
        blobs = new BooleanSetting("blobs", this, false, () -> mode.getValue().equals("Some"));
        blobs2 = new BooleanSetting("blobs2", this, false, () -> mode.getValue().equals("Some"));
        bumpy = new BooleanSetting("bumpy", this, false, () -> mode.getValue().equals("Some"));
        color_convolve = new BooleanSetting("color_convolve", this, false, () -> mode.getValue().equals("Some"));
        creeper = new BooleanSetting("creeper", this, false, () -> mode.getValue().equals("Some"));
        deconverge = new BooleanSetting("deconverge", this, false, () -> mode.getValue().equals("Some"));
        desaturate = new BooleanSetting("desaturate", this, false, () -> mode.getValue().equals("Some"));
        green = new BooleanSetting("green", this, false, () -> mode.getValue().equals("Some"));
        notch = new BooleanSetting("notch", this, false, () -> mode.getValue().equals("Some"));
        ntsc = new BooleanSetting("ntsc", this, false, () -> mode.getValue().equals("Some"));
        pencil = new BooleanSetting("pencil", this, false, () -> mode.getValue().equals("Some"));
        phosphor = new BooleanSetting("phosphor", this, false, () -> mode.getValue().equals("Some"));
        sobel = new BooleanSetting("sobel", this, false, () -> mode.getValue().equals("Some"));
        spider = new BooleanSetting("spider", this, false, () -> mode.getValue().equals("Some"));
        wobble = new BooleanSetting("wobble", this, false, () -> mode.getValue().equals("Some"));

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
        return ShaderEffectManager.getInstance().manage(new Identifier("shaders/post/" + name + ".json"));
    }

    private void checkAndRenderShader(BooleanSetting check, float tickDelta) {
        if (check.getValue())
            shaders.get(check.getName()).render(tickDelta);
    }
}
