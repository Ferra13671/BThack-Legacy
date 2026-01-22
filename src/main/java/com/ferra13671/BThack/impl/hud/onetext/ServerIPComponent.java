package com.ferra13671.BThack.impl.hud.onetext;

import com.ferra13671.BThack.managers.impl.setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.impl.hud.AbstractOneTextComponent;
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
