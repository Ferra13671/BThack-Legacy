package com.ferra13671.BThack.api.Social.Clans;

public enum ClanStatus {
    FRIENDLY("Friendly"),
    NEUTRAL("Neutral"),
    ENEMY("Enemy");




    private final String name;

    ClanStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
