package com.ferra13671.BThack.api.Managers.Thread;

public final class ThreadManager {

    private ThreadManager() {}


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
