package com.ferra13671.BThack.managers.impl.thread;

import com.ferra13671.BThack.api.utils.Initializable;

public final class ThreadManager implements Initializable {

    private ThreadManager() {}

    @Override
    public void init() {}

    public static BThackThread startNewThread(IThread iThread) {
        BThackThread thread = new BThackThread() {
            @Override
            public void threadAction() throws ThreadClosedException {
                iThread.start(this);
            }
        };

        thread.start();
        return thread;
    }

    public static BThackThread startNewThread(String threadName, IThread iThread) {
        BThackThread thread = new BThackThread(threadName) {
            @Override
            public void threadAction() throws ThreadClosedException {
                iThread.start(this);
            }
        };

        thread.start();
        return thread;
    }
}
