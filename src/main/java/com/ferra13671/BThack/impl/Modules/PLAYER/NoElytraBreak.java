package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.api.Events.Player.PlayerTravelEvent;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Ticker;
import com.ferra13671.BThack.mixins.accessor.IEntity;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

/**
 * Super bebra_tyan exploit for flying on elytres without losing durability
 * (Using fireworks will be broken, so you can use the setting to pause when using fireworks)
 * <p>
 * <b>I'm 99% sure Skeet.cc will steal this module >:)</b>
 */
public class NoElytraBreak extends Module {

    public static NumberSetting abuseDelay;

    public static BooleanSetting pauseIfFirework;

    public NoElytraBreak() {
        super("NoElytraBreak",
                "lang.module.NoElytraBreak",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );

        abuseDelay = new NumberSetting("Abuse Delay", this, 500, 100, 500, true);

        pauseIfFirework = new BooleanSetting("Pause If Firework", this, true);

        initSettings(
                abuseDelay,

                pauseIfFirework
        );
    }

    private final Ticker ticker = new Ticker();
    private boolean startAbuse = false;

    @Override
    public void onEnable() {
        super.onEnable();
        ticker.reset();
        startAbuse = false;
    }

    @EventSubscriber
    public void onTravel(PlayerTravelEvent e) {
        if (nullCheck()) {
            ticker.reset();
            return;
        }

        if (!startAbuse)
            if (mc.player.isFallFlying())
                startAbuse = true;

        if (startAbuse)
            abuseFlyAction();
    }

    private void abuseFlyAction() {
        if (pauseIfFirework.getValue()) {
            if (Managers.FIREWORK_MANAGER.isUsingFireWork()) {
                ticker.reset();
                return;
            }
        }

        IEntity player = (IEntity) mc.player;

        if (mc.player.getInventory().getArmorStack(2).getItem() != Items.ELYTRA || mc.player.verticalCollision) {
            landingAction(player);
            return;
        }

        if (ticker.passed(abuseDelay.getValue()) || flyCheck(player)) {

            Managers.NETWORK_MANAGER.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
            player.invokeSetFlag(7, true);
            ticker.reset();
        }
    }

    private void landingAction(IEntity entity) {
        ticker.reset();

        entity.invokeSetFlag(7, false);
        startAbuse = false;
    }

    private boolean flyCheck(IEntity entity) {
        if (mc.player.getInventory().getArmorStack(2).getItem() == Items.ELYTRA) {
            return !mc.player.verticalCollision && !entity.invokeGetFlag(7) || !mc.player.isFallFlying();
        } else
            return false;
    }
}
