package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.api.Events.InputEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Social.SocialManagers;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.impl.Modules.PLAYER.AutoFirework;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;

public class MiddleClick extends Module {

    public static ModeSetting mode;
    public static BooleanSetting swingHand;

    public MiddleClick() {
        super("MiddleClick",
                "lang.module.MiddleClick",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        mode = new ModeSetting("Mode", this, Arrays.asList("Friend", "Pearl", "Firework"));
        swingHand = new BooleanSetting("Swing Hand", this, false, () -> mode.getValue().equals("Pearl") || mode.getValue().equals("Firework"));

        initSettings(
                mode,
                swingHand
        );
    }

    @Override
    public void onChangeSetting(Setting setting) {
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
                        if (SocialManagers.FRIENDS.contains(target))
                            SocialManagers.FRIENDS.remove(target.getNameForScoreboard());
                        else
                            SocialManagers.FRIENDS.add(target.getNameForScoreboard());
                    }
                }
                case "Pearl" -> ItemUtils.useItem(Items.ENDER_PEARL, swingHand.getValue());
                case "Firework" -> AutoFirework.useFirework(swingHand.getValue());
            }
        }
    }
}
