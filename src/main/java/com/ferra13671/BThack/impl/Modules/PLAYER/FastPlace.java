package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.mixins.accessor.IMinecraftClient;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.item.BlockItem;

import java.util.Arrays;

public class FastPlace extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", this, Arrays.asList("Normal", "Ultra"));
    public final NumberSetting times = new NumberSetting("Times", this, 30, 5, 64, true, () -> mode.getValue().equals("Ultra"));

    public FastPlace() {
        super("FastPlace",
                "lang.module.FastPlace",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );

        initSettings(
                mode,
                times
        );
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        if (mc.player.getMainHandStack().getItem() instanceof BlockItem) {
            switch (mode.getValue()) {
                case "Normal" -> mc.itemUseCooldown = 0;
                case "Ultra" -> {
                    if (mc.options.useKey.isPressed()) {
                        for (int i = 0; i < times.getValue().intValue(); i++) {
                            ((IMinecraftClient) mc).useItem();
                            mc.itemUseCooldown = 0;
                        }
                    }
                }
            }
        }
    }
}
