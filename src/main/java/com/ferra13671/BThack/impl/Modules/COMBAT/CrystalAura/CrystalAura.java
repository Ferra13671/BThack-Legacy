package com.ferra13671.BThack.impl.Modules.COMBAT.CrystalAura;

import com.ferra13671.BTbot.api.Utils.Generate.GenerateNumber;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Build.BuildManager;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.*;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.BThack.api.Utils.Grim.GrimUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.ToolItem;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Deprecated
public class CrystalAura extends Module {

    public static ModeSetting page;

    //---------Place---------//
    public static NumberSetting placeDelay;
    public static NumberSetting placeRange;
    public static ModeSetting placeMode;

    public static BooleanSetting placeSwing;
    public static ModeSetting pSwingMode;

    public static BooleanSetting allowSwap;
    public static BooleanSetting allowInventory;
    //-----------------------//

    //---------Break---------//
    public static NumberSetting breakDelay;
    public static NumberSetting breakRange;
    public static ModeSetting breakMode;
    public static NumberSetting breakAttempts;
    public static BooleanSetting checkPlayers;

    public static BooleanSetting breakSwing;
    public static ModeSetting bSwingMode;
    //-----------------------//

    //---------Logic---------//
    public static ModeSetting crMode;
    public static BooleanSetting ignoreWalls;
    public static NumberSetting resetWait;
    public static BooleanSetting checkTheSequence;

    public static BooleanSetting rotate;
    public static ModeSetting rotateMode;
    public static BooleanSetting postRotate;
    public static ModeSetting rotateTo;
    public static BooleanSetting rotateMath;
    public static ModeSetting rMathMode;

    public static ModeSetting filterMode;
    public static NumberSetting targetRange;

    public static NumberSetting minDamage;
    public static NumberSetting maxSelfDmg;


    public static BooleanSetting pauseIfEat;
    public static BooleanSetting pauseIfMine;

    public static BooleanSetting pauseIfHealth;
    public static NumberSetting minHealth;
    //-----------------------//

    //---------Render---------//
    //------------------------//

    public CrystalAura() {
        super("CrystalAura",
                "",
                KeyboardUtils.RELEASE,
                MCategory.COMBAT,
                false
        );

        page = new ModeSetting("Page", this, Arrays.asList("Place", "Break", "Logic", "Render"));

        //---------Place---------//
        placeDelay = new NumberSetting("Place Delay", this, 1, 0, 10, true);
        placeRange = new NumberSetting("Place Range", this, 4, 2, 7, false);
        placeMode = new ModeSetting("Place Mode", this, Arrays.asList("Client", "Packet"));

        placeSwing = new BooleanSetting("Place Swing", this, false);
        pSwingMode = new ModeSetting("PSwingMode", this, Arrays.asList("Normal", "Spam"), () -> placeSwing.getValue());

        allowSwap = new BooleanSetting("Allow Swap", this, true);
        allowInventory = new BooleanSetting("Allow Inventory", this, true, () -> allowSwap.getValue());
        //-----------------------//

        //---------Break---------//
        breakDelay = new NumberSetting("Break Delay", this, 1, 0, 10, true);
        breakRange = new NumberSetting("Break Range", this, 4, 2, 7, false);
        breakMode = new ModeSetting("Break Mode", this, Arrays.asList("Client", "Packet"));
        breakAttempts = new NumberSetting("Break Attempts", this, 1, 1, 5, true);
        checkPlayers = new BooleanSetting("Check Players", this, true);

        breakSwing = new BooleanSetting("Break Swing", this, false);
        bSwingMode = new ModeSetting("BSwingMode", this, Arrays.asList("Normal", "Spam"));
        //-----------------------//

        //---------Logic---------//
        crMode = new ModeSetting("CrMode", this, Arrays.asList("BreakPlace", "PlaceBreak"));
        ignoreWalls = new BooleanSetting("Ignore Walls", this, true);
        resetWait = new NumberSetting("Reset Wait", this, 20, 5, 60, true);
        checkTheSequence = new BooleanSetting("Check The Sequence", this, true);

        rotate = new BooleanSetting("Rotate", this, true);
        rotateMode = new ModeSetting("Rotate Mode", this, Arrays.asList("Grim", "Packet", "Client", "None"), () -> rotate.getValue());
        postRotate = new BooleanSetting("Post Rotate", this, false, () -> rotate.getValue());
        rotateTo = new ModeSetting("Rotate To", this, Arrays.asList("All", "Place", "Break"), () -> rotate.getValue() && !rotateMode.getValue().equals("None"));
        rotateMath = new BooleanSetting("Rotate Math", this, true, () -> rotate.getValue());
        rMathMode = new ModeSetting("RMath Mode", this, Arrays.asList("New", "Old"), () -> rotate.getValue() && rotateMath.getValue());

        filterMode = new ModeSetting("Filter", this, Arrays.asList("Combined", "Max Attack", "Min Self"));
        targetRange = new NumberSetting("Target Range", this, 4, 2, 7, false);

        minDamage = new NumberSetting("Min Damage", this, 5, 5, 36, false);
        maxSelfDmg = new NumberSetting("Max Self Dmg", this, 8, 0, 36, false);

        pauseIfEat = new BooleanSetting("Pause If Eat", this, true);
        pauseIfMine = new BooleanSetting("Pause If Mine", this, true);

        pauseIfHealth = new BooleanSetting("Pause If Health", this, false);
        minHealth = new NumberSetting("Min Health", this, 7, 2, 16, false, pauseIfHealth::getValue);
        //-----------------------//

        //---------Render---------//
        //------------------------//


        initSettings(
                page,
                //---------Place---------//
                placeDelay,
                placeRange,
                placeMode,

                placeSwing,
                pSwingMode,

                allowSwap,
                allowInventory,
                //-----------------------//

                //---------Break---------//
                breakDelay,
                breakRange,
                breakMode,
                breakAttempts,
                checkPlayers,

                breakSwing,
                bSwingMode,
                //-----------------------//

                //---------Logic---------//
                crMode,
                ignoreWalls,
                resetWait,
                checkTheSequence,

                rotate,
                rotateMode,
                postRotate,
                rotateTo,
                rotateMath,
                rMathMode,

                filterMode,
                targetRange,

                minDamage,
                maxSelfDmg,

                pauseIfEat,
                pauseIfMine,

                pauseIfHealth,
                minHealth
                //-----------------------//

                //---------Render---------//
                //------------------------//
        );
    }

