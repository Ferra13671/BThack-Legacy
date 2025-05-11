package com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTasks;

import com.ferra13671.BTbot.api.Utils.Motion.Align.AlignWithXZ;
import com.ferra13671.BTbot.api.Utils.Motion.Align.WhereToAlign;
import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.core.Client.Systems.FileSystem.JsonUtils;
import com.ferra13671.BThack.api.Events.Entity.UpdateInputEvent;
import com.ferra13671.BThack.api.Managers.managers.Build.BuildManager;
import com.ferra13671.BThack.api.Managers.managers.Build.BuildThread3D;
import com.ferra13671.BThack.api.Managers.managers.Destroy.DestroyManager;
import com.ferra13671.BThack.api.Managers.managers.Destroy.SimpleDestroyThread;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Thread.ThreadClosedException;
import com.ferra13671.BThack.api.Managers.managers.TravelChange.TravelChanger;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.Grim.GrimUtils;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.Rotate.RotateUtils;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.block.Block;
import net.minecraft.block.FlowerBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;


public class TunnelTask extends ActionBotTask {


    public TunnelTask(Direction direction, double length) {
        super("Make Tunnel");
        this.mode = "Tunnel";

        this.direction = direction;
        this.length = length;

        this.needX = this.length * this.direction.getX();
        this.needZ = this.length * this.direction.getZ();

        this.taskDescription = Arrays.asList(
                "The more advanced version of MoveTask is equipped with special checks necessary for more safe tunnel digging.",
                "Unlike its ancestor, the direction and final length of the tunnel are used instead of coordinates. "
        );
    }

    private final double length;
    private final Direction direction;

    private final double needX;
    private final double needZ;

    public double maxX;
    public double minX;
    public double maxZ;
    public double minZ;
    protected boolean cancel;
    private float yaw = -99999999;
    private final TravelChanger travelChanger = new TravelChanger(1000,
            () -> new Float[]{yaw, mc.player.getPitch()},
            () -> GrimUtils.sendPreActionGrimPackets(yaw, mc.player.getPitch()),
            () -> true
    );

    private boolean moving = false;

    @EventSubscriber
    public void onInput(UpdateInputEvent e) {
        mc.player.input.movementForward = moving ? 1 : 0;
        mc.player.input.movementSideways = 0;
    }

