package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Box.RenderBox;
import com.ferra13671.BThack.api.Events.Render.RenderWorldLastEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.CategorySetting;
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

    public final CategorySetting targetsCategory = new CategorySetting("Targets", this);
    public final BooleanSetting players = new BooleanSetting("Players", this, true).inCategory(targetsCategory);
    public final BooleanSetting items = new BooleanSetting("Items", this, true).inCategory(targetsCategory);
    public final BooleanSetting monsters = new BooleanSetting("Monsters", this, true).inCategory(targetsCategory);
    public final BooleanSetting animals = new BooleanSetting("Animals", this, true).inCategory(targetsCategory);

    public final CategorySetting colorsCategory = new CategorySetting("Colors", this);
    public final ColorSetting playerColor = new ColorSetting("Player Color", this, new Color(255, 255, 255)).withBlockedAlpha().inCategory(colorsCategory);
    public final ColorSetting itemColor = new ColorSetting("Item Color", this, new Color(150, 150, 255)).withBlockedAlpha().inCategory(colorsCategory);
    public final ColorSetting monsterColor = new ColorSetting("Monster Color", this, new Color(211, 234, 43)).withBlockedAlpha().inCategory(colorsCategory);
    public final ColorSetting animalColor = new ColorSetting("Animal Color", this, new Color(176, 255, 86)).withBlockedAlpha().inCategory(colorsCategory);

    public ESP() {
        super("ESP",
                "lang.module.ESP",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        initSettings(
                targetsCategory,
                colorsCategory
        );
    }

    @EventSubscriber
    public void onRender(RenderWorldLastEvent e) {
        if (nullCheck()) return;

        ArrayList<RenderBox> renderBoxes = new ArrayList<>();

        for (PlayerEntity entity : mc.world.getPlayers())
            if (players.getValue() && entity != mc.player && entity != null)
                renderBoxes.add(createBox(entity, playerColor.getValue()));

        for (Entity mob : mc.world.getEntities()) {
            if (items.getValue() && mob instanceof ItemEntity)
                renderBoxes.add(createBox(mob, itemColor.getValue()));

            if (monsters.getValue() && KillAuraUtils.isHostile(mob) || KillAuraUtils.isGolem(mob))
                renderBoxes.add(createBox(mob, monsterColor.getValue()));

            if (animals.getValue() && KillAuraUtils.isPassive(mob))
                renderBoxes.add(createBox(mob, animalColor.getValue()));
        }

        BThackRender.boxRender.prepareBoxRender();
        BThackRender.boxRender.renderBoxes(renderBoxes);
        BThackRender.boxRender.stopBoxRender();
    }

    public RenderBox createBox(Entity mob, Color color) {
        return new RenderBox(
                EntityUtils.getLerpedBox(mob, mc.getRenderTickCounter().getTickDelta(true)),
                color.getRed() / 255f,
                color.getGreen() / 255f,
                color.getBlue() / 255f,
                0.6F,
                color.getRed() / 255f,
                color.getGreen() / 255f,
                color.getBlue() / 255f,
                0.2F
        );
    }
}
