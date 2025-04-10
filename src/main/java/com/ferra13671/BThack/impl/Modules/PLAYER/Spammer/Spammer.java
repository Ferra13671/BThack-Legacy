package com.ferra13671.BThack.impl.Modules.PLAYER.Spammer;

import com.ferra13671.BTbot.api.Utils.Generate.StringGenerator;
import com.ferra13671.BTbot.api.Utils.Generate.NumberGenerator;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.managers.Thread.ThreadManager;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.Constants;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class Spammer extends Module {

    public final ModeSetting spamMode = new ModeSetting("Mode", this, new ArrayList<>(Arrays.asList("InOrder", "Random")));
    public final NumberSetting delay = new NumberSetting("Delay(Second)", this, 45,1,600,true);
    public final BooleanSetting delaySpread = new BooleanSetting("Delay Spread", this, false);
    public final NumberSetting spreadRange = new NumberSetting("Spread range", this, 0.3, 0.1, 0.7, false, delaySpread::getValue);

    public final BooleanSetting antiSpam = new BooleanSetting("AntiSpam", this, true);
    public final NumberSetting aSpamLength = new NumberSetting("ASpam Length", this, 5, 1, 15, true, antiSpam::getValue);
    public final NumberSetting aSpamSpace = new NumberSetting("ASpam Space", this, 3, 1, 6, true, antiSpam::getValue);
    public final BooleanSetting aSpamCaps = new BooleanSetting("ASpam Caps", this, true, antiSpam::getValue);
    public final BooleanSetting aSpamNumbers = new BooleanSetting("ASpam Numbers", this, true, antiSpam::getValue);
    public final BooleanSetting aSpamSymbols = new BooleanSetting("ASpam Symbols", this, true, antiSpam::getValue);

    public Spammer() {
        super("Spammer",
                "lang.module.Spammer",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );

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

    private final ReadTXT readTXT = new ReadTXT();

    public int m = 1;

    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        ThreadManager.startNewThread(thread -> {
            Path path = Paths.get("BThack/Spammer/Spammer.txt");
            long delayInMillis = (long) (delay.getValue() * 1000);

            while (isEnabled()) {
                try {
                    this.arrayListInfo = spamMode.getValue();

                    String space = " ".repeat(Math.max(0, (int) aSpamSpace.getValue()));

                    if (Files.exists(path)) {
                        BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
                        String line = reader.readLine();
                        String tempLine = antiSpam.getValue() ? genAntiSpam() + space + line + space + genAntiSpam() : line;

                        switch (spamMode.getValue()) {
                            case "InOrder" -> {
                                if (m != 1) {
                                    for (int i = 1; i < m; i++) {
                                        if (line != null)
                                            line = reader.readLine();
                                    }
                                }
                                if (line != null) {
                                    ChatUtils.sendChatMessage(tempLine);
                                    m = m + 1;
                                } else {
                                    m = 1;
                                    continue;
                                }
                            }
                            case "Random" -> {
                                readTXT.read();
                                int randomValue = NumberGenerator.generateInt(1, readTXT.value);
                                if (randomValue == 1)
                                    ChatUtils.sendChatMessage(tempLine);
                                else {
                                    for (int i = 1; i < randomValue; i++) {
                                        line = reader.readLine();
                                    }
                                    if (line != null)
                                        ChatUtils.sendChatMessage(tempLine);
                                }
                            }
                        }
                        reader.close();
                    }

                    thread.sleepThread(
                            delaySpread.getValue() ?
                                    (int) (!Constants.RANDOM.nextBoolean() ? (delayInMillis * NumberGenerator.generateFloat(1, 1f + (float) spreadRange.getValue()))
                                            : (delayInMillis * NumberGenerator.generateFloat((float) spreadRange.getValue(), 1))) : delayInMillis);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private String genAntiSpam() {
        return StringGenerator.generateNextString(
                (int) aSpamLength.getValue(),
                aSpamCaps.getValue(),
                aSpamNumbers.getValue(),
                aSpamSymbols.getValue()
        );
    }
}
