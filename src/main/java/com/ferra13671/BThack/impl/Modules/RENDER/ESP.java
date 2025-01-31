package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Box.RenderBox;
import com.ferra13671.BThack.api.Events.Render.RenderWorldLastEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.EntityUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.KillAuraUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TODO: Color change
public class ESP extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", this, new ArrayList<>(Arrays.asList("Box", "Glow")));
    public final BooleanSetting players = new BooleanSetting("Players", this, true);
    public final BooleanSetting items = new BooleanSetting("Items", this, true);
    public final BooleanSetting monsters = new BooleanSetting("Monsters", this, true);
    public final BooleanSetting animals = new BooleanSetting("Animals", this, true);

    public ESP() {
        super("ESP",
                "lang.module.ESP",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        initSettings(
                mode,
                players,
                items,
                monsters,
                animals
        );
    }

    private boolean need = true;

    public final List<Entity> glowed = new ArrayList<>();

    @EventSubscriber
    public void onRender(RenderWorldLastEvent e) {
        if (nullCheck()) return;

        String _mode = mode.getValue();

        arrayListInfo = _mode;

        switch (_mode) {
            case "Box":
                if (need) {
                    for (Entity entity : glowed) {
                        entity.setGlowing(false);
                    }
                    glowed.clear();
                    need = false;
                }

                ArrayList<RenderBox> renderBoxes = new ArrayList<>();

                for (PlayerEntity entity : mc.world.getPlayers()) {
                    if (players.getValue() && entity != mc.player && entity != null) {
                        renderBoxes.add(new RenderBox(EntityUtils.getLerpedBox(entity, mc.getRenderTickCounter().getTickDelta(true)), 1,1,1, 0.6F,1,1,1, 0.2F));
                    }
                }
                for (Entity mob : mc.world.getEntities()) {
                    if (items.getValue() && mob instanceof ItemEntity) {
                        renderBoxes.add(new RenderBox(EntityUtils.getLerpedBox(mob, mc.getRenderTickCounter().getTickDelta(true)), 0.59F,0.59F, 1,0.6F,0.59F,0.59F,1, 0.2F));
                    }
                    if (monsters.getValue() && KillAuraUtils.isHostile(mob) || KillAuraUtils.isGolem(mob)) {
                        renderBoxes.add(new RenderBox(EntityUtils.getLerpedBox(mob, mc.getRenderTickCounter().getTickDelta(true)), 0.83F,0.92F,0.17F, 0.6F,0.83F,0.92F,0.17F, 0.2F));
                    }
                    if (animals.getValue() && KillAuraUtils.isPassive(mob)) {
                        renderBoxes.add(new RenderBox(EntityUtils.getLerpedBox(mob, mc.getRenderTickCounter().getTickDelta(true)), 0.69F,1,0.34F, 0.6F,0.69F,1,0.34F, 0.2F));
                    }
                }

                BThackRender.boxRender.prepareBoxRender();

                BThackRender.boxRender.renderBoxes(renderBoxes);

                BThackRender.boxRender.stopBoxRender();

                break;
            case "Glow":
                need = true;
                for (PlayerEntity playerEntity : mc.world.getPlayers()) {
                    if (playerEntity != mc.player && playerEntity != glowed && !playerEntity.isDead()) {
                        playerEntity.setGlowing(true);
                        glowed.add(playerEntity);
                    }
                }
                for (Entity mob : mc.world.getEntities()) {
                    if (mob != glowed) {
                        if (mob instanceof ItemEntity) {
                            mob.setGlowing(true);
                            glowed.add(mob);
                        }
                        if (mob instanceof MobEntity && mob.isAlive()) {
                            mob.setGlowing(true);
                            glowed.add(mob);
                        }
                        if (mob instanceof WaterCreatureEntity && mob.isAlive()) {
                            mob.setGlowing(true);
                            glowed.add(mob);
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onDisable() {
        if (nullCheck()) return;

        for (Entity entity : glowed) {
            entity.setGlowing(false);
        }
        glowed.clear();
        need = true;

        super.onDisable();
    }
}
