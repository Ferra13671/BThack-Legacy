package com.ferra13671.BThack.api.Social.Clans;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigSystem;
import net.minecraft.entity.player.PlayerEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class ClanManager {
    private static final List<Clan> clans = new ArrayList<>();

    public static Clan getClan(String clanName) {
        for (Clan clan : clans) {
            if (clan.getName().equals(clanName))
                return clan;
        }

        return null;
    }

    public static Clan getFirstClanFromMember(String memberName) {
        for (Clan clan : clans) {
            if (clan.getMembers().contains(memberName))
                return clan;
        }
        return null;
    }

    public static List<Clan> getClansFromMember(String memberName) {
        List<Clan> temp = new ArrayList<>();

        for (Clan clan : clans) {
            if (clan.getMembers().contains(memberName))
                temp.add(clan);
        }
        return temp;
    }

    public static void addClan(Clan clan) {
        String concurrentClan = clans.stream().map(Clan::getName).filter(s -> clan.getName().equals(s)).findFirst().orElse(null);
        if (concurrentClan != null) BThack.log("This clan already exists!");
        else {
            clans.add(clan);
            try {
                ConfigSystem.saveClans();
            } catch (IOException e) {
                BThack.error(e.getMessage());
            }
        }
    }

    public static void removeClan(Clan clan) {
        if (clans.contains(clan)) {
            clans.remove(clan);
            Path path = Paths.get("BThack/Social/Clans/" + clan.getName() + ".json");
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                BThack.error(e.getMessage());
            }
            try {
                ConfigSystem.saveClans();
            } catch (IOException e) {
                BThack.error(e.getMessage());
            }
        } else
            BThack.log("This clan doesn't exist!");
    }

    public static void reload() {
        clans.clear();
        try {
            ConfigSystem.loadClans();
        } catch (IOException e) {
            BThack.error(e.getMessage());
        }
    }

    public static boolean isAlly(PlayerEntity player) {
        String name = player.getDisplayName().getString();
        return isAlly(name);
    }

    public static boolean isAlly(String name) {
        for (Clan clan : clans) {
            if (clan.getMembers().contains(name)) return true;
        }
        return false;
    }

    public static List<Clan> getClans() {
        return clans;
    }
}
