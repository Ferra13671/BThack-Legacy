package com.ferra13671.BThack.api.motion;

import com.ferra13671.BThack.api.utils.Mc;
import com.ferra13671.BThack.managers.impl.thread.BThackThread;
import com.ferra13671.BThack.managers.impl.thread.ThreadClosedException;
import com.ferra13671.BThack.api.utils.rotate.RotateUtils;
import net.minecraft.util.math.Vec3d;

public class Goto extends BThackThread implements Mc {
    public double needX;
    public double needZ;
    public double maxX;
    public double minX;
    public double maxZ;
    public double minZ;
    protected boolean pause;
    protected boolean cancel;
    protected CollisionAction action;

    private boolean moving = false;

    public Goto(double needX, double needZ, CollisionAction action) {
        this.needX = needX;
        this.needZ = needZ;
        this.action = action;
    }

    public boolean isMoving() {
        return moving;
    }

    @Override
    public synchronized void start() {
        moving = true;
        super.start();
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public void threadAction() throws ThreadClosedException {
        maxX = needX + 0.15;
        minX = needX - 0.15;
        maxZ = needZ + 0.15;
        minZ = needZ - 0.15;

        while (toFarX() || toFarZ()) {
            checkThreadStopped();
            moving = true;

            mc.player.yaw = RotateUtils.rotations(new Vec3d(needX, mc.player.getY(), needZ))[0];
            mc.player.input.movementForward = 1;

            if (mc.player.horizontalCollision) {
                if (action == CollisionAction.JUMPING) {
                    pause = true;
                    tryJump();
                } else {
                    cancel();
                }
            }

            if (!toFarX() && !toFarZ()) {
                cancel();
                return;
            }

            if (cancel) {
                cancel();
                return;
            }

            Thread.yield();
        }
        cancel = false;
        pause = false;
        mc.player.input.movementForward = 0;
        moving = false;
    }

    @SuppressWarnings("DataFlowIssue")
    public void tryJump() {
        mc.player.input.movementForward = 0;
        double oldPosY = mc.player.getY();
        tryJumpInternal(oldPosY);
        tryJumpInternal(oldPosY);
        tryJumpInternal(oldPosY);
        if (mc.player.getY() < oldPosY + 0.4) {
            cancel = true;
            pause = false;
        }
    }

    @SuppressWarnings("DataFlowIssue")
    public void tryJumpInternal(double oldPosY) {
        if (mc.player.getY() < oldPosY + 0.4) {
            mc.player.jump();
            mc.player.input.movementForward = 1;
            sleepThread(600);
            mc.player.input.movementForward = 0;
        } else
            pause = false;
    }

    @SuppressWarnings("DataFlowIssue")
    private void cancel() throws ThreadClosedException {
        cancel = false;
        pause = false;
        mc.player.input.movementForward = 0;
        moving = false;
        stopOnException();
    }

    @SuppressWarnings("DataFlowIssue")
    private boolean toFarX() {
        return mc.player.getX() > maxX || mc.player.getX() < minX;
    }

    @SuppressWarnings("DataFlowIssue")
    private boolean toFarZ() {
        return mc.player.getZ() > maxZ || mc.player.getZ() < minZ;
    }
}
