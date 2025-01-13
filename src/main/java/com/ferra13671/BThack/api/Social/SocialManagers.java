package com.ferra13671.BThack.api.Social;

import com.ferra13671.BThack.Core.Client.ModuleList;
import net.minecraft.util.Formatting;

public final class SocialManagers {
    public static final SocialManager FRIENDS = new SocialManager("Friends/Friends.txt", "friend") {

        @Override
        public Formatting getColor() {
            return Formatting.valueOf(ModuleList.clientSettings.friendColor.getValue());
        }
    };
    public static final SocialManager ENEMIES = new SocialManager("Enemies/Enemies.txt", "enemy") {

        @Override
        public Formatting getColor() {
            return Formatting.valueOf(ModuleList.clientSettings.enemyColor.getValue());
        }
    };
}
