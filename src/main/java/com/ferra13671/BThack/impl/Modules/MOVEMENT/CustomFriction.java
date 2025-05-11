package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.core.Client.Client;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.DataList.BlockList;
import com.ferra13671.BThack.api.Utils.DataList.DataLists;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import net.minecraft.util.Formatting;

import java.util.Arrays;

public class CustomFriction extends Module {

    public final NumberSetting friction = new NumberSetting("Friction", this, 1, 0.01, 1.1, false);
    public final ModeSetting mode = new ModeSetting("Mode", this, Arrays.asList("WhiteList", "BlackList"));

    public CustomFriction() {
        super("CustomFriction",
                "lang.module.CustomFriction",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (!nullCheck())
            ChatUtils.sendMessage(Formatting.GRAY + "Use: " + Client.clientInfo.getChatPrefix() + DataLists.get("CustomFriction", BlockList.class).editDataListCommand.getAliases()[0]);
    }
}
