package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.core.Render.BThackMatrix;
import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.core.Render.Font.FontUtils;
import com.ferra13671.BThack.core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Animation.Animation;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.api.Module.HudComponent;
import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import net.minecraft.client.MinecraftClient;

import java.io.Closeable;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ModuleInfo(name = "ArrayList", category = "HUD", autoEnabled = true)
public class ArrayListComponent extends HudComponent {
    protected static ArrayListComponent INSTANCE;

    public final BooleanSetting drawRects = new BooleanSetting("Draw Rects", this, true);
    public final BooleanSetting backGround = new BooleanSetting("BackGround", this, true);
    public final NumberSetting backGroundAlpha = new NumberSetting("BGAlpha", this, 170, 20, 255, true, backGround::getValue);

    public ArrayListComponent() {
        super(
                MinecraftClient.getInstance().getWindow().getScaledWidth(),
                5
        );

        INSTANCE = this;
    }
    private static List<ArrayListModule> modules = new CopyOnWriteArrayList<>();

    public static void addModule(Module module) {
        if (!module.isVisible()) return;
        ArrayListModule arrayListModule = new ArrayListComponent.ArrayListModule(module);
        modules.removeIf(alm -> alm.equals(arrayListModule));
        modules.add(arrayListModule);
        sort();
    }

    public static void removeModule(Module module) {
        modules.forEach(arrayListModule -> {
            if (arrayListModule.getModule().equals(module) && !arrayListModule.closing) arrayListModule.close();
        });
    }

    public static void updateSizes() {
        ArrayListComponent.modules.forEach(ArrayListComponent.ArrayListModule::reloadSizes);
        sort();
    }

    private static void sort() {
        float newMaxLength = 0;
        for (ArrayListModule ars : modules) {
            if (ars.getLength() > newMaxLength) newMaxLength = ars.getLength();
        }
        modules = new CopyOnWriteArrayList<>(modules.stream().sorted((module1, module2) -> (int) ((module1.getLength() - module2.getLength()) * 100)).toList());
        Collections.reverse(modules);
        if (INSTANCE != null)
            INSTANCE.width = -(7 + newMaxLength);
    }

    @Override
    public void tick() {
        modules.removeIf(ArrayListModule::needRemove);
        modules.forEach(ArrayListModule::update);
    }

    @Override
    public void render() {
        int y = (int) this.getY();

        int count = 1;

        for (ArrayListModule arrayListModule : modules) {
            arrayListModule.render(getX(), y, count);

            y += 10;
            count++;
        }

        this.height = count * 10;
    }

    public int getArrayColor(int count) {
        if (ModuleList.HUD.gradient.getValue()) return ColorUtils.gradient(ModuleList.HUD.color1.getValue(), ModuleList.HUD.color2.getValue(), count, ModuleList.HUD.scale.getValue().floatValue(), ModuleList.HUD.speed.getValue().floatValue()).hashCode();
        else if (ModuleList.HUD.rainbow.getValue()) return ColorUtils.rainbow(count * ModuleList.HUD.scale.getValue().intValue(), ModuleList.HUD.speed.getValue().floatValue());
        else return ModuleList.HUD.color.getValue().hashCode();
    }


    public static class ArrayListModule implements Closeable {
        private final Animation moveAnimation = new Animation(Easing.SINE_OUT, 400);
        private final Animation alphaAnimation = new Animation(Easing.LINEAR, 200);
        private final Module module;

        private String lastText;
        private float length;
        private float height;
        private boolean closing = false;

        public ArrayListModule(Module module) {
            this.module = module;
            lastText = module.getArrayListName();
            length = FontUtils.getTextWidth(lastText);
            height = FontUtils.getTextHeight(lastText);
        }

        public void render(float x, float y, int count) {
            BThackMatrix.push();
            BThackMatrix.translate((float) (closing ? moveAnimation.getEase() : 1 - moveAnimation.getEase()) * length, 0, 0);

            double alpha = closing ? (1 - alphaAnimation.getEase()) : alphaAnimation.getEase();
            int arrayColor = ColorUtils.integrateAlpha(INSTANCE.getArrayColor(count), (int) (255 * alpha));

            if (INSTANCE.backGround.getValue())
                BThackRender.drawRect(x - 6 - length, y, x, y + 10, ColorUtils.fastRGBA(0, 0, 0, (int) (INSTANCE.backGroundAlpha.getValue() * alpha)));
            if (INSTANCE.drawRects.getValue())
                BThackRender.drawRect(x - 2, y, x, y + 10, arrayColor);
            BThackRender.drawString(lastText, (int) (x - 4 - length), (int) (y + 5 - (height / 2)), ColorUtils.integrateAlpha(arrayColor, (int) (255 * alpha)));

            BThackMatrix.pop();
        }

        public void update() {
            if (!lastText.equals(module.getArrayListName())) {
                lastText = module.getArrayListName();
                reloadSizes();
                sort();
            }
        }

        public void reloadSizes() {
            length = FontUtils.getTextWidth(lastText);
            height = FontUtils.getTextHeight(lastText);
        }

        public boolean needRemove() {
            return closing && alphaAnimation.getEase() >= 1;
        }

        public float getLength() {
            return length;
        }

        public Module getModule() {
            return module;
        }

        @Override
        public void close() {
            closing = true;
            alphaAnimation.reset();
            moveAnimation.reset();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof ArrayListModule ars)) return false;
            return ars.module.equals(this.module);
        }
    }
}
