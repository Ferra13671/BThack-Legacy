package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.api.Events.InputEvent;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.impl.Modules.PLAYER.AutoFirework;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;

public class MiddleClick extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", this, Arrays.asList("Friend", "Pearl", "Firework"));
    public final BooleanSetting swingHand = new BooleanSetting("Swing Hand", this, false, () -> mode.getValue().equals("Pearl") || mode.getValue().equals("Firework"));

    public MiddleClick() {
        super("MiddleClick",
                "lang.module.MiddleClick",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );
    }

    @Override
    public void onChangeSetting(Setting<?> setting) {
        arrayListInfo = mode.getValue();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        arrayListInfo = mode.getValue();
    }

    @EventSubscriber
    public void onMouse(InputEvent.MouseInputEvent e) {
        if (e.getButton() == GLFW.GLFW_MOUSE_BUTTON_MIDDLE && e.getAction() == GLFW.GLFW_PRESS && mc.currentScreen == null) {
            switch (mode.getValue()) {
                case "Friend" -> {
                    if (mc.targetedEntity instanceof PlayerEntity target && target.getDisplayName() != null) {
                        if (Managers.FRIENDS_MANAGER.contains(target))
                            Managers.FRIENDS_MANAGER.remove(target.getNameForScoreboard());
                        else
                            Managers.FRIENDS_MANAGER.add(target.getNameForScoreboard());
                    }
                }
                case "Pearl" -> ItemUtils.useItem(Items.ENDER_PEARL, swingHand.getValue(), mc.player.getYaw(), mc.player.getPitch());
                case "Firework" -> AutoFirework.useFirework(swingHand.getValue());
            }
        }
    }
}
