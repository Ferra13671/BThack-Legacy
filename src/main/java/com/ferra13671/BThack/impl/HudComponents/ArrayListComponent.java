package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Drawers.Drawers;
import com.ferra13671.BThack.Core.Render.Font.FontUtils;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Module.HudComponent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ArrayListComponent extends HudComponent {

    private final BooleanSetting drawRects = new BooleanSetting("Draw Rects", this, true);
    private final BooleanSetting backGround = new BooleanSetting("BackGround", this, true);
    private final NumberSetting backGroundAlpha = new NumberSetting("BGAlpha", this, 170, 20, 255, true, backGround::getValue);

    public ArrayListComponent() {
        super("ArrayList",
                MinecraftClient.getInstance().getWindow().getScaledWidth(),
                5,
                true
        );

        initSettings(
                drawRects,
                backGround,
                backGroundAlpha
        );
    }

    private final List<String> moduleStrings = new ArrayList<>();

    @Override
    public void tick() {
        moduleStrings.clear();
        List<String> tempList = new ArrayList<>();
        float newMaxLength = 0;
        for (Module module : Client.getAllModules()) {
            if (module.isEnabled() && module.visible) {
                String moduleName = !module.arrayListInfo.isEmpty() ? module.name + " " + Formatting.GRAY + "[" + Formatting.WHITE +  module.arrayListInfo + Formatting.GRAY + "]" : module.name;
                tempList.add(moduleName);
                float length = FontUtils.getTextWidth(moduleName);
                if (length > newMaxLength)
                    newMaxLength = length;
            }
        }
        moduleStrings.addAll(tempList.stream().sorted((string1, string2) -> (int) ((FontUtils.getTextWidth(string2) - FontUtils.getTextWidth(string1)) * 100)).toList());
        width = -(7 + newMaxLength);
    }

    @Override
    public void render() {
        int y = (int) this.getY();

        int count = 1;

        ArrayList<Runnable> backgroundDrawers = new ArrayList<>();
        ArrayList<Runnable> rectDrawers = new ArrayList<>();
        ArrayList<Runnable> textDrawers = new ArrayList<>();
        for (String string : moduleStrings) {
            final int fY = y;
            final int fCount = count;

            if (backGround.getValue())
                backgroundDrawers.add(() -> Drawers.RECT.draw((int) (getX() - 6 - FontUtils.getTextWidth(string)), fY, (int) getX(), fY + 10));
            if (drawRects.getValue())
                rectDrawers.add(() -> BThackRender.drawRect((int) getX() - 2, fY, (int) getX(), fY + 10, getArrayColor(fCount)));
            textDrawers.add(() -> drawText(string, (int) (getX() - 4 - FontUtils.getTextWidth(string)), (int) (fY + 5 - (FontUtils.getTextHeight(string) / 2d)), getArrayColor(fCount)));

            y += 10;
            count++;
        }
        if (!backgroundDrawers.isEmpty()) {
            Drawers.RECT.begin(ColorUtils.fastRGBA(0, 0, 0, (int) backGroundAlpha.getValue()));
            backgroundDrawers.forEach(Runnable::run);
            Drawers.RECT.end();
        }
        if (!rectDrawers.isEmpty())
            rectDrawers.forEach(Runnable::run);
        if (!textDrawers.isEmpty())
            textDrawers.forEach(Runnable::run);

        this.height = count * 10;
    }

    public int getArrayColor(int count) {
        if (ModuleList.HUD.rainbow.getValue()) {
            return ColorUtils.rainbowType((int) ModuleList.HUD.rainbowType.getValue(), count);
        } else {
            if (ModuleList.clickGui.customColor.getValue()) return ClickGui.getClickGuiColor(false);
            else return (new Color(Client.clientInfo.getColorTheme().arrayListColor())).hashCode();
        }
    }
}
