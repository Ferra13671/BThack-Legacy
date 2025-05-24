package com.ferra13671.BThack.core.Client.Systems.ConfigSystem;

import com.ferra13671.BThack.Constants;
import com.ferra13671.TextureUtils.PathMode;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

public final class ConfigUtils {

    public static void registerFiles(String name, String path) throws IOException {
        Path p = Paths.get("BThack/" + path + "/" + name + ".json");
        if (Files.exists(p)) {
            File file = new File("BThack/" + path + "/" + name + ".json");
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }

        Files.createFile(p);
    }

    public static OutputStreamWriter createWriter(Path path) throws IOException {
        return new OutputStreamWriter(Files.newOutputStream(path), StandardCharsets.UTF_8);
    }

    public static String jsonToString(JsonObject object) {
        return Constants.GSON.toJson(JsonParser.parseString(object.toString()));
    }

    public static InputStream newInputStream(String path, PathMode pathMode) {
        if (pathMode == PathMode.INSIDEJAR) {
            return ConfigUtils.class.getClassLoader().getResourceAsStream(path);
        } else {
            try {
                return Files.newInputStream(Paths.get(path));
            } catch (IOException e) {
                return null;
            }
        }
    }

    public static void loadFromJson(String fileName, String path, Consumer<JsonObject> runnable, Runnable notFoundFileRunnable) throws IOException {
        Path path1 = Paths.get("BThack/" + path + "/" + fileName + ".json");
        if (!Files.exists(path1)) {
            notFoundFileRunnable.run();
            return;
        }

        InputStream inputStream = Files.newInputStream(path1);
        JsonObject jsonObject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();

        runnable.accept(jsonObject);

        inputStream.close();
    }

    public static void saveInJson(String fileName, String path, Consumer<JsonObject> runnable) throws IOException {
        registerFiles(fileName, path.replace("BThack/", ""));
        OutputStreamWriter fileOutputStreamWriter = createWriter(Paths.get("BThack/" + path + "/" + fileName + ".json"));

        JsonObject jsonObject = new JsonObject();

        runnable.accept(jsonObject);

        String jsonString = jsonToString(jsonObject);
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }
}
