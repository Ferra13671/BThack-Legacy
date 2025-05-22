package com.ferra13671.BThack.impl.HudComponents.OneTextComponents;

import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.core.Client.Client;
import com.ferra13671.BThack.managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.impl.HudComponents.AbstractOneTextComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;

import java.util.Arrays;

@ModuleInfo(name = "RealTime", category = "HUD", autoEnabled = true)
public class RealTimeComponent extends AbstractOneTextComponent {

    public final ModeSetting mode = new ModeSetting("Hour Mode", this, Arrays.asList("24", "12"));

    public RealTimeComponent() {
        super(MinecraftClient.getInstance().getWindow().getScaledWidth() / 2f, 10);
    }

    @Override
    public String getText() {
        return "Real Time " + Formatting.WHITE + Client.getRealTime(mode.getValue());
    }
}
