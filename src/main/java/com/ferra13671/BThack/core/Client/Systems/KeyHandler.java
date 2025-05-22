package com.ferra13671.BThack.core.Client.Systems;

import com.ferra13671.BThack.core.Client.Client;
import com.ferra13671.BThack.events.InputEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

public class KeyHandler implements Mc {

    @EventSubscriber
    public void onKey(InputEvent.KeyInputEvent e) {
        switch (e.getAction()) {
            case PRESS -> {
                if (e.getKeyCode() != KeyboardUtils.RELEASE && mc.currentScreen == null)
                    Client.keyPress(e.getKeyCode());
                if (!KeyboardUtils.getActiveKeys().contains(e.getKeyCode()))
                    KeyboardUtils.addActiveKey(e.getKeyCode());
            }
            case RELEASE -> {
                if (KeyboardUtils.getActiveKeys().contains(e.getKeyCode()))
                    KeyboardUtils.removeActiveKey(e.getKeyCode());
            }
        }
    }
}
