package com.ferra13671.BThack.impl.Modules.Player;

import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Utils.MathUtils;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.CategorySetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.managers.managers.Thread.ThreadManager;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.Constants;
import com.ferra13671.BThack.impl.Modules.Player.Spammer.ReadTXT;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringHelper;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@ModuleInfo(name = "PMSpammer", description = "lang.module.PMSpammer", category = "PLAYER")
public class PMSpammer extends Module {

    public final ModeSetting spamMode = new ModeSetting("Mode", this, new ArrayList<>(Arrays.asList("InOrder", "Random")));
    public final NumberSetting delay = new NumberSetting("Delay(Second)", this, 45,1,600,true);
    public final BooleanSetting delaySpread = new BooleanSetting("Delay Spread", this, false);
    public final NumberSetting spreadRange = new NumberSetting("Spread range", this, 0.3, 0.1, 0.7, false, delaySpread::getValue);

    public final CategorySetting antiSpamCategory = new CategorySetting("AntiSpam", this);
    public final BooleanSetting antiSpam = new BooleanSetting("AntiSpam", this, true).inCategory(antiSpamCategory);
    public final NumberSetting aSpamLength = new NumberSetting("ASpam Length", this, 5, 1, 15, true, antiSpam::getValue).inCategory(antiSpamCategory);
    public final NumberSetting aSpamSpace = new NumberSetting("ASpam Space", this, 3, 1, 6, true, antiSpam::getValue).inCategory(antiSpamCategory);
    public final BooleanSetting aSpamCaps = new BooleanSetting("ASpam Caps", this, true, antiSpam::getValue).inCategory(antiSpamCategory);
    public final BooleanSetting aSpamNumbers = new BooleanSetting("ASpam Numbers", this, true, antiSpam::getValue).inCategory(antiSpamCategory);
    public final BooleanSetting aSpamSymbols = new BooleanSetting("ASpam Symbols", this, true, antiSpam::getValue).inCategory(antiSpamCategory);


    protected final ReadTXT readTXT = new ReadTXT();

    private int m = 1;

