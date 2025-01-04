package com.ferra13671.BThack.Core.FileSystem;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonUtils {

    public static void add(JsonObject jsonObject, String property, Boolean bool) {
        jsonObject.add(property, new JsonPrimitive(bool));
    }

    public static void add(JsonObject jsonObject, String property, Number number) {
        jsonObject.add(property, new JsonPrimitive(number));
    }

    public static void add(JsonObject jsonObject, String property, String string) {
        jsonObject.add(property, new JsonPrimitive(string));
    }

    public static void add(JsonObject jsonObject, String property, Character c) {
        jsonObject.add(property, new JsonPrimitive(c));
    }

    public static void add(JsonObject jsonObject, String property, JsonElement element) {
        jsonObject.add(property, element);
    }

    public static boolean _null(JsonObject object, String checkObjectName) {
        return object.get(checkObjectName) == null;
    }

    public static boolean equalsNull(JsonObject object, String... objectsNames) {
        for (String s : objectsNames) {
            if (object.get(s) == null) return true;
        }
        return false;
    }
}
