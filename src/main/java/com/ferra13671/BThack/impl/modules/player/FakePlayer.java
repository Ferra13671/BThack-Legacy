package com.ferra13671.BThack.impl.modules.player;

import com.ferra13671.BThack.events.DisconnectEvent;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.api.utils.PlayerUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.Entity;

@ModuleInfo(name = "FakePlayer", description = "lang.module.FakePlayer", category = "PLAYER")
public class FakePlayer extends Module {

    private Entity fakePlayer;

    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }
        super.onEnable();
        fakePlayer = PlayerUtils.createNewFakePlayer(mc.player, "FakePlayer");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (fakePlayer != null) {
            PlayerUtils.removeEntity(fakePlayer);
            fakePlayer = null;
        }
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onDisconnect(DisconnectEvent e) {
        fakePlayer = null;
        toggle();
    }
}
