package com.ferra13671.BThack.api.Managers.managers.TravelChange;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.Camera.RotateCameraEvent;
import com.ferra13671.BThack.api.Events.Player.ChangePlayerLookEvent;
import com.ferra13671.BThack.api.Events.Player.PlayerTravelEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.Initializable;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Rotate.RotateUtils;
import com.ferra13671.BThack.impl.Modules.PLAYER.FreeCam;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.math.Vec2f;

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
        changers.sort(Comparator.comparing(changer -> changer.priority));
        Collections.reverse(changers);
    }

    public float getLastYaw() {
        return lastYaw;
    }

    public float getLastPitch() {
        return lastPitch;
    }

    @EventSubscriber
    public void onCameraRotate(RotateCameraEvent e) {
        if (!changers.isEmpty() && !ModuleList.freeCam.isEnabled()) {
            e.setRotation(new Vec2f(freeCamData.yaw, freeCamData.pitch));
        }
    }

    @EventSubscriber
    public void onChangePlayer(ChangePlayerLookEvent e) {
        if (!changers.isEmpty() && !ModuleList.freeCam.isEnabled()) {
            e.cancel();
            freeCamData.changeLookDirection(e.cursorDeltaX, e.cursorDeltaY);
        }
    }

    @EventSubscriber
    public void onPlayerTravel(PlayerTravelEvent e) {
        if (!changers.isEmpty()) {
            Float[] rots = changers.getFirst().rotateGetter.get();
            lastYaw = rots[0];
            lastPitch = rots[1];
            mc.player.setYaw(lastYaw);
            mc.player.setPitch(lastPitch);
        }
    }
}
