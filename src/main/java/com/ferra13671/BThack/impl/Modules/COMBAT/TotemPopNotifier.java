package com.ferra13671.BThack.impl.Modules.COMBAT;

import com.ferra13671.BThack.api.Events.Entity.TotemPopEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Social.Clans.ClanSettingsBuilder;
import com.ferra13671.BThack.api.Social.SocialManagers;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.KillAuraUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.player.PlayerEntity;

public class TotemPopNotifier extends Module {

    public final BooleanSetting sendToPublic = new BooleanSetting("Send To Public", this, false);

    public final BooleanSetting checkYourself = new BooleanSetting("Check Yourself", this, false);

    public final BooleanSetting friends = new BooleanSetting("Friends", this, false);

    public final BooleanSetting clanManager = ClanSettingsBuilder.buildToggle(this);
    public final ModeSetting clanMode = ClanSettingsBuilder.buildStatusMode(this, clanManager);
    public final ModeSetting targetClan = ClanSettingsBuilder.buildClanTargetMode(this, clanManager, clanMode);

    public TotemPopNotifier() {
        super("TotemPopNotifier",
                "lang.module.TotemPopNotifier",
                KeyboardUtils.RELEASE,
                MCategory.COMBAT,
                false
        );

        initSettings(
                sendToPublic,
                checkYourself,
                friends,
                clanManager,
                clanMode,
                targetClan
        );
    }

    @EventSubscriber
    public void onTotemPop(TotemPopEvent e) {
        if (checkYourself.getValue())
            if (e.entity == mc.player) return;
        if (friends.getValue())
            if (SocialManagers.FRIENDS.contains((PlayerEntity) e.entity)) return;
        if (!KillAuraUtils.isSuccessfulClanMember((PlayerEntity) e.entity, clanManager.getValue(), clanMode.getValue(), targetClan.getValue())) return;

        String text = e.entity.getDisplayName().getString() + " just popped " + e.totemsPopped + " times!";
        if (sendToPublic.getValue())
            ChatUtils.sendChatMessage(text);
        else
            ChatUtils.sendMessage(text);
    }
}
