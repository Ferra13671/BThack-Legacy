package com.ferra13671.BThack.impl.Modules.Misc;

import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "VisualRange", description = "lang.module.VisualRange", category = "MISC")
public class VisualRange extends Module {

    public final BooleanSetting friends = new BooleanSetting("Friends", this, true);

    public final BooleanSetting enter = new BooleanSetting("Enter", this, true);
    public final BooleanSetting enterSound = new BooleanSetting("Enter Sound", this, false);

    public final BooleanSetting leave = new BooleanSetting("Leave", this, true);
    public final BooleanSetting leaveSound = new BooleanSetting("Leave Sound", this, false);


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
                        if (Managers.FRIENDS_MANAGER.contains(player)) return;

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
                        if (Managers.FRIENDS_MANAGER.contains(player)) return;

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
