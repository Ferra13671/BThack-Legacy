package com.ferra13671.BThack.api.Managers.managers.Macros;

import com.ferra13671.BThack.api.Utils.ChatUtils;

public class Macro implements Runnable {
    private int key;
    private String name, action;

    public Macro(String name, int key, String action) {
        this.key = key;
        this.name = name;
        this.action = action;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public void run() {
        if (action.startsWith("/"))
            ChatUtils.sendCommand(action);
        else
            ChatUtils.sendChatMessage(action);
    }
}
