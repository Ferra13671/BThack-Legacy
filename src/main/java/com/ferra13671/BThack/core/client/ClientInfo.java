package com.ferra13671.BThack.core.client;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.managers.impl.cape.CapeManager;
import com.ferra13671.BThack.api.utils.Mc;

import java.time.LocalDate;

public final class ClientInfo implements Mc {
    private String name = "BThack " + BThack.VERSION + " | " + mc.getSession().getUsername();
    @SuppressWarnings("FieldCanBeLocal")
    private final String cName = "BThack " + BThack.VERSION;
    private String chatPrefix = "$";
    private String font = "default";
    private CapeInfo capeInfo = CapeManager.DEFAULT_CAPE_INFO;
    private final boolean isWinter;

    ClientInfo() {
        int month = LocalDate.now().getMonth().getValue();
        isWinter = month == 12 || month == 1 || month == 2;
    }

    public void updateName() {
        name = "BThack " + BThack.VERSION + " | " + mc.getSession().getUsername();
    }

    //get
    public String getName() {
        return name;
    }

    public String getCName() {
        return cName;
    }

    public String getChatPrefix() {
        return chatPrefix;
    }

    public String getFont() {
        return font;
    }

    public CapeInfo getCapeInfo() {
        return capeInfo;
    }

    public boolean isWinter() {
        return isWinter;
    }

    //set
    public void setChatPrefix(String chatPrefix) {
        this.chatPrefix = chatPrefix;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public void setCapeInfo(CapeInfo capeInfo) {
        this.capeInfo = capeInfo;
    }

    public record CapeInfo(String dataPath, CapeDataType dataType) {}
    public enum CapeDataType {
        NONE,
        FILE,
        URL
    }
}
