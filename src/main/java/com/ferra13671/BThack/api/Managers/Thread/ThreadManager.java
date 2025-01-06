package com.ferra13671.BThack.api.Managers.Thread;

public final class ThreadManager {

    private ThreadManager() {}


    public static void startNewThread(IThread iThread) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                iThread.start(this);
            }
        };

        thread.start();
    }

    public static void startNewThread(String threadName, IThread iThread) {
        Thread thread = new Thread(threadName) {
            @Override
            public void run() {
                iThread.start(this);
            }
        };
        thread.start();
    }
}