    @Override
    public void play() throws ThreadClosedException {
        alignAction();

        Managers.TRAVEL_CHANGE_MANAGER.addChanger(travelChanger);
        BThack.EVENT_BUS.register(this);

        boolean scaffoldActivated = ModuleList.scaffold.isEnabled();

        try {

            double tempX = mc.player.getX() + needX;
            double tempZ = mc.player.getZ() + needZ;


            ModuleList.scaffold.setToggled(true);


            maxX = tempX + 0.15;
            minX = tempX - 0.15;
            maxZ = tempZ + 0.15;
            minZ = tempZ - 0.15;


            while (toFarX() || toFarZ()) {
                thread.checkThreadStopped();

                yaw = RotateUtils.rotations(new Vec3d(tempX, mc.player.getY(), tempZ))[0];
                moving = !DestroyManager.isDestroying;

                if (!ModuleList.scaffold.isEnabled()) {
                    ModuleList.scaffold.setQuietlyToggled(true);
                }

                BlockPos blockPos1;
                BlockPos blockPos2;
                double x;
                double y = mc.player.getY() + 0.5;
                double z;

                if (checkLava()) {
                    ChatUtils.sendMessage("Lava Detected!");
                    emergencyTrap();
                    disableAction(scaffoldActivated);
                    return;
                }

                if (!mc.player.horizontalCollision) {
                    x = mc.player.getX() + (RotateUtils.getCordFactorFromDirection((int) yaw)[0]);
                    z = mc.player.getZ() + (RotateUtils.getCordFactorFromDirection((int) yaw)[1]);
                } else {
                    x = mc.player.getX() + (RotateUtils.getCordFactorFromDirection((int) yaw)[0]);
                    z = mc.player.getZ() + (RotateUtils.getCordFactorFromDirection((int) yaw)[1]);
                }

                blockPos1 = BlockPos.ofFloored(x, y, z);
                blockPos2 = new BlockPos(blockPos1.getX(), blockPos1.getY() + 1, blockPos1.getZ());
                Block block1 = mc.world.getBlockState(blockPos1).getBlock();
                Block block2 = mc.world.getBlockState(blockPos2).getBlock();

                SimpleDestroyThread destroyThread;

                if (!mc.world.isAir(blockPos1)) {
                    if (!BuildManager.ignoreBlocks.contains(block1) && !(block1 instanceof FlowerBlock)) {
                        moving = false;
                        if (!DestroyManager.isDestroying) {
                            destroyThread = new SimpleDestroyThread(blockPos1);
                            destroyThread.start();
                        }
                    }
                } else if (!mc.world.isAir(blockPos2)) {
                    if (!BuildManager.ignoreBlocks.contains(block2) && !(block2 instanceof FlowerBlock)) {
                        moving = false;
                        if (!DestroyManager.isDestroying) {
                            destroyThread = new SimpleDestroyThread(blockPos2);
                            destroyThread.start();
                        }
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
            }
        } finally {
            thread.checkThreadStopped();
            BThack.EVENT_BUS.unregister(this);
            Managers.TRAVEL_CHANGE_MANAGER.removeChanger(travelChanger);
            alignAction();

            disableAction(scaffoldActivated);
        }
    }
    private void disableScaffold(boolean scaffoldEnabled) {
        if (!scaffoldEnabled) {
            ModuleList.scaffold.setQuietlyToggled(false);
        }
    }

    private boolean toFarX() {
        return mc.player.getX() > maxX || mc.player.getX() < minX;
    }

    private boolean toFarZ() {
        return mc.player.getZ() > maxZ || mc.player.getZ() < minZ;
    }

    private boolean checkLava() {
        double x = mc.player.getX() + (RotateUtils.getCordFactorFromDirection((int) yaw)[0] * 2);
        double y = mc.player.getY() + 0.5;
        double z = mc.player.getZ() + (RotateUtils.getCordFactorFromDirection((int) yaw)[1] * 2);

        ArrayList<BlockPos> blockPosData = new ArrayList<>(Arrays.asList(
                BlockPos.ofFloored(x, y, z),
                BlockPos.ofFloored(x - 1, y, z),
                BlockPos.ofFloored(x + 1, y, z),
                BlockPos.ofFloored(x, y, z + 1),
                BlockPos.ofFloored(x, y, z - 1)
        ));

        for (BlockPos pos : blockPosData) {
            if (BuildManager.lavas.contains(mc.world.getBlockState(pos).getBlock())) {
                return true;
            } else if (BuildManager.lavas.contains(mc.world.getBlockState(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ())).getBlock())) {
                return true;
            } else if (BuildManager.lavas.contains(mc.world.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ())).getBlock())) {
                return true;
            } else if (BuildManager.lavas.contains(mc.world.getBlockState(new BlockPos(pos.getX(), pos.getY() + 2, pos.getZ())).getBlock())) {
                return true;
            }
        }
        return false;
    }

    private void emergencyTrap() {
        ArrayList<Vec3d> trapSchematic = new ArrayList<>(Arrays.asList(
                new Vec3d(1,1,0),
                new Vec3d(-1,1,0),
                new Vec3d(0,1,1),
                new Vec3d(0,1,-1),
                new Vec3d(1,2,0),
                new Vec3d(-1,2,0),
                new Vec3d(0,2,1),
                new Vec3d(0,2,-1),
                new Vec3d(0,3,0)
        ));

        WhereToAlign whereToAlign = new WhereToAlign();
        AlignWithXZ alignWithXZ = new AlignWithXZ(whereToAlign);

        alignWithXZ.alignWithXZ();

        for (int needSlot = 0; needSlot < 36; needSlot++) {
            Item item = mc.player.getInventory().getStack(needSlot).getItem();
            if (item instanceof BlockItem) {
                if (needSlot < 9) {
                    InventoryUtils.swapItem(needSlot);

                } else {
                    InventoryUtils.swapItemOnInventory(mc.player.getInventory().selectedSlot, needSlot);
                    mc.interactionManager.tick();
                }
                break;
            }
        }

        BlockPos startPos = BlockPos.ofFloored(mc.player.getX(), mc.player.getY() - 0.1, mc.player.getZ());

        BuildThread3D buildThread3D = new BuildThread3D();
        buildThread3D.set3DSchematic(1, trapSchematic, startPos);
        buildThread3D.start();
    }

    public void alignAction() throws ThreadClosedException {
        thread.checkThreadStopped();
        WhereToAlign whereToAlign = new WhereToAlign();
        AlignWithXZ alignWithXZ = new AlignWithXZ(whereToAlign);
        alignWithXZ.alignWithXZ();

        do {
            if (thread.isThreadClosed()) {
                alignWithXZ.getThread().closeThread();
                thread.stopOnException();
            }
            sleepThread(50);
        } while (alignWithXZ.isMoving());
    }

    public void disableAction(boolean scaffoldActivated) {
        moving = false;
        cancel = false;
        disableScaffold(scaffoldActivated);
    }






    public Direction getDirection() {
        return this.direction;
    }

    public double getLength() {
        return this.length;
    }


    public enum Direction {
        X_PLUS(1,0),
        X_MINUS(-1,0),
        Z_PLUS(0,1),
        Z_MINUS(0,-1);


        private final int x;
        private final int z;

        Direction(int x, int z) {
            this.x = x;
            this.z = z;
        }

        public int getX() {
            return this.x;
        }

        public int getZ() {
            return this.z;
        }
    }

    @Override
    public String getButtonName() {
        return getName() + ":  Direction: " + getDirection().name() + "  Length: " + getLength();
    }

    @Override
    public void save(JsonObject jsonObject) {
        String direction = getDirection().name();

        jsonObject.add("Direction", new JsonPrimitive(direction));
        jsonObject.add("Length", new JsonPrimitive(getLength()));
    }

    @Override
    public void load(JsonObject jsonObject) {
        if (JsonUtils.equalsNull(jsonObject, "Direction", "Length")) return;

        String directionString = jsonObject.get("Direction").getAsString();
        double length = jsonObject.get("Length").getAsDouble();

        Direction direction = Direction.X_PLUS;

        for (Direction e : Direction.values()) {
            if (e.name().equals(directionString)) {
                direction = e;
                break;
            }
        }


        ActionBotConfig.tasks.add(new TunnelTask(direction, length));
    }
}
