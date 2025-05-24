package com.ferra13671.BThack.impl.Modules.Combat;

import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import com.ferra13671.BThack.api.Utils.Rotate.RotateUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ShieldItem;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;

import java.util.ArrayList;
import java.util.Comparator;

@ModuleInfo(name = "AutoShield", description = "lang.module.AutoShield", category = "COMBAT")
public class AutoShield extends Module {

    private boolean pressed = false;
    private short delayTick = 0;

    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onClientTick(ClientTickEvent e) {
        if (nullCheck()) return;

        ArrayList<Entity> entities = new ArrayList<>();

        for (Entity entity : mc.world.getEntities()) {
            entities.add(entity);
        }

        Entity entity1 = entities.stream().filter(entity -> entity instanceof PersistentProjectileEntity).filter(entity -> !entity.onGround).min(Comparator.comparing(
                entity -> entity.distanceTo(mc.player))).filter(entity -> entity.distanceTo(mc.player) <= 5).orElse(null);

        PersistentProjectileEntity arrow = (PersistentProjectileEntity) entity1;

        if (arrow != null) {
            if (mc.player.getOffHandStack() != null) {
                if (mc.player.getOffHandStack().getItem() instanceof ShieldItem) {
                    float yaw = RotateUtils.rotations(arrow)[0];
                    Managers.NETWORK_MANAGER.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(yaw, mc.player.pitch, mc.player.onGround, mc.player.horizontalCollision));

                    //I don't know why but without it, minecraft doesn't want to recognize that the shield is activated
                    mc.options.useKey.setPressed(true);

                    mc.player.setCurrentHand(Hand.OFF_HAND);
                    ItemUtils.useItem(Hand.OFF_HAND, false, yaw, mc.player.getPitch());
                    pressed = true;

                    delayTick = 10;
                }
            }
        } else {
            if (delayTick > 0) {
                delayTick--;
            } else {
                if (mc.options.useKey.isPressed()) {
                    if (pressed) {
                        mc.options.useKey.setPressed(false);
                        mc.player.stopUsingItem();
                        pressed = false;
                    }
                }
            }
        }
    }
}
