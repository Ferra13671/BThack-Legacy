package com.ferra13671.BThack.impl.Modules.COMBAT.KillAura;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Events.PacketEvent;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.managers.TravelChange.TravelChanger;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Social.Clans.ClanSettingsBuilder;
import com.ferra13671.BThack.api.Utils.Grim.GrimUtils;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.BThack.api.Utils.Modules.KillAuraUtils;
import com.ferra13671.BThack.api.Utils.Ticker;
import com.ferra13671.BThack.mixins.accessor.packet.IPlayerInputC2SPacket;
import com.ferra13671.BThack.mixins.accessor.packet.IPlayerMoveC2SPacket;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ToolItem;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

public class KillAura extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", this, new ArrayList<>(Arrays.asList("Aura", "TriggerBot")));
    public final ModeSetting attackMode = new ModeSetting("AttackMode", this, new ArrayList<>(Arrays.asList("CoolDown", "Delay")));
    public final NumberSetting range = new NumberSetting("Range", this, 3.62, 1, 10, false, () -> mode.getValue().equals("Aura"));
    public final ModeSetting rotateMode = new ModeSetting("RotateMode", this, new ArrayList<>(Arrays.asList("Packet", "Vanilla", "Grim", "None")), () -> !mode.getValue().equals("TriggerBot")).defaultValue("Grim");
    public final NumberSetting packets = new NumberSetting("Packets", this, 1, 1, 5, true, () -> rotateMode.getValue().equals("Packet") && mode.getValue().equals("Aura"));
    public final NumberSetting delay = new NumberSetting("Delay(Second)", this, 1.4, 0.1, 4, false, () -> attackMode.getValue().equals("Delay"));
    public final NumberSetting postCooldown = new NumberSetting("Post Cooldown", this, 56, 0, 100, true, () -> attackMode.getValue().equals("CoolDown"));

    public final BooleanSetting instaAttack = new BooleanSetting("Insta Attack", this, false, () -> mode.getValue().equals("Aura") && !mode.getValue().equals("Grim"));
    public final NumberSetting lockTicks = new NumberSetting("Lock Ticks", this, 7, 3, 10, true, () -> mode.getValue().equals("Aura") && (mode.getValue().equals("Grim") || !instaAttack.getValue()));

    public final BooleanSetting players = new BooleanSetting("Players", this, true);
    public final BooleanSetting teammates = new BooleanSetting("Teammates", this, false);
    public final BooleanSetting friends = new BooleanSetting("Friends", this, false);
    public final BooleanSetting hostiles = new BooleanSetting("Hostiles", this, true);
    public final BooleanSetting passive = new BooleanSetting("Passive", this, true);
    public final BooleanSetting golems = new BooleanSetting("Golems", this, false);
    public final BooleanSetting otherMobs = new BooleanSetting("Other Mobs", this, true);

    public final BooleanSetting ignoreWalls = new BooleanSetting("Ignore Walls", this, false);

    public final BooleanSetting pauseIfEat = new BooleanSetting("Pause If Eat", this, true);
    public final BooleanSetting pauseIfMine = new BooleanSetting("Pause If Mine", this, true);

    public final BooleanSetting clanManager = ClanSettingsBuilder.buildToggle(this);
    public final ModeSetting clanMode = ClanSettingsBuilder.buildStatusMode(this, clanManager);
    public final ModeSetting targetClan = ClanSettingsBuilder.buildClanTargetMode(this, clanManager, clanMode);

    public KillAura() {
        super("KillAura",
                "lang.module.KillAura",
                KeyboardUtils.RELEASE,
                MCategory.COMBAT,
                false
        );

        initSettings(
                mode,
                attackMode,
                range,
                rotateMode,
                packets,
                delay,
                postCooldown,

                instaAttack,
                lockTicks,

                players,
                teammates,
                friends,
                hostiles,
                passive,
                golems,
                otherMobs,

                ignoreWalls,

                pauseIfEat,
                pauseIfMine,

                clanManager,
                clanMode,
                targetClan
        );
    }

    private Predicate<Entity> entityFilter;

    private final Ticker delayTicker = new Ticker();
    public Target targetedEntity;
    private float[] rotations;
    private final TravelChanger travelChanger = new TravelChanger(5000,
            () -> new Float[]{rotations[0], rotations[1]},
            () -> {if (rotateMode.getValue().equals("Grim")) GrimUtils.sendPreActionGrimPackets(Managers.TRAVEL_CHANGE_MANAGER.getYaw(), Managers.TRAVEL_CHANGE_MANAGER.getPitch());
            },
            () -> rotations != null && !needPause() && mode.getValue().equals("Aura") && (rotateMode.getValue().equals("Grim") || !instaAttack.getValue()) && targetedEntity != null
    );

    @Override
    public void onEnable() {
        super.onEnable();
        delayTicker.reset();
        targetedEntity = null;
        entityFilter = KillAuraUtils.createEntityFilter(hostiles, passive, golems, otherMobs);
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

    @EventSubscriber
    public void onPacket(PacketEvent.Send e) {
        if (needPause()) return;
        if (mode.getValue().equals("Aura")) {
            if (rotateMode.getValue().equals("Grim") || !instaAttack.getValue()) {
                if (targetedEntity == null) {
                    if (rotations != null) {
                        rotations[0] = MathHelper.lerp(mc.getRenderTickCounter().getTickDelta(true), rotations[0], mc.player.getYaw());
                        rotations[1] = MathHelper.lerp(mc.getRenderTickCounter().getTickDelta(true), rotations[1], mc.player.getPitch());
                        if (e.getPacket() instanceof PlayerInputC2SPacket) {
                            IPlayerInputC2SPacket packet = (IPlayerInputC2SPacket) e.getPacket();
                            changeInput(packet);
                        }
                        if (e.getPacket() instanceof PlayerMoveC2SPacket) {
                            IPlayerMoveC2SPacket packet = (IPlayerMoveC2SPacket) e.getPacket();
                            packet.setYaw(rotations[0]);
                            packet.setPitch(rotations[1]);
                        }
                        rotations = null;
                    }
                } else {
                    if (e.getPacket() instanceof PlayerInputC2SPacket) {
                        IPlayerInputC2SPacket packet = (IPlayerInputC2SPacket) e.getPacket();
                        changeInput(packet);
                    }
                }
            }
        }
    }

    //---------Aura---------//
    public void auraMode() {
        if (!mc.player.isAlive()) {
            targetedEntity = null;
            return;
        }
        if (!delayPassed()) return;
        if (targetedEntity == null || targetedEntity.lockTicks <= 0) {
            targetSearchAction();
        }

        attackTargetAction();
    }

    public void targetSearchAction() {
        Entity target = null;
        if (players.getValue())
            target = KillAuraUtils.filterPlayers(range.getValue(), friends.getValue(), teammates.getValue(), clanManager.getValue(), clanMode.getValue(), targetClan.getValue());

        if (target == null)
            target = KillAuraUtils.filterEntity(range.getValue(), entityFilter);

        if (target != null) {
            targetedEntity = new Target(target, 0);
        }
    }

    public void attackTargetAction() {
        if (targetedEntity != null) {
            if (!Managers.TRAVEL_CHANGE_MANAGER.containsChanger(travelChanger)) Managers.TRAVEL_CHANGE_MANAGER.addChanger(travelChanger);
            if (targetedEntity.lockTicks >= (int) lockTicks.getValue()) {
                RotateMode rotateMode = getRotateMode();
                KillAuraUtils.preAttackRotate(rotateMode, rotations, (int) packets.getValue());
                KillAuraUtils.attackNoRotate(targetedEntity.entity);
                //KillAuraUtils.postAttackRotate(rotateMode);
                delayTicker.reset();
                targetedEntity = null;
                if (Managers.TRAVEL_CHANGE_MANAGER.containsChanger(travelChanger)) Managers.TRAVEL_CHANGE_MANAGER.removeChanger(travelChanger);
            } else {
                Vec3d rotateVector = targetedEntity.entity.getPos();
                rotations = AimBotUtils.rotations(rotateVector);
                KillAuraUtils.preAttackRotate(getRotateMode(), rotations, (int) packets.getValue());
                if (rotateMode.getValue().equals("Grim") || !instaAttack.getValue()) {
                    targetedEntity = new Target(targetedEntity.entity, targetedEntity.lockTicks + 1);
                } else {
                    targetedEntity = new Target(targetedEntity.entity, (int) lockTicks.getValue());
                }
            }
        } else {
            if (Managers.TRAVEL_CHANGE_MANAGER.containsChanger(travelChanger)) Managers.TRAVEL_CHANGE_MANAGER.removeChanger(travelChanger);
        }
    }

    public void changeInput(IPlayerInputC2SPacket packet) {
        float forward = packet._getForward();
        float sideways = packet._getSideways();
        float delta = (mc.player.getYaw() - rotations[0]) * MathHelper.RADIANS_PER_DEGREE;
        float cos = MathHelper.cos(delta);
        float sin = MathHelper.sin(delta);
        packet._setSideways(Math.round(sideways * cos - forward * sin));
        packet._setForward(Math.round(forward * cos + sideways * sin));
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
                    KillAuraUtils.attack(ent, RotateMode.NONE, 0);
                }
            }
            if (entityFilter.test(ent)) {
                KillAuraUtils.attack(ent, RotateMode.NONE, 0);
            }

            delayTicker.reset();
        }
    }
    //----------------------------//

    public RotateMode getRotateMode() {
        return switch (rotateMode.getValue()) {
            case "Packet" -> RotateMode.PACKET;
            case "Vanilla" -> RotateMode.VANILLA;
            case "Grim" -> RotateMode.GRIM;
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
        if (pauseIfMine.getValue()) {
            return (mc.player.getActiveItem().getItem() instanceof ToolItem && mc.player.isUsingItem()) || (ModuleList.packetMine.isEnabled() && (ModuleList.packetMine.currentBreakingBlock != null || !ModuleList.packetMine.conveyorBlocks.isEmpty()));
        }
        if (pauseIfEat.getValue()) {
            if (ItemUtils.isFood(mc.player.getActiveItem()) && mc.player.isUsingItem()) return true;
        }
        return false;
    }

    public record Target(Entity entity, int lockTicks) {}
}