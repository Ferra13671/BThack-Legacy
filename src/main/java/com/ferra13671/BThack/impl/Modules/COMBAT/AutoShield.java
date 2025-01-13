package com.ferra13671.BThack.impl.Modules.COMBAT;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ShieldItem;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;

import java.util.ArrayList;
import java.util.Comparator;

public class AutoShield extends Module {

    public AutoShield() {
        super("AutoShield",
                "lang.module.AutoShield",
                KeyboardUtils.RELEASE,
                MCategory.COMBAT,
                false
        );
    }


    private boolean pressed = false;
    private short delayTick = 0;

    @EventSubscriber
    public void onClientTick(ClientTickEvent e) {
        if (nullCheck()) return;

        ArrayList<Entity> entities = new ArrayList<>();

        for (Entity entity : mc.world.getEntities()) {
            entities.add(entity);
        }

        Entity entity1 = entities.stream().filter(entity -> entity instanceof PersistentProjectileEntity).filter(entity -> !((PersistentProjectileEntity) entity).inGround).min(Comparator.comparing(
                entity -> entity.distanceTo(mc.player))).filter(entity -> entity.distanceTo(mc.player) <= 5).orElse(null);

        PersistentProjectileEntity arrow = (PersistentProjectileEntity) entity1;

        if (arrow != null) {
            if (mc.player.getOffHandStack() != null) {
                if (mc.player.getOffHandStack().getItem() instanceof ShieldItem) {
                    float yaw = AimBotUtils.rotations(arrow)[0];
                    mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(yaw, mc.player.pitch, mc.player.onGround));

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
