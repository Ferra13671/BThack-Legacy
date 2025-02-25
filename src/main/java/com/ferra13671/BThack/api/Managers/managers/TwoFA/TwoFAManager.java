package com.ferra13671.BThack.api.Managers.managers.TwoFA;

import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigUtils;
import com.ferra13671.BThack.Core.FileSystem.JsonUtils;
import com.ferra13671.BThack.api.Managers.managers.TwoFA.TOTP.TOTPGenerator;
import com.ferra13671.BThack.api.Managers.managers.TwoFA.TOTP.TotpSecret;
import com.ferra13671.BThack.api.Utils.Initializable;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class TwoFAManager implements Initializable {
    private final HashMap<String, String> twoFAKeys = new HashMap<>();

    private final TOTPGenerator totpGenerator = new TOTPGenerator();

    @Override
    public void init() {
    }

    public String getCode(String name) {
        if (twoFAKeys.containsKey(name)) {
            return totpGenerator.generateCurrent(TotpSecret.fromBase32EncodedString(twoFAKeys.get(name))).value();
        } else return null;
    }

    public void save() throws IOException {
        ConfigUtils.saveInJson("2FAKeys", "", jsonObject -> {
            JsonArray keys = new JsonArray();
            twoFAKeys.forEach((name, key) -> {
                JsonObject keyObject = new JsonObject();
                JsonUtils.add(keyObject, "nickname", name);
                JsonUtils.add(keyObject, "key", key);
                keys.add(keyObject);
            });
            JsonUtils.add(jsonObject, "keys", keys);
        });
    }

    public void load() throws IOException {
        ConfigUtils.loadFromJson("2FAKeys", "", jsonObject -> {
            if (!JsonUtils._null(jsonObject, "keys")) {
                JsonArray keys = jsonObject.getAsJsonArray("keys").getAsJsonArray();
                keys.asList().forEach(jsonElement -> {
                    JsonObject keyObject = jsonElement.getAsJsonObject();
                    if (!JsonUtils.equalsNull(keyObject, "nickname", "key"))
                        twoFAKeys.put(keyObject.get("nickname").getAsString(), keyObject.get("key").getAsString());
                });
            }
        }, () -> {});
    }

    public Set<String> getNames() {
        return twoFAKeys.keySet();
    }

    public boolean contains(String name) {
        return twoFAKeys.containsKey(name);
    }

    public void put(String name, String key) {
        twoFAKeys.put(name, key);
    }

    public void remove(String name) {
        twoFAKeys.remove(name);
    }
}
