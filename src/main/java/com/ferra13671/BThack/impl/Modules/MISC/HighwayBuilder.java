package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BTbot.api.Utils.Motion.Align.AlignWithXZ;
import com.ferra13671.BTbot.api.Utils.Motion.Align.WhereToAlign;
import com.ferra13671.BTbot.api.Utils.Motion.Goto.CollisionAction;
import com.ferra13671.BTbot.api.Utils.Motion.Goto.Goto;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.managers.Build.BuildManager;
import com.ferra13671.BThack.api.Managers.managers.Build.BuildThread3D;
import com.ferra13671.BThack.api.Managers.managers.Destroy.DestroyManager;
import com.ferra13671.BThack.api.Managers.managers.Destroy.DestroyThread3D;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.managers.Thread.BThackThread;
import com.ferra13671.BThack.api.Managers.managers.Thread.ThreadManager;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.MathUtils;
import com.ferra13671.BThack.api.Utils.Rotate.RotateMode;
import com.ferra13671.BThack.api.Utils.Rotate.RotateUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
  Known bugs:
        Terrible work on the corner highways
  TODO:
        Ability to freely rotate the camera when working
        More settings
 */
public class HighwayBuilder extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", this, new ArrayList<>(Arrays.asList("Highway", "Tunnel")));

    public final NumberSetting buildTicks = new NumberSetting("Build Ticks", this, 1, 0, 5, true, () -> !mode.getValue().equals("Tunnel"));
    public final BooleanSetting postMoveAlign = new BooleanSetting("Post Move Align", this, true);

    public final BooleanSetting borders = new BooleanSetting("Borders", this, true, () -> !mode.getValue().equals("Tunnel"));
    public final BooleanSetting extraBlocks = new BooleanSetting("Extra Blocks", this, false, () -> !mode.getValue().equals("Tunnel"));
    public final BooleanSetting onlyObsidian = new BooleanSetting("Only Obsidian", this, true, () -> !mode.getValue().equals("Tunnel"));
    public final NumberSetting highwWidth = new NumberSetting("Highw. Width", this, 4, 2, 6, true);
    public final NumberSetting tunnelHeight = new NumberSetting("Tunnel Height", this, 4, 3, 5, true);
    //If the value is less than 100, problems with moving between stages may appear
    public final NumberSetting stageDelay = new NumberSetting("Stage Delay", this, 100, 100, 200, true);

    public final BooleanSetting clearFloat = new BooleanSetting("Clear Float", this, true, () -> !mode.getValue().equals("Tunnel"));
    public final BooleanSetting clearBorder = new BooleanSetting("Clear Border", this, true, () -> !mode.getValue().equals("Tunnel"));
    public final BooleanSetting clearEBlocks = new BooleanSetting("Clear EBlocks", this, true, () -> !mode.getValue().equals("Tunnel"));


    public HighwayBuilder() {
        super("HighwayBuilder",
                "lang.module.HighwayBuilder",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        initSettings(
                mode,

                buildTicks,
                postMoveAlign,

                borders,
                extraBlocks,
                onlyObsidian,
                highwWidth,
                tunnelHeight,
                stageDelay,

                clearFloat,
                clearBorder,
                clearEBlocks
        );
    }

    public int highwayYaw = 0;
    public byte[] moveFactor;

    @EventSubscriber
    @SuppressWarnings({"DataFlowIssue", "unused"})
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        if (BuildManager.isBuilding) return;


        BlockPos blockPos = BlockPos.ofFloored(mc.player.getX(), Math.round(mc.player.getY()) - 1, mc.player.getZ());
        if (!mc.world.getBlockState(blockPos).isReplaceable()) return;

        int slot = InventoryUtils.findItem(BlockItem.class);
        if (onlyObsidian.getValue() && mode.getValue().equals("Highway")) {
            slot = InventoryUtils.findItem(Items.OBSIDIAN);
            if (slot == -1) slot = InventoryUtils.findItem(Items.CRYING_OBSIDIAN);
        }
        if (slot == -1) {
            ChatUtils.sendMessage(this.getChatName() + " " + Formatting.RED + LanguageSystem.translate("lang.module.Scaffold.noBlocks"));
            setToggled(false);
            return;
        }

        int oldSlot = mc.player.getInventory().selectedSlot;
        InventoryUtils.swapAction(oldSlot, slot, false, "Client");
        BuildManager.placeBlock(blockPos, RotateMode.GRIM);
        InventoryUtils.swapAction(oldSlot, slot, true, "Client");
    }

    @Override
    public void onEnable() {
        super.onEnable();
        ThreadManager.startNewThread("HighwayThread", thread -> {
            highwayYaw = RotateUtils.getAbsDirection(mc.player);
            moveFactor = getCordFactorFromDirection();

            alignAction(thread);

            while (this.isEnabled()) {
                byte[] moveFactor = RotateUtils.getCordFactorFromDirection(highwayYaw);

                waterAndLavaCheckAction(thread);

                breakAction(thread, moveFactor);

                if (!BuildManager.pickUpPlaceBlocks(true, onlyObsidian.getValue() && mode.getValue().equals("Highway") ? Arrays.asList(Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN) : new ArrayList<>())) {
                    ChatUtils.sendMessage(this.getChatName() + " " + Formatting.RED + LanguageSystem.translate("lang.module.Scaffold.noBlocks"));
                    setToggled(false);
                    return;
                }

                gotoAction(thread, moveFactor);
                if (postMoveAlign.getValue())
                    alignAction(thread);

                buildAction(thread);
            }
        });
    }

    /**
     * All the actions and logic for breaking interfering blocks.
     */
    private void breakAction(BThackThread thread, byte[] moveFactor) {
        ArrayList<Vec3i> schematic = getBreakSchematic(0, 0, 0,0);
        breakInternal(thread, moveFactor, schematic, true);

        ArrayList<Vec3i> buildSchematic = getBuildSchematic();
        schematic.removeIf(buildSchematic::contains);

        breakInternal(thread, moveFactor, schematic, false);
        if (mode.getValue().equals("Tunnel")) {
            breakInternal(thread, new byte[]{(byte) (moveFactor[0] + 1), (byte) (moveFactor[1] + 1)}, schematic, false);
            breakInternal(thread, new byte[]{(byte) (moveFactor[0] - 1), (byte) (moveFactor[1] - 1)}, schematic, false);
        }
    }

    @SuppressWarnings("DataFlowIssue")
    private void breakInternal(BThackThread thread, byte[] moveFactor, ArrayList<Vec3i> schematic, boolean ignoreObsidian) {
        DestroyThread3D destroyThread = new DestroyThread3D();
        destroyThread.set3DSchematic(schematic, BlockPos.ofFloored(mc.player.getX() + (moveFactor[0] * 2), Math.round(mc.player.getY()) - (!mode.getValue().equals("Tunnel") ? 1 : 0), mc.player.getZ() + (moveFactor[1] * 2)));
        destroyThread.setIgnoreBlocks(ignoreObsidian ? Arrays.asList(Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN) : new ArrayList<>());
        destroyThread.start();
        thread.sleepThread(2);
        while (DestroyManager.isDestroying) {
            thread.sleepThread(stageDelay.getValue().longValue());
        }
    }

    /**
     * All the logic and action to move.
     */
    @SuppressWarnings("DataFlowIssue")
    private void gotoAction(BThackThread thread, byte[] moveFactor) {
        boolean obstructionFound = !mc.world.isAir(BlockPos.ofFloored(mc.player.getX() + moveFactor[0], mc.player.getY(), mc.player.getZ() + moveFactor[1]));

        if (!mc.world.isAir(BlockPos.ofFloored(mc.player.getX() + moveFactor[0], mc.player.getY() + 1, mc.player.getZ() + moveFactor[1])))
            obstructionFound = true;

        Goto gotoN = new Goto(mc.player.getX() + (moveFactor[0] * (obstructionFound ? 0.16 : 0.5)), mc.player.getZ() + (moveFactor[1] * (obstructionFound ? 0.16 : 0.5)), CollisionAction.NONE);
        gotoN.start();
        thread.sleepThread(2);
        while (gotoN.isMoving()) {
            thread.sleepThread(stageDelay.getValue().longValue());
        }
    }

    /**
     * All logic and actions for XZ alignment.
     */
    private void alignAction(BThackThread thread) {
        WhereToAlign whereToAlign = new WhereToAlign();
        AlignWithXZ alignWithXZ = new AlignWithXZ(whereToAlign);
        alignWithXZ.alignWithXZ();

        thread.sleepThread(2);
        while (alignWithXZ.isMoving()) {
            thread.sleepThread(100);
        }
    }

    /**
     * All the actions and logic for placing highways.
     */
    @SuppressWarnings("DataFlowIssue")
    private void buildAction(BThackThread thread) {
        if (!mode.getValue().equals("Tunnel")) {
            ArrayList<Vec3i> schematic = getBuildSchematic();
            BuildThread3D buildThread3D = new BuildThread3D();
            buildThread3D.set3DSchematic(buildTicks.getValue().intValue(), schematic, BlockPos.ofFloored(mc.player.getX(), Math.round(mc.player.getY()) - 1, mc.player.getZ()));
            buildThread3D.setNeedBlocks(onlyObsidian.getValue() ? Arrays.asList(Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN) : new ArrayList<>());
            buildThread3D.start();
            thread.sleepThread(2);
            while (BuildManager.isBuilding) {
                thread.sleepThread(stageDelay.getValue().longValue());
            }
        }
    }

    /**
     * Checks for water and lava on the path.
     * (However, it doesn't work)
     */
    @SuppressWarnings("DataFlowIssue")
    private void waterAndLavaCheckAction(BThackThread thread) {
        ArrayList<Vec3i> checkRadius = getBreakSchematic(1, 1, 1, 1);
        ArrayList<Vec3i> checkBlocks = new ArrayList<>();
        for (Vec3i vec3i : checkRadius) {
            BlockPos pos = new BlockPos(vec3i);
            if (mc.world.getBlockState(pos).getBlock() instanceof FluidBlock) {
                checkBlocks.add(pos);
            }
        }
        BuildThread3D thread3D = new BuildThread3D();
        thread3D.set3DSchematic(1, checkBlocks, BlockPos.ofFloored(mc.player.getX() + moveFactor[0], Math.round(mc.player.getY()) - (!mode.getValue().equals("Tunnel") ? 1 : 0), mc.player.getZ() + moveFactor[1]));
        thread3D.start();

        thread.sleepThread(2);
        while (BuildManager.isBuilding) {
            thread.sleepThread(stageDelay.getValue().longValue());
        }

        for (Vec3i vec3i : checkBlocks) {
            if (mc.world.getBlockState(new BlockPos(vec3i)).getBlock() instanceof FluidBlock) {
                toggle();
                thread.closeThread();
            }
        }
    }

    /**
     * Returns the block mining scheme. (Can break on corner highways)
     */
    protected ArrayList<Vec3i> getBreakSchematic(int extraMinWidth, int extraMaxWidth, int extraMinHeight, int extraMaxHeight) {
        ArrayList<Vec3i> sch = new ArrayList<>();

        int a = (int) (-(highwWidth.getValue() / 2)) - (((borders.getValue() || extraBlocks.getValue()) && !mode.getValue().equals("Tunnel")) ? 1 : 0) - extraMinWidth;
        int b = (int) ((highwWidth.getValue() - 1) - ((int) (highwWidth.getValue() / 2))) + (((borders.getValue() || extraBlocks.getValue()) && !mode.getValue().equals("Tunnel")) ? 1 : 0) + extraMaxWidth;

        int extraValue = (!clearBorder.getValue() || mode.getValue().equals("Tunnel")) ? 1 : 0;

        addLine(a + extraValue, b - extraValue, -extraMinHeight + 1, sch);

        for (int i = -extraMinHeight + 2; i < tunnelHeight.getValue() + 1 + extraMaxHeight; i++)
            addLine(a, b, i, sch);

        if (clearFloat.getValue() || mode.getValue().equals("Tunnel")) {
            int e = !clearEBlocks.getValue() ? 1 : 0;
            addLine(a + e, b - e, -extraMinHeight, sch);
        }

        return sch;
    }

    public void addLine(int start, int end, int y, ArrayList<Vec3i> schematic) {
        List<Integer> abValues = MathUtils.getNumbers(start, end);
        boolean last = false;
        while (!abValues.isEmpty()) {
            Integer value = last ? abValues.getLast() : abValues.getFirst();
            addPos(value, y, schematic);
            abValues.remove(value);
            last = !last;
        }
    }

    public void addPos(int value, int y, ArrayList<Vec3i> schematic) {
        schematic.add(new Vec3i((value - moveFactor[0]) * moveFactor[1], y, (value - moveFactor[1]) * moveFactor[0]));
    }

    /**
     * Returns a schematic for placing the blocks. (Can break on corner highways)
     */
    protected ArrayList<Vec3i> getBuildSchematic() {
        ArrayList<Vec3i> sch = new ArrayList<>();

        int a = (int) (-(highwWidth.getValue() / 2)) - (extraBlocks.getValue() ? 1 : 0);
        int b = (int) ((highwWidth.getValue() - 1) - ((int) (highwWidth.getValue() / 2))) + (extraBlocks.getValue() ? 1 : 0);

        addLine(a, b, 0, sch);
        if (borders.getValue()) {
            int e = extraBlocks.getValue() ? 0 : 1;
            addPos(a - e, 1, sch);
            addPos(b + e, 1, sch);
        }

        return sch;
    }

    /**
     * Returns the 2 byte numbers required to correctly determine the position of the blocks on the schematics.
     */
    public byte[] getCordFactorFromDirection() {
        return switch (highwayYaw) {
            case 45 -> new byte[]{-1, -1};
            case 135 -> new byte[]{1, -1};

            case 225 -> new byte[]{1, 1};
            case 315 -> new byte[]{-1, 1};


            case 90 -> new byte[]{-1, 0};
            case 180 -> new byte[]{0, 1};
            case 270 -> new byte[]{1, 0};
            case 0 -> new byte[]{0, -1};
            default -> new byte[]{0, 0};
        };
    }
}
