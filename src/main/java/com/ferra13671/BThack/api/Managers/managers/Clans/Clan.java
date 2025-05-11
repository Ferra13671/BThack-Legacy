package com.ferra13671.BThack.api.Managers.managers.Clans;

import com.ferra13671.BThack.core.Client.Systems.ConfigSystem.SubConfigs;

import java.util.ArrayList;
import java.util.List;

public class Clan {
    private final String name;
    private ClanStatus status = ClanStatus.NEUTRAL;
    private final float[] color;

    private final List<String> members = new ArrayList<>();

    private Clan(String name, float r, float g, float b) {
        this.name = name;

        color = new float[]{r, g, b};
    }

    public boolean addMember(String memberName) {
        if (!members.contains(memberName)) {
            members.add(memberName);
            SubConfigs.CLANS.save();
            return true;
        } else {
            return false;
        }
    }

    public boolean removeMember(String memberName) {
        if (members.contains(memberName)) {
            members.removeIf(memberName::equals);
            SubConfigs.CLANS.save();
            return true;
        }
        return false;
    }

    public void setStatus(ClanStatus status) {
        this.status = status;
    }

    public ClanStatus getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public float getR() {
        return color[0];
    }

    public float getG() {
        return color[1];
    }

    public float getB() {
        return color[2];
    }

    public List<String> getMembers() {
        return members;
    }

    public static Clan of(String name, float r, float g, float b) {
        return new Clan(name, r, g, b);
    }
}
