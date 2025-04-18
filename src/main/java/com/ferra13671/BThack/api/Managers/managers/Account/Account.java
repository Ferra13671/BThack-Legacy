package com.ferra13671.BThack.api.Managers.managers.Account;

import com.ferra13671.BThack.mixins.accessor.IMinecraftClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.session.Session;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public record Account(String name, String autoAuth, String twoFA) {

    public void login() {
        ((IMinecraftClient) MinecraftClient.getInstance()).setSession(new Session(name,
                UUID.randomUUID(), "", Optional.empty(),
                Optional.empty(), Session.AccountType.LEGACY));
        MinecraftClient.getInstance().getWindow().setTitle("");
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Account account = (Account) object;
        return Objects.equals(name, account.name) && Objects.equals(twoFA, account.twoFA) && Objects.equals(autoAuth, account.autoAuth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, autoAuth, twoFA);
    }
}
