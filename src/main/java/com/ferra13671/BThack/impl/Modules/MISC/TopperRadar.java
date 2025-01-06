package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.Thread.ThreadManager;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Social.Clans.ClansUtils;
import com.ferra13671.BThack.api.Social.SocialManagers;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.BThack.api.Utils.Modules.KillAuraUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TopperRadar extends Module {

    public static NumberSetting threshold;
    public static NumberSetting amount;
    public static BooleanSetting onlyGoodArmor;

    public static BooleanSetting friends;
    public static BooleanSetting clanManager;
    public static ModeSetting clanMode;
    public static ModeSetting target;

    public static BooleanSetting autoDisconnect;
    public static NumberSetting shutdownDelay;

    protected static boolean pause = false;

    public TopperRadar() {
        super("TopperRadar",
                "lang.module.TopperRadar",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        threshold = new NumberSetting("Min Matches", this, 3, 1, 4, true);
        amount = new NumberSetting("Min Amount", this, 3, 1, 4, true);
        onlyGoodArmor = new BooleanSetting("Only Good Armor", this, true);

        friends = new BooleanSetting("Friends", this, false);
        clanManager = ClansUtils.getClanManagerSetting(this);
        clanMode = ClansUtils.getClanModeSetting(this, clanManager);
        target = ClansUtils.getClanTargetSetting(this, clanManager, clanMode);

        autoDisconnect = new BooleanSetting("AutoDisconnect", this, false);
        shutdownDelay = new NumberSetting("Shutdown Delay", this, 3, 1, 10, true, autoDisconnect::getValue);

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

    private static final ArrayList<PlayerEntity> reportedToppers = new ArrayList<>();

    @EventSubscriber
    public void onClientTick(ClientTickEvent e) {
        if (nullCheck() || pause) return;

        arrayListInfo = autoDisconnect.getValue() ? "AutoDisconnect" : "";

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player || reportedToppers.contains(player)) continue;
            if (!friends.getValue()) {
                if (SocialManagers.FRIENDS.contains(player)) continue;
            }
            if (!KillAuraUtils.isSuccessfulClanMember(player, clanManager, clanMode, target)) continue;

            int armorMatches = 0;

            for (ItemStack itemStack : player.getArmorItems()) {
                byte enchantmentMatches = 0;
                if (itemStack.getItem() instanceof ArmorItem armor) {
                    if (onlyGoodArmor.getValue()) {
                        if (armor.getMaterial() != ArmorMaterials.DIAMOND && armor.getMaterial() != ArmorMaterials.NETHERITE) continue;
                    }
                    NbtList list = itemStack.getEnchantments();
                    for (int i = 0; i < list.size(); i++) {
                        String enchName = list.getCompound(i).getString("id");
                        short lvl = list.getCompound(i).getShort("lvl");

                        if (((enchName.equals("minecraft:blast_protection") || enchName.equals("blast_protection")) && lvl > 2) ||  // 3-4
                                ((enchName.equals("minecraft:protection") || enchName.equals("protection")) && lvl > 2) ||  // 3-4
                                ((enchName.equals("minecraft:unbreaking") || enchName.equals("unbreaking")) && lvl > 1) || // 2-3
                                ((enchName.equals("minecraft:thorns") || enchName.equals("thorns")) && lvl > 1) || // 2-3
                                (enchName.equals("minecraft:mending") || enchName.equals("mending"))
                        ) {
                            enchantmentMatches++;
                        }
                    }
                }

                if (enchantmentMatches >= threshold.getValue())
                    armorMatches++;
            }

            if (armorMatches >= amount.getValue()) {
                String pattern = "#0.0";
                String health = new DecimalFormat(pattern).format(player.getHealth());

                String message = Formatting.GOLD + String.format(LanguageSystem.translate("lang.module.TopperRadar.spotted"), Formatting.RED, Formatting.GOLD, Formatting.AQUA, Formatting.WHITE + player.getDisplayName().getString(), Formatting.AQUA, Formatting.WHITE + health, Formatting.AQUA, Formatting.WHITE + AimBotUtils.getDirection(player));

                ChatUtils.sendMessage(message, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP);

                reportedToppers.add(player);

                if (autoDisconnect.getValue()) {
                    ThreadManager.startNewThread(thread -> {

                        int delay = (int) shutdownDelay.getValue();

                        ChatUtils.sendMessage(Formatting.RED + LanguageSystem.translate("lang.module.radars.disconnectCountdown"));

                        while (delay != 0) {
                            ChatUtils.sendMessage(String.format(Formatting.RED + LanguageSystem.translate("lang.module.radars.disconnectAfter"), delay));

                            try {
                                thread.sleep(1000);
                            } catch (InterruptedException ignored) {
                            }

                            delay--;
                        }

                        mc.player.networkHandler.onDisconnect(new DisconnectS2CPacket(Text.of(message)));

                        pause = false;
                    });
                    pause = true;
                }
            }
        }

        reportedToppers.removeIf(player ->!mc.world.getPlayers().contains(player));
    }
}
