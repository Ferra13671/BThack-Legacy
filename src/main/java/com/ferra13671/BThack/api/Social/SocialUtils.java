package com.ferra13671.BThack.api.Social;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public final class SocialUtils {
    public static void save(String path, List<String> arrayList) {
        boolean m = false;

        try {
            if (Files.exists(Paths.get("BThack/Social/" + path))) {
                File f = new File("BThack/Social/" + path);
                FileWriter writer = new FileWriter(f, false);

                for (String name : arrayList) {
                    if (!m) {
                        writer.write(name);
                        m = true;
                    } else {
                        writer.write(System.lineSeparator() + name);
                    }
                }

                writer.close();
            }
        } catch (IOException e) {
                throw new RuntimeException(e);
        }
    }

    public static void load(String path, List<String> arrayList) {
        arrayList.clear();

        try {
            if (Files.exists(Paths.get("BThack/Social/" + path))) {
                File f = new File("BThack/Social/" + path);
                BufferedReader reader = new BufferedReader(new FileReader(f));
                String line = reader.readLine();

                while (line != null) {
                    arrayList.add(line);
                    line = reader.readLine();
                }

                reader.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
