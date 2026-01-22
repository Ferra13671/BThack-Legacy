package com.ferra13671.BThack.managers.impl;

import com.ferra13671.BThack.core.client.systems.config.ConfigUtils;
import com.ferra13671.BThack.core.client.systems.file.JsonUtils;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.api.utils.Data;
import com.ferra13671.BThack.api.utils.Initializable;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class AutoAuthManager implements Initializable {
    private final HashMap<String, String> passwords = new HashMap<>();

    @Override
    public void init() {}

    public String getPassword(String nickname) {
        Data<String> password = new Data<>(passwords.getOrDefault(nickname, null));
        if (password.get() == null)
            Managers.ACCOUNT_MANAGER.getAccounts().forEach(account -> {
                if (account.name().equals(nickname) && !account.autoAuth().isEmpty()) password.set(account.autoAuth());
            });
        return password.get();
    }

    public void save() throws IOException {
        ConfigUtils.saveInJson("AutoAuthPasswords", "", jsonObject -> {
            JsonArray jsonElements = new JsonArray();
            passwords.forEach((playerName, password) -> {
                JsonObject info = new JsonObject();
                JsonUtils.add(info, playerName, password);
                jsonElements.add(info);
            });
            JsonUtils.add(jsonObject, "info", jsonElements);
        });
    }

    public void load() throws IOException {
        ConfigUtils.loadFromJson("AutoAuthPasswords", "", jsonObject -> {
            if (JsonUtils._null(jsonObject, "info")) return;
            for (JsonElement jsonElement : jsonObject.get("info").getAsJsonArray().asList()) {
                jsonElement.getAsJsonObject().asMap().forEach((playerName, password) -> passwords.put(playerName, password.getAsString()));
            }
        }, () -> {});
    }

    public Set<String> getNames() {
        return passwords.keySet();
    }

    public boolean contains(String nickname) {
        return passwords.containsKey(nickname);
    }

    public void put(String nickname, String password) {
        passwords.put(nickname, password);
    }

    public void remove(String nickname) {
        passwords.remove(nickname);
    }
}
