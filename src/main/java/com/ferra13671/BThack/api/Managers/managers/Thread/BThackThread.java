package com.ferra13671.BThack.api.Managers.managers.Thread;

public abstract class BThackThread extends Thread {
    private boolean closed;

    public BThackThread() {
    }

    public BThackThread(String threadName) {
        super(threadName);
    }


    @Override
    public final void run() {
        try {
            threadAction();
        } catch (ThreadClosedException e) {
            cancelAction();
        }
    }

    public abstract void threadAction() throws ThreadClosedException;

    public void cancelAction() {

    }

    public void checkThreadStopped() throws ThreadClosedException {
        if (isThreadClosed()) throw new ThreadClosedException();
    }

    public void stopOnException() throws ThreadClosedException {
        throw new ThreadClosedException();
    }

    public void closeThread() {
        closed = true;
    }

    public boolean isThreadClosed() {
        return closed;
    }


    @Override
    public void interrupt() {
        closeThread();
    }

    @Override
    public boolean isInterrupted() {
        return isThreadClosed();
    }
}
