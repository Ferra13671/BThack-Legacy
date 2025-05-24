package com.ferra13671.BThack.managers.managers.TravelChange;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.events.Entity.UpdateInputEvent;
import com.ferra13671.BThack.api.Utils.InputUtils;
import com.ferra13671.BThack.api.Utils.MathUtils;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.events.Camera.RotateCameraEvent;
import com.ferra13671.BThack.events.Player.ChangePlayerLookEvent;
import com.ferra13671.BThack.events.Player.PlayerTravelEvent;
import com.ferra13671.BThack.api.Utils.Mc;
import com.ferra13671.BThack.api.Utils.Initializable;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Rotate.RotateUtils;
import com.ferra13671.BThack.impl.Modules.Player.FreeCam;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TravelChangeManager implements Initializable, Mc {
    /*
    TravelChangers priority:

    KillAura: 10000
    LongJump(Elytra&Firework mode): 5000
    ElytraFlight: 5000
    MoveTask/TunnelTask: 1000
    Sprint: 500
     */

    private final List<TravelChanger> changers = new CopyOnWriteArrayList<>();
    private final FreeCam.FreeCamData freeCamData = new FreeCam.FreeCamData();
    private float lastYaw;
    private float lastPitch;

    @Override
    public void init() {
        BThack.EVENT_BUS.register(this);
        BThack.debug("Travel Change Manager inited!");
    }

    public void addChanger(TravelChanger changer) {
        if (!changers.contains(changer)) {
            changers.add(changer);
            filterChangers();
            freeCamData.yaw = RotateUtils.getCameraYaw();
            freeCamData.pitch = RotateUtils.getCameraPitch();
        }
    }

    @SuppressWarnings("DataFlowIssue")
    public void removeChanger(TravelChanger changer) {
        if (changers.contains(changer)) {
            changers.remove(changer);
            filterChangers();
            if (!Module.nullCheck() && !ModuleList.freeCam.isEnabled()) {
                mc.player.yaw = RotateUtils.getCameraYaw();
                mc.player.pitch = RotateUtils.getCameraPitch();
            }
        }
    }

    public boolean containsChanger(TravelChanger changer) {
        return changers.contains(changer);
    }

    private void filterChangers() {
        changers.sort(Comparator.comparing(TravelChanger::priority));
        Collections.reverse(changers);
    }

    public float getLastYaw() {
        return lastYaw;
    }

    public float getLastPitch() {
        return lastPitch;
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onCameraRotate(RotateCameraEvent e) {
        if (!changers.isEmpty() && !ModuleList.freeCam.isEnabled()) {
            e.setRotation(new Vec2f(freeCamData.yaw, freeCamData.pitch));
        }
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onChangePlayer(ChangePlayerLookEvent e) {
        if (!changers.isEmpty() && !ModuleList.freeCam.isEnabled()) {
            e.cancel();
            freeCamData.changeLookDirection(e.cursorDeltaX, e.cursorDeltaY);
        }
    }

    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onPlayerTravel(PlayerTravelEvent e) {
        if (!changers.isEmpty()) {
            Float[] rots = changers.getFirst().rotateGetter().get();
            lastYaw = rots[0];
            lastPitch = rots[1];
            mc.player.setYaw(lastYaw);
            mc.player.setPitch(lastPitch);
        }
    }

    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onInputUpdate(UpdateInputEvent e) {
        if (!changers.isEmpty()) {
            TravelChanger travelChanger = changers.getFirst();
            if (travelChanger.withMoveFix().get())
                moveFix(mc.player.isSneaking(), travelChanger);
        }
    }

    /*
    I'm too lazy to write all the math myself, so I just use a matrix for transform player input.
     */
    @SuppressWarnings("DataFlowIssue")
    private void moveFix(boolean sneaking, TravelChanger travelChanger) {
        float forward = (mc.player.input.playerInput.forward() ? 1 : mc.player.input.playerInput.backward() ? -1 : 0);
        float sideways = (mc.player.input.playerInput.left() ? 1 : mc.player.input.playerInput.right() ? -1 : 0);

        Matrix4f matrix = new Matrix4f();
        matrix.rotate((float) Math.toRadians(mc.player.getYaw() - RotateUtils.getCameraYaw()), 0, 1, 0);
        Vec3d updatedInput = MathUtils.transformPos(matrix, sideways, 0, forward);

        forward = (float) (travelChanger.strongMoveFix().get() ? updatedInput.getZ() : Math.round(updatedInput.getZ())) * (sneaking ? (float) mc.player.getAttributeValue(EntityAttributes.SNEAKING_SPEED) : 1);
        sideways = (float) (travelChanger.strongMoveFix().get() ? updatedInput.getX() : Math.round(updatedInput.getX())) * (sneaking ? (float) mc.player.getAttributeValue(EntityAttributes.SNEAKING_SPEED) : 1);

        InputUtils.setForward(forward > 0.0f);
        InputUtils.setBackward(forward < 0.0f);
        InputUtils.setLeft(sideways > 0.0f);
        InputUtils.setRight(sideways < 0.0f);

        mc.player.input.movementForward = forward;
        mc.player.input.movementSideways = sideways;
    }
}
