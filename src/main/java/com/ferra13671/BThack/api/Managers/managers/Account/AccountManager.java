package com.ferra13671.BThack.api.Managers.managers.Account;

import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigUtils;
import com.ferra13671.BThack.Core.FileSystem.JsonUtils;
import com.ferra13671.BThack.api.Utils.Initializable;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccountManager implements Initializable {
    private final List<Account> accounts = new ArrayList<>();

    @Override
    public void init() {
    }

    public void addAccount(Account account) {
        if (!accounts.contains(account))
            accounts.add(account);
    }

    public void removeAccount(String name) {
        accounts.removeIf(account -> account.name().equals(name));
    }

    public void replaceAccount(Account oldAccount, Account newAccount) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).equals(oldAccount)) {
                accounts.set(i, newAccount);
                return;
            }
        }
    }

    public List<Account> getAccounts() {
        return new ArrayList<>(accounts);
    }

    public void save() throws IOException {
        ConfigUtils.saveInJson("Accounts", "", jsonObject -> {
            JsonArray accountList = new JsonArray();
            accounts.forEach(account -> {
                JsonObject accountObject = new JsonObject();
                JsonUtils.add(accountObject, "Name", account.name());
                JsonUtils.add(accountObject, "AutoAuth", account.autoAuth());
                JsonUtils.add(accountObject, "twoFA", account.twoFA());
                accountList.add(accountObject);
            });
            jsonObject.add("Accounts", accountList);
        });
    }

    public void load() throws IOException {
        ConfigUtils.loadFromJson("Accounts", "", jsonObject -> {
            if (!JsonUtils._null(jsonObject, "Accounts")) {
                JsonArray accountList = jsonObject.get("Accounts").getAsJsonArray();
                accountList.forEach(jsonElement -> {
                    if (jsonElement.isJsonObject()) {
                        JsonObject accountObject = jsonElement.getAsJsonObject();
                        String[] data = new String[3];

                        if (!JsonUtils._null(accountObject, "Name")) {
                            try {
                                data[0] = accountObject.get("Name").getAsString();
                            } catch (Exception ignored) {
                                return;
                            }
                        }
                        if (!JsonUtils._null(accountObject, "AutoAuth")) {
                            try {
                                data[1] = accountObject.get("AutoAuth").getAsString();
                            } catch (Exception ignored) {}
                        }
                        if (!JsonUtils._null(accountObject, "twoFA")) {
                            try {
                                data[2] = accountObject.get("twoFA").getAsString();
                            } catch (Exception ignored) {}
                        }
                        addAccount(new Account(data[0], data[1], data[2]));
                    }
                });
            }
        }, () -> {});
    }
}
