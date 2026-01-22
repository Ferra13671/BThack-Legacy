package com.ferra13671.BThack.impl.modules.player.ActionBot.Config.ActionBotTasks;

import com.ferra13671.BThack.core.client.systems.file.JsonUtils;
import com.ferra13671.BThack.managers.impl.place.PlaceManager;
import com.ferra13671.BThack.managers.impl.place.PlaceThread3D;
import com.ferra13671.BThack.managers.impl.thread.ThreadClosedException;
import com.ferra13671.BThack.impl.modules.player.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.modules.player.ActionBot.Config.ActionBotTask;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlaceTask extends ActionBotTask {

    private final int x;
    private final int y;
    private final int z;

    public PlaceTask(int x, int y, int z) {
        super("Place");
        mode = "Place";

        this.x = x;
        this.y = y;
        this.z = z;

        this.taskDescription = Arrays.asList(
                "When enabled, it place the block at the selected coordinates.",
                "Coordinates should be entered by the ratio of the player's coordinates."
        );
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public void play() throws ThreadClosedException {
        PlaceThread3D buildThread3D = new PlaceThread3D();
        buildThread3D.set3DSchematic(1, new ArrayList<>(List.of(new Vec3d(mc.player.getX() + x, mc.player.getY() + y, mc.player.getZ() + z))), new BlockPos(0,0,0));
        buildThread3D.start();
        while (PlaceManager.isBuilding) {
            if (thread.isThreadClosed()) {
                buildThread3D.closeThread();
                thread.stopOnException();
            }
            sleepThread(50);
        }
    }

    @Override
    public String getButtonName() {
        return getName() + ":  X: " + x + "  Y: " + y + "  Z: " + z;
    }

    @Override
    public void save(JsonObject jsonObject) {
        jsonObject.add("X", new JsonPrimitive(x));
        jsonObject.add("Y", new JsonPrimitive(y));
        jsonObject.add("Z", new JsonPrimitive(z));
    }

    @Override
    public void load(JsonObject jsonObject) {
        if (JsonUtils.equalsNull(jsonObject, "X", "Y", "Z")) return;

        ActionBotConfig.tasks.add(new PlaceTask(jsonObject.get("X").getAsInt(), jsonObject.get("Y").getAsInt(), jsonObject.get("Z").getAsInt()));
    }
}
