package com.ferra13671.BThack.impl.Modules.PLAYER.Spammer;

import com.ferra13671.BTbot.api.Utils.Generate.AntiSpamGenerate;
import com.ferra13671.BTbot.api.Utils.Generate.GenerateNumber;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.Thread.ThreadManager;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Spammer extends Module {

    Random random = new Random();

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

    public Spammer() {
        super("Spammer",
                "lang.module.Spammer",
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

    public int m = 1;

    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        ThreadManager.startNewThread(thread -> {
            Path path = Paths.get("BThack/Spammer/Spammer.txt");

            while (this.isEnabled()) {
                try {
                    this.arrayListInfo = spamMode.getValue();

                    String space = "";
                    for (int i = 0; i < (int) aSpamSpace.getValue(); i++) {
                        space = space + " ";
                    }

                    if (Files.exists(path)) {
                        BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
                        String line = reader.readLine();

                        switch (spamMode.getValue()) {
                            case "InOrder":
                                if (m != 1) {
                                    for (int i = 1; i < m; i++) {
                                        if (line != null) {
                                            line = reader.readLine();
                                        }
                                    }
                                }
                                if (line != null) {
                                    String tempLine;
                                    if (antiSpam.getValue()) {
                                        tempLine = genAntiSpam() + space + line + space + genAntiSpam();
                                    } else {
                                        tempLine = line;
                                    }
                                    ChatUtils.sendChatMessage(tempLine);
                                    m = m + 1;
                                } else {
                                    m = 1;
                                    continue;
                                }
                                break;
                            case "Random":
                                Spammer.readTXT.read();
                                int randomValue = GenerateNumber.generateInt(1, Spammer.readTXT.value);
                                if (randomValue == 1) {
                                    String tempLine;
                                    if (antiSpam.getValue()) {
                                        tempLine = genAntiSpam() + space + line + space + genAntiSpam();
                                    } else {
                                        tempLine = line;
                                    }
                                    ChatUtils.sendChatMessage(tempLine);
                                }
                                else {
                                    for (int i = 1; i < randomValue; i++) {
                                        line = reader.readLine();
                                    }
                                    if (line != null) {
                                        String tempLine = "";
                                        if (antiSpam.getValue()) {
                                            tempLine = genAntiSpam() + space + line + space + genAntiSpam();
                                        } else {
                                            tempLine = line;
                                        }
                                        ChatUtils.sendChatMessage(tempLine);
                                    }
                                }
                                break;
                        }
                        reader.close();
                    }

                    int a = (int) (delay.getValue() * 1000);

                    if (delaySpread.getValue()) {
                        if (!random.nextBoolean()) {
                            a = (int) (a * GenerateNumber.generateFloat(1, 1f + (float) spreadRange.getValue()));
                        } else {
                            a = (int) (a * GenerateNumber.generateFloat((float) spreadRange.getValue(), 1));
                        }
                    }

                    try {
                        thread.sleep(a);
                    } catch (InterruptedException ignored) {}
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
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
