package com.ferra13671.BThack.impl.HudComponents.OneTextComponents;

import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.impl.HudComponents.AbstractOneTextComponent;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.Objects;

public class PingComponent extends AbstractOneTextComponent {
    private final ModeSetting mode;

    public PingComponent() {
        super("Ping",
                5,
                115,
                true
        );

        mode = new ModeSetting("Ping Mode", this, Arrays.asList("Normal", "Short"));

        initSettings(
                mode
        );
    }

    @Override
    public String getText() {
        return switch (mode.getValue()) {
            case "Normal" -> "Ping " + Formatting.WHITE + getPing() + "ms";
            case "Short" -> "" + Formatting.WHITE + getPing() + "ms";
            default -> "";
        };
    }

    private int getPing() {
        if (mc.player != null && mc.getNetworkHandler() != null && mc.getNetworkHandler().getPlayerListEntry(mc.player.getGameProfile().getName()) != null) {
            return Objects.requireNonNull(mc.getNetworkHandler().getPlayerListEntry(mc.player.getGameProfile().getName())).getLatency();
        }

        return -1;
    }
}
