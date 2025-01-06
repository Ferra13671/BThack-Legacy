package com.ferra13671.BThack.impl.Modules.PLAYER;


import com.ferra13671.BTbot.api.Utils.Generate.AntiSpamGenerate;
import com.ferra13671.BTbot.api.Utils.Generate.GenerateNumber;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.Thread.ThreadManager;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.impl.Modules.PLAYER.Spammer.ReadTXT;
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

public class PMSpammer extends Module {

    public static ModeSetting spamMode;
    public static NumberSetting delay;
    public static BooleanSetting delaySpread;
    public static NumberSetting spreadRange;

    public static BooleanSetting antiSpam;
    public static NumberSetting aSpamLength;
    public static NumberSetting aSpamSpace;
    public static BooleanSetting aSpamCaps;
    public static BooleanSetting aSpamNumbers;
    public static BooleanSetting aSpamSymbols;

    protected static ReadTXT readTXT = new ReadTXT();

    public PMSpammer() {
        super("PMSpammer",
                "lang.module.PMSpammer",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );

        spamMode = new ModeSetting("Mode", this, new ArrayList<>(Arrays.asList("InOrder", "Random")));
        delay = new NumberSetting("Delay(Second)", this, 45,1,600,true);
        delaySpread = new BooleanSetting("Delay Spread", this, false);
        spreadRange = new NumberSetting("Spread range", this, 0.3, 0.1, 0.7, false, delaySpread::getValue);

        antiSpam = new BooleanSetting("AntiSpam", this, true);
        aSpamLength = new NumberSetting("ASpam Length", this, 5, 1, 15, true, antiSpam::getValue);
        aSpamSpace = new NumberSetting("ASpam Space", this, 3, 1, 6, true, antiSpam::getValue);
        aSpamCaps = new BooleanSetting("ASpam Caps", this, true, antiSpam::getValue);
        aSpamNumbers = new BooleanSetting("ASpam Numbers", this, true, antiSpam::getValue);
        aSpamSymbols = new BooleanSetting("ASpam Symbols", this, true, antiSpam::getValue);

        initSettings(
                spamMode,
                delay,
                delaySpread,
                spreadRange,

                antiSpam,
                aSpamLength,
                aSpamSpace,
                aSpamCaps,
                aSpamNumbers,
                aSpamSymbols
        );
    }

    Random random = new Random();

