package com.ferra13671.BThack.managers.impl;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.utils.Initializable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class SocialManager implements Initializable {
    private final String socialFilePath;
    private final String playerSocialName;
    private final List<String> players = new ArrayList<>();


    public SocialManager(String socialFilePath, String playerSocialName) {
        this.socialFilePath = socialFilePath;
        this.playerSocialName = playerSocialName;
    }

    @Override
    public void init() {
        load();
    }

    public void save() {
        boolean m = false;

        try {
            if (Files.exists(Paths.get("BThack/Social/" + socialFilePath))) {
                File f = new File("BThack/Social/" + socialFilePath);
                FileWriter writer = new FileWriter(f, false);

                for (String name : players) {
                    if (!m) {
                        writer.write(name);
                        m = true;
                    } else
                        writer.write(System.lineSeparator() + name);
                }

                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load() {
        players.clear();

        try {
            if (Files.exists(Paths.get("BThack/Social/" + socialFilePath))) {
                File f = new File("BThack/Social/" + socialFilePath);
                BufferedReader reader = new BufferedReader(new FileReader(f));
                String line = reader.readLine();

                while (line != null) {
                    players.add(line);
                    line = reader.readLine();
                }

                reader.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract Formatting getColor();

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
