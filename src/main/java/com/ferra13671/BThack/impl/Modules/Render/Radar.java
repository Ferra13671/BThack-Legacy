package com.ferra13671.BThack.impl.Modules.Render;

import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.events.Render.RenderHudPreEvent;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.managers.managers.Clans.Clan;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.client.util.Window;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.awt.*;

@ModuleInfo(name = "Radar", description = "lang.module.Radar", category = "RENDER")
public class Radar extends Module {

    public final NumberSetting opacity = new NumberSetting("Opacity", this, 0.5, 0.05, 1, false);
    public final NumberSetting scale = new NumberSetting("Scale", this, 100, 100, 150, true);
    public final NumberSetting range = new NumberSetting("Range", this, 100, 50, 200, true);
    public final BooleanSetting outline = new BooleanSetting("Outline Rect", this, false);

    public final BooleanSetting players = new BooleanSetting("Players", this, true);
    public final BooleanSetting mobs = new BooleanSetting("Mobs", this, true);
    public final BooleanSetting animals = new BooleanSetting("Animals", this, true);


    private final Color mobColor = new Color(255, 255, 0);
    private final Color animalColor = new Color(0, 255, 0);
    private final Color friendColor = new Color(0, 225, 255);
    private final Color enemyColor = new Color(255, 0, 0);

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onRender(RenderHudPreEvent e) {
        Window sr = mc.getWindow();
        Color rectColor = new Color(0, 0, 0, opacity.getValue().floatValue());

        //Very strong math, yeeaah.
        float yaw = (mc.player.yaw / 360);
        yaw = yaw - (float)Math.floor(yaw);
        yaw = yaw * 360;

        float _range = range.getValue().floatValue();
        float _scale = scale.getValue().floatValue();

        //Drawing a radar map
        BThackRender.drawRect(sr.getScaledWidth(), sr.getScaledHeight(), sr.getScaledWidth() - (int) _scale, sr.getScaledHeight() - (int) _scale, rectColor.hashCode());
        BThackRender.drawTriangle(sr.getScaledWidth() - (_scale / 2), (int)((sr.getScaledHeight() - (_scale / 2)) + 3), 4, -yaw, -1);
        BThackRender.drawCenteredString("X-", sr.getScaledWidth() - 6, sr.getScaledHeight() - ((int) _scale / 2f), -1);
        BThackRender.drawString("X+", (int)(sr.getScaledWidth() - _scale) + 2, sr.getScaledHeight() - ((int) _scale / 2f), -1);
        BThackRender.drawCenteredString("Z+", sr.getScaledWidth() - (int)(_scale / 2), (sr.getScaledHeight() - (int) _scale) + 1, -1);
        BThackRender.drawCenteredString("Z-", sr.getScaledWidth() - (int)(_scale / 2), sr.getScaledHeight() - 9, -1);
        //////

        if (outline.getValue()) {
            BThackRender.drawOutlineRect(sr.getScaledWidth(), sr.getScaledHeight(), sr.getScaledWidth() - (int) _scale, sr.getScaledHeight() - (int) _scale, 1, -1);
        }

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player != mc.player) {
                double posX = player.getX();
                double posZ = player.getZ();
                double radarPosX = mc.player.getX() - posX;
                double radarPosZ = mc.player.getZ() - posZ;

                if (radarPosX < (int) _range && radarPosZ < (int) _range) {

                    radarPosX = ((sr.getScaledWidth() - (sr.getScaledWidth() - _scale)) / 100) * ((radarPosX / _range) * 100);
                    radarPosZ = ((sr.getScaledHeight() - (sr.getScaledHeight() - _scale)) / 100) * ((radarPosZ / _range) * 100);

                    int x = sr.getScaledWidth() - ((int) _scale / 2);
                    int y = sr.getScaledHeight() - ((int) _scale / 2);

                    x = x + (int) radarPosX;
                    y = y + (int) radarPosZ;

                    if (isCurrentCords(x,y, (sr.getScaledWidth() - (int) _scale) + 1, sr.getScaledWidth() - 1, (sr.getScaledHeight() - (int) _scale) + 1, sr.getScaledHeight() - 1)) {
                        if (Managers.FRIENDS_MANAGER.contains(player)) {
                            BThackRender.drawSquare(x, y, 1, friendColor.hashCode());
                        } else if (Managers.ENEMIES_MANAGER.contains(player)) {
                            BThackRender.drawSquare(x, y, 1, enemyColor.hashCode());
                        } else if (Managers.CLAN_MANAGER.isAlly(player)) {
                            Clan clan = Managers.CLAN_MANAGER.getFirstClanFromMember(player.getDisplayName().getString());

                            if (clan != null) {
                                BThackRender.drawSquare(x,y,3, -1);
                                Color color = new Color(clan.getR(), clan.getG(), clan.getB());
                                BThackRender.drawSquare(x,y,2, color.hashCode());
                            } else {
                                BThackRender.drawSquare(x, y, 1, -1);
                            }
                        } else if (players.getValue()) {
                            BThackRender.drawSquare(x, y, 1, -1);
                        }
                    }
                }
            }
        }
        for (Entity entity : mc.world.getEntities()) {
            double posX = entity.getX();
            double posZ = entity.getZ();
            double radarPosX = mc.player.getX() - posX;
            double radarPosZ = mc.player.getZ() - posZ;

            if (radarPosX < (int) _range && radarPosZ < (int) _range) {
                radarPosX = ((sr.getScaledWidth() - (sr.getScaledWidth() - _scale)) / 100) * ((radarPosX / _range) * 100);
                radarPosZ = ((sr.getScaledHeight() - (sr.getScaledHeight() - _scale)) / 100) * ((radarPosZ / _range) * 100);

                int x = sr.getScaledWidth() - ((int) _scale / 2);
                int y = sr.getScaledHeight() - ((int) _scale / 2);

                x = x + (int) radarPosX;
                y = y + (int) radarPosZ;

                if (isCurrentCords(x,y, (sr.getScaledWidth() - (int) _scale) + 1, sr.getScaledWidth() - 1, (sr.getScaledHeight() - (int) _scale) + 1, sr.getScaledHeight() - 1)) {
                    if ((entity instanceof MobEntity || entity instanceof GolemEntity) && mobs.getValue()) {
                        BThackRender.drawSquare(x, y, 1, mobColor.hashCode());
                    } else if ((entity instanceof WaterCreatureEntity || entity instanceof PassiveEntity) && animals.getValue()) {
                        BThackRender.drawSquare(x, y, 1, animalColor.hashCode());
                    }
                }
            }
        }
    }

    private boolean isCurrentCords(int checkX, int checkY, int needMinX, int needMaxX, int needMinY, int needMaxY) {
        return checkX > needMinX && checkX < needMaxX && checkY > needMinY && checkY < needMaxY;
    }
}
