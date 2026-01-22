package com.ferra13671.BThack.api.utils;

import java.util.concurrent.atomic.AtomicBoolean;

public interface Initializable {
    AtomicBoolean inited = new AtomicBoolean(false);

    void init();
}
