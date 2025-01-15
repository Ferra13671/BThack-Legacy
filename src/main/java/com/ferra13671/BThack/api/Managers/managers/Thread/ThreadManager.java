package com.ferra13671.BThack.api.Managers.managers.Thread;

import com.ferra13671.BThack.api.Managers.Manager;

public final class ThreadManager implements Manager {

    private ThreadManager() {}

    @Override
    public void init() {
        //no action
    }

    public static void startNewThread(IThread iThread) {
        BThackThread thread = new BThackThread() {
            @Override
            public void threadAction() throws ThreadClosedException {
                iThread.start(this);
            }
        };

        thread.start();
    }

    public static void startNewThread(String threadName, IThread iThread) {
        BThackThread thread = new BThackThread(threadName) {
            @Override
            public void threadAction() throws ThreadClosedException {
                iThread.start(this);
            }
        };

        thread.start();
    }
}
