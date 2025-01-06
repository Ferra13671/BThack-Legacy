package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BTbot.api.Utils.Motion.Align.AlignWithXZ;
import com.ferra13671.BTbot.api.Utils.Motion.Align.WhereToAlign;
import com.ferra13671.BTbot.api.Utils.Motion.Goto.CollisionAction;
import com.ferra13671.BTbot.api.Utils.Motion.Goto.Goto;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Build.BuildManager;
import com.ferra13671.BThack.api.Managers.Build.BuildThread3D;
import com.ferra13671.BThack.api.Managers.Destroy.DestroyManager;
import com.ferra13671.BThack.api.Managers.Destroy.DestroyThread3D;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.Thread.ThreadManager;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.ModifyBlockPos;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.block.Blocks;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.Arrays;

public class HighwayBuilder extends Module {

    public static ModeSetting mode;

    public static NumberSetting buildTicks;
    public static BooleanSetting postMoveAlign;

    public static BooleanSetting borders;
    public static BooleanSetting extraBlocks;
    public static BooleanSetting onlyObsidian;
    public static NumberSetting highwWidth;
    public static NumberSetting tunnelHeight;
    public static NumberSetting actDelay;

    public static BooleanSetting clearFloat;
    public static BooleanSetting clearBorder;
    public static BooleanSetting clearEBlocks;


