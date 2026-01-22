package com.ferra13671.BThack.impl.modules.movement;

import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.core.client.Client;
import com.ferra13671.BThack.managers.impl.setting.Settings.ModeSetting;
import com.ferra13671.BThack.managers.impl.setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.utils.ChatUtils;
import com.ferra13671.BThack.api.utils.datalist.BlockList;
import com.ferra13671.BThack.api.utils.datalist.DataLists;
import net.minecraft.util.Formatting;

import java.util.Arrays;

@ModuleInfo(name = "CustomFriction", description = "lang.module.CustomFriction", category = "MOVEMENT")
public class CustomFriction extends Module {

    public final NumberSetting friction = new NumberSetting("Friction", this, 1, 0.01, 1.1, false);
    public final ModeSetting mode = new ModeSetting("Mode", this, Arrays.asList("WhiteList", "BlackList"));


    @Override
    public void onEnable() {
        super.onEnable();
        if (!nullCheck())
            ChatUtils.sendMessage(Formatting.GRAY + "Use: " + Client.clientInfo.getChatPrefix() + DataLists.get("CustomFriction", BlockList.class).editDataListCommand.getAliases()[0]);
    }
}
