package com.ferra13671.BThack.impl.HudComponents.OneTextComponents;

import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.impl.HudComponents.AbstractOneTextComponent;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.Objects;

public class PingComponent extends AbstractOneTextComponent {

    public final ModeSetting mode = new ModeSetting("Ping Mode", this, Arrays.asList("Normal", "Short"));

    public PingComponent() {
        super("Ping",
                5,
                145,
                true
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
