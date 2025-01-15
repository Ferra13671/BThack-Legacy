package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Line.RenderLine;
import com.ferra13671.BThack.api.Events.Render.RenderWorldLastEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Social.Clans.Clan;
import com.ferra13671.BThack.api.Social.Clans.ClanManager;
import com.ferra13671.BThack.api.Social.SocialManagers;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.KillAuraUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;

public class Tracers extends Module {

    public final BooleanSetting players = new BooleanSetting("Players", this, true);
    public final BooleanSetting mobs = new BooleanSetting("Mobs", this, true);
    public final BooleanSetting animals = new BooleanSetting("Animals", this, true);
    public final BooleanSetting items = new BooleanSetting("Items", this, false);

    public Tracers() {
        super("Tracers",
                "lang.module.Tracers",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        initSettings(
                players,
                mobs,
                animals,
                items
        );
    }

    @EventSubscriber(priority = -1)
    @SuppressWarnings("unused")
    public void onRender(RenderWorldLastEvent e) {
        if (nullCheck()) return;

        ArrayList<RenderLine> lines = new ArrayList<>();

        if (players.getValue()) {
            for (PlayerEntity playerEntity : mc.world.getPlayers()) {
                if (playerEntity != null && playerEntity != mc.player && !playerEntity.isDead()) {
                    String name = playerEntity.getDisplayName().getString();
                    if (SocialManagers.FRIENDS.contains(name)) {
                        lines.add(new RenderLine(playerEntity, 0.03f, 0.96f, 0.86f, 1f));
                    } else if (SocialManagers.ENEMIES.contains(name)) {
                        lines.add(new RenderLine(playerEntity, 1f, 0, 0, 1f));
                    } else if (ClanManager.isAlly(name)) {
                        Clan clan = ClanManager.getFirstClanFromMember(name);
                        if (clan != null) {
                            lines.add(new RenderLine(playerEntity, clan.getR(), clan.getG(), clan.getB(), 1f));
                        } else {
                            lines.add(new RenderLine(playerEntity, 1f, 1f, 1f, 1f));
                        }
                    } else {
                        lines.add(new RenderLine(playerEntity, 1f, 1f, 1f, 1f));
                    }
                }
            }
        }

        if (mobs.getValue() || animals.getValue() || items.getValue()) {
            for (Entity entity : mc.world.getEntities()) {
                if (mobs.getValue() && KillAuraUtils.isHostile(entity)) {
                    lines.add(new RenderLine(entity, 0.83f,0.92f,0.17f, 1f));
                } else if (animals.getValue() && KillAuraUtils.isPassive(entity)) {
                    lines.add(new RenderLine(entity, 0.69f,1f,0.34f, 1f));
                } else if (items.getValue() && entity instanceof ItemEntity) {
                    lines.add(new RenderLine(entity, 0.59f,0.59f, 1f, 1f));
                }
            }
        }

        BThackRender.lineRender.prepareLineRenderer();
        BThackRender.lineRender.renderLines(lines);
        BThackRender.lineRender.stopLineRenderer();
    }
}
