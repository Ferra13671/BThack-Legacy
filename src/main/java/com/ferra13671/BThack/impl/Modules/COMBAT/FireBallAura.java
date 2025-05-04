package com.ferra13671.BThack.impl.Modules.COMBAT;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.*;
import com.ferra13671.BThack.api.Utils.Modules.KillAuraUtils;
import com.ferra13671.BThack.api.Utils.Grim.GrimUtils;
import com.ferra13671.BThack.api.Utils.Rotate.RotateMode;
import com.ferra13671.BThack.api.Utils.Rotate.RotateUtils;
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

    public final NumberSetting range = new NumberSetting("Range", this, 3, 2, 6, false);

    public final BooleanSetting rotate = new BooleanSetting("Rotate", this, true);
    public final ModeSetting rotateMode = new ModeSetting("Rotate Mode", this, Arrays.asList("Packet", "Grim"));

    public final BooleanSetting shulkerBullets = new BooleanSetting("Shulker Bullets", this, true);

    public final BooleanSetting noDurability = new BooleanSetting("No Durability", this, false);

    public FireBallAura() {
        super("FireBallAura",
                "lang.module.FireBallAura",
                KeyboardUtils.RELEASE,
                MCategory.COMBAT,
                false
        );

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
            float[] rots = RotateUtils.rotations(entity);
            switch (rotateMode.getValue()) {
                case "Packet" ->
                        Managers.NETWORK_MANAGER.sendPacket(
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
                        Managers.NETWORK_MANAGER.sendPacket(
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
