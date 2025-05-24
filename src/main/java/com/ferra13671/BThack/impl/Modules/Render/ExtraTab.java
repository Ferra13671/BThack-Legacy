package com.ferra13671.BThack.impl.Modules.Render;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.impl.Modules.Client.ClientSettings;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@ModuleInfo(name = "ExtraTab", description = "lang.module.ExtraTab", category = "RENDER")
public class ExtraTab extends Module {

    public final NumberSetting tabSize = new NumberSetting("Tab Size", this, 200, 100, 2000, true);
    public final BooleanSetting friends = new BooleanSetting("Friends", this, true);
    public final BooleanSetting enemies = new BooleanSetting("Enemies", this, true);
    public final BooleanSetting yourself = new BooleanSetting("Yourself", this, true);


    @Override
    public void onEnable() {
        if (BThack.isFuturePresent()) {
            if (!nullCheck()) sendNotification(Formatting.RED + LanguageSystem.translate("lang.module.ExtraTab.futurePresentMessage"));
            setEnabled(false);
            return;
        }

        super.onEnable();
    }
    @SuppressWarnings("DataFlowIssue")

    public Text getModifiedPlayerName(String name) {
        if (friends.getValue() && Managers.FRIENDS_MANAGER.contains(name))
            return Text.literal(ClientSettings.getFriendColor() + name);
        if (enemies.getValue() && Managers.ENEMIES_MANAGER.contains(name))
            return Text.literal(ClientSettings.getEnemyColor() + name);
        if (yourself.getValue() && mc.player.getDisplayName().getString().equals(name))
            return Text.literal(ClientSettings.getOwnColor() + name);

        return Text.literal(name);
    }
}
