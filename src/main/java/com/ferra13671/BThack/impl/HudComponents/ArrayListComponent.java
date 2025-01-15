package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.HudComponent.HudComponent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ArrayListComponent extends HudComponent {

    private final BooleanSetting drawRects;

    public ArrayListComponent() {
        super("ArrayList",
                MinecraftClient.getInstance().getWindow().getScaledWidth(),
                5,
                true
        );

        drawRects = new BooleanSetting("Draw Rects", this, true);

        initSettings(
                drawRects
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
                float length = mc.textRenderer.getWidth(moduleName);
                if (length > newMaxLength)
                    newMaxLength = length;
            }
        }
        moduleStrings.addAll(tempList.stream().sorted((string1, string2) -> mc.textRenderer.getWidth(string2) - mc.textRenderer.getWidth(string1)).toList());
        width = -(7 + newMaxLength);
    }

    @Override
    public void render() {
        int y = (int) this.getY();

        int count = 1;

        for (String string : moduleStrings) {
            if (ModuleList.HUD.rainbow.getValue()) {
                if (drawRects.getValue())
                    BThackRender.drawRect((int) getX(), y, (int) getX() - 2, y + 10, getArrayColor(count));
                drawText(string, (int) (getX() - 4 - mc.textRenderer.getWidth(string)), y, getArrayColor(count));
            } else {
                if (drawRects.getValue())
                    BThackRender.drawRect((int) getX(), y, (int) getX() - 2, y + 10, getArrayColor(count));
                drawText(string, ((int) getX() - 4 - mc.textRenderer.getWidth(string)), y, getArrayColor(count));
            }

            y += 10;
            count++;
        }

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
