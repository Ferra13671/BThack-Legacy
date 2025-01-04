package com.ferra13671.BThack.impl.Modules.PLAYER.Spammer;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ReadTXT {
    public int value = 1;

    public void read() {
        try {
            Path path = Paths.get("BThack/Spammer/Spammer.txt");
            value = 0;
            if (Files.exists(path)) {
                BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
                String line = reader.readLine();
                while (line != null) {
                    line = reader.readLine();
                    if (line != null)
                        value = value + 1;
                }
                reader.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
