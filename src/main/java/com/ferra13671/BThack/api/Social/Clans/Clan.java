package com.ferra13671.BThack.api.Social.Clans;

import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigSystem;

import java.io.IOException;
import java.util.ArrayList;

public class Clan {
    private final String name;
    private String status = "NULL";
    private final float r;
    private final float g;
    private final float b;

    public final ArrayList<Ally> members = new ArrayList<>();

    public Clan(String name, float r, float g, float b) {
        this.name = name;

        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Clan(String name, String colour) {
        this.name = name;

        this.r = colour.equals("green") || colour.equals("blue") ? 0 : colour.equals("violet") ? 0.63f : 1;
        this.g = colour.equals("orange") ? 0.4f : colour.equals("red") || colour.equals("blue") || colour.equals("violet") ? 0 : 1;
        this.b = colour.equals("red") || colour.equals("orange") || colour.equals("yellow") || colour.equals("green") ? 0 : 1;
    }

    public boolean addMemberToClan(String memberName) {
        ArrayList<String> memberNames = new ArrayList<>();
        for (Ally ally : this.members) {
            memberNames.add(ally.name());
        }

        if (!memberNames.contains(memberName)) {
            members.add(new Ally(memberName));
            try {
                ConfigSystem.saveClans();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean removeMemberFromClan(String memberName) {
        for (Ally ally : members) {
            if (ally.name().equals(memberName)) {
                members.remove(ally);
                try {
                    ConfigSystem.saveClans();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return true;
            }
        }
        return false;
    }

    public boolean setStatus(String status) {
        if (ClanStatus.FRIENDLY.getName().equals(status)) {
            this.status = ClanStatus.FRIENDLY.getName();
            return true;
        } else
        if (ClanStatus.ENEMY.getName().equals(status)) {
            this.status = ClanStatus.ENEMY.getName();
            return true;
        } else
        if (ClanStatus.NEUTRAL.getName().equals(status)) {
            this.status = ClanStatus.NEUTRAL.getName();
            return true;
        } else
            return false;
    }

    public String getStatus() {
        return this.status;
    }

    public String getName() {
        return this.name;
    }

    public float getR() {
        return this.r;
    }

    public float getG() {
        return this.g;
    }

    public float getB() {
        return this.b;
    }
}
