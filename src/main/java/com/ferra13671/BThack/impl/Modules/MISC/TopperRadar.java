package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.Constants;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.managers.Thread.ThreadManager;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Managers.managers.Clans.ClanSettingsBuilder;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.KillAuraUtils;
import com.ferra13671.BThack.api.Utils.Rotate.RotateUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;

public class TopperRadar extends Module {

    public final NumberSetting threshold = new NumberSetting("Min Matches", this, 3, 1, 4, true);
    public final NumberSetting amount = new NumberSetting("Min Amount", this, 3, 1, 4, true);
    public final BooleanSetting onlyGoodArmor = new BooleanSetting("Only Good Armor", this, true);

    public final BooleanSetting friends = new BooleanSetting("Friends", this, false);
    public final BooleanSetting clanManager = ClanSettingsBuilder.buildToggle(this);
    public final ModeSetting clanMode = ClanSettingsBuilder.buildStatusMode(this, clanManager);
    public final ModeSetting target = ClanSettingsBuilder.buildClanTargetMode(this, clanManager, clanMode);

    public final BooleanSetting autoDisconnect = new BooleanSetting("AutoDisconnect", this, false);
    public final NumberSetting shutdownDelay = new NumberSetting("Shutdown Delay", this, 3, 1, 10, true, autoDisconnect::getValue);

    public TopperRadar() {
        super("TopperRadar",
                "lang.module.TopperRadar",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        initSettings(
                threshold,
                amount,
                onlyGoodArmor,

                friends,
                clanManager,
                clanMode,
                target,
                autoDisconnect,
                shutdownDelay
        );
    }

    private final ArrayList<AbstractClientPlayerEntity> reportedToppers = new ArrayList<>();
    protected boolean pause = false;

    @EventSubscriber
    public void onClientTick(ClientTickEvent e) {
        if (nullCheck() || pause) return;

        arrayListInfo = autoDisconnect.getValue() ? "AutoDisconnect" : "";

        for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player || reportedToppers.contains(player)) continue;
            if (!friends.getValue()) {
                if (Managers.FRIENDS_MANAGER.contains(player)) continue;
            }
            if (!KillAuraUtils.isSuccessfulClanMember(player, clanManager, clanMode, target)) continue;

            int armorMatches = 0;

            for (ItemStack itemStack : player.getArmorItems()) {
                byte enchantmentMatches = 0;
                if (itemStack.getItem() instanceof ArmorItem armor) {
                    if (onlyGoodArmor.getValue()) {
                        if (armor.getMaterial() != ArmorMaterials.DIAMOND && armor.getMaterial() != ArmorMaterials.NETHERITE) continue;
                    }

                    for (RegistryEntry<Enchantment> ench : itemStack.getEnchantments().getEnchantments()) {
                        int lvl = itemStack.getEnchantments().getLevel(ench);

                        if ((ench.equals(mc.world.getRegistryManager().get(Enchantments.BLAST_PROTECTION.getRegistryRef()).getEntry(Enchantments.BLAST_PROTECTION).get()) &&
                                    lvl > 2) ||  // 3-4
                                (ench.equals(mc.world.getRegistryManager().get(Enchantments.PROTECTION.getRegistryRef()).getEntry(Enchantments.PROTECTION).get()) &&
                                        lvl > 2) ||  // 3-4
                                (ench.equals(mc.world.getRegistryManager().get(Enchantments.UNBREAKING.getRegistryRef()).getEntry(Enchantments.UNBREAKING).get()) &&
                                        lvl > 1) || // 2-3
                                (ench.equals(mc.world.getRegistryManager().get(Enchantments.THORNS.getRegistryRef()).getEntry(Enchantments.THORNS).get()) &&
                                        lvl > 1) || // 2-3
                                ench.equals(mc.world.getRegistryManager().get(Enchantments.MENDING.getRegistryRef()).getEntry(Enchantments.MENDING).get())
                        ) {
                            enchantmentMatches++;
                        }
                    }
                }

                if (enchantmentMatches >= threshold.getValue())
                    armorMatches++;
            }

            if (armorMatches >= amount.getValue()) {
                String health = Constants.DECIMAL_FORMAT.format(player.getHealth());

                String message = Formatting.GOLD + String.format(LanguageSystem.translate("lang.module.TopperRadar.spotted"), Formatting.RED, Formatting.GOLD, Formatting.AQUA, Formatting.WHITE + player.getDisplayName().getString(), Formatting.AQUA, Formatting.WHITE + health, Formatting.AQUA, Formatting.WHITE + RotateUtils.getDirection(player));

                ChatUtils.sendMessage(message, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP);

                reportedToppers.add(player);

                if (autoDisconnect.getValue()) {
                    ThreadManager.startNewThread(thread -> {

                        int delay = shutdownDelay.getValue().intValue();

                        ChatUtils.sendMessage(Formatting.RED + LanguageSystem.translate("lang.module.radars.disconnectCountdown"));

                        while (delay != 0) {
                            ChatUtils.sendMessage(String.format(Formatting.RED + LanguageSystem.translate("lang.module.radars.disconnectAfter"), delay));

                            thread.sleepThread(1000);

                            delay--;
                        }

                        mc.player.networkHandler.onDisconnect(new DisconnectS2CPacket(Text.literal(message)));

                        pause = false;
                    });
                    pause = true;
                }
            }
        }

        reportedToppers.removeIf(player ->!mc.world.getPlayers().contains(player));
    }
}
