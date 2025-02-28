package com.ferra13671.BThack.Core.BThackUpdater;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.FileSystem.JsonUtils;
import com.ferra13671.BThack.api.Utils.Data;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.LanguageAdapter;
import net.fabricmc.loader.api.LanguageAdapterException;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.impl.util.SystemProperties;

import javax.swing.*;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class BThackUpdater implements LanguageAdapter {
    public static final Path UPDATE_DATA_PATH = getDirectory0("BThack/UpdateData.jar");

    static {
        checkAndFinalizeUpdate();
    }

    public static void checkAndFinalizeUpdate() {
        if (Files.exists(UPDATE_DATA_PATH)) {
            BThack.log("Start finalize BThack updating...");
            File bthackFile = findBThackFile();
            if (bthackFile == null) {
                BThack.error("BThack file not found!");
                return;
            }
            try {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(Files.newInputStream(UPDATE_DATA_PATH));
                FileOutputStream fileOutputStream = new FileOutputStream(bthackFile);
                byte[] data = new byte[1024];
                int downloadedBytes;
                while ((downloadedBytes = bufferedInputStream.read(data, 0, 1024)) != -1)
                    fileOutputStream.write(data, 0, downloadedBytes);
                bufferedInputStream.close();
                fileOutputStream.close();
                Files.delete(UPDATE_DATA_PATH);
                BThack.log("The BThack update has been fully finalized!");
                JOptionPane.showMessageDialog(null, "The second stage of the update is complete, restart your minecraft.");
            } catch (Exception e) {
                BThack.error(e.getMessage());
            }
        }
    }

    public static UpdaterThread startUpdate() throws Exception {
        String fileName = "BThack-" + BThack.instance.MC_VERSION + "-fabric" + BThack.instance.versionInfo.getNewVersion().replace(BThack.instance.MC_VERSION, "") + (BThack.instance.withBaritone() ? "" : "-NoBaritone") + ".jar";
        URL url = new URI("https://github.com/Ferra13671/BThack/releases/download/" + BThack.instance.versionInfo.getNewVersion() + "/" + fileName).toURL();
        BThack.initLog(url.toString());
        try {
            Files.createFile(UPDATE_DATA_PATH);
        } catch (Exception ignored) {}
        UpdaterThread thread = new UpdaterThread(url, UPDATE_DATA_PATH);
        thread.start();
        return thread;
    }

    public static File findBThackFile() {
        Data<File> value = new Data<>(null);
        try {
            Files.walkFileTree(getDirectory0("mods"), EnumSet.of(FileVisitOption.FOLLOW_LINKS), 1, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                    File file = path.toFile();
                    try {
                        JarFile mod = new JarFile(file);
                        InputStream modInfo = mod.getInputStream(new JarEntry("fabric.mod.json"));
                        if (modInfo != null) {
                            JsonObject jsonObject = JsonParser.parseReader(new InputStreamReader(modInfo)).getAsJsonObject();
                            if (!JsonUtils._null(jsonObject, "id")) {
                                if (jsonObject.get("id").getAsString().equals("bthack")) value.set(file);
                            }
                        }
                    } catch (Exception ignored) {}

                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            BThack.error(e.getMessage());
        }
        return value.get();
    }

    private static Path getDirectory0(String and) {
        String directory = null;
        if (and.equals("mods"))
            directory = System.getProperty(SystemProperties.MODS_FOLDER);

        return directory != null ? Paths.get(directory) : FabricLoader.getInstance().getGameDir().resolve(and);
    }

    @Override
    public <T> T create(ModContainer mod, String value, Class<T> type) throws LanguageAdapterException {
        return null;
    }
}
