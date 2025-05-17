package com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTasks;

import com.ferra13671.BThack.api.Motion.Align.AlignToBlockCenter;
import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Utils.InputUtils;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.core.Client.Systems.FileSystem.JsonUtils;
import com.ferra13671.BThack.api.Events.Entity.UpdateInputEvent;
import com.ferra13671.BThack.api.Managers.managers.Build.BuildManager;
import com.ferra13671.BThack.api.Managers.managers.Destroy.DestroyManager;
import com.ferra13671.BThack.api.Managers.managers.Destroy.SimpleDestroyThread;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Thread.ThreadClosedException;
import com.ferra13671.BThack.api.Managers.managers.TravelChange.TravelChanger;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.Rotate.RotateUtils;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.block.FlowerBlock;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;

public class MoveTask extends ActionBotTask {
    private final Type type;
    private final double needX;
    private final double needZ;
    private final boolean scaffold;


    public MoveTask(double needX, double needZ, boolean scaffold, Type type) {
        super("Move");
        this.mode = "Move";

        this.needX = needX;
        this.needZ = needZ;
        this.scaffold = scaffold;
        this.type = type;

        this.taskDescription = Arrays.asList(
                "When the task is activated, the player starts walking to the specified coordinates.",
                "Coordinates should be entered by the ratio of the player's coordinates.",
                "I.e., if you need to write 5 at x coordinates, if you want the",
                "the player has moved 5 blocks along the x-coordinate."
        );
    }
    public double maxX;
    public double minX;
    public double maxZ;
    public double minZ;
    protected boolean cancel;
    private float yaw = -99999999;



    private boolean moving = false;
    private boolean jumping = false;
    private final TravelChanger travelChanger = new TravelChanger(1000,
            () -> new Float[]{yaw, mc.player.getPitch()},
            () -> false,
            () -> false
    );

    @EventSubscriber
    public void onInput(UpdateInputEvent e) {
        InputUtils.setInput(moving, false, false, false, jumping, mc.player.input.playerInput.sneak(), mc.player.input.playerInput.sprint());
    }