    public static int m = 1;

    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        ThreadManager.startNewThread(thread -> {
            Path path = Paths.get("BThack/Spammer/Spammer.txt");
            String mode = spamMode.getValue();
            sendNotification(Formatting.AQUA + LanguageSystem.translate("lang.module.PMSpammer.startSpam"));
            while (ModuleList.pmSpammer.isEnabled()) {
                ModuleList.pmSpammer.arrayListInfo = mode;

                String space = "";
                for (int i = 0; i < (int) aSpamSpace.getValue(); i++) {
                    space = space + " ";
                }

                if (Objects.equals(mode, "InOrder")) {
                    try {
                        if (Files.exists(path)) {
                            BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
                            String line = reader.readLine();
                            if (m != 1) {
                                for (int i = 1; i < m; i++) {
                                    if (line != null) {
                                        line = reader.readLine();
                                    }
                                }
                            }
                            if (line != null) {
                                Set<PlayerListEntry> playerInfos = new HashSet<>(mc.player.networkHandler.getPlayerList());

                                for (PlayerListEntry info : playerInfos) {
                                    if (!ModuleList.pmSpammer.isEnabled())
                                        thread.stop();

                                    String tempLine;
                                    if (antiSpam.getValue()) {
                                        tempLine = genAntiSpam() + space + line + space + genAntiSpam();
                                    } else {
                                        tempLine = line;
                                    }

                                    String playerName = getPlayerName(info);

                                    if (playerName.equals(mc.player.getName().getString())) continue;

                                    sendNotification(Formatting.AQUA + String.format(LanguageSystem.translate("lang.module.PMSpammer.trySend"), playerName));

                                    sendMessage("/w " + playerName + " " + tempLine);
                                    long a = (long) (delay.getValue() * 1000);
                                    if (delaySpread.getValue()) {
                                        if (!random.nextBoolean()) {
                                            a = (long) (a * GenerateNumber.generateFloat(1, 1f + (float) spreadRange.getValue()));
                                        } else {
                                            a = (long) (a * GenerateNumber.generateFloat((float) spreadRange.getValue(), 1));
                                        }
                                    }
                                    try {
                                        thread.sleep(a);
                                    } catch (Exception ignored) {
                                    }
                                }
                                m = m + 1;
                                sendNotification(Formatting.AQUA + LanguageSystem.translate("lang.module.PMSpammer.movingToNext"));
                            } else {
                                m = 1;
                            }
                            reader.close();
                            if (!ModuleList.pmSpammer.isEnabled())
                                thread.stop();
                            long a = (long) (delay.getValue() * 1000);
                            if (delaySpread.getValue()) {
                                if (!random.nextBoolean()) {
                                    a = (long) (a * GenerateNumber.generateFloat(1, 1f + (float) spreadRange.getValue()));
                                } else {
                                    a = (long) (a * GenerateNumber.generateFloat((float) spreadRange.getValue(), 1));
                                }
                            }
                            try {
                                thread.sleep(a);
                            } catch (Exception ignored) {
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else
                if (Objects.equals(mode, "Random")) {
                    if (Files.exists(path)) {
                        PMSpammer.readTXT.read();
                        long randomValue = 1 + (int) (Math.random() * (PMSpammer.readTXT.value));
                        try {
                            BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
                            String line = reader.readLine();

                            for (int i = 1; i < randomValue; i++) {
                                line = reader.readLine();
                            }
                            if (line != null) {
                                Set<PlayerListEntry> playerInfos = new HashSet<>(mc.player.networkHandler.getPlayerList());

                                for (PlayerListEntry info : playerInfos) {
                                    if (!ModuleList.pmSpammer.isEnabled())
                                        thread.stop();

                                    String playerName = getPlayerName(info);

                                    String tempLine;
                                    if (antiSpam.getValue()) {
                                        tempLine = genAntiSpam() + space + line + space + genAntiSpam();
                                    } else {
                                        tempLine = line;
                                    }

                                    if (playerName.equals(mc.player.getName().getString())) continue;

                                    sendNotification(Formatting.AQUA + String.format(LanguageSystem.translate("lang.module.PMSpammer.trySend"), playerName));

                                    sendMessage("/w " + playerName + " " + tempLine);
                                    long a = (long) (delay.getValue() * 1000);
                                    if (delaySpread.getValue()) {
                                        if (!random.nextBoolean()) {
                                            a = (long) (a * GenerateNumber.generateFloat(1, 1f + (float) spreadRange.getValue()));
                                        } else {
                                            a = (long) (a * GenerateNumber.generateFloat((float) spreadRange.getValue(), 1));
                                        }
                                    }
                                    try {
                                        thread.sleep(a);
                                    } catch (Exception ignored) {
                                    }
                                }
                            }
                            reader.close();
                            if (!ModuleList.pmSpammer.isEnabled())
                                thread.stop();
                            long a = (long) (delay.getValue() * 1000);
                            if (delaySpread.getValue()) {
                                if (!random.nextBoolean()) {
                                    a = (long) (a * GenerateNumber.generateFloat(1, 1f + (float) spreadRange.getValue()));
                                } else {
                                    a = (long) (a * GenerateNumber.generateFloat((float) spreadRange.getValue(), 1));
                                }
                            }
                            try {
                                thread.sleep(a);
                            } catch (Exception ignored) {
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }

            PMSpammer.readTXT.value = 0;
        });
    }

    public String getPlayerName(PlayerListEntry networkPlayerInfoIn) {
        return networkPlayerInfoIn.getDisplayName() != null ? networkPlayerInfoIn.getDisplayName().getString() : networkPlayerInfoIn.getProfile().getName();
    }

    public boolean sendMessage(String chatText) {
        chatText = this.normalize(chatText);
        if (chatText.isEmpty()) {
            return true;
        } else {
            mc.inGameHud.getChatHud().addToMessageHistory(chatText);

            if (chatText.startsWith("/")) {
                mc.player.networkHandler.sendChatCommand(chatText.substring(1));
            } else {
                mc.player.networkHandler.sendChatMessage(chatText);
            }

            return true;
        }
    }

    public String normalize(String chatText) {
        return StringHelper.truncateChat(StringUtils.normalizeSpace(chatText.trim()));
    }

    private String genAntiSpam() {
        return AntiSpamGenerate.generateNextString(
                (int) aSpamLength.getValue(),
                aSpamCaps.getValue(),
                aSpamNumbers.getValue(),
                aSpamSymbols.getValue()
        );
    }
}
