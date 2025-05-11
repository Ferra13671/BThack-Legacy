package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Events.Entity.SetVelocityEvent;
import com.ferra13671.BThack.api.Events.Player.PlayerJumpEvent;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.managers.TravelChange.TravelChanger;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Grim.GrimUtils;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.StrafeUtils;
import com.ferra13671.BThack.api.Utils.Ticker;
import com.ferra13671.BThack.impl.Modules.PLAYER.AutoFirework;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;

public class LongJump extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", this, Arrays.asList("Normal", "Normal2", "Glide", "Elytra&Firework"));
    public final NumberSetting speed = new NumberSetting("Speed", this, 1.5, 1.1, 5, false, () -> mode.getValue().equals("Normal"));
    public final NumberSetting strength = new NumberSetting("Strength", this, 1.5, 1.1, 5, false, () -> mode.getValue().equals("Normal2"));
    public final NumberSetting fallSpeed = new NumberSetting("Fall Speed", this, 0.05,0.05, 0.125, false, () -> mode.getValue().equals("Glide"));

    public final NumberSetting flyTime = new NumberSetting("Fly Time", this, 0.5, 0.1, 1, false, () -> mode.getValue().equals("Elytra&Firework"));
    public final ModeSetting swapMode = new ModeSetting("Swap Mode", this, Arrays.asList("Swap", "Pickup"), () -> mode.getValue().equals("Elytra&Firework"));
    public final NumberSetting pitch = new NumberSetting("Pitch", this, 3, 0, 20, false, () -> mode.getValue().equals("Elytra&Firework"));
    public final BooleanSetting grim = new BooleanSetting("Grim", this, true, () -> mode.getValue().equals("Elytra&Firework"));

    public LongJump() {
        super("LongJump",
                "lang.module.LongJump",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );
    }
    private final ElytraAndFireworkMode elytraAndFireworkMode = new ElytraAndFireworkMode();
    private final GlideMode glideMode = new GlideMode();
    private final TravelChanger travelChanger = new TravelChanger(5000, () -> new Float[]{mc.player.getYaw(), (float) -pitch.getValue()}, () -> {
        if (grim.getValue())
            GrimUtils.sendPreActionGrimPackets(Managers.TRAVEL_CHANGE_MANAGER.getLastYaw(), Managers.TRAVEL_CHANGE_MANAGER.getLastPitch());
    }, () -> true);

    @Override
    public void onEnable() {
        super.onEnable();
        ModuleList.elytraFlight.setToggled(false);
    }

    @EventSubscriber
    public void onJump(PlayerJumpEvent e) {
        switch (mode.getValue()) {
            case "Normal" -> {
                double[] speedF = StrafeUtils.getMoveFactors(speed.getValue());
                mc.player.velocity.x = speedF[0];
                mc.player.velocity.z = speedF[1];
            }
            case "Normal2" -> {
                mc.player.velocity.x *= strength.getValue();
                mc.player.velocity.z *= strength.getValue();
            }
            case "Glide" -> glideMode.start();
            case "Elytra&Firework" -> {
                int fireworkSlot = InventoryUtils.findItem(Items.FIREWORK_ROCKET);
                int elytraSlot = InventoryUtils.findItem(Items.ELYTRA);
                if (fireworkSlot == -1) {
                    EFMstop();
                    return;
                }
                if (elytraSlot == -1) {
                    if (!(mc.player.getInventory().getArmorStack(2).getItem() instanceof ElytraItem)) {
                        EFMstop();
                        return;
                    }
                }

                elytraAndFireworkMode.start();
            }
        }
    }

    public void EFMstop() {
        sendNotification(Formatting.RED + LanguageSystem.translate("lang.module.LongJump.ItemsNotFound"));
        setToggled(false);
    }


    public class ElytraAndFireworkMode {
        /*
            /----- Stages -----\

            0. Pre Swap
            1. Jumping
            2. Start Flying
            3. Use Firework
            4. Flying
            5. Post Swap

            \------------------/
         */
        private int stage = 0;
        private final Ticker ticker = new Ticker();

        private boolean jumped = false;
        private boolean sendFlying = false;

        public void start() {
            BThack.EVENT_BUS.register(this);
            stage = 0;
            ticker.reset();
            Managers.TRAVEL_CHANGE_MANAGER.addChanger(travelChanger);
        }

        @EventSubscriber
        public void onTick(ClientTickEvent e) {
            if (nullCheck()) {
                stop();
                return;
            }

            switch (stage) {
                case 0 -> {
                    if (mc.player.getInventory().getArmorStack(2).getItem() instanceof ElytraItem) {
                        nextStage();
                        return;
                    }
                    for (int needSlot = 0; needSlot < 36; needSlot++) {
                        if (mc.player.getInventory().getStack(needSlot).getItem() instanceof ElytraItem) {
                            int item;
                            if (needSlot < 9)
                                item = needSlot + 36;
                            else
                                item = needSlot;

                            if (swapMode.getValue().equals("Swap")) {
                                pc.clickSlot(0, InventoryUtils.CHESTPLATE_SLOT, 0, SlotActionType.QUICK_MOVE);
                                pc.clickSlot(0, item, 0, SlotActionType.QUICK_MOVE);
                            } else {
                                InventoryUtils.replaceItems(InventoryUtils.CHESTPLATE_SLOT, item);
                            }
                            break;
                        }
                    }
                    nextStage();
                }
                case 1 -> {
                    if (!mc.player.verticalCollision) {
                        nextStage();
                        return;
                    }
                    if (!jumped) {
                        mc.player.input.jumping = true;
                        jumped = true;
                    }
                    if (ticker.passed(200)) nextStage();
                }
                case 2 -> {
                    if (mc.player.isFallFlying()) {
                        nextStage();
                        return;
                    }
                    if (!sendFlying) {
                        mc.player.startFallFlying();
                        Managers.NETWORK_MANAGER.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
                        sendFlying = true;
                    }
                    if (ticker.passed(300)) nextStage();
                }
                case 3 -> {
                    AutoFirework.useFirework(false);
                    nextStage();
                }
                case 4 -> {
                    if (ticker.passed(flyTime.getValue() * 1000) || mc.player.verticalCollision) nextStage();
                }
                case 5 -> {
                    if (!(mc.player.getInventory().getArmorStack(2).getItem() instanceof ElytraItem)) {
                        stop();
                        return;
                    }
                    for (int needSlot = 0; needSlot < 36; needSlot++) {
                        if (mc.player.getInventory().getStack(needSlot).getItem() instanceof ArmorItem armorItem) {
                            if (armorItem.getSlotType() == EquipmentSlot.CHEST) {
                                int item;
                                if (needSlot < 9)
                                    item = needSlot + 36;
                                else
                                    item = needSlot;

                                if (swapMode.getValue().equals("Swap")) {
                                    pc.clickSlot(0, InventoryUtils.CHESTPLATE_SLOT, 0, SlotActionType.QUICK_MOVE);
                                    pc.clickSlot(0, item, 0, SlotActionType.QUICK_MOVE);
                                } else {
                                    InventoryUtils.replaceItems(InventoryUtils.CHESTPLATE_SLOT, item);
                                }
                                stop();
                                return;
                            }
                        }
                    }
                    for (int needSlot = 0; needSlot < 36; needSlot++) {
                        if (mc.player.getInventory().getStack(needSlot).getItem() == Items.AIR) {
                            int item;
                            if (needSlot < 9)
                                item = needSlot + 36;
                            else
                                item = needSlot;

                            if (swapMode.getValue().equals("Swap")) {
                                pc.clickSlot(0, InventoryUtils.CHESTPLATE_SLOT, 0, SlotActionType.QUICK_MOVE);
                                pc.clickSlot(0, item, 0, SlotActionType.QUICK_MOVE);
                            } else {
                                InventoryUtils.replaceItems(InventoryUtils.CHESTPLATE_SLOT, item);
                            }
                            break;
                        }
                    }
                    stop();
                }
            }
        }

        private void nextStage() {
            stage++;
            ticker.reset();
            jumped = false;
            sendFlying = false;
        }

        public void stop() {
            nextStage();
            BThack.EVENT_BUS.unregister(this);
            Managers.TRAVEL_CHANGE_MANAGER.removeChanger(travelChanger);
            stage = 0;
        }
    }
    public class GlideMode {
        private final Ticker afterJumpDelay = new Ticker();

        public void start() {
            BThack.EVENT_BUS.register(this);
            afterJumpDelay.reset();
        }

        @EventSubscriber
        public void onSetVelocity(SetVelocityEvent e) {
            if (!afterJumpDelay.passed(100)) return;
            if (!mc.player.verticalCollision) {
                if (mc.player.fallDistance > 0) {
                    e.setVelocity(new Vec3d(e.getVelocity().x, -fallSpeed.getValue(), e.getVelocity().z));
                }
            } else stop();
        }

        public void stop() {
            BThack.EVENT_BUS.unregister(this);
        }
    }
}