    private final Ticker placeTicker = new Ticker();
    private final Ticker breakTicker = new Ticker();
    private final Ticker resetTicker = new Ticker();

    private Entity target = null;
    private CrystalPos crystal = new CrystalPos(null, 0, 0);

    private CrystalAction lastAction;


    @Override
    public void onEnable() {
        super.onEnable();

        placeTicker.reset();
        breakTicker.reset();
        resetTicker.reset();
        lastAction = CrystalAction.NONE;

        target = null;
        crystal = null;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        target = null;
        crystal = null;
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        target = getNearbyPlayer(targetRange.getValue());
        if (target == null) return;

        action();
    }

    public void action() {
        switch (crMode.getValue()) {
            case "BreakPlace" -> {
                breakAction();
                placeAction();
            }
            case "PlaceBreak" -> {
                placeAction();
                breakAction();
            }
        }
    }

    //---------Break Action---------//
    public void breakAction() {
        if (checkPause()) return;
        /*
        if (checkTheSequence.getValue()) {
            if (lastAction == CrystalAction.BREAK) return;
        }

         */

        if (!breakTicker.passed(breakDelay.getValue() * 50)) return;

        if (crystal != null) {
            boolean breaked = false;
            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof EndCrystalEntity cr) {
                    BlockPos pos = new ModifyBlockPos(cr.getX(), cr.getY(), cr.getZ());
                    if (pos.equals(crystal.position())) {
                        breakInternalPre(cr);
                        breaked = true;
                        break;
                    }
                }
            }
            if (!breaked) {
                if (resetTicker.passed(resetWait.getValue())) {
                    crystal = null;
                    resetTicker.reset();
                }
            }
        }


        EndCrystalEntity entityEnderCrystal;
        ArrayList<EndCrystalEntity> entities = new ArrayList<>();
        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof EndCrystalEntity cr)
                entities.add(cr);
        }

        entityEnderCrystal = entities.stream().filter(entity2 -> MathUtils.getDistance(mc.player.getPos(), entity2.getPos()) < breakRange.getValue())
                .min(Comparator.comparing(entity -> mc.player.distanceTo(entity)))
                .orElse(null);

        breakInternalPre(entityEnderCrystal);
        lastAction = CrystalAction.BREAK;
    }

    public void breakInternalPre(Entity entityEnderCrystal) {

        if (entityEnderCrystal == null || !entityEnderCrystal.isAlive()) return;
        if (checkPlayers.getValue()) {
            PlayerEntity player = getNearbyPlayer(breakRange.getValue());
            if (player == null) return;
        }

        if (!BlockUtils.hasLineOfSight(entityEnderCrystal.getPos()) && !ignoreWalls.getValue()) return;

        if (rotate.getValue()) {
            Vec3d rotateVector = getBreakMathRot(entityEnderCrystal.getPos(), entityEnderCrystal);

            float[] rots = AimBotUtils.rotations(rotateVector);

            rotatePre(rots[0], rots[1]);
        }

        breakInternal(entityEnderCrystal);

        if (rotate.getValue())
            rotatePost();

        breakTicker.reset();
    }

    public void breakInternal(Entity endCrystalEntity) {
        for (int i = 0; i < breakAttempts.getValue(); i++) {
            switch (breakMode.getValue()) {
                case "Client" -> {
                    mc.interactionManager.attackEntity(mc.player, endCrystalEntity);
                    mc.player.networkHandler.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
                }
                case "Packet" -> {
                    mc.interactionManager.syncSelectedSlot();
                    mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(target, mc.player.isSneaking()));
                    mc.player.networkHandler.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
                    mc.player.resetLastAttackedTicks();
                }
            }
            if (breakSwing.getValue()) {
                switch (bSwingMode.getValue()) {
                    case "Normal" -> mc.player.swingHand(Hand.MAIN_HAND);
                    case "Spam" -> {
                        mc.player.swingHand(Hand.MAIN_HAND);
                        mc.player.swingHand(Hand.MAIN_HAND);
                        mc.player.swingHand(Hand.MAIN_HAND);
                        mc.player.swingHand(Hand.MAIN_HAND);
                        mc.player.swingHand(Hand.MAIN_HAND);
                        mc.player.swingHand(Hand.MAIN_HAND);
                    }
                }
            }
        }
    }

    public Vec3d getBreakMathRot(Vec3d vec, Entity entity) {
        if (rotateMath.getValue()) {
            switch (rMathMode.getValue()) {
                case "New" -> {
                    double xLength = Math.abs(entity.getBoundingBox().maxX - entity.getBoundingBox().minX);
                    double yLength = Math.abs(entity.getBoundingBox().maxY - entity.getBoundingBox().minY);
                    double zLength = Math.abs(entity.getBoundingBox().maxZ - entity.getBoundingBox().minZ);
                    vec.x += GenerateNumber.generateDouble(-(xLength / 3), xLength / 3);
                    vec.y += GenerateNumber.generateDouble(-(yLength / 3), yLength / 3);
                    vec.z += GenerateNumber.generateDouble(-(zLength / 3), zLength / 3);
                }
                case "Old" -> {
                    vec.x += (Math.random() / 5);
                    vec.y += (Math.random() / 5);
                    vec.z += (Math.random() / 5);
                }
            }
        }
        return vec;
    }
    //------------------------------//

    //---------Place Action---------//
    public void placeAction() {
        if (checkPause() || crystal != null) return;
        if (checkTheSequence.getValue()) {
            if (lastAction == CrystalAction.PLACE) return;
        }

        if (!placeTicker.passed(placeDelay.getValue() * 50)) return;

        List<CrystalPos> crystalPositions = new ArrayList<>();
        CrystalPos tempCrystalPos;

        for (BlockPos pos : getCrystalPositions()) {
            if (MathUtils.getDistance(mc.player.getPos(), pos.toCenterPos()) >= placeRange.getValue()) continue;

            double targetDamage = CrystalUtils.calculateDamage(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, target);
            double selfDamage = mc.player.getAbilities().creativeMode ? 0 : CrystalUtils.calculateDamage(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, mc.player);

            if (targetDamage < minDamage.getValue()) continue;
            if (selfDamage > maxSelfDmg.getValue()) continue;

            crystalPositions.add(new CrystalPos(pos, targetDamage, selfDamage));
        }

        tempCrystalPos = filterCrystalPoses(crystalPositions);
        if (tempCrystalPos == null) {
            crystal = null;
            return;
        }
        crystal = tempCrystalPos;

        if (crystal.position() != null) {
            if (!swapToCrystal()) return;


            if (rotate.getValue()) {
                Vec3d vec3d = getPlaceMathRot(crystal.position().toCenterPos());

                float[] rots = AimBotUtils.rotations(vec3d);
                rotatePre(rots[0], rots[1]);
            }

            placeInternal(crystal.position());

            if (rotate.getValue())
                rotatePost();
        }

        placeTicker.reset();
        resetTicker.reset();

        lastAction = CrystalAction.PLACE;
    }

    public void placeInternal(BlockPos pos) {
        BlockHitResult hitResult = BuildManager.getHitResult(pos.add(0, 1, 0));
        switch (placeMode.getValue()) {
            case "Client" -> mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hitResult);
            case "Packet" -> mc.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, hitResult, 0));
        }
    }

    public Vec3d getPlaceMathRot(Vec3d vec) {
        if (rotateMath.getValue()) {
            vec.x += (Math.random() / 5);
            vec.y += (Math.random() / 5);
            vec.z += (Math.random() / 5);
        }
        return vec;
    }
    //------------------------------//

    public void rotatePre(float yaw, float pitch) {
        switch (rotateMode.getValue()) {
            case "Grim" -> {
                GrimUtils.sendPreActionGrimPackets(yaw, pitch);
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, mc.player.onGround));
            }
            case "Packet" -> mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, mc.player.onGround));
            case "Client" -> {
                mc.player.setYaw(yaw);
                mc.player.setPitch(pitch);
            }
        }
    }

    public void rotatePost() {
        if (rotateMode.getValue().equals("Grim") && postRotate.getValue()) {
            GrimUtils.sendPostActionGrimPackets();
        }
    }

    public boolean swapToCrystal() {
        if (mc.player.getInventory().getStack(mc.player.getInventory().selectedSlot).getItem() == Items.END_CRYSTAL) return true;
        if (!allowSwap.getValue()) return false;

        int slot = InventoryUtils.findItem(Items.END_CRYSTAL, allowInventory.getValue() ? 36 : 9);
        if (slot == -1) return false;

        if (slot < 9) {
            InventoryUtils.swapItem(slot);
        } else {
            int freeSlot = InventoryUtils.findFreeHotbarSlot();
            if (freeSlot == -1) {
                freeSlot = mc.player.getInventory().selectedSlot;
            }
            InventoryUtils.swapItemOnInventory(freeSlot, slot);
            InventoryUtils.swapItem(freeSlot);
        }
        return true;
    }

    public PlayerEntity getNearbyPlayer(double range) {
        ArrayList<PlayerEntity> players = new ArrayList<>();
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (mc.player == player) continue;
            players.add(player);
        }

        return players.stream().filter(player -> player != mc.player && MathUtils.getDistance(mc.player.getPos(), player.getPos()) < range)
                .min(Comparator.comparing(player2 -> mc.player.distanceTo(player2))).orElse(null);
    }

    public List<BlockPos> getCrystalPositions() {
        List<BlockPos> poses = BlockUtils.getNearbyBlocks(mc.player, placeRange.getValue(), false).stream()
                .filter(blockPos ->
                        getCollidedEntity(BlockUtils.createBox(blockPos.add(0,1,0), 0.5, 0.5, 0.5, true)) == null &&
                                CrystalUtils.canPlaceCrystal(blockPos)
                )
                .toList();
        return poses;
    }

    public Entity getCollidedEntity(Box box) {
        for (Entity entity : mc.world.getEntities()) {
            if (entity.getBoundingBox().offset(0, 0.5, 0).intersects(box)) return entity;
        }
        return null;
    }

    public CrystalPos filterCrystalPoses(List<CrystalPos> poses) {
        Stream<CrystalPos> stream = poses.stream();
        CrystalPos result = null;

        switch (filterMode.getValue()) {
            case "Max Attack" -> result = stream.max(Comparator.comparing(CrystalPos::targetDamage)).orElse(null);
            case "Min Self" -> result = stream.min(Comparator.comparing(CrystalPos::selfDamage)).orElse(null);
            case "Combined" -> {
                CrystalPos temp = new CrystalPos(null, 0, 1000);
                for (CrystalPos crystalPos : poses) {
                    if (crystalPos.selfDamage() <= temp.selfDamage() && crystalPos.targetDamage() >= temp.targetDamage())
                        temp = crystalPos;
                }
                if (temp.targetDamage() != 0 && temp.selfDamage() != 1000) {
                    result = temp;
                }
            }
        }

        return result;
    }

    public boolean checkPause() {
        if (pauseIfHealth.getValue()) {
            if ((mc.player.getHealth() + mc.player.getAbsorptionAmount()) <= minHealth.getValue()) return true;
        }

        if (pauseIfEat.getValue()) {
            if (mc.player.getActiveItem().getItem().getFoodComponent() != null && mc.player.isUsingItem()) return true;
        }

        if (pauseIfMine.getValue()) {
            return ( mc.player.getActiveItem().getItem() instanceof ToolItem && mc.player.isUsingItem()) || (ModuleList.packetMine.isEnabled() && (ModuleList.packetMine.currentBreakingBlock != null || !ModuleList.packetMine.conveyorBlocks.isEmpty()));
        }

        return false;
    }

    private enum CrystalAction {
        PLACE,
        BREAK,
        NONE
    }
}