    public HighwayBuilder() {
        super("HighwayBuilder",
                "lang.module.HighwayBuilder",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        mode = new ModeSetting("Mode", this, new ArrayList<>(Arrays.asList("Highway", "Tunnel")));

        buildTicks = new NumberSetting("Build Ticks", this, 1, 0, 5, true, () -> !mode.getValue().equals("Tunnel"));
        postMoveAlign = new BooleanSetting("Post Move Align", this, true);

        borders = new BooleanSetting("Borders", this, true, () -> !mode.getValue().equals("Tunnel"));
        extraBlocks = new BooleanSetting("Extra Blocks", this, false, () -> !mode.getValue().equals("Tunnel"));
        onlyObsidian = new BooleanSetting("Only Obsidian", this, true, () -> !mode.getValue().equals("Tunnel"));
        highwWidth = new NumberSetting("Highw. Width", this, 4, 2, 6, true);
        tunnelHeight = new NumberSetting("Tunnel Height", this, 4, 3, 5, true);
        actDelay = new NumberSetting("ActDelay", this, 50, 20, 200, true);

        clearFloat = new BooleanSetting("Clear Float", this, true, () -> !mode.getValue().equals("Tunnel"));
        clearBorder = new BooleanSetting("Clear Border", this, true, () -> !mode.getValue().equals("Tunnel"));
        clearEBlocks = new BooleanSetting("Clear EBlocks", this, true, () -> !mode.getValue().equals("Tunnel"));

        initSettings(
                mode,

                buildTicks,
                postMoveAlign,

                borders,
                extraBlocks,
                onlyObsidian,
                highwWidth,
                tunnelHeight,
                actDelay,

                clearFloat,
                clearBorder,
                clearEBlocks
        );
    }

    public int highwayYaw = 0;

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        if (BuildManager.isBuilding) return;


        BlockPos blockPos = BlockPos.ofFloored(mc.player.getX(), Math.round(mc.player.getY()) - 1, mc.player.getZ());
        if (!mc.world.getBlockState(blockPos).isReplaceable()) return;

        BuildThread3D thread3D = new BuildThread3D();
        thread3D.set3DSchematic(0 ,Arrays.asList(new Vec3i(0,0,0)), blockPos);
        thread3D.setNeedBlocks(onlyObsidian.getValue() && mode.getValue().equals("Highway") ? Arrays.asList(Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN) : new ArrayList<>());
        thread3D.start();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        ThreadManager.startNewThread("HighwayThread", thread -> {
            highwayYaw = AimBotUtils.getAbsDirection(mc.player);

            alignAction(thread);

            while (this.isEnabled()) {
                ModuleList.scaffold.setToggled(false);

                while (BuildManager.isBuilding) {
                    try {
                        thread.sleep((int) actDelay.getValue());
                    } catch (Exception ignored) {
                    }
                }

                byte[] moveFactor = AimBotUtils.getCordFactorFromDirection(highwayYaw);

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

    private void breakAction(Thread thread, byte[] moveFactor) {
        ArrayList<Vec3i> schematic = getBreakSchematic(highwayYaw, 0, 0, 0,0);
        breakInternal(thread, moveFactor, schematic, true);

        ArrayList<Vec3i> tempSchematic = new ArrayList<>();
        for (Vec3i vec : getBuildSchematic(highwayYaw)) {
            if (schematic.contains(vec))
                tempSchematic.add(vec);
        }
        schematic.removeIf(tempSchematic::contains);

        breakInternal(thread, moveFactor, schematic, false);
        if (mode.getValue().equals("Tunnel")) {
            breakInternal(thread, new byte[]{(byte) (moveFactor[0] + 1), (byte) (moveFactor[1] + 1)}, schematic, false);
            breakInternal(thread, new byte[]{(byte) (moveFactor[0] - 1), (byte) (moveFactor[1] - 1)}, schematic, false);
        }
    }

    private void breakInternal(Thread thread, byte[] moveFactor, ArrayList<Vec3i> schematic, boolean ignoreObsidian) {
        DestroyThread3D destroyThread = new DestroyThread3D();
        destroyThread.set3DSchematic(schematic, new ModifyBlockPos(mc.player.getX() + (moveFactor[0] * 2), Math.round(mc.player.getY()) - (!mode.getValue().equals("Tunnel") ? 1 : 0), mc.player.getZ() + (moveFactor[1] * 2)));
        destroyThread.setIgnoreBlocks(ignoreObsidian ? Arrays.asList(Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN) : new ArrayList<>());
        destroyThread.start();
        try {
            thread.sleep(2);
        } catch (Exception ignored) {}
        while (DestroyManager.isDestroying) {
            try {
                thread.sleep((int) actDelay.getValue());
            } catch (Exception ignored) {
            }
        }
    }

    /*
    All the logic and action to move.
     */
    private void gotoAction(Thread thread, byte[] moveFactor) {
        boolean obstructionFound = !mc.world.isAir(new ModifyBlockPos(mc.player.getX() + moveFactor[0], mc.player.getY(), mc.player.getZ() + moveFactor[1]));

        if (!mc.world.isAir(new ModifyBlockPos(mc.player.getX() + moveFactor[0], mc.player.getY() + 1, mc.player.getZ() + moveFactor[1])))
            obstructionFound = true;

        Goto gotoN = new Goto(mc.player.getX() + (moveFactor[0] * (obstructionFound ? 0.16 : 1)), mc.player.getZ() + (moveFactor[1] * (obstructionFound ? 0.16 : 1)), CollisionAction.NONE);
        gotoN.start();
        try {
            thread.sleep(2);
        } catch (Exception ignored) {}
        while (gotoN.isMoving()) {
            try {
                thread.sleep((int) actDelay.getValue());
            } catch (Exception ignored) {
            }
        }
    }

    /*
    All logic and actions for XZ alignment.
     */
    private void alignAction(Thread thread) {
        WhereToAlign whereToAlign = new WhereToAlign();
        AlignWithXZ alignWithXZ = new AlignWithXZ(whereToAlign);
        alignWithXZ.alignWithXZ();
        try {
            thread.sleep(2);
        } catch (Exception ignored) {}
        while (alignWithXZ.isMoving()) {
            try {
                thread.sleep(100);
            } catch (Exception ignored) {}
        }
    }

    /*
    All the actions and logic for placing highways.
     */
    private void buildAction(Thread thread) {
        if (!mode.getValue().equals("Tunnel")) {
            ArrayList<Vec3i> schematic = getBuildSchematic(highwayYaw);
            BuildThread3D buildThread3D = new BuildThread3D();
            buildThread3D.set3DSchematic((int) buildTicks.getValue(), schematic, BlockPos.ofFloored(mc.player.getX(), Math.round(mc.player.getY()) - 1, mc.player.getZ()));
            buildThread3D.setNeedBlocks(onlyObsidian.getValue() ? Arrays.asList(Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN) : new ArrayList<>());
            buildThread3D.start();
            try {
                thread.sleep(2);
            } catch (Exception ignored) {}
            while (BuildManager.isBuilding) {
                try {
                    thread.sleep((int) actDelay.getValue());
                } catch (Exception ignored) {
                }
            }
        }
    }

    /*
    Checks for water and lava on the path. (However, it doesn't work)
     */
    private void waterAndLavaCheckAction(Thread thread) {
        byte[] moveFactor = getCordFactorFromDirection(highwayYaw);
        ArrayList<Vec3i> checkRadius = getBreakSchematic(highwayYaw, 1, 1, 1, 1);
        ArrayList<Vec3i> checkBlocks = new ArrayList<>();
        for (Vec3i vec3i : checkRadius) {
            BlockPos pos = new BlockPos(vec3i);
            if (mc.world.getBlockState(pos).isLiquid()) {
                checkBlocks.add(pos);
            }
        }
        BuildThread3D thread3D = new BuildThread3D();
        thread3D.set3DSchematic(1, checkBlocks, new ModifyBlockPos(mc.player.getX() + (moveFactor[0]), Math.round(mc.player.getY()) - (!mode.getValue().equals("Tunnel") ? 1 : 0), mc.player.getZ() + (moveFactor[1])));
        thread3D.start();

        try {
            thread.sleep(2);
        } catch (Exception ignored) {}
        while (BuildManager.isBuilding) {
            try {
                thread.sleep((int) actDelay.getValue());
            } catch (Exception ignored) {
            }
        }

        for (Vec3i vec3i : checkBlocks) {
            if (mc.world.getBlockState(new BlockPos(vec3i)).isLiquid()) {
                toggle();
                thread.stop();
            }
        }
    }

    /*
    Returns the block mining scheme. (Can break on corner trunks)
     */
    protected ArrayList<Vec3i> getBreakSchematic(int highwayYaw, int extraMinWidth, int extraMaxWidth, int extraMinHeight, int extraMaxHeight) {
        byte[] moveF = getCordFactorFromDirection(highwayYaw);
        int a = (int) (-(highwWidth.getValue() / 2)) - (((borders.getValue() || extraBlocks.getValue()) && !mode.getValue().equals("Tunnel")) ? 1 : 0) - extraMinWidth;
        int b = (int) ((highwWidth.getValue() - 1) - ((int) (highwWidth.getValue() / 2))) + (((borders.getValue() || extraBlocks.getValue()) && !mode.getValue().equals("Tunnel")) ? 1 : 0) + extraMaxWidth;
        ArrayList<Vec3i> sch = new ArrayList<>();
        int ab = a;
        if (!clearBorder.getValue() || mode.getValue().equals("Tunnel")) {
            a += 1;
            b -= 1;
            ab = a;
        }
        while (ab != 0) {
            sch.add(new Vec3i((ab - moveF[0]) * moveF[1], -extraMinHeight + 1, (ab - moveF[1]) * moveF[0]));
            ab++;
        }
        ab = b;
        while (ab != -1) {
            sch.add(new Vec3i((ab - moveF[0]) * moveF[1], -extraMinHeight + 1, (ab - moveF[1]) * moveF[0]));
            ab--;
        }
        if (!clearBorder.getValue() || mode.getValue().equals("Tunnel")) {
            a -= 1;
            b += 1;
        }
        ab = a;
        for (int i = -extraMinHeight + 2; i < tunnelHeight.getValue() + 1 + extraMaxHeight; i++) {
            while (ab != 0) {
                sch.add(new Vec3i((ab - moveF[0]) * moveF[1], i, (ab - moveF[1]) * moveF[0]));
                ab++;
            }
            ab = b;
            while (ab != -1) {
                sch.add(new Vec3i((ab - moveF[0]) * moveF[1], i, (ab - moveF[1]) * moveF[0]));
                ab--;
            }
            ab = a;
        }
        if (clearFloat.getValue() || mode.getValue().equals("Tunnel")) {
            if (!clearEBlocks.getValue()) {
                a += 1;
                b -= 1;
            }
            ab = a;
            while (ab != 0) {
                sch.add(new Vec3i((ab - moveF[0]) * moveF[1], -extraMinHeight, (ab - moveF[1]) * moveF[0]));
                ab++;
            }
            ab = b;
            while (ab != -1) {
                sch.add(new Vec3i((ab - moveF[0]) * moveF[1], -extraMinHeight, (ab - moveF[1]) * moveF[0]));
                ab--;
            }
        }
        return sch;
    }

    /*
    Returns a schematic for installing the blocks. (Can break on corner trunks)
     */
    protected ArrayList<Vec3i> getBuildSchematic(int highwayYaw) {
        byte[] moveF = getCordFactorFromDirection(highwayYaw);
        int a = (int) (-(highwWidth.getValue() / 2)) - (extraBlocks.getValue() ? 1 : 0);
        int b = (int) ((highwWidth.getValue() - 1) - ((int) (highwWidth.getValue() / 2))) + (extraBlocks.getValue() ? 1 : 0);
        ArrayList<Vec3i> sch = new ArrayList<>();
        int ab = a;
        while (ab != 1) {
            sch.add(new Vec3i((ab - moveF[0]) * moveF[1], 0, (ab - moveF[1]) * moveF[0]));
            ab++;
        }
        ab = b;
        while (ab != 0) {
            sch.add(new Vec3i((ab - moveF[0]) * moveF[1], 0, (ab - moveF[1]) * moveF[0]));
            ab--;
        }
        if (borders.getValue()) {
            a -= extraBlocks.getValue() ? 0 : 1;
            b += extraBlocks.getValue() ? 0 : 1;
            sch.add(new Vec3i((a - moveF[0]) * moveF[1], 1, (a - moveF[1]) * moveF[0]));
            sch.add(new Vec3i((b - moveF[0]) * moveF[1], 1, (b - moveF[1]) * moveF[0]));
        }
        return sch;
    }

    /*
    Returns the 2 byte numbers required to correctly determine the position of the blocks on the schematics.
     */
    public byte[] getCordFactorFromDirection(int yaw) {
        return switch (yaw) {
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
