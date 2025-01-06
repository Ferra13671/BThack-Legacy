package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.mixins.accessor.IMinecraftClient;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.item.BlockItem;

import java.util.Arrays;

public class FastPlace extends Module {

    public static ModeSetting mode;
    public static NumberSetting times;

    public FastPlace() {
        super("FastPlace",
                "lang.module.FastPlace",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );

        mode = new ModeSetting("Mode", this, Arrays.asList("Normal", "Ultra"));
        times = new NumberSetting("Times", this, 30, 5, 64, true, () -> mode.getValue().equals("Ultra"));

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
                        for (int i = 0; i < (int) times.getValue(); i++) {
                            ((IMinecraftClient) mc).useItem();
                            mc.itemUseCooldown = 0;
                        }
                    }
                }
            }
        }
    }
}