    @Override
    @SuppressWarnings("DataFlowIssue")
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        ThreadManager.startNewThread(thread -> {
            Path path = Paths.get("BThack/Spammer/Spammer.txt");
            String mode = spamMode.getValue();
            sendNotification(Formatting.AQUA + LanguageSystem.translate("lang.module.PMSpammer.startSpam"));

            long delayInMillis = (long) (delay.getValue() * 1000);
            while (ModuleList.pmSpammer.isEnabled()) {
                ModuleList.pmSpammer.arrayListInfo = mode;

                String space = " ".repeat(Math.max(0, aSpamSpace.getValue().intValue()));

                if (Objects.equals(mode, "InOrder")) {
                    try {
                        if (Files.exists(path)) {
                            BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
                            String line = reader.readLine();

                            if (m != 1) {
                                for (int i = 1; i < m; i++) {
                                    if (line != null)
                                        line = reader.readLine();
                                }
                            }
                            if (line != null) {
                                Set<PlayerListEntry> playerInfos = new HashSet<>(mc.player.networkHandler.getPlayerList());
                                String tempLine = antiSpam.getValue() ? genAntiSpam() + space + line + space + genAntiSpam() : line;

                                for (PlayerListEntry info : playerInfos) {
                                    if (!ModuleList.pmSpammer.isEnabled())
                                        thread.stopOnException();

                                    String playerName = getPlayerName(info);

                                    if (playerName.equals(mc.player.getName().getString())) continue;

                                    sendNotification(Formatting.AQUA + String.format(LanguageSystem.translate("lang.module.PMSpammer.trySend"), playerName));

                                    sendMessage("/w " + playerName + " " + tempLine);

                                    thread.sleepThread(
                                            delaySpread.getValue() ?
                                                    (int) (!Constants.RANDOM.nextBoolean() ? (delayInMillis * MathUtils.randomFloat(1, 1f + spreadRange.getValue().floatValue()))
                                                            : (delayInMillis * MathUtils.randomFloat(spreadRange.getValue().floatValue(), 1))) : delayInMillis);
                                }
                                m = m + 1;
                                sendNotification(Formatting.AQUA + LanguageSystem.translate("lang.module.PMSpammer.movingToNext"));
                            } else m = 1;
                            reader.close();

                            if (!ModuleList.pmSpammer.isEnabled())
                                thread.stopOnException();

                            thread.sleepThread(
                                    delaySpread.getValue() ?
                                            (int) (!Constants.RANDOM.nextBoolean() ? (delayInMillis * MathUtils.randomFloat(1, 1f + spreadRange.getValue().floatValue()))
                                                    : (delayInMillis * MathUtils.randomFloat(spreadRange.getValue().floatValue(), 1))) : delayInMillis);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else
                if (Objects.equals(mode, "Random")) {
                    if (Files.exists(path)) {
                        readTXT.read();
                        long randomValue = 1 + (int) (Math.random() * (readTXT.value));
                        try {
                            BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
                            String line = reader.readLine();

                            for (int i = 1; i < randomValue; i++) {
                                line = reader.readLine();
                            }
                            if (line != null) {
                                Set<PlayerListEntry> playerInfos = new HashSet<>(mc.player.networkHandler.getPlayerList());
                                String tempLine = antiSpam.getValue() ? genAntiSpam() + space + line + space + genAntiSpam() : line;

                                for (PlayerListEntry info : playerInfos) {
                                    if (!ModuleList.pmSpammer.isEnabled())
                                        thread.stopOnException();

                                    String playerName = getPlayerName(info);

                                    if (playerName.equals(mc.player.getName().getString())) continue;

                                    sendNotification(Formatting.AQUA + String.format(LanguageSystem.translate("lang.module.PMSpammer.trySend"), playerName));

                                    sendMessage("/w " + playerName + " " + tempLine);

                                    thread.sleepThread(
                                            delaySpread.getValue() ?
                                                    (int) (!Constants.RANDOM.nextBoolean() ? (delayInMillis * MathUtils.randomFloat(1, 1f + spreadRange.getValue().floatValue()))
                                                            : (delayInMillis * MathUtils.randomFloat(spreadRange.getValue().floatValue(), 1))) : delayInMillis);
                                }
                            }
                            reader.close();

                            if (!ModuleList.pmSpammer.isEnabled())
                                thread.stopOnException();

                            thread.sleepThread(
                                    delaySpread.getValue() ?
                                            (int) (!Constants.RANDOM.nextBoolean() ? (delayInMillis * MathUtils.randomFloat(1, 1f + spreadRange.getValue().floatValue()))
                                                    : (delayInMillis * MathUtils.randomFloat(spreadRange.getValue().floatValue(), 1))) : delayInMillis);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }

            readTXT.value = 0;
        });
    }

    public String getPlayerName(PlayerListEntry networkPlayerInfoIn) {
        return networkPlayerInfoIn.getDisplayName() != null ? networkPlayerInfoIn.getDisplayName().getString() : networkPlayerInfoIn.getProfile().getName();
    }

    @SuppressWarnings("DataFlowIssue")
    public boolean sendMessage(String chatText) {
        chatText = this.normalize(chatText);
        if (!chatText.isEmpty()) {
            mc.inGameHud.getChatHud().addToMessageHistory(chatText);

            if (chatText.startsWith("/"))
                mc.player.networkHandler.sendChatCommand(chatText.substring(1));
            else
                mc.player.networkHandler.sendChatMessage(chatText);

        }
        return true;
    }

    public String normalize(String chatText) {
        return StringHelper.truncateChat(StringUtils.normalizeSpace(chatText.trim()));
    }

    private String genAntiSpam() {
        return MathUtils.randomString(
                aSpamLength.getValue().intValue(),
                aSpamCaps.getValue(),
                aSpamNumbers.getValue(),
                aSpamSymbols.getValue()
        );
    }
}
