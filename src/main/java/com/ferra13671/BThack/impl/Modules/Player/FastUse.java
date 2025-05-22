package com.ferra13671.BThack.impl.Modules.Player;

import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.events.PacketEvent;
import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import com.ferra13671.BThack.mixins.accessor.IMinecraftClient;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.item.*;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;

import java.util.Arrays;

@ModuleInfo(name = "FastUse", description = "lang.module.FastUse", category = "PLAYER")
public class FastUse extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", this, Arrays.asList("Normal", "Ultra"));
    public final NumberSetting times = new NumberSetting("Times", this, 30, 5, 64, true, () -> mode.getValue().equals("Ultra"));

    public final BooleanSetting crystals = new BooleanSetting("Crystals", this, true);
    public final BooleanSetting fishRods = new BooleanSetting("Fishing Rods", this, true);
    public final BooleanSetting throwables = new BooleanSetting("Throwables", this, false);
    public final BooleanSetting expBottle = new BooleanSetting("ExpBottle", this, true, () -> !throwables.getValue());
    public final BooleanSetting others = new BooleanSetting("Others", this, true);


    private boolean sending = false;

    @EventSubscriber
    public void onUpdate(ClientTickEvent e) {
        if (nullCheck()) return;

        Item item = mc.player.getActiveItem().getItem();

        if (crystals.getValue())
            if (item instanceof EndCrystalItem)
                fastUseAction();
        if (fishRods.getValue())
            if (item instanceof FishingRodItem)
                fastUseAction();
        if (throwables.getValue()) {
            if (item instanceof FireworkRocketItem) fastUseAction();
        }
        if (others.getValue())
            if (!(item instanceof FishingRodItem || item instanceof BlockItem) &&
                    !ItemUtils.isFood(item.getDefaultStack()) &&
                    !(item instanceof BowItem)
            )
                fastUseAction();
    }

    @EventSubscriber
    public void onPacketSend(PacketEvent.Send e) {
        if (sending) return;
        if (e.getPacket() instanceof PlayerInteractItemC2SPacket packet) {
            Hand hand = packet.getHand();

            Item item = hand == Hand.MAIN_HAND ? mc.player.getMainHandStack().getItem() : mc.player.getOffHandStack().getItem();

            if (throwables.getValue())
                if (item instanceof ExperienceBottleItem ||
                        item instanceof EggItem ||
                        item instanceof EnderPearlItem ||
                        item instanceof EnderEyeItem ||
                        item instanceof FireworkRocketItem ||
                        item instanceof SnowballItem ||
                        item instanceof SplashPotionItem
                )
                    fastUseAction();
            if (expBottle.getValue() && !throwables.getValue())
                if (item instanceof ExperienceBottleItem)
                    fastUseAction();
        }
        if (e.getPacket() instanceof PlayerInteractBlockC2SPacket packet) {
            Hand hand = packet.getHand();

            Item item = hand == Hand.MAIN_HAND ? mc.player.getMainHandStack().getItem() : mc.player.getOffHandStack().getItem();

            if (throwables.getValue()) {
                if (item instanceof FireworkRocketItem) fastUseAction();
            }
        }
    }

    public void fastUseAction() {
        if (mode.getValue().equals("Normal"))
            mc.itemUseCooldown = 0;
        else
            ultraMegaSuper52691488UltimateUse();
    }

    public void ultraMegaSuper52691488UltimateUse() {
        if (mc.options.useKey.isPressed()) {
            sending = true;
            try {
                for (int i = 0; i < times.getValue().intValue(); i++) {
                    ((IMinecraftClient) mc).useItem();
                    mc.itemUseCooldown = 0;
                }
            } finally {
                sending = false;
            }
        }
    }
}
