package com.ferra13671.BThack.impl.Modules.COMBAT;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.KillAuraUtils;
import com.ferra13671.BThack.api.Utils.Grim.GrimUtils;
import com.ferra13671.BThack.impl.Modules.COMBAT.KillAura.RotateMode;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ToolItem;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import java.util.*;

public class FireBallAura extends Module {

    public static NumberSetting range;


    public static BooleanSetting rotate;
    public static ModeSetting rotateMode;

    public static BooleanSetting shulkerBullets;

    public static BooleanSetting noDurability;

    public FireBallAura() {
        super("FireBallAura",
                "lang.module.FireBallAura",
                KeyboardUtils.RELEASE,
                MCategory.COMBAT,
                false
        );

        range = new NumberSetting("Range", this, 3, 2, 6, false);

        rotate = new BooleanSetting("Rotate", this, true);
        rotateMode = new ModeSetting("Rotate Mode", this, Arrays.asList("Packet", "Grim"));

        shulkerBullets = new BooleanSetting("Shulker Bullets", this, true);

        noDurability = new BooleanSetting("No Durability", this, false);

        initSettings(
                range,

                rotate,
                rotateMode,

                shulkerBullets
        );
    }

    private final Set<Entity> fireBalls = Sets.newHashSet();

    @EventSubscriber
    public void onClientTick(ClientTickEvent e) {
        if (nullCheck()) return;

        arrayListInfo = "" + range.getValue();

        ArrayList<Entity> entities = new ArrayList<>();

        for (Entity entity : mc.world.getEntities()) {
            entities.add(entity);
        }

        Entity fireBall = entities.stream().filter(this::checkEntity).min(Comparator.comparing(
                entity -> entity.distanceTo(mc.player))).filter(entity -> entity.distanceTo(mc.player) <= range.getValue()).orElse(null);

        if (fireBall != null) {
            if (!fireBalls.contains(fireBall)) {
                float[] oldRot = new float[]{mc.player.yaw, mc.player.pitch};
                int oldSlot = mc.player.getInventory().selectedSlot;

                preSwap();
                preRotate(fireBall);
                KillAuraUtils.attack(fireBall, RotateMode.NONE, 0);
                postRotate(oldRot[0], oldRot[1]);
                postSwap(oldSlot);
            }
        }

        fireBalls.removeIf(entity -> !entities.contains(entity));
    }

    public void preRotate(Entity entity) {
        if (rotate.getValue()) {
            float[] rots = AimBotUtils.rotations(entity);
            switch (rotateMode.getValue()) {
                case "Packet" ->
                        mc.player.networkHandler.sendPacket(
                                new PlayerMoveC2SPacket.LookAndOnGround(rots[0], rots[1], mc.player.onGround)
                        );
                case "Grim" -> GrimUtils.sendPreActionGrimPackets(rots[0], rots[1]);
            }
        }
    }

    public void postRotate(float oldYaw, float oldPitch) {
        if (rotate.getValue()) {
            switch (rotateMode.getValue()) {
                case "Packet" ->
                        mc.player.networkHandler.sendPacket(
                                new PlayerMoveC2SPacket.LookAndOnGround(oldYaw, oldPitch, mc.player.onGround)
                        );
                case "Grim" -> GrimUtils.sendPostActionGrimPackets();
            }
        }
    }

    public void preSwap() {
        if (!noDurability.getValue()) return;
        for (int i = 0; i < 9; i++) {
            Item item = mc.player.getInventory().getStack(i).getItem();
            if (!(item instanceof ToolItem)) {
                InventoryUtils.swapItem(i);
                return;
            }
        }
    }

    public void postSwap(int oldSlot) {
        if (oldSlot == mc.player.getInventory().selectedSlot) return;
        InventoryUtils.swapItem(oldSlot);
    }

    public boolean checkEntity(Entity entity) {
        return entity instanceof FireballEntity || (shulkerBullets.getValue() && entity instanceof ShulkerBulletEntity);
    }
}
