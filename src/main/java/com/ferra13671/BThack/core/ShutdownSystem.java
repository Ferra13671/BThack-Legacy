package com.ferra13671.BThack.core;

import java.util.ArrayList;
import java.util.List;

public class ShutdownSystem {
    private static final List<Runnable> shutdownHooks = new ArrayList<>();

    public static void addShutdownHook(Runnable hook) {
        shutdownHooks.add(hook);
    }

    public static void init() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdownHooks.forEach(Runnable::run)));
    }
}
