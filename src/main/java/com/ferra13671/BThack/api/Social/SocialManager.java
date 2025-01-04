package com.ferra13671.BThack.api.Social;

import com.ferra13671.BThack.BThack;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class SocialManager {
    private final String socialFilePath;
    private final String playerSocialName;
    private final List<String> players = new ArrayList<>();


    public SocialManager(String socialFilePath, String playerSocialName) {
        this.socialFilePath = socialFilePath;
        this.playerSocialName = playerSocialName;
    }

    public void save() {
        SocialUtils.save(socialFilePath, players);
    }

    public void load() {
        SocialUtils.load(socialFilePath, players);
    }

    public List<String> getPlayers() {
        return players;
    }

    public boolean add(String player) {
        load();
        if (!players.contains(player)) {
            players.add(player);
            save();
            return true;
        } else {
            BThack.log(String.format("This %s already exists!", playerSocialName));
        }
        return false;
    }

    public boolean remove(String player) {
        load();
        if (players.contains(player)) {
            players.remove(player);
            save();
            return true;
        } else {
            BThack.log(String.format("This %s doesn't exist!", playerSocialName));
        }
        return false;
    }

    public boolean contains(PlayerEntity player) {
        String name = player.getNameForScoreboard();
        return players.contains(name);
    }

    public boolean contains(String player) {
        return players.contains(player);
    }
}
