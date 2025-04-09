package com.ferra13671.BThack.api.Utils.Modules;

import com.ferra13671.BTbot.api.Utils.Generate.NumberGenerator;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Social.Clans.Clan;
import com.ferra13671.BThack.api.Social.Clans.ClanStatus;
import com.ferra13671.BThack.api.Social.Clans.ClanManager;
import com.ferra13671.BThack.api.Social.SocialManagers;
import com.ferra13671.BThack.api.Utils.PlayerUtils;
import com.ferra13671.BThack.api.Utils.RotateMode;
import com.ferra13671.BThack.api.Utils.RotateUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public final class KillAuraUtils implements Mc {

    public static void attack(Entity target, RotateMode rotateMode, int packets) {
        attackInternal(target, rotateMode, packets);
    }

    private static void attackInternal(Entity target, RotateMode rotateMode, int packets) {
        Vec3d rotateVector = target.getPos();
        double xLength = Math.abs(target.getBoundingBox().maxX - target.getBoundingBox().minX);
        double yLength = Math.abs(target.getBoundingBox().maxY - target.getBoundingBox().minY);
        double zLength = Math.abs(target.getBoundingBox().maxZ - target.getBoundingBox().minZ);
        rotateVector.x += NumberGenerator.generateDouble(-(xLength / 3), xLength / 3);
        rotateVector.y += NumberGenerator.generateDouble(-(yLength / 3), yLength / 3);
        rotateVector.z += NumberGenerator.generateDouble(-(zLength / 3), zLength / 3);
        float[] rotations = RotateUtils.rotations(rotateVector);
        preAttackRotate(rotateMode, rotations, packets);
        attackNoRotate(target);
        rotateMode.postRotate();
    }

    public static void attackNoRotate(Entity target) {
        mc.interactionManager.attackEntity(mc.player, target);
        mc.player.swingHand(Hand.MAIN_HAND);
    }

    public static void preAttackRotate(RotateMode rotateMode, float[] rotations, int packets) {
        if (rotateMode == RotateMode.PACKET1)
            for (int i = 0; i < packets; i++)
                rotateMode.preRotate(rotations[0], rotations[1]);
        else rotateMode.preRotate(rotations[0], rotations[1]);
    }

    public static PlayerEntity filterPlayers(double range, boolean friends, boolean teammates, boolean clanManager, String clanMode, String targetClan, Predicate<Entity> extraFilter) {
        return mc.world.getPlayers().stream().filter(entityPlayer -> filterPlayer(entityPlayer, friends, teammates, clanManager, clanMode, targetClan) && extraFilter.test(entityPlayer)).min(Comparator.comparing(entityPlayer ->
                entityPlayer.distanceTo(mc.player))).filter(entityPlayer -> entityPlayer.distanceTo(mc.player) <= range).orElse(null);
    }

    public static PlayerEntity filterPlayers(double range, boolean friends, boolean teammates, boolean clanManager, String clanMode, String targetClan) {
        return filterPlayers(range, friends, teammates, clanManager, clanMode, targetClan, (entity) -> true);
    }

    public static boolean filterPlayer(PlayerEntity entityPlayer, boolean friends, boolean teammates, boolean clanManager, String clanMode, String targetClan) {
        return entityPlayer != mc.player && !isFriend(entityPlayer, friends) && !isTeammate(entityPlayer, teammates) && isSuccessfulClanMember(entityPlayer, clanManager, clanMode, targetClan) && entityPlayer.isAlive();
    }

    public static Entity filterEntity(double range, Predicate<Entity> entityFilter) {
        ArrayList<Entity> entities = new ArrayList<>();
        for (Entity entity : mc.world.getEntities()) {
            entities.add(entity);
        }

        return entities.stream().filter(entity1 -> entity1 != mc.player && entity1.isAlive() && entityFilter.test(entity1)).min(Comparator.comparing(entity1 ->
                entity1.distanceTo(mc.player))).filter(entity1 -> entity1.distanceTo(mc.player) <= range).orElse(null);
    }

    public static Entity filterEntity(double range) {
        return filterEntity(range, (entity) -> true);
    }

    public static boolean canBeSeeTarget(BooleanSetting ignoreWalls, Entity target) {
        return canBeSeeTarget(ignoreWalls.getValue(), target);
    }

    public static boolean canBeSeeTarget(boolean ignoreWalls, Entity target) {
        if (!ignoreWalls) {
            return PlayerUtils.canEntityBeSeen(mc.player, target);
        } else {
            return true;
        }
    }

    public static boolean isMob(Entity entity) {
        return entity instanceof MobEntity || entity instanceof FlyingEntity || entity instanceof PassiveEntity || entity instanceof AmbientEntity || entity instanceof WaterCreatureEntity || entity instanceof GolemEntity;
    }

    public static boolean isHostile(Entity entity) {
        return (entity instanceof HostileEntity) || entity instanceof GolemEntity;
    }

    public static boolean isPassive(Entity entity) {
        return entity instanceof PassiveEntity;
    }

    public static boolean isGolem(Entity entity) {
        return entity instanceof GolemEntity;
    }

    public static boolean isOtherMob(Entity entity) {
        return !isHostile(entity) && !isPassive(entity) && !isGolem(entity) && entity instanceof MobEntity;
    }

    public static Predicate<Entity> createEntityFilter(BooleanSetting hostiles, BooleanSetting passive, BooleanSetting golems, BooleanSetting otherMobs, BooleanSetting ignoreWalls) {
        return entity -> canBeSeeTarget(ignoreWalls, entity) && ((hostiles.getValue() && isHostile(entity)) || (passive.getValue() && isPassive(entity)) || (golems.getValue() && isGolem(entity)) || (otherMobs.getValue() && isOtherMob(entity)));
    }

    public static boolean isFriend(PlayerEntity player, BooleanSetting friends) {
        return isFriend(player, friends.getValue());
    }

    public static boolean isFriend(PlayerEntity player, boolean friends) {
        if (!friends) {
            return SocialManagers.FRIENDS.contains(player);
        }
        return false;
    }

    public static boolean isTeammate(PlayerEntity player, BooleanSetting teammates) {
        return isTeammate(player, teammates.getValue());
    }

    public static boolean isTeammate(PlayerEntity player, boolean teammates) {
        if (!teammates) {
            return mc.player.isTeammate(player);
        }
        return false;
    }

    public static boolean isSuccessfulClanMember(PlayerEntity player, BooleanSetting clanManager, ModeSetting clanMode, ModeSetting targetClan) {
        return isSuccessfulClanMember(player, clanManager.getValue(), clanMode.getValue(), targetClan.getValue());
    }

    public static boolean isSuccessfulClanMember(PlayerEntity player, boolean clanManager, String clanMode, String targetClan) {
        if (clanManager) {
            List<Clan> clans = ClanManager.getClansFromMember(player.getDisplayName().getString());
            switch (clanMode) {
                case "Only Enemy":
                    if (!clans.isEmpty()) {
                        for (Clan clan : clans) {
                            if (clan.getStatus() == ClanStatus.ENEMY) {
                                return true;
                            }
                        }
                        return false;
                    } else {
                        return true;
                    }
                case "Neutral Also":
                    if (!clans.isEmpty()) {
                        for (Clan clan : clans) {
                            if (clan.getStatus() == ClanStatus.ENEMY || clan.getStatus() == ClanStatus.NEUTRAL) {
                                return true;
                            }
                        }
                        return false;
                    } else {
                        return true;
                    }
                case "Target Clan":
                    if (!clans.isEmpty()) {
                        for (Clan clan : clans) {
                            if (clan.getName().equals(targetClan)) {
                                return true;
                            }
                        }
                        return false;
                    } else {
                        return true;
                    }
                case "All Clans":
                default:
                    return true;
            }
        } else {
            return true;
        }
    }
}
