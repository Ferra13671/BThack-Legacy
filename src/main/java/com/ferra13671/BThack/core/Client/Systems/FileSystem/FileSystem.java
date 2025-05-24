package com.ferra13671.BThack.core.Client.Systems.FileSystem;

import com.ferra13671.BThack.BThack;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class FileSystem {

    public static void start() throws IOException {
        registerFolder("HudComponents", "");
        registerFolder("Social", "");
        registerFolder("Friends", "/Social");
        registerFolder("Enemies", "/Social");
        registerFolder("Spammer", "");
        registerFolder("Modules", "");
        registerFolder("ActionBotConfigs", "");
        registerFolder("DefaultConfig", "/ActionBot");
        registerFolder("Configs", "");
        registerFolder("Fonts", "");
        registerFolder("CustomCapes", "");

        registerFile("AutoAuthPasswords", "", FileType.JSON);
        registerFile("2FAKeys", "", FileType.JSON);
        registerFile("VersionInfo", "", FileType.JSON);
        registerFile("Friends", "Social/Friends", FileType.TXT);
        registerFile("Enemies", "Social/Enemies", FileType.TXT);
        registerFile("Spammer", "Spammer", FileType.TXT);
        registerFile("CurrentConfig", "Modules", FileType.TXT);
        registerFile("Frames", "", FileType.JSON);
        registerFile("ClientInfo", "", FileType.JSON);
        registerFile("Waypoints", "", FileType.JSON);
        registerFile("Macros", "", FileType.JSON);
        registerFile("Default", "ActionBotConfigs", FileType.JSON);
    }

    public static void registerFile(String name, String path, FileType typeFile) throws IOException {
        Path path1 = Paths.get(Paths.get("BThack/" + path + "/" + name + "." + typeFile.getFileType()).toUri());
        if (!Files.exists(path1)) {
            Files.createFile(path1);
            BThack.debug(name + "." + typeFile.getFileType() + " file was created successfully");
            if (typeFile == FileType.JSON) {
                BufferedWriter writer = Files.newBufferedWriter(path1, StandardCharsets.UTF_8);
                writer.write("{}");
                writer.close();
            }
        } else {
            BThack.debug(name + "." + typeFile.getFileType() + " file already exists");
        }

    }

    public static void registerFolder(String name, String path) throws IOException {
        Path path1 = Paths.get(Paths.get("BThack" + path + "/" + name).toUri());
        if (!Files.exists(path1)) {
            Files.createDirectories(path1);
            BThack.debug(name + " folder created successfully");
        } else {
            BThack.debug(name + " folder already exists");
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void deleteDirectory(File directory) {
        File[] contents = directory.listFiles();
        if (contents != null)
            for (File file : contents)
                deleteDirectory(file);
        directory.delete();
    }

    public enum FileType {
        TXT("txt"),
        JSON("json");

        private final String fileType;

        FileType(String fileType) {
            this.fileType = fileType;
        }

        public String getFileType() {
            return fileType;
        }
    }
}
