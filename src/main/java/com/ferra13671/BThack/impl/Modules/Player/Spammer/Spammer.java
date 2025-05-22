package com.ferra13671.BThack.impl.Modules.Player.Spammer;

import com.ferra13671.BThack.managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.CategorySetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.managers.managers.Thread.ThreadManager;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.Constants;
import com.ferra13671.BThack.api.Utils.MathUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

@ModuleInfo(name = "Spammer", description = "lang.module.Spammer", category = "PLAYER")
public class Spammer extends Module {

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

                    String space = " ".repeat(Math.max(0, aSpamSpace.getValue().intValue()));

                    if (Files.exists(path)) {
                        BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
                        String line = reader.readLine();

                        switch (spamMode.getValue()) {
                            case "InOrder" -> {
                                if (m != 1) {
                                    for (int i = 1; i < m; i++) {
                                        if (line != null)
                                            line = reader.readLine();
                                    }
                                }
                                String tempLine = antiSpam.getValue() ? genAntiSpam() + space + line + space + genAntiSpam() : line;
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
                                int randomValue = MathUtils.randomInt(1, readTXT.value);
                                String tempLine = antiSpam.getValue() ? genAntiSpam() + space + line + space + genAntiSpam() : line;
                                if (randomValue == 1)
                                    ChatUtils.sendChatMessage(tempLine);
                                else {
                                    for (int i = 1; i < randomValue; i++) {
                                        line = reader.readLine();
                                    }
                                    tempLine = antiSpam.getValue() ? genAntiSpam() + space + line + space + genAntiSpam() : line;
                                    if (line != null)
                                        ChatUtils.sendChatMessage(tempLine);
                                }
                            }
                        }
                        reader.close();
                    }

                    thread.sleepThread(
                            delaySpread.getValue() ?
                                    (int) (!Constants.RANDOM.nextBoolean() ? (delayInMillis * MathUtils.randomFloat(1, 1f + spreadRange.getValue().floatValue()))
                                            : (delayInMillis * MathUtils.randomFloat(spreadRange.getValue().floatValue(), 1))) : delayInMillis);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
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
