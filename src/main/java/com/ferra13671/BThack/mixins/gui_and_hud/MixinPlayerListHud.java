package com.ferra13671.BThack.mixins.gui_and_hud;

import com.ferra13671.BThack.Core.Client.ModuleList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Comparator;
import java.util.List;

@Mixin(PlayerListHud.class)
public abstract class MixinPlayerListHud {

    @Shadow protected abstract Text applyGameModeFormatting(PlayerListEntry entry, MutableText name);

    @Shadow @Final private MinecraftClient client;

    @Shadow @Final private static Comparator<PlayerListEntry> ENTRY_ORDERING;

    @Inject(method = "getPlayerName", at = @At(value = "HEAD"), cancellable = true)
    public void modifyGetPlayerName(PlayerListEntry entry, CallbackInfoReturnable<Text> cir) {
        Text text = entry.getDisplayName() != null ? this.applyGameModeFormatting(entry, entry.getDisplayName().copy()) : applyGameModeFormatting(entry, Team.decorateName(entry.getScoreboardTeam(), Text.literal(entry.getProfile().getName())));

        if (ModuleList.extraTab.isEnabled())
            text = ModuleList.extraTab.getModifiedPlayerName(text.getString());

        cir.setReturnValue(text);
        cir.cancel();
    }

    @Inject(method = "collectPlayerEntries", at = @At(value = "HEAD"), cancellable = true)
    public void modifyCollectPlayerEntries(CallbackInfoReturnable<List<PlayerListEntry>> cir) {
        if (ModuleList.extraTab.isEnabled()) {
            cir.setReturnValue(client.player.networkHandler.getListedPlayerListEntries()
                    .stream().sorted(ENTRY_ORDERING).limit((int) ModuleList.extraTab.tabSize.getValue()).toList());
            cir.cancel();
        }
    }
}
