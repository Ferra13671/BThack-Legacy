package com.ferra13671.BThack.impl.modules.render;

import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.core.render.BThackRender;
import com.ferra13671.BThack.core.render.box.RenderBox;
import com.ferra13671.BThack.events.render.RenderWorldLastEvent;
import com.ferra13671.BThack.managers.impl.setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.impl.setting.Settings.CategorySetting;
import com.ferra13671.BThack.managers.impl.setting.Settings.ColorSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.utils.EntityUtils;
import com.ferra13671.BThack.api.utils.modules.KillAuraUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.awt.*;
import java.util.ArrayList;

@ModuleInfo(name = "ESP", description = "lang.module.ESP", category = "RENDER")
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

    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
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
