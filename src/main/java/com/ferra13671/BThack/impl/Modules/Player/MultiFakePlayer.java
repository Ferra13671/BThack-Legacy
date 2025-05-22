package com.ferra13671.BThack.impl.Modules.Player;

import com.ferra13671.BThack.events.InputEvent;
import com.ferra13671.BThack.managers.managers.Setting.Settings.KeyCodeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Utils.PlayerUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.UUID;

@ModuleInfo(name = "MultiFakePlayer", description = "lang.module.MultiFakePlayer", category = "PLAYER")
public class MultiFakePlayer extends Module {

    public final KeyCodeSetting summonKey = new KeyCodeSetting("Summon Key", this);


    private final ArrayList<Entity> fakePlayers = new ArrayList<>();

    @Override
    public void onEnable() {
        super.onEnable();

        arrayListInfo = "0";
    }

    @EventSubscriber
    public void onInput(InputEvent.KeyInputEvent e) {
        if (nullCheck()) return;

        if (summonKey.isPressed()) {
            Entity fakePlayer = PlayerUtils.createNewFakePlayer(mc.player, new GameProfile(UUID.randomUUID(), "FakePlayer" + fakePlayers.size()));
            fakePlayers.add(fakePlayer);
            arrayListInfo = "" + fakePlayers.size();
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        for (Entity fakePlayer : fakePlayers) {
            PlayerUtils.removeEntity(fakePlayer);
        }

        fakePlayers.clear();
    }
}