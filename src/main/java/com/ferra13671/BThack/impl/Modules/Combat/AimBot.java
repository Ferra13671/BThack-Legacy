package com.ferra13671.BThack.impl.Modules.Combat;

import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.events.Render.RenderWorldLastEvent;
import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.managers.managers.Clans.ClanSettingsBuilder;
import com.ferra13671.BThack.api.Utils.Modules.KillAuraUtils;
import com.ferra13671.BThack.api.Utils.Rotate.RotateUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;

@ModuleInfo(name = "AimBot", description = "lang.module.AimBot", category = "COMBAT")
public class AimBot extends Module {

    public final NumberSetting range = new NumberSetting("Range", this, 4.0,1,5,false);
    public final BooleanSetting players = new BooleanSetting("Players", this, true);
    public final BooleanSetting mobs = new BooleanSetting("Mobs", this, true);
    public final BooleanSetting teammates = new BooleanSetting("Teammates", this, false);
    public final BooleanSetting friends = new BooleanSetting("Friends", this, false);
    public final BooleanSetting ignoreWalls = new BooleanSetting("Ignore Walls", this, false);

    public final BooleanSetting clanManager = ClanSettingsBuilder.buildToggle(this);
    public final ModeSetting clanMode = ClanSettingsBuilder.buildStatusMode(this, clanManager);
    public final ModeSetting target = ClanSettingsBuilder.buildClanTargetMode(this, clanManager, clanMode);

    @EventSubscriber
    public void onUpdate(RenderWorldLastEvent e) {
        if (nullCheck()) return;

        rotate();
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        arrayListInfo = "" + range.getValue();
    }

    public static void rotate() {

        PlayerEntity player = KillAuraUtils.filterPlayers(ModuleList.aimBot.range.getValue(), ModuleList.aimBot.friends.getValue(), ModuleList.aimBot.teammates.getValue(), ModuleList.aimBot.clanManager.getValue(), ModuleList.aimBot.clanMode.getValue(), ModuleList.aimBot.target.getValue());

        Entity entity = KillAuraUtils.filterEntity(ModuleList.aimBot.range.getValue());

        if (ModuleList.aimBot.players.getValue() && player != null && KillAuraUtils.canBeSeeTarget(ModuleList.aimBot.ignoreWalls, player)) {
            RotateUtils.rotateToEntity(player);
        }
        if (ModuleList.aimBot.mobs.getValue()) {
            if (entity != null && KillAuraUtils.canBeSeeTarget(ModuleList.aimBot.ignoreWalls, entity) && entity.isAlive() && !(entity instanceof ItemEntity)) {
                RotateUtils.rotateToEntity(entity);
            }
        }
    }
}
