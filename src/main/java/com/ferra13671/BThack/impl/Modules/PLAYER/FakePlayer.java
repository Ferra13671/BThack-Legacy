package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.api.Events.DisconnectEvent;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Utils.PlayerUtils;
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
        if (mc.player != null && mc.world != null) {
            fakePlayer = PlayerUtils.createNewFakePlayer(mc.player, "FakePlayer");
        }
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
    public void onDisconnect(DisconnectEvent e) {
        fakePlayer = null;
        toggle();
    }
}
