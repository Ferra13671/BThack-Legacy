package com.ferra13671.BThack.impl.Modules.Movement;

import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.events.Entity.SetVelocityEvent;
import com.ferra13671.BThack.events.Entity.UpdateInputEvent;
import com.ferra13671.BThack.events.PacketEvent;
import com.ferra13671.BThack.events.Player.PlayerTravelEvent;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.Setting;
import com.ferra13671.BThack.managers.managers.TravelChange.TravelChanger;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.*;
import com.ferra13671.BThack.api.Utils.Modules.StrafeUtils;
import com.ferra13671.BThack.api.Utils.Grim.GrimUtils;
import com.ferra13671.BThack.api.Utils.Rotate.RotateUtils;
import com.ferra13671.BThack.impl.Modules.Player.AutoFirework;
import com.ferra13671.BThack.mixins.accessor.entity.IEntity;
import com.ferra13671.BThack.mixins.accessor.entity.ILivingEntity;
import com.ferra13671.BThack.mixins.accessor.packet.IPlayerMoveC2SPacket;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.player.PlayerPosition;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.common.CommonPingS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;

/*

       @SuppressWarnings({"ConstantConditions", "unused"})    <------  Fuck

 */
@ModuleInfo(name = "ElytraFlight", description = "lang.module.ElytraFlight", category = "MOVEMENT")
public class ElytraFlight extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", this, new ArrayList<>(Arrays.asList("Bounce", "Firework", "Pitch40", "Boost", "Timer", "Auto Glide", "1.12.2 Control")));

    //Bounce Settings          <--------   One of the best free Bounce ElytraFly <3
    public final ModeSetting alwaysPress = new ModeSetting("Always Press", this, Arrays.asList("None", "Sprint", "Shift", "Multi"), () -> mode.getValue().equals("Bounce")).defaultValue("Sprint");
    public final BooleanSetting strafing = new BooleanSetting("Strafing", this, true, () -> mode.getValue().equals("Bounce"));
    public final BooleanSetting groundTakeoffFix = new BooleanSetting("Ground Takeoff Fix", this, true, () -> mode.getValue().equals("Bounce"));
    public final NumberSetting takeoffTime = new NumberSetting("Takeoff Time", this, 100, 50, 500, true, () -> mode.getValue().equals("Bounce"));
    public final BooleanSetting grim = new BooleanSetting("Grim", this, true, () -> mode.getValue().equals("Bounce"));

    public final BooleanSetting autoWalk = new BooleanSetting("Auto Walk", this, true, () -> mode.getValue().equals("Bounce"));
    public final BooleanSetting autoJump = new BooleanSetting("Auto Jump", this, true, () -> mode.getValue().equals("Bounce"));

    public final BooleanSetting abusePitch = new BooleanSetting("Abuse Pitch", this, true, () -> mode.getValue().equals("Bounce"));
    public final NumberSetting pitch = new NumberSetting("Pitch", this, 74.4, 0, 90, false, () -> abusePitch.getValue() && mode.getValue().equals("Bounce"));

    public final BooleanSetting fireworkStart = new BooleanSetting("Firework Start", this, false, () -> mode.getValue().equals("Bounce"));
    public final NumberSetting fireworkPitchSetting = new NumberSetting("Firework Pitch", this, -1.20, -5, 0, false, () -> abusePitch.getValue() && mode.getValue().equals("Bounce"));
    public final NumberSetting ignoreSpeed = new NumberSetting("Ignore SpedM", this, 17, 10, 27, false, () -> fireworkStart.getValue() && mode.getValue().equals("Bounce"));
    /////////

    //Firework Settings
    public final NumberSetting upPitch = new NumberSetting("Up Pitch", this, -40, -90, -5, false, () -> mode.getValue().equals("Firework"));
    public final NumberSetting downPitch = new NumberSetting("Down Pitch", this, 40, 5, 90, false, () -> mode.getValue().equals("Firework"));
    public final BooleanSetting allowStrafing = new BooleanSetting("Allow Strafing", this, true, () -> mode.getValue().equals("Firework"));
    public final NumberSetting fireworkDelay = new NumberSetting("FireWork Delay", this, 0, 0, 2500, true, () -> mode.getValue().equals("Firework"));
    public final BooleanSetting noYMove = new BooleanSetting("No Y Move", this, true, () -> mode.getValue().equals("Firework"));
    public final BooleanSetting ignoreMovement = new BooleanSetting("Ignore Movement", this, true, () -> mode.getValue().equals("Firework"));

    public final ModeSetting moveMode = new ModeSetting("Move Mode", this, new ArrayList<>(Arrays.asList("Instance", "Lerp")), () -> mode.getValue().equals("Firework"));
    public final NumberSetting lerpSpeed = new NumberSetting("Lerp Speed", this, 0.5, 0.05, 0.75, false, () -> mode.getValue().equals("Firework") && moveMode.getValue().equals("Lerp"));
    public final ModeSetting stopMode = new ModeSetting("Stop Mode", this, new ArrayList<>(Arrays.asList("None", "Glide", "Jitter")), () -> mode.getValue().equals("Firework"));
    public final BooleanSetting yAlso = new BooleanSetting("Y Also", this, false, () -> mode.getValue().equals("Firework") && (stopMode.getValue().equals("Glide") || stopMode.getValue().equals("Jitter")));
    public final NumberSetting downSpeed = new NumberSetting("Down Speed", this, 0.05, 0, 0.1, false, () -> mode.getValue().equals("Firework") && yAlso.getValue() && (stopMode.getValue().equals("Glide") || stopMode.getValue().equals("Jitter")));
    public final BooleanSetting stopPackets = new BooleanSetting("Stop Packets", this, false, () -> mode.getValue().equals("Firework") && (stopMode.getValue().equals("Glide") || stopMode.getValue().equals("Jitter")));
    public final BooleanSetting onGround = new BooleanSetting("OnGround", this, false, () -> mode.getValue().equals("Firework") && (stopMode.getValue().equals("Glide") || stopMode.getValue().equals("Jitter")));
    /////////

    //Pitch40 Settings
    public final NumberSetting upHeight = new NumberSetting("Start Height", this, 120, 60, 400, false, () -> mode.getValue().equals("Pitch40"));
    public final NumberSetting downHeight = new NumberSetting("Down Height", this, 80, 0, 360, false, () -> mode.getValue().equals("Pitch40"));
    public final NumberSetting pitchSpeed = new NumberSetting("Pitch Speed", this, 4, 1, 6, false, () -> mode.getValue().equals("Pitch40"));

    public final ModeSetting travelMode = new ModeSetting("Travel Mode", this, Arrays.asList("Rewrite", "Pitch"), () -> mode.getValue().equals("Pitch40"));
    /////////

    //Boost Settings
    public final NumberSetting maxSpeed = new NumberSetting("Max speed", this, 35, 20, 50, false, () -> mode.getValue().equals("Boost"));
    public final NumberSetting boostStrength = new NumberSetting("Boost Strength", this, 0.01, 0.001, 0.07, false, () -> mode.getValue().equals("Boost"));
    public final BooleanSetting yMotionAlso = new BooleanSetting("Y motion also", this, false, () -> mode.getValue().equals("Boost"));
    public final NumberSetting startBoostPitch = new NumberSetting("Boost pitch", this, 45, 80, 10, false, () -> mode.getValue().equals("Boost"));
    /////////

    //Timer Settings
    public final NumberSetting slowDelay = new NumberSetting("Slow Delay", this, 400, 100, 2000, true, () -> mode.getValue().equals("Timer"));

    public final ModeSetting fastMode = new ModeSetting("Fast Mode", this, Arrays.asList("Insta", "Smooth"), () -> mode.getValue().equals("Timer"));
    public final NumberSetting plusSpeed = new NumberSetting("Plus Speed", this, 1, 0.5, 2, false, () -> mode.getValue().equals("Timer") && fastMode.getValue().equals("Smooth"));
    public final NumberSetting fastFactor = new NumberSetting("Fast Factor", this, 2, 1.2, 4, false, () -> mode.getValue().equals("Timer"));
    public final NumberSetting postDelay = new NumberSetting("Post Delay", this, 400, 0, 4000, true, () -> mode.getValue().equals("Timer"));
    /////////

    //Auto Glide Settings
    public final NumberSetting gUpHeight = new NumberSetting("G. Up Height", this, 1700, 400, 10000, false, () -> mode.getValue().equals("Auto Glide"));
    public final NumberSetting gDownHeight = new NumberSetting("G. Down Height", this, 320, 306, 500, false, () -> mode.getValue().equals("Auto Glide"));

    public final NumberSetting gUpPitch = new NumberSetting("Glide Up P.", this, -90, -90, -30, false, () -> mode.getValue().equals("Auto Glide"));
    public final NumberSetting gDownPitch = new NumberSetting("Glide Down P.", this, 20, 0, 65, false, () -> mode.getValue().equals("Auto Glide"));

    public final BooleanSetting grimRocket = new BooleanSetting("Grim Rocket", this, true, () -> mode.getValue().equals("Auto Glide"));
    public final BooleanSetting noElytraBreak = new BooleanSetting("No Elytra Break", this, true, () -> mode.getValue().equals("Auto Glide"));
    /////////

    //1.12.2 Control Settings
    public final ModeSetting page = new ModeSetting("Page", this, Arrays.asList("Generic", "Extra"), () -> mode.getValue().equals("1.12.2 Control"));



    public final BooleanSetting durabilityWarning = new BooleanSetting("Durability Warning", this, true, () -> mode.getValue().equals("1.12.2 Control") && page.getValue().equals("Generic"));
    public final NumberSetting warningsThreshold = new NumberSetting("Warn. Threshold", this, 5, 1, 50, true, () -> mode.getValue().equals("1.12.2 Control") && page.getValue().equals("Generic"));
    public final BooleanSetting autoLanding = new BooleanSetting("Auto Landing", this, false, () -> mode.getValue().equals("1.12.2 Control") && page.getValue().equals("Generic"));

    public final BooleanSetting easyTakeoff = new BooleanSetting("Easy Takeoff", this, true, () -> mode.getValue().equals("1.12.2 Control") && page.getValue().equals("Generic"));
    public final BooleanSetting takeoffTimer = new BooleanSetting("Takeoff Timer", this, true, () -> mode.getValue().equals("1.12.2 Control") && page.getValue().equals("Generic"));
    public final BooleanSetting highPingOptimize = new BooleanSetting("High Ping Optimize", this, false, () -> mode.getValue().equals("1.12.2 Control") && page.getValue().equals("Generic"));
    public final NumberSetting minTakeoffHeight = new NumberSetting("MinTakeoffHeight", this, 0.5f, 0.0f, 1.5f, false, () -> mode.getValue().equals("1.12.2 Control") && page.getValue().equals("Generic"));

    public final NumberSetting startSpeed = new NumberSetting("Start Speed", this, 40, 0, 100, true, () -> mode.getValue().equals("1.12.2 Control") && page.getValue().equals("Generic"));
    public final NumberSetting accelerateTime = new NumberSetting("Accelerate Time", this, 0.0f, 0.0f, 20.0f, false, () -> mode.getValue().equals("1.12.2 Control") && page.getValue().equals("Generic"));

    public final BooleanSetting spoofPitch = new BooleanSetting("Spoof Pitch", this, true, () -> mode.getValue().equals("1.12.2 Control") && page.getValue().equals("Generic"));
    public final BooleanSetting blockInteract = new BooleanSetting("Block Interact", this, false, () -> mode.getValue().equals("1.12.2 Control") && page.getValue().equals("Generic"));
    public final NumberSetting forwardPitch = new NumberSetting("Forward Pitch", this, -30, -90, 90, true, () -> mode.getValue().equals("1.12.2 Control") && page.getValue().equals("Generic"));



    public final NumberSetting baseBoostPitch = new NumberSetting("Base Boost Pitch", this, 20, 0, 90, true, () -> mode.getValue().equals("1.12.2 Control") && page.getValue().equals("Extra"));
    public final BooleanSetting NCPStrict = new BooleanSetting("NCP Strict", this, true, () -> mode.getValue().equals("1.12.2 Control") && page.getValue().equals("Extra"));
    public final BooleanSetting legacyLookBoost = new BooleanSetting("Legacy Look Boost", this, false, () -> mode.getValue().equals("1.12.2 Control") && page.getValue().equals("Extra"));
    public final BooleanSetting autoControlAltitude = new BooleanSetting("Auto Control Altitude", this, false, () -> mode.getValue().equals("1.12.2 Control") && page.getValue().equals("Extra"));
    public final BooleanSetting dynamicDownSpeed = new BooleanSetting("Dynamic Down Speed", this, false, () -> mode.getValue().equals("1.12.2 Control") && page.getValue().equals("Extra"));
    public final NumberSetting speedC = new NumberSetting("Speed C", this, 1.75, 0.0f, 10, false, () -> mode.getValue().equals("1.12.2 Control") && page.getValue().equals("Extra"));
    public final NumberSetting fallSpeedC = new NumberSetting("Fall Speed C", this, 0.0, 0.0f, 0.3f, false, () -> mode.getValue().equals("1.12.2 Control") && page.getValue().equals("Extra"));
    public final NumberSetting downSpeedC = new NumberSetting("Down Speed C", this, 1.0f, 1.0f, 5.0f, false, () -> mode.getValue().equals("1.12.2 Control") && page.getValue().equals("Extra"));
    public final NumberSetting dynamicDownSpeedC = new NumberSetting("Dynamic Down Speed C", this, 2.0f, 1.0f, 5.0f, false, () -> mode.getValue().equals("1.12.2 Control") && page.getValue().equals("Extra"));
    /////////


    //Bounce fields
    public static boolean fireworkUsed = false;
    public Ticker takeoffTicker;

    //Firework fields
    public Ticker fireworkDelayTicker = new Ticker();
    public static boolean fireworkCheck = false;
    public float prevFireworkYaw = Float.MIN_VALUE;
    public float prevFireworkPitch = Float.MIN_VALUE;
    public float fireworkYaw = Float.MIN_VALUE;
    public float fireworkPitch = Float.MIN_VALUE;

    //Pitch40 fields
    public float pitch40pitch = 40;
    public boolean pitchingDown = true;

    //Timer fields
    private TimerModeAction timerAction = TimerModeAction.NONE;
    public final Ticker slowTicker = new Ticker();
    public final Ticker postTicker = new Ticker();
    public boolean usedFirework = false;
    public float fastTickSpeed = 1;

    //Auto Glide fields
    public boolean allowAutoGlide = false;
    public float autoGlidePitch;
    private AutoGlideModeAction autoGlideAction = AutoGlideModeAction.NONE;

    //1.12.2 Control fields
    public double _hoverTarget = -1.0;
    public float _packetYaw = 0.0f;
    public float _packetPitch = 0.0f;
    public boolean _hoverState = false;
    public int _boostingTick = 0;

    public boolean _elytraIsEquipped = false;
    public int _elytraDurability = 0;
    public boolean _outOfDurability = false;
    public boolean _wasInLiquid = false;
    public static boolean _isFlying = false;
    public boolean _isStandingStillH = false;
    public boolean _isStandingStill = false;
    public float _speedPercentage = 0.0f;
    public TravelPacket travelPacket = null;

    public final TravelChanger travelChanger = new TravelChanger(5000,
            () -> switch (mode.getValue()) {
                case "Bounce" -> new Float[]{bounceYawRotate(RotateUtils.getCameraYaw()), bouncePitchRotate(RotateUtils.getCameraPitch())};
                case "Firework" -> new Float[]{fireworkYaw, fireworkPitch};
                case "Pitch40" -> new Float[]{RotateUtils.getCameraYaw(), (travelMode.getValue().equals("Rewrite") ? pitch40pitch : RotateUtils.getCameraPitch())};
                case "Auto Glide" -> new Float[]{RotateUtils.getCameraYaw(), getAutoGlidePitch()};
                default -> new Float[]{RotateUtils.getCameraYaw(), RotateUtils.getCameraPitch()};
            },
            () -> false,
            () -> false
    );

    @Override
    public void onChangeSetting(Setting<?> setting) {
        if (mode.getValue().equals("Bounce"))
            arrayListInfo = mode.getValue() + (abusePitch.getValue() ? (";" + pitch.getValue()) : "");
        else
            arrayListInfo = mode.getValue();

        if (isEnabled()) {
            if (mode.getValue().equals("1.12.2 Control") || (mode.getValue().equals("Pitch40") && travelMode.getValue().equals("Pitch"))) Managers.TRAVEL_CHANGE_MANAGER.removeChanger(travelChanger);
            else Managers.TRAVEL_CHANGE_MANAGER.addChanger(travelChanger);
        }
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        ModuleList.timer.setEnabled(false);
        ModuleList.fastFall.setEnabled(false);
        ModuleList.longJump.setEnabled(false);

        super.onEnable();
        fireworkUsed = false;
        takeoffTicker = new Ticker();
        if (groundTakeoffFix.getValue()) {
            if (mc.player.isOnGround()) {
                takeoffTicker.reset();
            }
        }

        fireworkCheck = false;
        prevFireworkYaw = Float.MIN_VALUE;
        prevFireworkPitch = Float.MIN_VALUE;
        fireworkYaw = Float.MIN_VALUE;
        fireworkPitch = Float.MIN_VALUE;
        fireworkDelayTicker.reset();

        //Pitch40
        pitch40pitch = 40;
        pitchingDown = true;
        if (mode.getValue().equals("Pitch40")) {
            if (mc.player.getY() < upHeight.getValue()) {
                toggle();
                return;
            }
        }

        //Timer
        timerAction = TimerModeAction.NONE;
        slowTicker.reset();
        postTicker.reset();
        fastTickSpeed = 1;

        //Auto Glide
        allowAutoGlide = false;
        autoGlidePitch = 0;
        autoGlideAction = AutoGlideModeAction.NONE;

        //1.12.2 Control
        autoLanding.setValue(false);
        _speedPercentage = startSpeed.getValue().floatValue(); /* For acceleration */
        _hoverTarget = -1.0;
        travelPacket = null;

        if (mode.getValue().equals("1.12.2 Control") || (mode.getValue().equals("Pitch40") && travelMode.getValue().equals("Pitch"))) Managers.TRAVEL_CHANGE_MANAGER.removeChanger(travelChanger);
        else Managers.TRAVEL_CHANGE_MANAGER.addChanger(travelChanger);

        if (mode.getValue().equals("Bounce"))
            arrayListInfo = mode.getValue() + (abusePitch.getValue() ? (";" + pitch.getValue()) : "");
        else
            arrayListInfo = mode.getValue();
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onDisable() {
        super.onDisable();

        if (nullCheck()) return;

        fireworkUsed = false;
        /////////

        // Firework Disable
        fireworkCheck = false;
        fireworkYaw = Float.MIN_VALUE;
        fireworkPitch = Float.MIN_VALUE;
        /////////

        // Timer Disable
        usedFirework = false;
        timerAction = TimerModeAction.NONE;
        Managers.TICK_MANAGER.applyTickModifier(1);
        fastTickSpeed = 1;
        /////////

        //1.12.2 Control Disable
        reset(true);
        /////////

        Managers.TRAVEL_CHANGE_MANAGER.removeChanger(travelChanger);
    }

    @EventSubscriber
    @SuppressWarnings({"ConstantConditions", "unused"})
    public void onSend(PacketEvent.Send e) {
        if (nullCheck()) return;
        if (mc.player.getInventory().getArmorStack(2).getItem() != Items.ELYTRA) return;

        if (mode.getValue().equals("Firework")) {
            fireworkPacket(e);
        }
    }

    @EventSubscriber
    @SuppressWarnings({"ConstantConditions", "unused"})
    public void onReceive(PacketEvent.Receive e) {
        switch (mode.getValue()) {
            case "Firework" -> {
                if (!isMoving() && stopMode.getValue().equals("Glide") || stopMode.getValue().equals("Jitter") && stopPackets.getValue()) {
                    if (e.getPacket() instanceof CommonPingS2CPacket)
                        e.setCancelled(true);
                }
            }
            case "1.12.2 Control" -> {
                if (mc.player.isSpectator() || !_elytraIsEquipped || _elytraDurability <= 1 || !_isFlying) return;

                if (e.getPacket() instanceof PlayerPositionLookS2CPacket packet)
                    e.setPacket(new PlayerPositionLookS2CPacket(packet.teleportId(), new PlayerPosition(packet.change().position(), packet.change().deltaMovement(), packet.change().yaw(), 0), packet.relatives()));
            }
        }
    }

    @EventSubscriber
    @SuppressWarnings({"ConstantConditions", "unused"})
    public void onTravel(PlayerTravelEvent e) {
        if (nullCheck()) {
            setEnabled(false);
            return;
        }

        arrayListInfo = mode.getValue();

        if (mc.player.getInventory().getArmorStack(2).getItem() != Items.ELYTRA) return;

        switch (mode.getValue()) {
            case "Bounce" -> bounceMode();
            case "Firework" -> fireworkMode();
            case "Pitch40" -> pitch40Action();
            case "Auto Glide" -> autoGlideAction();
            case "Boost" -> boostAction();
            case "Timer" -> timerMode();
            case "1.12.2 Control" -> {
                if (mc.player.isSpectator()) return;
                stateUpdate(e);
                if (_elytraIsEquipped && _elytraDurability > 1) {
                    if (autoLanding.getValue()) {
                        landing(e);
                        return;
                    }
                    if (!_isFlying) {
                        takeoff(e);
                    } else {
                        Managers.TICK_MANAGER.applyTickModifier(1);
                        ((IEntity)mc.player).invokeSetFlag(3, false);
                        controlMode(e);
                    }
                    spoofRotation();
                } else if (!_outOfDurability) {
                    reset(true);
                }
            }
        }
    }

    @EventSubscriber
    @SuppressWarnings({"ConstantConditions", "unused"})
    public void onSetVelocity(SetVelocityEvent e) {
        if (mode.getValue().equals("Firework")) {
            if (!isMoving()) {
                if (stopMode.getValue().equals("Glide")) {
                    e.setVelocity(yAlso.getValue() ? new Vec3d(0, -downSpeed.getValue(), 0) : new Vec3d(0, e.getVelocity().y, 0));
                }
                if (stopMode.getValue().equals("Jitter")) {
                    e.setVelocity(yAlso.getValue() ? new Vec3d(0, mc.player.age % 2 == 0 ? downSpeed.getValue() : -downSpeed.getValue(), 0) : new Vec3d(0, e.getVelocity().y, 0));
                }
            } else {
                if (noYMove.getValue()) {
                    if (!mc.options.jumpKey.isPressed() && !mc.options.sneakKey.isPressed()) {
                        Vec3d vec3d = e.getVelocity();
                        vec3d.y = -0.0001;   // No Y Motion lol
                        e.setVelocity(vec3d);
                    }
                }
            }
        }
    }

    @EventSubscriber
    @SuppressWarnings({"ConstantConditions", "unused"})
    public void onInputUpdate(UpdateInputEvent e) {
        if (mode.getValue().equals("Bounce")) {
            bounceInput();
        }
    }

    //---------Bounce Mode---------//
    @SuppressWarnings("ConstantConditions")
    public void bounceMode() {
        ((ILivingEntity) mc.player).setJumpingCooldown(0);

        if (fireworkStart.getValue()) {
            if (!fireworkUsed) {
                if (PlayerUtils.getEntitySpeed(mc.player) < ignoreSpeed.getValue()) {
                    AutoFirework.useFirework(false);
                }
                fireworkUsed = true;
            }
        }

        if (alwaysPress.getValue().equals("Sprint") || alwaysPress.getValue().equals("Multi"))
            mc.player.setSprinting(true);
        if (alwaysPress.getValue().equals("Shift") || alwaysPress.getValue().equals("Multi"))
            mc.player.setSneaking(true);


        if (!mc.player.isGliding()) {
            mc.player.startGliding();
            Managers.NETWORK_MANAGER.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
        }

        //Ground takeoff fix
        if (takeoffTicker != null) {
            if (takeoffTicker.getTime() != -1) {
                if (!takeoffTicker.passed(takeoffTime.getValue())) {
                    Managers.TICK_MANAGER.applyTickModifier(5);
                } else {
                    Managers.TICK_MANAGER.applyTickModifier(1);
                    takeoffTicker = null;
                }
            }
        }
        sendBounceGrimPacket();
    }

    @SuppressWarnings("ConstantConditions")
    public void bounceInput() {
        if (autoWalk.getValue()) {
            InputUtils.setForward(true);
            mc.player.input.movementForward = 1;
        }
        if (autoJump.getValue())
            InputUtils.setJumping(true);

        if (alwaysPress.getValue().equals("Sprint") || alwaysPress.getValue().equals("Multi"))
            InputUtils.setSprinting(true);
        if (alwaysPress.getValue().equals("Shift") || alwaysPress.getValue().equals("Multi"))
            InputUtils.setSneaking(true);
    }

    public float bouncePitchRotate(float standardValue) {
        if (abusePitch.getValue()) {
            float pitch = Managers.FIREWORK_MANAGER.isUsingFireWork() ? fireworkPitchSetting.getValue().floatValue() : ModuleList.elytraFlight.pitch.getValue().floatValue();
            if (strafing.getValue()) {
                if (KeyboardUtils.isKeyDown(mc.options.jumpKey.getDefaultKey().getCode())) pitch = Math.max(pitch - 25, -90);
                if (KeyboardUtils.isKeyDown(mc.options.sneakKey.getDefaultKey().getCode())) pitch = Math.min(pitch + 25, 90);
            }
            return pitch;
        }
        return standardValue;
    }

    public float bounceYawRotate(float standardValue) {
        if (strafing.getValue()) {
            if (!mc.player.verticalCollision)
                return StrafeUtils.getPlayerYawOnKeybindings();
        }
        return mc.player.isOnGround() ? Managers.TRAVEL_CHANGE_MANAGER.getLastYaw() : standardValue;
    }

    public void sendBounceGrimPacket() {
        if (grim.getValue())
            GrimUtils.sendPreActionGrimPackets(Managers.TRAVEL_CHANGE_MANAGER.getLastYaw(), Managers.TRAVEL_CHANGE_MANAGER.getLastPitch());
    }
    //-----------------------------//


    //---------Firework Mode---------//
    @SuppressWarnings("ConstantConditions")
    public void fireworkMode() {
        updateRotate();

        IEntity player = (IEntity) mc.player;

        if (!player.invokeGetFlag(7)) {
            player.invokeSetFlag(7, true);
            Managers.NETWORK_MANAGER.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
        }

        if (isMoving() || ignoreMovement.getValue()) {
            if (!Managers.FIREWORK_MANAGER.isUsingFireWork() && !fireworkCheck) {
                if (fireworkDelayTicker.passed(fireworkDelay.getValue())) {
                    AutoFirework.useFirework(false);
                    fireworkCheck = true;
                    fireworkDelayTicker.reset();
                }
            } else {
                fireworkDelayTicker.reset();
            }
        }
        if (Managers.FIREWORK_MANAGER.isUsingFireWork())
            fireworkCheck = false;
    }

    @SuppressWarnings("ConstantConditions")
    public void updateRotate() {
        switch (moveMode.getValue()) {
            case "Instance" -> {
                fireworkYaw = calcYaw();
                fireworkPitch = calcPitch();
            }
            case "Lerp" -> {
                if (fireworkYaw == Float.MIN_VALUE) fireworkYaw = mc.player.yaw;
                if (fireworkPitch == Float.MIN_VALUE) fireworkPitch = mc.player.pitch;
                fireworkYaw = MathHelper.lerp(lerpSpeed.getValue().floatValue(), fireworkYaw, calcYaw());
                fireworkPitch = MathHelper.lerp(lerpSpeed.getValue().floatValue(), fireworkPitch, calcPitch());
            }
        }
        if (fireworkYaw != prevFireworkYaw || fireworkPitch != prevFireworkPitch) sendStrafePacket();
        prevFireworkYaw = fireworkYaw;
        prevFireworkPitch = fireworkPitch;
    }

    public void sendStrafePacket() {
        GrimUtils.sendPreActionGrimPackets(fireworkYaw, fireworkPitch);
    }

    /*
    public Float[] getFireworkModeRots() {
        float yaw;
        float pitch;
        if (stopMode.getValue().equals("Glide") || stopMode.getValue().equals("Jitter")) {
            yaw = isMoving() ? fireworkYaw : mc.player.age % 2 == 0 ? fireworkYaw : -fireworkYaw;
        } else {
            yaw = fireworkYaw;
        }
        pitch = fireworkPitch;

        return new Float[]{yaw, pitch};
    }

     */

    @SuppressWarnings("ConstantConditions")
    public void fireworkPacket(PacketEvent.Send e) {
        if (e.getPacket() instanceof PlayerMoveC2SPacket packet) {
            if (stopMode.getValue().equals("Glide") || stopMode.getValue().equals("Jitter")) {
                if (onGround.getValue() && !isMoving()) {
                    ((IPlayerMoveC2SPacket) packet).setOnGround(true);
                }
            }
        }
    }

    public boolean isMoving() {
        if (allowStrafing.getValue()) {
            return mc.options.forwardKey.isPressed() || mc.options.backKey.isPressed() || mc.options.leftKey.isPressed() || mc.options.rightKey.isPressed();
        } else {
            return mc.options.forwardKey.isPressed();
        }
    }

    @SuppressWarnings("ConstantConditions")
    private float calcYaw() {
        float yaw = mc.player.yaw;
        if (allowStrafing.getValue()) {
            yaw = StrafeUtils.getPlayerYawOnKeybindings();
        }
        return yaw;
    }

    private float calcPitch() {
        float pitch = -0.8f;
        if (mc.options.sneakKey.isPressed()) {
            pitch += downPitch.getValue().floatValue();
        }
        if (mc.options.jumpKey.isPressed()) {
            pitch += upPitch.getValue().floatValue();
        }
        return pitch;
    }
    //-------------------------------//

    //---------Pitch40 Mode---------//
    @SuppressWarnings("ConstantConditions")
    public void pitch40Action() {
        if (pitchingDown && mc.player.getY() <= downHeight.getValue()) {
            pitchingDown = false;
        }
        else if (!pitchingDown && mc.player.getY() >= upHeight.getValue()) {
            pitchingDown = true;
        }

        if (!pitchingDown && pitch40pitch > -40) {
            pitch40pitch -= pitchSpeed.getValue().floatValue();

            if (pitch40pitch < -40) pitch40pitch = -40;
        } else if (pitchingDown && pitch40pitch < 40) {
            pitch40pitch += pitchSpeed.getValue().floatValue();

            if (pitch40pitch > 40) pitch40pitch = 40;
        }

        if (travelMode.getValue().equals("Rewrite"))
            GrimUtils.sendPreActionGrimPackets(RotateUtils.getCameraYaw(), pitch40pitch);
        else
            mc.player.setPitch(pitch40pitch);
    }
    //------------------------------//

    //---------Boost Mode---------//
    @SuppressWarnings("ConstantConditions")
    public void boostAction() {
        if (PlayerUtils.getEntitySpeed(mc.player) < maxSpeed.getValue() && mc.player.isGliding()) {
            if (mc.player.pitch > startBoostPitch.getValue()) {
                double boost = 1 + boostStrength.getValue();
                mc.player.velocity.x *= boost;
                mc.player.velocity.z *= boost;
                if (yMotionAlso.getValue())
                    mc.player.velocity.y *= boost;
            }
        }
    }
    //----------------------------//

    //---------Timer Mode---------//
    @SuppressWarnings("ConstantConditions")
    public void timerMode() {
        if (!mc.player.isGliding()) {
            return;
        }
        if (ModuleList.noElytraBreak.isEnabled()) {
            ModuleList.noElytraBreak.setEnabled(false);
        }

        switch (timerAction) {
            case SLOW_TIMER:
                if (!postTicker.passed(postDelay.getValue())) {
                    Managers.TICK_MANAGER.applyTickModifier(1);
                    return;
                }
            case NONE:
                if (!usedFirework) {
                    AutoFirework.useFirework(false);
                    usedFirework = true;
                }
                if (!slowTicker.passed(slowDelay.getValue())) {
                    Managers.TICK_MANAGER.applyTickModifier((50f / 0.1f) / 50);
                } else {
                    slowTicker.reset();
                    timerAction = TimerModeAction.FAST_TIMER;
                    Managers.FIREWORK_MANAGER.updateFireWorkTick();
                }
                fastTickSpeed = 0.1f;
                break;
            case FAST_TIMER:
                Managers.TICK_MANAGER.applyTickModifier((50f / getFastTickSpeed()) / 50);
                if (!Managers.FIREWORK_MANAGER.isUsingFireWork()) {
                    usedFirework = false;
                    timerAction = TimerModeAction.SLOW_TIMER;
                    slowTicker.reset();
                    postTicker.reset();
                }
                break;
        }
    }

    public float getFastTickSpeed() {
        float value = fastFactor.getValue().floatValue();

        if (fastMode.getValue().equals("Smooth")) {
            value = fastTickSpeed;
            if (fastTickSpeed > fastFactor.getValue().floatValue()) return value;
            fastTickSpeed += 0.05f * plusSpeed.getValue().floatValue();
            if (fastTickSpeed > fastFactor.getValue().floatValue()) {
                fastTickSpeed = fastFactor.getValue().floatValue();
            }
        }

        return value;
    }
    //----------------------------//

    //---------Auto Glide---------//
    @SuppressWarnings("ConstantConditions")
    public void autoGlideAction() {
        if (mc.player.isGliding()) {
            allowAutoGlide = true;
        } else {
            if (mc.player.getInventory().getArmorStack(2).getItem() == Items.ELYTRA) {
                if (mc.player.verticalCollision)
                    allowAutoGlide = false;
            } else {
                allowAutoGlide = false;
            }
        }

        if (!allowAutoGlide || !mc.player.isGliding()) return;

        if (mc.player.getY() <= gUpHeight.getValue()) {
            if (autoGlideAction != AutoGlideModeAction.UP) {
                if (mc.player.getY() >= gDownHeight.getValue()) {
                    autoGlideAction = AutoGlideModeAction.DOWN;
                } else {
                    autoGlideAction = AutoGlideModeAction.UP;
                }
            }
        } else {
            autoGlideAction = AutoGlideModeAction.DOWN;
        }

        switch (autoGlideAction) {
            case UP -> autoGlideUpAction();
            case DOWN -> autoGlideDownAction();
        }
    }

    public void autoGlideUpAction() {
        if (!Managers.FIREWORK_MANAGER.isUsingFireWork()) {
            AutoFirework.useFirework(false);
        }
        autoGlidePitch = gUpPitch.getValue().floatValue();
        if (ModuleList.noElytraBreak.isEnabled())
            ModuleList.noElytraBreak.setEnabled(false);
        if (grimRocket.getValue()) {
            if (!ModuleList.extendedFirework.isEnabled()) {
                ModuleList.extendedFirework.setEnabled(true);
            }
        }
    }

    public void autoGlideDownAction() {
        autoGlidePitch = Managers.FIREWORK_MANAGER.isUsingFireWork() ? -0.8f : gDownPitch.getValue().floatValue();
        if (noElytraBreak.getValue()) {
            if (!ModuleList.noElytraBreak.isEnabled())
                ModuleList.noElytraBreak.setEnabled(true);
        }
    }

    public float getAutoGlidePitch() {
        return autoGlidePitch;
    }
    //----------------------------//


    //---------1.12.2 Control Mode---------//
    @SuppressWarnings("ConstantConditions")
    private void controlMode(PlayerTravelEvent event) {
        /* States and movement input */
        double currentSpeed = Math.sqrt(mc.player.velocity.x * mc.player.velocity.x + mc.player.velocity.z * mc.player.velocity.z);
        boolean moveUp;
        if (!legacyLookBoost.getValue()) {
            moveUp = mc.options.jumpKey.isPressed();
        } else {
            moveUp = mc.player.pitch < -10.0f && !_isStandingStillH;
        }

        boolean moveDown;
        /*
        if (Client.getModuleByName("GuiMove").isEnabled() && mc.currentScreen != null || moveUp) {
            moveDown = false;
        } else {
            moveDown = mc.options.sneakKey.isPressed();
        }

         */

        moveDown = mc.options.sneakKey.isPressed();

        /* Dynamic down speed */
        double calcDownSpeed;
        if (dynamicDownSpeed.getValue()) {
            double minDownSpeed = Math.min(downSpeedC.getValue(), dynamicDownSpeedC.getValue());
            double maxDownSpeed = Math.max(downSpeedC.getValue(), dynamicDownSpeedC.getValue());
            if (mc.player.pitch > 0) {
                calcDownSpeed = mc.player.pitch / 90.0 * (maxDownSpeed - minDownSpeed) + minDownSpeed;
            } else calcDownSpeed = minDownSpeed;
        } else
            calcDownSpeed = downSpeedC.getValue();

        /* Hover */
        if (_hoverTarget < 0.0 || moveUp) {
            _hoverTarget = mc.player.getY();
        } else if (moveDown) {
            _hoverTarget = mc.player.getY() - calcDownSpeed;
        }
        _hoverState = _hoverState ? mc.player.getY() < _hoverTarget : mc.player.getX() < _hoverTarget - 0.1
                && autoControlAltitude.getValue();

        /* Set velocity */
        if (!_isStandingStillH || moveUp) {
            if ((moveUp || _hoverState) && (currentSpeed >= 0.8 || mc.player.velocity.y > 1.0)) {
                upwardFlight(currentSpeed, getYaw());
            } else { /* Runs when pressing wasd */
                _packetPitch = forwardPitch.getValue().floatValue();
                mc.player.velocity.y = fallSpeedC.getValue();
                setSpeed(getYaw(), moveUp);
                _boostingTick = 0;
            }
        } else {
            mc.player.setVelocity(0.0, 0.0, 0.0); /* Stop moving if no inputs are pressed */
        }

        if (moveDown) mc.player.velocity.y = -calcDownSpeed; /* Runs when holding shift */

        event.setCancelled(true);
    }

    @SuppressWarnings("ConstantConditions")
    private void upwardFlight(double currentSpeed, double yaw) {
        double multipliedSpeed = 0.128 * Math.min(speedC.getValue(), 2.0f);
        float strictPitch = (float) Math.toDegrees(Math.asin((multipliedSpeed - Math.sqrt(multipliedSpeed * multipliedSpeed - 0.0348)) / 0.12));
        float basePitch = NCPStrict.getValue() && strictPitch < baseBoostPitch.getValue() && !Float.isNaN(strictPitch) ? -strictPitch
                : (float) -baseBoostPitch.getValue();
        float targetPitch = mc.player.pitch < 0.0f ?
                Math.max(mc.player.pitch * (90.0f - baseBoostPitch.getValue().floatValue()) / 90.0f - baseBoostPitch.getValue().floatValue(), -90.0f) :
                (float) -baseBoostPitch.getValue();

        if (_packetPitch <= basePitch && _boostingTick > 2) {
            if (_packetPitch < targetPitch) _packetPitch += 17.0f;
            if (_packetPitch > targetPitch) _packetPitch -= 17.0f;
            _packetPitch = Math.max(_packetPitch, targetPitch);
        } else {
            _packetPitch = basePitch;
        }
        _boostingTick++;

        /* These are actually the original Minecraft elytra fly code lol */
        double pitch = Math.toRadians(_packetPitch);
        double targetMotionX = Math.sin(-yaw) * Math.sin(-pitch);
        double targetMotionZ = Math.cos(yaw) * Math.sin(-pitch);
        double targetSpeed = Math.sqrt(targetMotionX * targetMotionX + targetMotionZ * targetMotionZ);
        double upSpeed = currentSpeed * Math.sin(-pitch) * 0.04;
        double fallSpeed = Math.cos(pitch) * Math.cos(pitch) * 0.06 - 0.08;

        mc.player.velocity.x -= upSpeed * targetMotionX / targetSpeed - (targetMotionX / targetSpeed * currentSpeed - mc.player.velocity.x) * 0.1;
        mc.player.velocity.y += upSpeed * 3.2 + fallSpeed;
        mc.player.velocity.z -= upSpeed * targetMotionZ / targetSpeed - (targetMotionZ / targetSpeed * currentSpeed - mc.player.velocity.z) * 0.1;

        /* Passive motion loss */
        mc.player.velocity.x *= 0.99;
        mc.player.velocity.y *= 0.98;
        mc.player.velocity.z *= 0.99;
    }

    @SuppressWarnings("ConstantConditions")
    private void reset(boolean cancelFlying) {
        _wasInLiquid = false;
        _isFlying = false;
        //isPacketFlying = false;
        Managers.TICK_MANAGER.applyTickModifierWithFactor(1);
        mc.player.getAbilities().setFlySpeed(0.05f);
        if (cancelFlying)
            mc.player.getAbilities().flying = false;

    }

    /**
     * Calculate a speed with a non linear acceleration over time
     *
     * @return boostingSpeed if boosting is true, else return an accelerated speed.
     */
    private double getSpeed(boolean boosting) {
        if (boosting) {
            return NCPStrict.getValue() ? Math.min(speedC.getValue(), 2.0f) : speedC.getValue();
        } else if (accelerateTime.getValue() != 0.0 && startSpeed.getValue() != 100.0) {
            _speedPercentage = Math.min(_speedPercentage + (100.0f - startSpeed.getValue().floatValue()) / (accelerateTime.getValue().floatValue() * 20.0f), 100.0f);
            double speedMultiplier = _speedPercentage / 100.0;

            return getSettingSpeed() * speedMultiplier * (Math.cos(speedMultiplier * Math.PI) * -0.5 + 0.5);
        } else {
            return getSettingSpeed();
        }
    }

    private double getSettingSpeed() {
        return speedC.getValue();
    }

    @SuppressWarnings("ConstantConditions")
    private void setSpeed(double yaw, boolean boosting) {
        double acceleratedSpeed = getSpeed(boosting);
        mc.player.setVelocity(Math.sin(-yaw) * acceleratedSpeed, mc.player.velocity.y, Math.cos(yaw) * acceleratedSpeed);
    }

    /**
     *  Calculate yaw for control and packet mode
     *
     *  @return Yaw in radians based on player rotation yaw and movement input
     */
    private double getYaw() {
        double yawRad = PlayerUtils.calcMoveYaw();
        _packetYaw = (float) Math.toDegrees(yawRad);
        return yawRad;
    }

    @SuppressWarnings("ConstantConditions")
    private void takeoff(PlayerTravelEvent event) {
        /* Pause Takeoff if server is lagging, player is in water/lava, or player is on ground */
        float timerSpeed = highPingOptimize.getValue() ? 400.0f : 200.0f;
        float height = highPingOptimize.getValue() ? 0.0f : minTakeoffHeight.getValue().floatValue();
        boolean closeToGround = mc.player.getY() <= PlayerUtils.getGroundPos(mc.world, mc.player).y + height && !_wasInLiquid && !mc.isInSingleplayer();

        if (!easyTakeoff.getValue() || mc.player.onGround) {
            reset(mc.player.onGround);
            return;
        }

        if (mc.player.velocity.y < 0 && !highPingOptimize.getValue() || mc.player.velocity.y < -0.02) {
            if (closeToGround) {
                Managers.TICK_MANAGER.applyTickModifierWithFactor(0.5);
                return;
            }

            if (!highPingOptimize.getValue() && !_wasInLiquid && !mc.isInSingleplayer()) { /* Cringe moment when you use elytra flight in single player world */
                event.setCancelled(true);
                mc.player.setVelocity(0.0, -0.02, 0.0);
            }

            if (takeoffTimer.getValue() && !mc.isInSingleplayer()) Managers.TICK_MANAGER.applyTickModifier((timerSpeed * 2.0f) / 50);
            Managers.NETWORK_MANAGER.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
            _hoverTarget = mc.player.getY() + 0.2;
        } else if (highPingOptimize.getValue() && !closeToGround) {
            Managers.TICK_MANAGER.applyTickModifier(timerSpeed / 50);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void landing(PlayerTravelEvent event) {
        if (mc.player.onGround) {
            sendNotification("Landed!");
            autoLanding.setValue(false);
            return;
        } else if (mc.player.getAbilities().flying || !mc.player.isGliding()) { //|| isPacketFlying) {
            reset(true);
            takeoff(event);
            return;
        } else {
            double groundY = PlayerUtils.getGroundPos(mc.world, mc.player).y;
            if (mc.player.getY() > groundY + 1.0) {
                Managers.TICK_MANAGER.applyTickModifier(1);
                mc.player.velocity.y = Math.max(Math.min(-(mc.player.getY() - groundY) / 20.0, -0.5), -5.0);
            } else if (mc.player.velocity.y != 0.0) {
                /* Pause falling to reset fall distance */
                if (!mc.isInSingleplayer()) Managers.TICK_MANAGER.applyTickModifier(200.0f / 50); /* Use timer to pause longer */
                mc.player.velocity.y = 0.0;
            } else {
                mc.player.velocity.y = -0.2;
            }
        }
        mc.player.setVelocity(0.0, mc.player.velocity.y, 0.0); /* Kills horizontal motion */
        event.setCancelled(true);
    }

    /* Holds player in the air */
    @SuppressWarnings("ConstantConditions")
    private void holdPlayer(PlayerTravelEvent event) {
        event.setCancelled(true);
        Managers.TICK_MANAGER.applyTickModifier(1);
        mc.player.setVelocity(0.0, -0.01, 0.0);
    }

    @SuppressWarnings("ConstantConditions")
    private void stateUpdate(PlayerTravelEvent event) {
        /* Elytra Check */
        ItemStack armorSlot = mc.player.getInventory().armor.get(2);
        _elytraIsEquipped = armorSlot.getItem() == Items.ELYTRA;

        /* Elytra Durability Check */
        if (_elytraIsEquipped) {
            int oldDurability = _elytraDurability;
            _elytraDurability = armorSlot.getMaxDamage() - armorSlot.getDamage();

            /* Elytra Durability Warning, runs when player is in the air and durability changed */
            if (!mc.player.onGround && oldDurability != _elytraDurability) {
                if (durabilityWarning.getValue() && _elytraDurability > 1 && _elytraDurability < warningsThreshold.getValue() * armorSlot.getMaxDamage() / 100) {
                    mc.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                    sendNotification("Warning: Elytra has " + (_elytraDurability - 1) + " durability remaining");
                } else if (_elytraDurability <= 1 && !_outOfDurability) {
                    _outOfDurability = true;
                    if (durabilityWarning.getValue()) {
                        mc.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                        sendNotification("Elytra is out of durability, holding player in the air");
                    }
                }
            }
        } else {
            _elytraDurability = 0;
        }

        /* Holds player in the air if run out of durability */
        if (!mc.player.onGround && _elytraDurability <= 1 && _outOfDurability) {
            holdPlayer(event);
        } else if (_outOfDurability) {
            _outOfDurability = false; /* Reset if player is on the ground or replace with a new elytra */
        }

        /* wasInLiquid check */
        if (mc.player.isTouchingWater() || mc.player.isInLava()) {
            _wasInLiquid = true;
        } else if (mc.player.onGround || _isFlying) {
            _wasInLiquid = false;
        }

        /* Elytra flying status check */
        _isFlying = mc.player.isGliding();

        /* Movement input check */
        _isStandingStillH = !mc.options.forwardKey.isPressed() && !mc.options.backKey.isPressed() && !mc.options.leftKey.isPressed() && !mc.options.rightKey.isPressed();
        _isStandingStill = _isStandingStillH && !mc.options.jumpKey.isPressed() && !mc.options.sneakKey.isPressed();

        /* Reset acceleration */
        if (!_isFlying || _isStandingStill) {
            _speedPercentage = startSpeed.getValue().floatValue();
        }

        /* Modify leg swing */
        /*
        if (shouldSwing()) {
            mc.player.prevLimbSwingAmount = mc.player.limbSwingAmount;
            mc.player.limbSwing += (float) getSlider(this.name, "Swing Speed");
            float speedRatio = (mc.player.getAIMoveSpeed() / (float) getSettingSpeed());
            mc.player.limbSwingAmount += ((speedRatio * (float) getSlider(this.name, "Swing Amount")) - mc.player.limbSwingAmount) * 0.4f;
        }

         */
    }

    @SuppressWarnings("ConstantConditions")
    private void spoofRotation() {
        if (mc.player.isSpectator() || !_elytraIsEquipped || _elytraDurability <= 1 || !_isFlying)
            return;

        boolean cancelRotation = false;
        Vec2f rotation = new Vec2f(mc.player.yaw, mc.player.pitch);

        if (autoLanding.getValue()) {
            rotation = new Vec2f(rotation.x, -20f);
        } else  {
            if (!_isStandingStill) {
                rotation = new Vec2f(_packetYaw, rotation.y);
            }
            if (spoofPitch.getValue()) {
                if (!_isStandingStill)
                    rotation = new Vec2f(rotation.x, _packetPitch);

                /* Cancels rotation packets if player is not moving and not clicking */
                cancelRotation = _isStandingStill && ((!mc.options.useKey.isPressed() && !mc.options.attackKey.isPressed() && blockInteract.getValue()) || !blockInteract.getValue());
            }
        }

        TravelPacket packet;

        if (cancelRotation) {
            packet = new TravelPacket(false, null);
        } else {
            packet = new TravelPacket(true, rotation);
        }
        travelPacket = packet;
    }
    //-------------------------------------//

    private enum TimerModeAction {
        NONE,
        SLOW_TIMER,
        //USED_FIREWORK,
        FAST_TIMER
        //TAKEOFF
    }


    private enum AutoGlideModeAction {
        NONE,
        UP,
        DOWN
    }

    public record TravelPacket(boolean rotate, Vec2f rot) {}
}
