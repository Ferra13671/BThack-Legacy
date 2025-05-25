package com.ferra13671.BThack.impl.Modules.Combat;

import com.ferra13671.BThack.api.Module.ModuleInfo;
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
    public final BooleanSetting invisibles = new BooleanSetting("Invisibles", this, true, players::getValue);
    public final BooleanSetting teammates = new BooleanSetting("Teammates", this, false, players::getValue);
    public final BooleanSetting friends = new BooleanSetting("Friends", this, false, players::getValue);
    public final BooleanSetting mobs = new BooleanSetting("Mobs", this, true);
    public final BooleanSetting ignoreWalls = new BooleanSetting("Ignore Walls", this, false);

    public final BooleanSetting clanManager = ClanSettingsBuilder.buildToggle(this);
    public final ModeSetting clanMode = ClanSettingsBuilder.buildStatusMode(this, clanManager);
    public final ModeSetting target = ClanSettingsBuilder.buildClanTargetMode(this, clanManager, clanMode);

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onUpdate(RenderWorldLastEvent e) {
        if (nullCheck()) return;

        PlayerEntity player = KillAuraUtils.filterPlayers(range.getValue(), invisibles.getValue(), friends.getValue(), teammates.getValue(), clanManager.getValue(), clanMode.getValue(), target.getValue());

        Entity entity = KillAuraUtils.filterEntity(range.getValue());

        if (players.getValue() && player != null && KillAuraUtils.canBeSeeTarget(ignoreWalls, player))
            RotateUtils.rotateToEntity(player);

        if (mobs.getValue() && entity != null && KillAuraUtils.canBeSeeTarget(ignoreWalls, entity) && entity.isAlive() && !(entity instanceof ItemEntity))
            RotateUtils.rotateToEntity(entity);
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        arrayListInfo = "" + range.getValue();
    }
}
