package com.ferra13671.BThack.impl.Modules.Combat;

import com.ferra13671.BThack.events.Entity.TotemPopEvent;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.managers.managers.Clans.ClanSettingsBuilder;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.Modules.KillAuraUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Formatting;

@ModuleInfo(name = "TotemPopNotifier", description = "lang.module.TotemPopNotifier", category = "COMBAT")
public class TotemPopNotifier extends Module {

    public final BooleanSetting sendToPublic = new BooleanSetting("Send To Public", this, false);
    public final BooleanSetting yourselfAlso = new BooleanSetting("Yourself Also", this, false);
    public final BooleanSetting messageSound = new BooleanSetting("Message Sound", this, false);
    public final BooleanSetting friends = new BooleanSetting("Friends", this, false);

    public final BooleanSetting clanManager = ClanSettingsBuilder.buildToggle(this);
    public final ModeSetting clanMode = ClanSettingsBuilder.buildStatusMode(this, clanManager);
    public final ModeSetting targetClan = ClanSettingsBuilder.buildClanTargetMode(this, clanManager, clanMode);


    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onTotemPop(TotemPopEvent e) {
        if (!yourselfAlso.getValue() && e.entity == mc.player) return;
        if (friends.getValue() && Managers.FRIENDS_MANAGER.contains((PlayerEntity) e.entity)) return;
        if (!KillAuraUtils.isSuccessfulClanMember((PlayerEntity) e.entity, clanManager.getValue(), clanMode.getValue(), targetClan.getValue())) return;

        String text = "" + Formatting.WHITE + Formatting.BOLD + e.entity.getDisplayName().getString() + Formatting.RESET + Formatting.GOLD + " just popped " + Formatting.WHITE + Formatting.BOLD + e.totemsPopped + Formatting.RESET + Formatting.GOLD + " times!";
        if (messageSound.getValue())
            mc.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP);
        if (sendToPublic.getValue())
            ChatUtils.sendChatMessage(text);
        else
            ChatUtils.sendMessage(text);
    }
}
