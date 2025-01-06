package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.Camera.PositionCameraEvent;
import com.ferra13671.BThack.api.Events.Camera.RotateCameraEvent;
import com.ferra13671.BThack.api.Events.Player.ChangePlayerLookEvent;
import com.ferra13671.BThack.api.Events.SetOpaqueCubeEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.option.GameOptions;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class FreeCam extends Module {

    public static NumberSetting speed;

    public FreeCam() {
        super("FreeCam",
                "lang.module.FreeCam",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );

        speed = new NumberSetting("Speed", this, 10, 1, 100, false);

        initSettings(
                speed
        );
    }

    private final FreeCamData freeCamData = new FreeCamData();


    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        ModuleList.deathCamera.setToggled(false);

        super.onEnable();

        freeCamData.reset();
        mc.player.input = new FreecamKeyboardInput(mc.options, freeCamData);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (nullCheck()) return;
        mc.player.input = new KeyboardInput(mc.options);
    }

    @EventSubscriber
    public void onCameraPosition(PositionCameraEvent e) {
        e.setPosition(freeCamData.lastPosition.lerp(freeCamData.position, e.getTickDelta()));
    }

    @EventSubscriber
    public void onCameraRotate(RotateCameraEvent e) {
        e.setRotation(new Vec2f(freeCamData.yaw, freeCamData.pitch));
    }

    @EventSubscriber
    public void onMouseUpdate(ChangePlayerLookEvent e) {
        e.cancel();
        freeCamData.changeLookDirection(e.cursorDeltaX, e.cursorDeltaY);
    }

    @EventSubscriber
    public void onSetOpaqueCube(SetOpaqueCubeEvent e) {
        e.setCancelled(true);
    }

    public static class FreecamKeyboardInput extends Input {

        private final GameOptions options;
        private final FreeCamData freeCamData;

        public FreecamKeyboardInput(GameOptions options, FreeCamData freeCamData) {
            this.options = options;
            this.freeCamData = freeCamData;
        }

        @Override
        public void tick(boolean slowDown, float slowDownFactor) {
            unset();
            float _speed = (float) speed.getValue() / 10f;
            float fakeMovementForward = getMovementMultiplier(options.forwardKey.isPressed(), options.backKey.isPressed());
            float fakeMovementSideways = getMovementMultiplier(options.leftKey.isPressed(), options.rightKey.isPressed());
            Vec2f dir = handleVanillaMotion(_speed, fakeMovementForward, fakeMovementSideways);

            float y = 0;
            if (options.jumpKey.isPressed()) {
                y += _speed;
            } else if (options.sneakKey.isPressed()) {
                y -= _speed;
            }

            freeCamData.lastPosition = freeCamData.position;
            freeCamData.position = freeCamData.position.add(dir.x, y, dir.y);
        }

        private void unset() {
            this.pressingForward = false;
            this.pressingBack = false;
            this.pressingLeft = false;
            this.pressingRight = false;
            this.movementForward = 0;
            this.movementSideways = 0;
            this.jumping = false;
            this.sneaking = false;
        }

        private float getMovementMultiplier(boolean positive, boolean negative) {
            if (positive == negative) {
                return 0.0F;
            } else {
                return positive ? 1.0F : -1.0F;
            }
        }

        private Vec2f handleVanillaMotion(final float speed, float forward, float strafe) {
            if (forward == 0.0f && strafe == 0.0f) {
                return Vec2f.ZERO;
            } else if (forward != 0.0f && strafe != 0.0f) {
                forward *= (float) Math.sin(0.7853981633974483);
                strafe *= (float) Math.cos(0.7853981633974483);
            }
            return new Vec2f((float) (forward * speed * -Math.sin(Math.toRadians(freeCamData.yaw)) + strafe * speed * Math.cos(Math.toRadians(freeCamData.yaw))),
                    (float) (forward * speed * Math.cos(Math.toRadians(freeCamData.yaw)) - strafe * speed * -Math.sin(Math.toRadians(freeCamData.yaw))));
        }
    }

    public static class FreeCamData implements Mc {
        public Vec3d position, lastPosition;

        public float yaw, pitch;

        public void reset() {
            position = mc.gameRenderer.getCamera().getPos();
            lastPosition = position;

            yaw = mc.player.getYaw();
            pitch = mc.player.getPitch();
        }

        public void changeLookDirection(double cursorDeltaX, double cursorDeltaY) {
            float f = (float)cursorDeltaY * 0.15F;
            float g = (float)cursorDeltaX * 0.15F;
            this.pitch += f;
            this.yaw += g;
            this.pitch = MathHelper.clamp(pitch, -90.0F, 90.0F);
        }
    }
}
