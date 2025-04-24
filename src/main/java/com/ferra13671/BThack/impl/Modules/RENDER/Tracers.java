package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Line.RenderLine;
import com.ferra13671.BThack.api.Events.Render.RenderWorldLastEvent;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ColorSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Managers.managers.Clans.Clan;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.KillAuraUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.awt.*;
import java.util.ArrayList;

public class Tracers extends Module {

    public final BooleanSetting players = new BooleanSetting("Players", this, true);

    public final BooleanSetting hostile = new BooleanSetting("Mobs", this, true);
    public final ColorSetting hostileColor = new ColorSetting("Hostile Color", this, new Color(212, 235, 43), hostile::getValue).withBlockedAlpha();

    public final BooleanSetting animals = new BooleanSetting("Animals", this, true);
    public final ColorSetting animalColor = new ColorSetting("Animal Color", this, new Color(176, 255, 87), animals::getValue).withBlockedAlpha();

    public final BooleanSetting items = new BooleanSetting("Items", this, false);
    public final ColorSetting itemColor = new ColorSetting("Item Color", this, new Color(150, 150, 255), items::getValue).withBlockedAlpha();

    public Tracers() {
        super("Tracers",
                "lang.module.Tracers",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        initSettings(
                players,

                hostile,
                hostileColor,

                animals,
                animalColor,

                items,
                itemColor
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
                    if (Managers.FRIENDS_MANAGER.contains(name)) {
                        lines.add(new RenderLine(playerEntity, 0.03f, 0.96f, 0.86f, 1f));
                    } else if (Managers.ENEMIES_MANAGER.contains(name)) {
                        lines.add(new RenderLine(playerEntity, 1f, 0, 0, 1f));
                    } else if (Managers.CLAN_MANAGER.isAlly(name)) {
                        Clan clan = Managers.CLAN_MANAGER.getFirstClanFromMember(name);
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

        if (hostile.getValue() || animals.getValue() || items.getValue()) {
            for (Entity entity : mc.world.getEntities()) {
                if (hostile.getValue() && KillAuraUtils.isHostile(entity)) {
                    lines.add(new RenderLine(entity, hostileColor.getValue().getRed() / 255f,hostileColor.getValue().getGreen() / 255f,hostileColor.getValue().getBlue() / 255f, 1f));
                } else if (animals.getValue() && KillAuraUtils.isPassive(entity)) {
                    lines.add(new RenderLine(entity, animalColor.getValue().getRed() / 255f,animalColor.getValue().getGreen() / 255f,animalColor.getValue().getBlue() / 255f, 1f));
                } else if (items.getValue() && entity instanceof ItemEntity) {
                    lines.add(new RenderLine(entity, itemColor.getValue().getRed() / 255f,itemColor.getValue().getGreen() / 255f, itemColor.getValue().getBlue() / 255f, 1f));
                }
            }
        }

        BThackRender.lineRender.prepareLineRenderer();
        BThackRender.lineRender.renderLines(lines);
        BThackRender.lineRender.stopLineRenderer();
    }
}
