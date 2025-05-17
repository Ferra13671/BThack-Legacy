package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.api.Events.PacketEvent;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import com.ferra13671.BThack.api.Utils.MathUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(name = "AutoFish", description = "lang.module.AutoFish", category = "PLAYER")
public class AutoFish extends Module {

    public final NumberSetting patience = new NumberSetting("Patience", this, 150, 50, 500, true);
    public final NumberSetting retryDelay = new NumberSetting("Retry Delay", this, 12, 5, 20, true);
    public final NumberSetting biteDelay = new NumberSetting("Bite Delay", this, 0, 0, 20, true);

    public final NumberSetting validRange = new NumberSetting("Valid Range", this, 30, 10, 50, false);


    private int castRodTimer;
    private int reelInTimer;
    private boolean soundBiteDetected;

    @Override
    public void onEnable() {
        super.onEnable();

        soundBiteDetected = false;
        castRodTimer = 0;
        reelInTimer = 0;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        soundBiteDetected = false;
        castRodTimer = 0;
        reelInTimer = 0;
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck())  return;

        if(castRodTimer > 0)
            castRodTimer--;
        if(reelInTimer > 0)
            reelInTimer--;

        if (!swapToBestRod()) return;

        if (!isFishing()) {
            if(castRodTimer > 0)
                return;

            reelInTimer = 20 * patience.getValue().intValue();

            ItemUtils.useItem(Hand.MAIN_HAND, true, mc.player.getYaw(), mc.player.getPitch());
            castRodTimer = retryDelay.getValue().intValue();
            return;
        }

        if(soundBiteDetected) {
            reelInTimer = biteDelay.getValue().intValue();
            soundBiteDetected = false;
        } else if (mc.player.fishHook.getHookedEntity() != null) {
            reelInTimer = biteDelay.getValue().intValue();
        }

        if(reelInTimer == 0)
        {
            ItemUtils.useItem(Hand.MAIN_HAND, true, mc.player.getYaw(), mc.player.getPitch());
            reelInTimer = retryDelay.getValue().intValue();
            castRodTimer = retryDelay.getValue().intValue();
        }
    }

    @EventSubscriber
    public void onPacket(PacketEvent.Receive e) {
        if (e.getPacket() instanceof PlaySoundS2CPacket packet) {
            if (packet.getSound().value() == SoundEvents.ENTITY_FISHING_BOBBER_SPLASH) {
                if (!isFishing()) return;

                double distance = MathUtils.getDistance(new Vec3d(packet.getX(), packet.getY(), packet.getZ()), mc.player.fishHook.getPos());
                if (distance < validRange.getValue()) {
                    soundBiteDetected = true;
                }
            }
        }
    }


    private boolean isFishing() {
        return mc.player != null && mc.player.fishHook != null
                && !mc.player.fishHook.isRemoved()
                && mc.player.getMainHandStack().isOf(Items.FISHING_ROD);
    }



    private boolean swapToBestRod() {
        int bestRodQuality = -1;
        int bestRodSlot = -1;

        int tempRodQuality;

        ItemStack stack = mc.player.getInventory().getStack(mc.player.getInventory().selectedSlot);

        if (stack.getItem() instanceof FishingRodItem) {
            bestRodQuality = getRodQuality(stack);
            bestRodSlot = mc.player.getInventory().selectedSlot;
        }

        for (int i = 0; i < 36; i++) {
            stack = mc.player.getInventory().getStack(i);
            tempRodQuality = getRodQuality(stack);
            if (tempRodQuality > bestRodQuality) {
                bestRodQuality = tempRodQuality;
                bestRodSlot = i;
            }
        }

        if (bestRodSlot != -1) {
            if (bestRodSlot == mc.player.getInventory().selectedSlot) return true;
            if (bestRodSlot < 9) {
                InventoryUtils.swapItem(bestRodSlot);
            } else {
                InventoryUtils.swapItemOnInventory(mc.player.getInventory().selectedSlot, bestRodSlot);
            }
            return true;
        }
        return false;
    }


    private int getRodQuality(ItemStack stack) {
        if(stack.isEmpty() || !(stack.getItem() instanceof FishingRodItem))
            return -1;

        int luckOfTheSeaLvl = ItemUtils.getEnchantmentLevel(stack, Enchantments.LUCK_OF_THE_SEA);
        int lureLvl = ItemUtils.getEnchantmentLevel(stack, Enchantments.LURE);
        int unbreakingLvl = ItemUtils.getEnchantmentLevel(stack, Enchantments.UNBREAKING);
        int mendingLvl = ItemUtils.getEnchantmentLevel(stack, Enchantments.MENDING);
        int noVanishLvl = EnchantmentHelper.hasAnyEnchantmentsWith(stack, EnchantmentEffectComponentTypes.PREVENT_EQUIPMENT_DROP) ? 0 : 1;

        return luckOfTheSeaLvl * 9 + lureLvl * 9 + unbreakingLvl * 2 + mendingLvl
                + noVanishLvl;
    }
}
