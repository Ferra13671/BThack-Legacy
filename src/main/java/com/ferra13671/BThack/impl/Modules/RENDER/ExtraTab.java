package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Social.SocialManagers;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ExtraTab extends Module {

    public final NumberSetting tabSize = new NumberSetting("Tab Size", this, 200, 100, 2000, true);
    public final BooleanSetting friends = new BooleanSetting("Friends", this, true);
    public final BooleanSetting enemies = new BooleanSetting("Enemies", this, true);
    public final BooleanSetting yourself = new BooleanSetting("Yourself", this, true);

    public ExtraTab() {
        super("ExtraTab",
                "lang.module.ExtraTab",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        initSettings(
                tabSize,
                friends,
                enemies,
                yourself
        );
    }

    public Text getModifiedPlayerName(String name) {
        if (friends.getValue()) {
            if (SocialManagers.FRIENDS.contains(name)) {
                return Text.of(Formatting.GREEN + name);
            }
        }
        if (enemies.getValue()) {
            if (SocialManagers.ENEMIES.contains(name)) {
                return Text.of(Formatting.RED + name);
            }
        }
        if (mc.player.getDisplayName().getString().equals(name))
            return Text.of(Formatting.AQUA + name);

        return Text.of(name);
    }
}
