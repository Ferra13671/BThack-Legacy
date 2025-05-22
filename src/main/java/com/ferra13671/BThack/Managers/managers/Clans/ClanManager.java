package com.ferra13671.BThack.managers.managers.Clans;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.core.Client.Systems.ConfigSystem.SubConfigs;
import com.ferra13671.BThack.api.Utils.Initializable;
import net.minecraft.entity.player.PlayerEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class ClanManager implements Initializable {
    private final List<Clan> clans = new ArrayList<>();

    @Override
    public void init() {}

    public Clan getFirstClanFromMember(String memberName) {
        for (Clan clan : clans) {
            if (clan.getMembers().contains(memberName))
                return clan;
        }
        return null;
    }

    public List<Clan> getClansFromMember(String memberName) {
        List<Clan> temp = new ArrayList<>();

        for (Clan clan : clans) {
            if (clan.getMembers().contains(memberName))
                temp.add(clan);
        }
        return temp;
    }

    public void addClan(Clan clan) {
        String concurrentClan = clans.stream().map(Clan::getName).filter(s -> clan.getName().equals(s)).findFirst().orElse(null);
        if (concurrentClan != null) BThack.log("This clan already exists!");
        else {
            clans.add(clan);
            SubConfigs.CLANS.save();
        }
    }

    public void removeClan(Clan clan) {
        if (clans.contains(clan)) {
            clans.remove(clan);
            Path path = Paths.get("BThack/Social/Clans/" + clan.getName() + ".json");
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                BThack.error(e.getMessage());
            }
            SubConfigs.CLANS.save();
        } else
            BThack.log("This clan doesn't exist!");
    }

    public void reload() {
        clans.clear();
        SubConfigs.CLANS.load();
    }

    public boolean isAlly(PlayerEntity player) {
        String name = player.getDisplayName().getString();
        return isAlly(name);
    }

    public boolean isAlly(String name) {
        for (Clan clan : clans) {
            if (clan.getMembers().contains(name)) return true;
        }
        return false;
    }

    public List<Clan> getClans() {
        return clans;
    }
}
