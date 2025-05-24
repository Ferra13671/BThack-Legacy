package com.ferra13671.BThack.impl.Modules.Player;

import com.ferra13671.BThack.events.Player.PlayerTravelEvent;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Utils.Ticker;
import com.ferra13671.BThack.mixins.accessor.entity.IEntity;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

@ModuleInfo(name = "NoElytraBreak", description = "lang.module.NoElytraBreak", category = "PLAYER")
public class NoElytraBreak extends Module {

    public final NumberSetting abuseDelay = new NumberSetting("Abuse Delay", this, 500, 100, 500, true);

    public final BooleanSetting pauseIfFirework = new BooleanSetting("Pause If Firework", this, true);

    private final Ticker ticker = new Ticker();
    private boolean startAbuse = false;

    @Override
    public void onEnable() {
        super.onEnable();
        ticker.reset();
        startAbuse = false;
    }

    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onTravel(PlayerTravelEvent e) {
        if (nullCheck()) {
            ticker.reset();
            return;
        }

        if (!startAbuse && mc.player.isGliding())
                startAbuse = true;

        if (startAbuse)
            abuseFlyAction();
    }

    @SuppressWarnings("DataFlowIssue")
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

    @SuppressWarnings("DataFlowIssue")
    private boolean flyCheck(IEntity entity) {
        if (mc.player.getInventory().getArmorStack(2).getItem() == Items.ELYTRA)
            return !mc.player.verticalCollision && !entity.invokeGetFlag(7) || !mc.player.isGliding();
        else
            return false;
    }
}
