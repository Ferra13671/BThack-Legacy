package com.ferra13671.BThack.core.client.systems.config;

@SuppressWarnings("CallToPrintStackTrace")
public abstract class SubConfig {

    public final void save() {
        try {
            saveSubConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final void load() {
        try {
            loadSubConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void saveSubConfig() throws Exception;

    protected abstract void loadSubConfig() throws Exception;
}
