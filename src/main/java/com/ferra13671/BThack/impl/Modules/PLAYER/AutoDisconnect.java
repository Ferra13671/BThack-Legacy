package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.mixins.accessor.IPlayerInventory;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;

/**
 * @author Ferra13671 and Nikitadan4pi
 */

public class AutoDisconnect extends Module {

    public final BooleanSetting autoToggle = new BooleanSetting("Auto Toggle", this, true);

    public final BooleanSetting health = new BooleanSetting("Health", this, true);
    public final NumberSetting minHealth = new NumberSetting("Min Health", this, 10,1,20,true, health::getValue);

    public final BooleanSetting introvert = new BooleanSetting("Introvert", this,false);
    public final NumberSetting range = new NumberSetting("Range", this, 7, 4, 15, false, introvert::getValue);
    public final BooleanSetting friends = new BooleanSetting("Friends", this, false, introvert::getValue);

    public final BooleanSetting height = new BooleanSetting("Height", this, false);
    public final NumberSetting minHeight = new NumberSetting("Min Height", this, 30, 0, 300, false, height::getValue);

    public final BooleanSetting totems = new BooleanSetting("Totems", this, false);
    public final NumberSetting minTotems = new NumberSetting("Min Totems", this, 3, 1, 11, true, totems::getValue);

    public AutoDisconnect() {
        super("AutoDisconnect",
                "lang.module.AutoDisconnect",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );
    }

    @EventSubscriber
    public void onPlayerTick(ClientTickEvent e) {
        if (nullCheck()) return;

        if (health.getValue())
            autoDisconnectAction();

        if (introvert.getValue())
            introvertAction();

        if (height.getValue())
            heightAction();

        if (totems.getValue())
            totemsAction();
    }

    public void autoDisconnectAction() {
        float playerHP = mc.player.getHealth() + mc.player.getAbsorptionAmount();
        double minHP = minHealth.getValue();
        if (playerHP <= minHP) {
            mc.player.networkHandler.onDisconnect(new DisconnectS2CPacket(Text.literal("Your health has reached its minimum limit(" + minHP + "). You have been disconnected.  Your HP: " + playerHP)));
            if (autoToggle.getValue()) {
                toggle();
            }
        }
    }

    public void introvertAction() {
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player) continue;

            if (player.distanceTo(mc.player) < range.getValue().floatValue()) {
                if (friends.getValue()) {
                    mc.player.networkHandler.onDisconnect(new DisconnectS2CPacket(Text.literal(getChatName() + " You were disconnected because a player was detected near you.")));
                    if (autoToggle.getValue()) toggle();
                } else {
                    if (!Managers.FRIENDS_MANAGER.contains(player)) {
                        mc.player.networkHandler.onDisconnect(new DisconnectS2CPacket(Text.literal(getChatName() + " You were disconnected because a player was detected near you.")));
                        if (autoToggle.getValue()) toggle();
                    }
                }
            }
        }
    }

    public void heightAction() {
        if (mc.player.getY() < minHeight.getValue()) {
            mc.player.networkHandler.onDisconnect(new DisconnectS2CPacket(Text.literal(String.format(getChatName() + " Your position on Y was less than %s. You've been disconnected.", minHeight.getValue()))));
            if (autoToggle.getValue()) toggle();
        }
    }

    public void totemsAction() {
        int totems = 0;

        for (DefaultedList<ItemStack> list : ((IPlayerInventory) mc.player.getInventory()).getCombinedInventory()) {
            for (ItemStack stack : list) {
                if (stack.getItem() == Items.TOTEM_OF_UNDYING)
                    totems += stack.getCount();
            }
        }

        if (totems < minTotems.getValue()) {
            mc.player.networkHandler.onDisconnect(new DisconnectS2CPacket(Text.literal(String.format(getChatName() + " The number of your totems is less than the minimum number allowed(current: %s minimum: %s).", totems, minTotems.getValue()))));
            if (autoToggle.getValue()) toggle();
        }
    }
}