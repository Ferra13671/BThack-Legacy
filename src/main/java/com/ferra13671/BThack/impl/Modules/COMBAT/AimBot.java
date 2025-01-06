package com.ferra13671.BThack.impl.Modules.COMBAT;

import com.ferra13671.BThack.api.Events.Render.RenderWorldEvent;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Social.Clans.ClansUtils;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.KillAuraUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;

public class AimBot extends Module {

    public static NumberSetting range;
    public static BooleanSetting players;
    public static BooleanSetting mobs;
    public static BooleanSetting teammates;
    public static BooleanSetting friends;
    public static BooleanSetting ignoreWalls;

    public static BooleanSetting clanManager;
    public static ModeSetting clanMode;
    public static ModeSetting target;

    public AimBot() {
        super(
                "AimBot",
                "lang.module.AimBot",
                KeyboardUtils.RELEASE,
                MCategory.COMBAT,
                false
        );

        range = new NumberSetting("Range", this, 4.0,1,5,false);
        players = new BooleanSetting("Players", this, true);
        mobs = new BooleanSetting("Mobs", this, true);
        teammates = new BooleanSetting("Teammates", this, false);
        friends = new BooleanSetting("Friends", this, false);
        ignoreWalls = new BooleanSetting("Ignore Walls", this, false);

        clanManager = ClansUtils.getClanManagerSetting(this);
        clanMode = ClansUtils.getClanModeSetting(this, clanManager);
        target = ClansUtils.getClanTargetSetting(this, clanManager, clanMode);

        initSettings(
                range,
                players,
                mobs,
                teammates,
                friends,
                ignoreWalls,
                clanManager,
                clanMode,
                target
        );
    }

    @EventSubscriber
    public void onUpdate(RenderWorldEvent.End e) {
        if (nullCheck()) return;

        rotate(this.name);
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        arrayListInfo = "" + range.getValue();
    }

    public static void rotate(String mod) {

        PlayerEntity player = KillAuraUtils.filterPlayers(range.getValue(), friends.getValue(), teammates.getValue(), clanManager.getValue(), clanMode.getValue(), target.getValue());

        Entity entity = KillAuraUtils.filterEntity(range.getValue());

        if (players.getValue() && player != null && KillAuraUtils.canBeSeeTarget(ignoreWalls, player)) {
            AimBotUtils.rotateToEntity(player);
        }
        if (mobs.getValue()) {
            if (entity != null && KillAuraUtils.canBeSeeTarget(ignoreWalls, entity) && entity.isAlive() && !(entity instanceof ItemEntity)) {
                AimBotUtils.rotateToEntity(entity);
            }
        }
    }
}
