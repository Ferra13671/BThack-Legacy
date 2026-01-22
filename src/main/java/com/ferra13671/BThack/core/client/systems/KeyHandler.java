package com.ferra13671.BThack.core.client.systems;

import com.ferra13671.BThack.core.client.Client;
import com.ferra13671.BThack.events.InputEvent;
import com.ferra13671.BThack.api.utils.Mc;
import com.ferra13671.BThack.api.utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

public class KeyHandler implements Mc {

    @EventSubscriber
    @SuppressWarnings("unused")
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
