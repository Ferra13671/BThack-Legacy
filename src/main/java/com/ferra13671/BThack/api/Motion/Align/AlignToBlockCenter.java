package com.ferra13671.BThack.api.Motion.Align;


import com.ferra13671.BThack.api.Interfaces.Mc;

public class AlignToBlockCenter implements Mc {
    private AlignThread thread;

    public void align() {
        double needX = Math.floor(mc.player.getX()) + 0.5;
        double needZ = Math.floor(mc.player.getZ()) + 0.5;

        double minX = needX - 0.15;
        double maxX = needX + 0.15;

        double minZ = needZ - 0.15;
        double maxZ = needZ + 0.15;

        thread = new AlignThread(needX, needZ, minX, maxX, minZ, maxZ);
        thread.start();
    }

    public boolean isMoving() {
        return thread.isMoving;
    }

    public AlignThread getThread() {
        return thread;
    }
}
