package com.ferra13671.BThack.api.Managers.managers;

import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigUtils;
import com.ferra13671.BThack.Core.FileSystem.JsonUtils;
import com.ferra13671.BThack.api.Utils.Initializable;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class AutoAuthManager implements Initializable {
    private final HashMap<String, String> passwords = new HashMap<>();


    @Override
    public void init() {
    }

    public String getPassword(String nickname) {
        return passwords.getOrDefault(nickname, null);
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
