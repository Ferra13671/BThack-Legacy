package com.ferra13671.BThack.impl.modules.player.ActionBot.Config.ActionBotTasks;


import com.ferra13671.BThack.core.client.systems.file.JsonUtils;
import com.ferra13671.BThack.api.utils.ChatUtils;
import com.ferra13671.BThack.impl.modules.player.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.modules.player.ActionBot.Config.ActionBotTask;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Arrays;

public class SendMessageTask extends ActionBotTask {
    private final String message;

    public SendMessageTask(String message) {
        super("Send Message");
        this.mode = "SendMessage";

        this.message = message;

        this.taskDescription = Arrays.asList(
                "When a task is activated, the player sends the desired message to the chat.",
                "This task only supports the English language."
        );
    }

    @Override
    public void play() {
        ChatUtils.sendChatMessage(this.getMessage());
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public String getButtonName() {
        return getName() + ": " + getMessage();
    }

    @Override
    public void save(JsonObject jsonObject) {
        jsonObject.add("Message", new JsonPrimitive(getMessage()));
    }

    @Override
    public void load(JsonObject jsonObject) {
        if (JsonUtils._null(jsonObject, "Message")) return;

        String message = jsonObject.get("Message").getAsString();

        ActionBotConfig.tasks.add(new SendMessageTask(message));
    }
}
