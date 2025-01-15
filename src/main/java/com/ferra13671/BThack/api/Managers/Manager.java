package com.ferra13671.BThack.api.Managers;

import java.util.concurrent.atomic.AtomicBoolean;

public interface Manager {
    AtomicBoolean inited = new AtomicBoolean(false);

    void init();
}
