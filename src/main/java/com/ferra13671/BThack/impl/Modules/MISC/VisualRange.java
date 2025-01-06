package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Social.SocialManagers;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class VisualRange extends Module {

    public BooleanSetting friends;

    public BooleanSetting enter;
    public BooleanSetting enterSound;

    public BooleanSetting leave;
    public BooleanSetting leaveSound;

    public VisualRange() {
        super("VisualRange",
                "lang.module.VisualRange",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        friends = new BooleanSetting("Friends", this, true);

        enter = new BooleanSetting("Enter", this, true);
        enterSound = new BooleanSetting("Enter Sound", this, false);

        leave = new BooleanSetting("Leave", this, true);
        leaveSound = new BooleanSetting("Leave Sound", this, false);

        initSettings(
                friends,

                enter,
                enterSound,

                leave,
                leaveSound
        );
    }

    private final List<String> players = new ArrayList<>();

    @Override
    public void onEnable() {
        super.onEnable();
        players.clear();
    }

    public void onAddEntity(Entity entity) {
        if (entity instanceof PlayerEntity player) {
            String playerName = entity.getDisplayName().getString();
            if (player != mc.player && !players.contains(playerName)) {
                if (enter.getValue()) {
                    if (!friends.getValue())
                        if (SocialManagers.FRIENDS.contains(player)) return;

                    String text = getChatName() + String.format(LanguageSystem.translate("lang.module.VisualRange.playerEntered"), playerName + Formatting.RESET + Formatting.GOLD);
                    if (enterSound.getValue())
                        ChatUtils.sendMessage(text, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP);
                    else
                        ChatUtils.sendMessage(text);
                }
                players.add(playerName);
            }
        }
    }

    public void onRemoveEntity(Entity entity) {
        if (entity instanceof PlayerEntity player) {
            String playerName = entity.getDisplayName().getString();
            if (player != mc.player && players.contains(playerName)) {
                if (leave.getValue()) {
                    if (!friends.getValue())
                        if (SocialManagers.FRIENDS.contains(player)) return;

                    String text = getChatName() + String.format(LanguageSystem.translate("lang.module.VisualRange.playerLeaved"), playerName + Formatting.RESET + Formatting.GOLD);
                    if (leaveSound.getValue())
                        ChatUtils.sendMessage(text, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP);
                    else
                        ChatUtils.sendMessage(text);
                }
                players.remove(playerName);
            }
        }
    }
}
