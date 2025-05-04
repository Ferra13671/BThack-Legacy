package com.ferra13671.BTbot.api.Utils.Motion.Align;


import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Events.Entity.UpdateInputEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Thread.BThackThread;
import com.ferra13671.BThack.api.Managers.managers.Thread.ThreadClosedException;
import com.ferra13671.BThack.api.Managers.managers.TravelChange.TravelChanger;
import com.ferra13671.BThack.api.Utils.Grim.GrimUtils;
import com.ferra13671.BThack.api.Utils.Rotate.RotateUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.math.Vec3d;

public class AlignThread extends BThackThread implements Mc {
    boolean isMoving = false;

    private final double needX;
    private final double needZ;
    private final double minX;
    private final double maxX;
    private final double minZ;
    private final double maxZ;

    public float yaw = -99999999;
    private final TravelChanger travelChanger = new TravelChanger(1000,
            () -> new Float[]{yaw, mc.player.getPitch()},
            () -> GrimUtils.sendPreActionGrimPackets(yaw, mc.player.getPitch()),
            () -> true
    );

    public AlignThread(double needX, double needZ, double minX, double maxX, double minZ, double maxZ) {
        this.needX = needX;
        this.needZ = needZ;
        this.minX = minX;
        this.maxX = maxX;
        this.minZ = minZ;
        this.maxZ = maxZ;
    }

    @Override
    public void start() {
        isMoving = true;
        super.start();
    }

    @Override
    public void threadAction() throws ThreadClosedException {
        BThack.EVENT_BUS.register(this);
        Managers.TRAVEL_CHANGE_MANAGER.addChanger(travelChanger);
        isMoving = true;
        try {
            while (!check()) {
                checkThreadStopped();
                rotate();
                Thread.yield();
            }
        } finally {
            isMoving = false;
            mc.player.input.movementForward = 0;
            BThack.EVENT_BUS.unregister(this);
            Managers.TRAVEL_CHANGE_MANAGER.removeChanger(travelChanger);
        }
    }

    @EventSubscriber
    public void onInput(UpdateInputEvent e) {
        mc.player.input.movementForward = 1;
        mc.player.input.movementSideways = 0;
        mc.player.input.sneaking = false;
    }

    private void rotate() {
        yaw = RotateUtils.rotations(new Vec3d(needX, mc.player.getY(), needZ))[0];
    }

    private boolean check() {
        return (mc.player.getX() > minX && mc.player.getX() < maxX) && (mc.player.getZ() > minZ && mc.player.getZ() < maxZ);
    }
}
