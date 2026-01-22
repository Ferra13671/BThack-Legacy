package com.ferra13671.BThack.core.client.systems.gui;

import com.ferra13671.BThack.gui.widget.LanguageSelectorWidget;
import com.ferra13671.BThack.gui.widget.OutdatedVersionWidget;
import com.ferra13671.BThack.gui.widget.UpdateWidget;

public class BThackWidgets {
    private static boolean inited = false;

    public static ScreenWidget LANGUAGE_SELECTOR;
    public static ScreenWidget OUTDATED_VERSION;
    public static ScreenWidget UPDATE;

    public static void init() {
        if (inited) return;

        LANGUAGE_SELECTOR = new LanguageSelectorWidget();
        OUTDATED_VERSION = new OutdatedVersionWidget();
        UPDATE = new UpdateWidget();

        inited = true;
    }
}
