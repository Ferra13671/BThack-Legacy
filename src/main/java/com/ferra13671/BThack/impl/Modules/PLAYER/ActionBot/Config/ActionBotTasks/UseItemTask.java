package com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTasks;

import com.ferra13671.BThack.Core.FileSystem.JsonUtils;
import com.ferra13671.BThack.api.Managers.managers.Thread.ThreadClosedException;
import com.ferra13671.BThack.api.Utils.Grim.GrimUtils;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.Hand;

import java.util.Arrays;

public class UseItemTask extends ActionBotTask {

    private final float yaw;
    private final float pitch;

    public UseItemTask(float yaw, float pitch) {
        super("UseItem");
        mode = "UseItem";

        this.yaw = yaw;
        this.pitch = pitch;

        this.taskDescription = Arrays.asList(
                "When enabled, it use item at the selected yaw and pitch.",
                "Coordinates should be entered by the ratio of the player's coordinates."
        );
    }

    @Override
    public void play() throws ThreadClosedException {
        GrimUtils.sendPreActionGrimPackets(yaw, pitch);
        ItemUtils.useItem(Hand.MAIN_HAND, true, yaw, pitch);
        GrimUtils.sendPostActionGrimPackets();
    }

    @Override
    public String getButtonName() {
        return getName() + ":  Yaw: " + yaw + "  Pitch: " + pitch;
    }

    @Override
    public void save(JsonObject jsonObject) {
        jsonObject.add("Yaw", new JsonPrimitive(yaw));
        jsonObject.add("Pitch", new JsonPrimitive(pitch));
    }

    @Override
    public void load(JsonObject jsonObject) {
        if (JsonUtils.equalsNull(jsonObject, "Yaw", "Pitch")) return;

        ActionBotConfig.tasks.add(new UseItemTask(jsonObject.get("Yaw").getAsFloat(), jsonObject.get("Pitch").getAsFloat()));
    }
}
