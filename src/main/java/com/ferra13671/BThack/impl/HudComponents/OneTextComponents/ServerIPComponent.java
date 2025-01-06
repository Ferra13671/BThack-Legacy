package com.ferra13671.BThack.impl.HudComponents.OneTextComponents;

import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.impl.HudComponents.AbstractOneTextComponent;
import net.minecraft.util.Formatting;

import java.util.Objects;

public class ServerIPComponent extends AbstractOneTextComponent {

    private final BooleanSetting isShort;

    public ServerIPComponent() {
        super("ServerIP",
                5,
                95,
                true
        );

        isShort = new BooleanSetting("Short", this, false);

        initSettings(
                isShort
        );
    }

    @Override
    public String getText() {
        return (isShort.getValue() ? "" : "IP: ") + Formatting.WHITE + (mc.isIntegratedServerRunning() ? "Singleplayer" : Objects.requireNonNull(mc.getCurrentServerEntry()).address);
    }

    public static String getIP() {
        return (mc.isIntegratedServerRunning() ? "Singleplayer" : (mc.getCurrentServerEntry() != null ? mc.getCurrentServerEntry().address : ""));
    }
}
