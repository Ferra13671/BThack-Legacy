package com.ferra13671.BThack.api.Managers.managers.Thread;

import com.ferra13671.BThack.api.Utils.Initializable;

public final class ThreadManager implements Initializable {

    private ThreadManager() {}

    @Override
    public void init() {
        //no action
    }

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
