package com.ferra13671.BThack.impl.Modules.Player.ActionBot.Config.ActionBotTasks;

import com.ferra13671.BThack.core.Client.Systems.FileSystem.JsonUtils;
import com.ferra13671.BThack.managers.managers.Place.PlaceManager;
import com.ferra13671.BThack.managers.managers.Thread.ThreadClosedException;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import com.ferra13671.BThack.impl.Modules.Player.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.Modules.Player.ActionBot.Config.ActionBotTask;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Arrays;

public class UseItemOnBlockTask extends ActionBotTask {

    private final int x;
    private final int y;
    private final int z;

    public UseItemOnBlockTask(int x, int y, int z) {
        super("UseItemOnBlock");
        mode = "UseItemOnBlock";

        this.x = x;
        this.y = y;
        this.z = z;

        this.taskDescription = Arrays.asList(
                "When enabled, it use item in your main hand on block at the selected coordinates.",
                "Coordinates should be entered by the ratio of the player's coordinates."
        );
    }

    @Override
    public void play() throws ThreadClosedException {
        ItemUtils.useItemOnBlock(PlaceManager.getHitResult(BlockPos.ofFloored(mc.player.getX() + x, mc.player.getY() + y, mc.player.getZ() + z), false, Direction.UP));
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

        ActionBotConfig.tasks.add(new UseItemOnBlockTask(jsonObject.get("X").getAsInt(), jsonObject.get("Y").getAsInt(), jsonObject.get("Z").getAsInt()));
    }
}
