package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Box.RenderBox;
import com.ferra13671.BThack.api.Events.Render.RenderWorldLastEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ColorSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.EntityUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.KillAuraUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.awt.*;
import java.util.ArrayList;

public class ESP extends Module {

    public final BooleanSetting players = new BooleanSetting("Players", this, true);
    public final ColorSetting playerColor = new ColorSetting("Player Color", this, new Color(255, 255, 255), players::getValue).withBlockedAlpha();

    public final BooleanSetting items = new BooleanSetting("Items", this, true);
    public final ColorSetting itemColor = new ColorSetting("Item Color", this, new Color(150, 150, 255), items::getValue).withBlockedAlpha();

    public final BooleanSetting monsters = new BooleanSetting("Monsters", this, true);
    public final ColorSetting monsterColor = new ColorSetting("Monster Color", this, new Color(211, 234, 43), monsters::getValue).withBlockedAlpha();

    public final BooleanSetting animals = new BooleanSetting("Animals", this, true);
    public final ColorSetting animalColor = new ColorSetting("Animal Color", this, new Color(176, 255, 86), animals::getValue).withBlockedAlpha();

    public ESP() {
        super("ESP",
                "lang.module.ESP",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        initSettings(
                players,
                playerColor,

                items,
                itemColor,

                monsters,
                monsterColor,

                animals,
                animalColor
        );
    }

    @EventSubscriber
    public void onRender(RenderWorldLastEvent e) {
        if (nullCheck()) return;

        ArrayList<RenderBox> renderBoxes = new ArrayList<>();

        for (PlayerEntity entity : mc.world.getPlayers()) {
            if (players.getValue() && entity != mc.player && entity != null) {
                renderBoxes.add(new RenderBox(
                        EntityUtils.getLerpedBox(entity, mc.getRenderTickCounter().getTickDelta(true)),
                        playerColor.getValue().getRed() / 255f,
                        playerColor.getValue().getGreen() / 255f,
                        playerColor.getValue().getBlue() / 255f,
                        0.6F,
                        playerColor.getValue().getRed() / 255f,
                        playerColor.getValue().getGreen() / 255f,
                        playerColor.getValue().getBlue() / 255f,
                        0.2F
                ));
            }
        }
        for (Entity mob : mc.world.getEntities()) {
            if (items.getValue() && mob instanceof ItemEntity) {
                renderBoxes.add(new RenderBox(
                        EntityUtils.getLerpedBox(mob, mc.getRenderTickCounter().getTickDelta(true)),
                        itemColor.getValue().getRed() / 255f,
                        itemColor.getValue().getGreen() / 255f,
                        itemColor.getValue().getBlue() / 255f,
                        0.6F,
                        itemColor.getValue().getRed() / 255f,
                        itemColor.getValue().getGreen() / 255f,
                        itemColor.getValue().getBlue() / 255f,
                        0.2F
                ));
            }
            if (monsters.getValue() && KillAuraUtils.isHostile(mob) || KillAuraUtils.isGolem(mob)) {
                renderBoxes.add(new RenderBox(
                        EntityUtils.getLerpedBox(mob, mc.getRenderTickCounter().getTickDelta(true)),
                        monsterColor.getValue().getRed() / 255f,
                        monsterColor.getValue().getGreen() / 255f,
                        monsterColor.getValue().getBlue() / 255f,
                        0.6F,
                        monsterColor.getValue().getRed() / 255f,
                        monsterColor.getValue().getGreen() / 255f,
                        monsterColor.getValue().getBlue() / 255f,
                        0.2F
                ));
            }
            if (animals.getValue() && KillAuraUtils.isPassive(mob)) {
                renderBoxes.add(new RenderBox(
                        EntityUtils.getLerpedBox(mob, mc.getRenderTickCounter().getTickDelta(true)),
                        animalColor.getValue().getRed() / 255f,
                        animalColor.getValue().getGreen() / 255f,
                        animalColor.getValue().getBlue() / 255f,
                        0.6F,
                        animalColor.getValue().getRed() / 255f,
                        animalColor.getValue().getGreen() / 255f,
                        animalColor.getValue().getBlue() / 255f,
                        0.2F
                ));
            }
        }

        BThackRender.boxRender.prepareBoxRender();

        BThackRender.boxRender.renderBoxes(renderBoxes);

        BThackRender.boxRender.stopBoxRender();
    }
}
