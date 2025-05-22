package com.ferra13671.BThack.impl.Modules.Combat;

import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.CategorySetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.managers.managers.TravelChange.TravelChanger;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.managers.managers.Clans.ClanSettingsBuilder;
import com.ferra13671.BThack.api.Utils.*;
import com.ferra13671.BThack.api.Utils.Modules.KillAuraUtils;
import com.ferra13671.BThack.api.Utils.Rotate.RotateMode;
import com.ferra13671.BThack.api.Utils.Rotate.RotateUtils;
import com.ferra13671.BThack.mixins.accessor.IMinecraftClient;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

@ModuleInfo(name = "KillAura", description = "lang.module.KillAura", category = "COMBAT")
public class KillAura extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", this, new ArrayList<>(Arrays.asList("Aura", "TriggerBot")));
    public final ModeSetting attackMode = new ModeSetting("AttackMode", this, new ArrayList<>(Arrays.asList("CoolDown", "Delay")));
    public final NumberSetting delay = new NumberSetting("Delay(Second)", this, 1.4, 0.1, 4, false, () -> attackMode.getValue().equals("Delay"));
    public final NumberSetting range = new NumberSetting("Range", this, 3.62, 1, 10, false, () -> mode.getValue().equals("Aura"));

    public final CategorySetting rotateCategory = new CategorySetting("Rotate", this);
    public final BooleanSetting instaRotate = new BooleanSetting("Insta Rotate", this, false, () -> mode.getValue().equals("Aura")).inCategory(rotateCategory);
    public final ModeSetting rotateMath = new ModeSetting("Rotate Math", this, Arrays.asList("New", "Old", "Always"), () -> mode.getValue().equals("Aura") && !instaRotate.getValue()).inCategory(rotateCategory);
    public final NumberSetting targetRotateDelay = new NumberSetting("Target Rot. Delay", this, 150, 0, 500, true, () -> mode.getValue().equals("Aura") && !instaRotate.getValue() && !rotateMath.getValue().equals("Always")).inCategory(rotateCategory);
    public final NumberSetting rotateStep = new NumberSetting("Rotate Step", this, 0.6125, 0.35, 0.8, false, () -> mode.getValue().equals("Aura") && !instaRotate.getValue() && rotateMath.getValue().equals("New")).inCategory(rotateCategory);
    public final NumberSetting lockTicks = new NumberSetting("Lock Ticks", this, 5, 3, 10, true, () -> mode.getValue().equals("Aura") && !instaRotate.getValue()).inCategory(rotateCategory);
    public final BooleanSetting grim = new BooleanSetting("Grim", this, true, () ->  mode.getValue().equals("Aura")).inCategory(rotateCategory);
    public final ModeSetting rotateMode = new ModeSetting("RotateMode", this, new ArrayList<>(Arrays.asList("Packet", "Vanilla", "None")), () ->  mode.getValue().equals("Aura") && instaRotate.getValue()).inCategory(rotateCategory);
    public final NumberSetting packets = new NumberSetting("Packets", this, 1, 1, 5, true, () -> rotateMode.getValue().equals("Packet") && mode.getValue().equals("Aura")).inCategory(rotateCategory);

    public final CategorySetting moveFixCategory = new CategorySetting("Move Fix", this);
    public final BooleanSetting moveFix = new BooleanSetting("Move Fix", this, true).inCategory(moveFixCategory);
    public final ModeSetting moveFixMode = new ModeSetting("Mode", this, Arrays.asList("Legal", "Strong")).inCategory(moveFixCategory);

    public final BooleanSetting ignoreWalls = new BooleanSetting("Ignore Walls", this, false);

    public final CategorySetting targetsCategory = new CategorySetting("Targets", this);
    public final BooleanSetting players = new BooleanSetting("Players", this, true).inCategory(targetsCategory);
    public final BooleanSetting teammates = new BooleanSetting("Teammates", this, false).inCategory(targetsCategory);
    public final BooleanSetting friends = new BooleanSetting("Friends", this, false).inCategory(targetsCategory);
    public final BooleanSetting hostiles = new BooleanSetting("Hostiles", this, true).inCategory(targetsCategory);
    public final BooleanSetting passive = new BooleanSetting("Passive", this, true).inCategory(targetsCategory);
    public final BooleanSetting golems = new BooleanSetting("Golems", this, false).inCategory(targetsCategory);
    public final BooleanSetting otherMobs = new BooleanSetting("Other Mobs", this, true).inCategory(targetsCategory);
    public final BooleanSetting clanManager = ClanSettingsBuilder.buildToggle(this).inCategory(targetsCategory);
    public final ModeSetting clanMode = ClanSettingsBuilder.buildStatusMode(this, clanManager).inCategory(targetsCategory);
    public final ModeSetting targetClan = ClanSettingsBuilder.buildClanTargetMode(this, clanManager, clanMode).inCategory(targetsCategory);

    public final CategorySetting pauseCategory = new CategorySetting("Pause", this);
    public final BooleanSetting pauseIfEat = new BooleanSetting("If Eat", this, true).inCategory(pauseCategory);
    public final BooleanSetting pauseIfMine = new BooleanSetting("If Mine", this, true).inCategory(pauseCategory);
    public final BooleanSetting pauseIfBlink = new BooleanSetting("If Blink", this, false).inCategory(pauseCategory);


    private Predicate<Entity> entityFilter;

    private final Ticker delayTicker = new Ticker();
    private final Ticker deleteTravelTicker = new Ticker();
    private boolean travelCancelled = true;
    public Target targetedEntity;
    private float[] targetRotation;
    private float[] currentRotation;
    private final TravelChanger travelChanger = new TravelChanger(10000,
            () -> new Float[]{currentRotation[0], currentRotation[1]},
            moveFix::getValue,
            () -> moveFixMode.getValue().equals("Strong")
    );
    private Entity prevAttackedEntity;

    private final Ticker updateRotTicker = new Ticker();

    @Override
    public void onEnable() {
        super.onEnable();
        delayTicker.reset();
        targetedEntity = null;
        entityFilter = KillAuraUtils.createEntityFilter(hostiles, passive, golems, otherMobs, ignoreWalls);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        delayTicker.reset();
        targetedEntity = null;
        Managers.TRAVEL_CHANGE_MANAGER.removeChanger(travelChanger);
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        String killAuraMode = mode.getValue();

        arrayListInfo = killAuraMode + (!killAuraMode.equals("TriggerBot") ? "; " + range.getValue() : "");

        if (needPause()) return;

        switch (killAuraMode) {
            case "Aura" -> auraMode();
            case "TriggerBot" -> triggerBotMode();
        }

    }

    //---------Aura---------//
    public void auraMode() {
        if (!mc.player.isAlive()) {
            targetedEntity = null;
            return;
        }
        if (targetedEntity != null) {
            if (mc.player.distanceTo(targetedEntity.entity) > range.getValue() || targetedEntity.entity.isDead() || mc.world.getEntityById(targetedEntity.entity.getId()) == null) {
                if (!travelCancelled) {
                    deleteTravelTicker.reset();
                    travelCancelled = true;
                }
                targetedEntity = null;
                prevAttackedEntity = null;
            }
        }

        if (targetedEntity == null || targetedEntity.lockTicks <= 0 && (delayPassed() && rotateMath.getValue().equals("Always"))) {
            targetSearchAction();
        }

        attackTargetAction();
    }

    public void targetSearchAction() {
        LivingEntity target = null;
        if (players.getValue())
            target = KillAuraUtils.filterPlayers(range.getValue(), friends.getValue(), teammates.getValue(), clanManager.getValue(), clanMode.getValue(), targetClan.getValue(), entity -> !entity.isSpectator() && !((PlayerEntity) entity).isCreative());

        if (target == null)
            target = (LivingEntity) KillAuraUtils.filterEntity(range.getValue(), entityFilter);

        if (target != null) {
            targetedEntity = new Target(target, 0);
            travelCancelled = false;
        } else {
            if (!travelCancelled) {
                deleteTravelTicker.reset();
                travelCancelled = true;
            }
            targetedEntity = null;
            prevAttackedEntity = null;
        }
    }

    public void attackTargetAction() {
        if (targetedEntity != null) {
            if (!Managers.TRAVEL_CHANGE_MANAGER.containsChanger(travelChanger)) Managers.TRAVEL_CHANGE_MANAGER.addChanger(travelChanger);
            if (rotateMath.getValue().equals("Always") || updateRotTicker.passed(targetRotateDelay.getValue())) {
                targetRotation = RotateUtils.rotations(targetedEntity.entity);
                updateRotTicker.reset();
            }
            switch (rotateMath.getValue()) {
                case "Old", "Always" -> currentRotation = targetRotation;
                case "New" -> {
                    if (currentRotation != null) {
                        currentRotation[0] += (targetRotation[0] - currentRotation[0]) * rotateStep.getValue().floatValue();
                        currentRotation[1] += (targetRotation[1] - currentRotation[1]) * rotateStep.getValue().floatValue();
                    } else currentRotation = new float[]{RotateUtils.getCameraYaw(), RotateUtils.getCameraPitch()};
                }
            }
            if (!delayPassed()) return;
            if (instaRotate.getValue() || targetedEntity.lockTicks >= lockTicks.getValue()) {
                if (instaRotate.getValue()) {
                    RotateMode rotateMode = getRotateMode();
                    KillAuraUtils.preAttackRotate(rotateMode, currentRotation, packets.getValue().intValue());
                }
                KillAuraUtils.attackNoRotate(targetedEntity.entity);
                delayTicker.reset();
                prevAttackedEntity = targetedEntity.entity;
            } else {
                targetedEntity = new Target(targetedEntity.entity, prevAttackedEntity == targetedEntity.entity ? lockTicks.getValue().intValue() : targetedEntity.lockTicks + 1);
            }
        } else {
            if (travelCancelled && (deleteTravelTicker.passed(200) || !rotateMath.getValue().equals("New"))) {
                if (Managers.TRAVEL_CHANGE_MANAGER.containsChanger(travelChanger))
                    Managers.TRAVEL_CHANGE_MANAGER.removeChanger(travelChanger);
                currentRotation = null;
            } else {
                currentRotation[0] += (RotateUtils.getCameraYaw() - currentRotation[0]) * rotateStep.getValue().floatValue();
                currentRotation[1] += (RotateUtils.getCameraPitch() - currentRotation[1]) * rotateStep.getValue().floatValue();
            }
        }
    }
    //----------------------//

    //---------TriggerBot---------//
    public void triggerBotMode() {
        if (!delayPassed()) return;
        HitResult objectMouseOver = mc.crosshairTarget;

        if (objectMouseOver instanceof EntityHitResult entityHitResult) {
            Entity ent = entityHitResult.getEntity();

            if (players.getValue() && ent instanceof PlayerEntity player) {
                if (KillAuraUtils.filterPlayer(player, friends.getValue(), teammates.getValue(), clanManager.getValue(), clanMode.getValue(), targetClan.getValue())) {
                    ((IMinecraftClient) mc).attack();
                }
            }
            if (entityFilter.test(ent)) {
                ((IMinecraftClient) mc).attack();
            }

            delayTicker.reset();
        }
    }
    //----------------------------//

    public RotateMode getRotateMode() {
        return switch (rotateMode.getValue()) {
            case "Packet" -> RotateMode.PACKET;
            case "Vanilla" -> RotateMode.VANILLA;
            default -> RotateMode.NONE;
        };
    }

    public boolean delayPassed() {
        return switch (attackMode.getValue()) {
            case "CoolDown" -> mc.player.getAttackCooldownProgress(0) >= 1.0;
            case "Delay" -> delayTicker.passed((int) (delay.getValue() * 1000));
            default -> true;
        };
    }

    public boolean needPause() {
        return (
                (pauseIfMine.getValue() && ItemUtils.isTool(mc.player.getActiveItem().getItem()) && mc.player.isUsingItem()) || (ModuleList.packetMine.isEnabled() && (ModuleList.packetMine.currentBreakingBlock != null || !ModuleList.packetMine.conveyorBlocks.isEmpty()))
                || (pauseIfEat.getValue() && ItemUtils.isFood(mc.player.getActiveItem()) && mc.player.isUsingItem())
                || (pauseIfBlink.getValue() && ModuleList.blink.isEnabled())
        );
    }

    public record Target(LivingEntity entity, int lockTicks) {}
}