    @Override
    public void play() throws ThreadClosedException {
        alignAction();

        Managers.TRAVEL_CHANGE_MANAGER.addChanger(travelChanger);
        BThack.EVENT_BUS.register(this);

        boolean scaffoldActivated = true;

        try {

            double tempX = mc.player.getX() + needX;
            double tempZ = mc.player.getZ() + needZ;

            if (scaffold) {
                scaffoldActivated = ModuleList.scaffold.isEnabled();
                ModuleList.scaffold.setToggled(true);
            }


            maxX = tempX + 0.15;
            minX = tempX - 0.15;
            maxZ = tempZ + 0.15;
            minZ = tempZ - 0.15;

            while (toFarX() || toFarZ()) {
                thread.checkThreadStopped();
                yaw = RotateUtils.rotations(new Vec3d(tempX, mc.player.getY(), tempZ))[0];
                moving = !DestroyManager.isDestroying || type != Type.Through_Obstacles;

                if (scaffold) {
                    if (!ModuleList.scaffold.isEnabled()) {
                        ModuleList.scaffold.setToggled(true);
                    }
                }

                if (mc.player.horizontalCollision) {
                    switch (type) {
                        case Default:
                            ChatUtils.sendMessage("[ActionBot: MoveTask] " + Formatting.YELLOW + "The player ran into an obstacle. Skipping a task.");
                            disableAction(scaffoldActivated);
                            return;
                        case AutoJump:
                            tryJump();
                            break;
                        case Through_Obstacles:
                            moving = false;
                            if (!DestroyManager.isDestroying) {
                                double x = mc.player.getX() + RotateUtils.getCordFactorFromDirection((int) yaw)[0];
                                double z = mc.player.getZ() + RotateUtils.getCordFactorFromDirection((int) yaw)[1];
                                double y = mc.player.getY() + 0.5;
                                BlockPos blockPos = BlockPos.ofFloored(x, y, z);
                                if (BuildManager.ignoreBlocks.contains(mc.world.getBlockState(blockPos).getBlock()) || mc.world.isAir(blockPos) || mc.world.getBlockState(blockPos).getBlock() instanceof FlowerBlock) {
                                    blockPos = new BlockPos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ());
                                }
                                SimpleDestroyThread destroyThread = new SimpleDestroyThread(blockPos);
                                destroyThread.start();
                            }
                            break;
                    }
                }

                if (!toFarX() && !toFarZ()) {
                    disableAction(scaffoldActivated);
                    return;
                }

                if (cancel) {
                    disableAction(scaffoldActivated);
                    return;
                }

                Thread.yield();
            }
        } finally {
            BThack.EVENT_BUS.unregister(this);
            Managers.TRAVEL_CHANGE_MANAGER.removeChanger(travelChanger);
            alignAction();

            disableAction(scaffoldActivated);
            thread.checkThreadStopped();
        }
    }
    private void disableScaffold(boolean scaffoldEnabled) {
        if (scaffold) {
            if (!scaffoldEnabled) {
                ModuleList.scaffold.setToggled(false);
            }
        }
    }

    private boolean toFarX() {
        return mc.player.getX() > maxX || mc.player.getX() < minX;
    }

    private boolean toFarZ() {
        return mc.player.getZ() > maxZ || mc.player.getZ() < minZ;
    }

    private void tryJump() throws ThreadClosedException {
        double oldPosY = mc.player.getY();
        jumping = true;
        moving = true;

        sleepThread(600);
        thread.checkThreadStopped();

        moving = false;
        jumping = false;
        if (mc.player.getY() < oldPosY + 0.4) {
            jumping = true;
            moving = true;

            sleepThread(600);
            thread.checkThreadStopped();

            jumping = false;
            moving = false;
        }
        if (mc.player.getY() < oldPosY + 0.4) {
            jumping = true;
            moving = true;

            sleepThread(600);
            thread.checkThreadStopped();

            jumping = false;
            moving = false;
        }
        if (mc.player.getY() < oldPosY + 0.4) {
            cancel = true;
        }
    }

    public void alignAction() throws ThreadClosedException {
        thread.checkThreadStopped();
        AlignToBlockCenter alignToBlockCenter = new AlignToBlockCenter();
        alignToBlockCenter.align();
        do {
            if (thread.isThreadClosed()) {
                alignToBlockCenter.getThread().closeThread();
                thread.stopOnException();
            }
            sleepThread(50);
        } while (alignToBlockCenter.isMoving());
    }

    public void disableAction(boolean scaffoldActivated) {
        moving = false;
        cancel = false;
        disableScaffold(scaffoldActivated);
    }





    public double getNeedX() {
        return this.needX;
    }

    public double getNeedZ() {
        return this.needZ;
    }

    public boolean isScaffold() {
        return this.scaffold;
    }

    public Type getType() {
        return this.type;
    }

    @Override
    public String getButtonName() {
        return getName() + ":  X: " + getNeedX() + "  Z: " + getNeedZ() + "  Scaffold: " + isScaffold() + "  Type: " + getType().name();
    }

    @Override
    public void save(JsonObject jsonObject) {
        String type = getType().name();

        jsonObject.add("NeedX", new JsonPrimitive(getNeedX()));
        jsonObject.add("NeedZ", new JsonPrimitive(getNeedZ()));
        jsonObject.add("Scaffold", new JsonPrimitive(isScaffold()));
        jsonObject.add("Type", new JsonPrimitive(type));
    }

    @Override
    public void load(JsonObject jsonObject) {
        if (JsonUtils.equalsNull(jsonObject, "NeedX", "NeedZ", "Scaffold", "Type")) return;

        double needX = jsonObject.get("NeedX").getAsDouble();
        double needZ = jsonObject.get("NeedZ").getAsDouble();
        boolean scaffold = jsonObject.get("Scaffold").getAsBoolean();
        String type = jsonObject.get("Type").getAsString();

        Type moveType = Type.Default;

        for (Type e : Type.values()) {
            if (e.name().equals(type)) {
                moveType = e;
                break;
            }
        }

        ActionBotConfig.tasks.add(new MoveTask(needX, needZ, scaffold, moveType));
    }

    public enum Type {
        Default,
        AutoJump,
        Through_Obstacles
    }
}
