package com.ferra13671.BThack.api.Managers.managers.Cape;

import com.ferra13671.BThack.api.Utils.Initializable;
import net.minecraft.util.Identifier;

public class CapeManager implements Initializable {
    private boolean enabled = true;
    private Cape currentCape = Cape.fromIdentifier(Identifier.of("bthack", "bthack_cape.png"));

    @Override
    public void init() {
    }

    public void setCape(Cape cape) {
        if (currentCape != null) currentCape.close();
        currentCape = cape;
    }

    public Cape getCape() {
        return currentCape;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
