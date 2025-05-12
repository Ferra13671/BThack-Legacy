package com.ferra13671.BThack.impl.HudComponents.OneTextComponents;

import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.impl.HudComponents.AbstractOneTextComponent;
import net.minecraft.util.Formatting;

import java.util.Objects;

@ModuleInfo(name = "ServerIP", category = "HUD", autoEnabled = true)
public class ServerIPComponent extends AbstractOneTextComponent {

    public final BooleanSetting isShort = new BooleanSetting("Short", this, false);

    public ServerIPComponent() {
        super(5, 115);
    }

    @Override
    public String getText() {
        return (isShort.getValue() ? "" : "IP: ") + Formatting.WHITE + (mc.isIntegratedServerRunning() ? "Singleplayer" : Objects.requireNonNull(mc.getCurrentServerEntry()).address);
    }

    public static String getIP() {
        return (mc.isIntegratedServerRunning() ? "Singleplayer" : (mc.getCurrentServerEntry() != null ? mc.getCurrentServerEntry().address : ""));
    }
}
