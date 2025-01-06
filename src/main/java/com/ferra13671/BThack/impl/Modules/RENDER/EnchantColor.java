package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.Core.Render.Utils.RainbowUtils;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import net.minecraft.util.math.ColorHelper;

public class EnchantColor extends Module {

    public static NumberSetting enchantSpeed;
    public static NumberSetting enchantSize;

    public static NumberSetting alphaColor;
    public static NumberSetting redColor;
    public static NumberSetting greenColor;
    public static NumberSetting blueColor;

    public static BooleanSetting rainbow;
    public static NumberSetting rainbowSpeed;

    public EnchantColor() {
        super("EnchantColor",
                "lang.module.EnchantColor",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        enchantSpeed = new NumberSetting("Ench. Speed", this, 1, 0, 2, false);
        enchantSize = new NumberSetting("Ench. Size", this, 1, 0.1, 5, false);

        alphaColor = new NumberSetting("Alpha", this, 180, 0, 255, true);

        redColor = new NumberSetting("Red", this, 255, 0, 255, true, () -> !rainbow.getValue());
        greenColor = new NumberSetting("Green", this, 255, 0, 255, true, () -> !rainbow.getValue());
        blueColor = new NumberSetting("Blue", this, 255, 0, 255, true, () -> !rainbow.getValue());

        rainbow = new BooleanSetting("Rainbow", this, false);
        rainbowSpeed = new NumberSetting("Rainbow Speed", this, 2, 1, 4, true, rainbow::getValue);

        initSettings(
                enchantSpeed,
                enchantSize,

                alphaColor,

                redColor,
                greenColor,
                blueColor,

                rainbow,
                rainbowSpeed
        );
    }

    public static float[] getEnchantColor() {
        float red;
        float green;
        float blue;
        float alpha = (float) (alphaColor.getValue() / 255d);

        if (!rainbow.getValue()) {
            red = (float) (redColor.getValue() / 255d);
            green = (float) (greenColor.getValue() / 255d);
            blue = (float) (blueColor.getValue() / 255d);
        } else {
            int rainbowType = (int) rainbowSpeed.getValue();
            float speed = RainbowUtils.getRainbowRectSpeed(rainbowType)[0];
            int delay = (int) RainbowUtils.getRainbowRectSpeed(rainbowType)[1];
            int argb = ColorUtils.rainbow(delay, speed);
            red = ColorHelper.Argb.getRed(argb) / 255f;
            green = ColorHelper.Argb.getGreen(argb) / 255f;
            blue = ColorHelper.Argb.getBlue(argb) / 255f;
        }
        return new float[]{red, green, blue, alpha};
    }








    /*
    This method crashes minecraft when entering the world,
     for now let it be here, may be useful someday  :/
     */

    //public static final Identifier itemEnch = new Identifier("bthack", "enchant/enchanted_glint_item.png");
    //public static final Identifier entityEnch = new Identifier("bthack", "enchant/enchanted_glint_entity.png");

    /*
    @Override
    public void onEnable() {
        super.onEnable();

        RenderSystem.recordRenderCall(() -> {
            RenderLayer.ARMOR_GLINT = RenderLayer.of(
                    "armor_glint",
                    VertexFormats.POSITION_TEXTURE,
                    VertexFormat.DrawMode.QUADS,
                    1536,
                    RenderLayer.MultiPhaseParameters.builder().program(ARMOR_GLINT_PROGRAM).
                            texture(new RenderPhase.Texture(entityEnch, true, false)).
                            writeMaskState(COLOR_MASK).
                            cull(DISABLE_CULLING).
                            depthTest(EQUAL_DEPTH_TEST).
                            transparency(GLINT_TRANSPARENCY).
                            texturing(GLINT_TEXTURING).
                            layering(VIEW_OFFSET_Z_LAYERING).
                            build(false));
            RenderLayer.ARMOR_GLINT.startDrawing();

            RenderLayer.ARMOR_ENTITY_GLINT = RenderLayer.of(
                    "armor_entity_glint",
                    VertexFormats.POSITION_TEXTURE,
                    VertexFormat.DrawMode.QUADS,
                    1536,
                    RenderLayer.MultiPhaseParameters.builder().program(ARMOR_ENTITY_GLINT_PROGRAM).
                            texture(new RenderPhase.Texture(entityEnch, true, false)).
                            writeMaskState(COLOR_MASK).
                            cull(DISABLE_CULLING).
                            depthTest(EQUAL_DEPTH_TEST).
                            transparency(GLINT_TRANSPARENCY).
                            texturing(ENTITY_GLINT_TEXTURING).
                            layering(VIEW_OFFSET_Z_LAYERING).
                            build(false));
            RenderLayer.ARMOR_ENTITY_GLINT.startDrawing();

            RenderLayer.GLINT_TRANSLUCENT = RenderLayer.of(
                    "glint_translucent",
                    VertexFormats.POSITION_TEXTURE,
                    VertexFormat.DrawMode.QUADS,
                    1536,
                    RenderLayer.MultiPhaseParameters.builder().program(TRANSLUCENT_GLINT_PROGRAM).
                            texture(new RenderPhase.Texture(itemEnch, true, false)).
                            writeMaskState(COLOR_MASK).
                            cull(DISABLE_CULLING).
                            depthTest(EQUAL_DEPTH_TEST).
                            transparency(GLINT_TRANSPARENCY).
                            texturing(GLINT_TEXTURING).
                            target(ITEM_ENTITY_TARGET).
                            build(false));
            RenderLayer.GLINT_TRANSLUCENT.startDrawing();

            RenderLayer.GLINT = RenderLayer.of(
                    "glint",
                    VertexFormats.POSITION_TEXTURE,
                    VertexFormat.DrawMode.QUADS,
                    1536,
                    RenderLayer.MultiPhaseParameters.builder().program(GLINT_PROGRAM).
                            texture(new RenderPhase.Texture(itemEnch, true, false)).
                            writeMaskState(COLOR_MASK).
                            cull(DISABLE_CULLING).
                            depthTest(EQUAL_DEPTH_TEST).
                            transparency(GLINT_TRANSPARENCY).
                            texturing(GLINT_TEXTURING).
                            build(false));
            RenderLayer.GLINT.startDrawing();

            RenderLayer.DIRECT_GLINT = RenderLayer.of(
                    "glint_direct",
                    VertexFormats.POSITION_TEXTURE,
                    VertexFormat.DrawMode.QUADS,
                    1536,
                    RenderLayer.MultiPhaseParameters.builder().program(DIRECT_GLINT_PROGRAM).
                            texture(new RenderPhase.Texture(itemEnch, true, false)).
                            writeMaskState(COLOR_MASK).
                            cull(DISABLE_CULLING).
                            depthTest(EQUAL_DEPTH_TEST).
                            transparency(GLINT_TRANSPARENCY).
                            texturing(GLINT_TEXTURING).
                            build(false));
            RenderLayer.DIRECT_GLINT.startDrawing();

            RenderLayer.ENTITY_GLINT = RenderLayer.of(
                    "entity_glint",
                    VertexFormats.POSITION_TEXTURE,
                    VertexFormat.DrawMode.QUADS,
                    1536,
                    RenderLayer.MultiPhaseParameters.builder().program(ENTITY_GLINT_PROGRAM).
                            texture(new RenderPhase.Texture(entityEnch, true, false)).
                            writeMaskState(COLOR_MASK).
                            cull(DISABLE_CULLING).
                            depthTest(EQUAL_DEPTH_TEST).
                            transparency(GLINT_TRANSPARENCY).
                            target(ITEM_ENTITY_TARGET).
                            texturing(ENTITY_GLINT_TEXTURING).
                            build(false));
            RenderLayer.ENTITY_GLINT.startDrawing();

            RenderLayer.DIRECT_ENTITY_GLINT= RenderLayer.of(
                    "entity_glint_direct",
                    VertexFormats.POSITION_TEXTURE,
                    VertexFormat.DrawMode.QUADS,
                    1536,
                    RenderLayer.MultiPhaseParameters.builder().program(DIRECT_ENTITY_GLINT_PROGRAM).
                            texture(new RenderPhase.Texture(entityEnch, true, false)).
                            writeMaskState(COLOR_MASK).
                            cull(DISABLE_CULLING).
                            depthTest(EQUAL_DEPTH_TEST).
                            transparency(GLINT_TRANSPARENCY).
                            texturing(ENTITY_GLINT_TEXTURING).
                            build(false));
            RenderLayer.DIRECT_ENTITY_GLINT.startDrawing();
        });
    }

    @Override
    public void onDisable() {
        super.onDisable();
        RenderSystem.recordRenderCall(() -> {
            RenderLayer.ARMOR_GLINT = RenderLayer.of(
                    "armor_glint",
                    VertexFormats.POSITION_TEXTURE,
                    VertexFormat.DrawMode.QUADS,
                    1536,
                    RenderLayer.MultiPhaseParameters.builder().program(ARMOR_GLINT_PROGRAM).
                            texture(new RenderPhase.Texture(entityEnch, true, false)).
                            writeMaskState(COLOR_MASK).
                            cull(DISABLE_CULLING).
                            depthTest(EQUAL_DEPTH_TEST).
                            transparency(GLINT_TRANSPARENCY).
                            texturing(GLINT_TEXTURING).
                            layering(VIEW_OFFSET_Z_LAYERING).
                            build(false));
            RenderLayer.ARMOR_GLINT.startDrawing();

            RenderLayer.ARMOR_ENTITY_GLINT = RenderLayer.of(
                    "armor_entity_glint",
                    VertexFormats.POSITION_TEXTURE,
                    VertexFormat.DrawMode.QUADS,
                    1536,
                    RenderLayer.MultiPhaseParameters.builder().program(ARMOR_ENTITY_GLINT_PROGRAM).
                            texture(new RenderPhase.Texture(entityEnch, true, false)).
                            writeMaskState(COLOR_MASK).
                            cull(DISABLE_CULLING).
                            depthTest(EQUAL_DEPTH_TEST).
                            transparency(GLINT_TRANSPARENCY).
                            texturing(ENTITY_GLINT_TEXTURING).
                            layering(VIEW_OFFSET_Z_LAYERING).
                            build(false));
            RenderLayer.ARMOR_ENTITY_GLINT.startDrawing();

            RenderLayer.GLINT_TRANSLUCENT = RenderLayer.of(
                    "glint_translucent",
                    VertexFormats.POSITION_TEXTURE,
                    VertexFormat.DrawMode.QUADS,
                    1536,
                    RenderLayer.MultiPhaseParameters.builder().program(TRANSLUCENT_GLINT_PROGRAM).
                            texture(new RenderPhase.Texture(itemEnch, true, false)).
                            writeMaskState(COLOR_MASK).
                            cull(DISABLE_CULLING).
                            depthTest(EQUAL_DEPTH_TEST).
                            transparency(GLINT_TRANSPARENCY).
                            texturing(GLINT_TEXTURING).
                            target(ITEM_ENTITY_TARGET).
                            build(false));
            RenderLayer.GLINT_TRANSLUCENT.startDrawing();

            RenderLayer.GLINT = RenderLayer.of(
                    "glint",
                    VertexFormats.POSITION_TEXTURE,
                    VertexFormat.DrawMode.QUADS,
                    1536,
                    RenderLayer.MultiPhaseParameters.builder().program(GLINT_PROGRAM).
                            texture(new RenderPhase.Texture(itemEnch, true, false)).
                            writeMaskState(COLOR_MASK).
                            cull(DISABLE_CULLING).
                            depthTest(EQUAL_DEPTH_TEST).
                            transparency(GLINT_TRANSPARENCY).
                            texturing(GLINT_TEXTURING).
                            build(false));
            RenderLayer.GLINT.startDrawing();

            RenderLayer.DIRECT_GLINT = RenderLayer.of(
                    "glint_direct",
                    VertexFormats.POSITION_TEXTURE,
                    VertexFormat.DrawMode.QUADS,
                    1536,
                    RenderLayer.MultiPhaseParameters.builder().program(DIRECT_GLINT_PROGRAM).
                            texture(new RenderPhase.Texture(itemEnch, true, false)).
                            writeMaskState(COLOR_MASK).
                            cull(DISABLE_CULLING).
                            depthTest(EQUAL_DEPTH_TEST).
                            transparency(GLINT_TRANSPARENCY).
                            texturing(GLINT_TEXTURING).
                            build(false));
            RenderLayer.DIRECT_GLINT.startDrawing();

            RenderLayer.ENTITY_GLINT = RenderLayer.of(
                    "entity_glint",
                    VertexFormats.POSITION_TEXTURE,
                    VertexFormat.DrawMode.QUADS,
                    1536,
                    RenderLayer.MultiPhaseParameters.builder().program(ENTITY_GLINT_PROGRAM).
                            texture(new RenderPhase.Texture(entityEnch, true, false)).
                            writeMaskState(COLOR_MASK).
                            cull(DISABLE_CULLING).
                            depthTest(EQUAL_DEPTH_TEST).
                            transparency(GLINT_TRANSPARENCY).
                            target(ITEM_ENTITY_TARGET).
                            texturing(ENTITY_GLINT_TEXTURING).
                            build(false));
            RenderLayer.ENTITY_GLINT.startDrawing();

            RenderLayer.DIRECT_ENTITY_GLINT = RenderLayer.of(
                    "entity_glint_direct",
                    VertexFormats.POSITION_TEXTURE,
                    VertexFormat.DrawMode.QUADS,
                    1536,
                    RenderLayer.MultiPhaseParameters.builder().program(DIRECT_ENTITY_GLINT_PROGRAM).
                            texture(new RenderPhase.Texture(entityEnch, true, false)).
                            writeMaskState(COLOR_MASK).
                            cull(DISABLE_CULLING).
                            depthTest(EQUAL_DEPTH_TEST).
                            transparency(GLINT_TRANSPARENCY).
                            texturing(ENTITY_GLINT_TEXTURING).
                            build(false));
            RenderLayer.DIRECT_ENTITY_GLINT.startDrawing();
        });
    }

     */
}
