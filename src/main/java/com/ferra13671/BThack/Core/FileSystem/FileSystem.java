package com.ferra13671.BThack.Core.FileSystem;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigUtils;
import com.google.gson.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class FileSystem {
    private static final Gson gson = (new GsonBuilder()).setPrettyPrinting().create();

    public static void start() throws IOException {

        registerFolder("HudComponents", "");
        registerFolder("Social", "");
        registerFolder("Friends", "/Social");
        registerFolder("Enemies", "/Social");
        registerFolder("Spammer", "");
        registerFolder("Modules", "");
        registerFolder("Themes", "");
        registerFolder("ColourThemes", "/Themes");
        registerFolder("ActionBotConfigs", "");
        registerFolder("DefaultConfig", "/ActionBot");
        registerFolder("Wallpapers", "");
        registerFolder("Configs", "");
        registerFolder("Fonts", "");
        registerFolder("CustomCapes", "");
        registerFile("AutoAuthPasswords", "", FileType.JSON);
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

    public static void createTutorialJsonTheme() throws IOException {
        ConfigUtils.registerFiles("tutorialTheme", "Themes/ColourThemes");

        OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(Files.newOutputStream(Paths.get("BThack/Themes/ColourThemes/tutorialTheme.json")), StandardCharsets.UTF_8);

        JsonObject colourThemeObject = new JsonObject();
        JsonObject coloursObject = new JsonObject();

        colourThemeObject.add("Name", new JsonPrimitive("TutorialTheme"));

        coloursObject.add("color", new JsonPrimitive(0x191CFF));
        coloursObject.add("backgroundColor", new JsonPrimitive(0xFF111111));
        coloursObject.add("backgroundHoveredColor", new JsonPrimitive(0xFF222222));
        coloursObject.add("moduleEnabledColor", new JsonPrimitive(0x191CFF));
        coloursObject.add("moduleDisabledColor", new JsonPrimitive(0xFFFFFF));
        coloursObject.add("arrayListColor", new JsonPrimitive(0x191CFF));

        colourThemeObject.add("Colours", coloursObject);
        String jsonString = gson.toJson(JsonParser.parseString(colourThemeObject.toString()));
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }

    public static void deleteDirectory(File directory) {
        File[] contents = directory.listFiles();
        if (contents != null) {
            for (File file : contents) {
                deleteDirectory(file);
            }
        }
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
