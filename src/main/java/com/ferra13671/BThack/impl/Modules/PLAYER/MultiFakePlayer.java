package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.api.Events.InputEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.KeyCodeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.PlayerUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.UUID;

public class MultiFakePlayer extends Module {

    public static KeyCodeSetting summonKey;

    public MultiFakePlayer() {
        super("MultiFakePlayer",
                "lang.module.MultiFakePlayer",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );

        summonKey = new KeyCodeSetting("Summon Key", this);

        initSettings(
                summonKey
        );
    }

    ArrayList<Entity> fakePlayers = new ArrayList<>();

    @EventSubscriber
    public void onInput(InputEvent.KeyInputEvent e) {
        if (nullCheck()) return;

        if (summonKey.isPressed()) {
            Entity fakePlayer = PlayerUtils.createNewFakePlayer(mc.player, new GameProfile(UUID.randomUUID(), "FakePlayer" + fakePlayers.size()));
            fakePlayers.add(fakePlayer);
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