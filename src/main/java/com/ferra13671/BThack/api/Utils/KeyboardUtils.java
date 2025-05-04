package com.ferra13671.BThack.api.Utils;

import com.google.common.collect.Sets;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Set;

@SuppressWarnings("unused")
public final class KeyboardUtils {
    private static final HashMap<String, Integer> keyCodeMap = new HashMap<>();
    private static final HashMap<Integer, String> keyNameMap = new HashMap<>();

    private static final Set<Integer> activeKeys = Sets.newHashSet();


    static {
        Field[] fields = KeyboardUtils.class.getFields();
        try {
            for (Field field : fields) {
                if ( Modifier.isStatic(field.getModifiers())
                        && Modifier.isPublic(field.getModifiers())
                        && Modifier.isFinal(field.getModifiers())
                        && field.getType().equals(int.class)
                        && field.getName().startsWith("KEY_")) {

                    int key = field.getInt(null);
                    String name = field.getName().substring(4).toUpperCase();
                    keyCodeMap.put(name, key);
                    keyNameMap.put(key, name);
                }
            }
        } catch (Exception ignored) {}
    }

    public static void addActiveKey(int key) {
        activeKeys.add(key);
    }

    public static void removeActiveKey(int key) {
        activeKeys.remove(key);
    }

    public static boolean isKeyDown(int key) {
        return activeKeys.contains(key);
    }

    public static Set<Integer> getActiveKeys() {
        return activeKeys;
    }

    public static boolean containsKey(String key) {
        return keyCodeMap.containsKey(key.toUpperCase());
    }

    public static boolean containsIndex(int index) {
        return keyCodeMap.containsValue(index);
    }

    public static int getKeyIndex(String key) {
        return keyCodeMap.get(key.toUpperCase());
    }

    public static Set<String> getKeys() {
        return keyCodeMap.keySet();
    }

    public static String getKeyName(int key) {
        return keyNameMap.getOrDefault(key, "UNKNOWN");
    }

    public static final int KEY_0 = 48;
    public static final int KEY_1 = 49;
    public static final int KEY_2 = 50;
    public static final int KEY_3 = 51;
    public static final int KEY_4 = 52;
    public static final int KEY_5 = 53;
    public static final int KEY_6 = 54;
    public static final int KEY_7 = 55;
    public static final int KEY_8 = 56;
    public static final int KEY_9 = 57;
    public static final int KEY_A = 65;
    public static final int KEY_B = 66;
    public static final int KEY_C = 67;
    public static final int KEY_D = 68;
    public static final int KEY_E = 69;
    public static final int KEY_F = 70;
    public static final int KEY_G = 71;
    public static final int KEY_H = 72;
    public static final int KEY_I = 73;
    public static final int KEY_J = 74;
    public static final int KEY_K = 75;
    public static final int KEY_L = 76;
    public static final int KEY_M = 77;
    public static final int KEY_N = 78;
    public static final int KEY_O = 79;
    public static final int KEY_P = 80;
    public static final int KEY_Q = 81;
    public static final int KEY_R = 82;
    public static final int KEY_S = 83;
    public static final int KEY_T = 84;
    public static final int KEY_U = 85;
    public static final int KEY_V = 86;
    public static final int KEY_W = 87;
    public static final int KEY_X = 88;
    public static final int KEY_Y = 89;
    public static final int KEY_Z = 90;
    public static final int KEY_F1 = 290;
    public static final int KEY_F2 = 291;
    public static final int KEY_F3 = 292;
    public static final int KEY_F4 = 293;
    public static final int KEY_F5 = 294;
    public static final int KEY_F6 = 295;
    public static final int KEY_F7 = 296;
    public static final int KEY_F8 = 297;
    public static final int KEY_F9 = 298;
    public static final int KEY_F10 = 299;
    public static final int KEY_F11 = 300;
    public static final int KEY_F12 = 301;
    public static final int KEY_F13 = 302;
    public static final int KEY_F14 = 303;
    public static final int KEY_F15 = 304;
    public static final int KEY_F16 = 305;
    public static final int KEY_F17 = 306;
    public static final int KEY_F18 = 307;
    public static final int KEY_F19 = 308;
    public static final int KEY_F20 = 309;
    public static final int KEY_F21 = 310;
    public static final int KEY_F22 = 311;
    public static final int KEY_F23 = 312;
    public static final int KEY_F24 = 313;
    public static final int KEY_F25 = 314;
    public static final int KEY_ENTER = 257;
    public static final int KEY_SHIFT = 340;
    public static final int KEY_NUMLOCK = 282;
    public static final int KEY_NUMPAD0 = 320;
    public static final int KEY_NUMPAD1 = 321;
    public static final int KEY_NUMPAD2 = 322;
    public static final int KEY_NUMPAD3 = 323;
    public static final int KEY_NUMPAD4 = 324;
    public static final int KEY_NUMPAD5 = 325;
    public static final int KEY_NUMPAD6 = 326;
    public static final int KEY_NUMPAD7 = 327;
    public static final int KEY_NUMPAD8 = 328;
    public static final int KEY_NUMPAD9 = 329;
    public static final int KEY_NUMPADCOMMA = 330;
    public static final int KEY_NUMPADENTER = 335;
    public static final int KEY_NUMPADEQUALS = 336;
    public static final int KEY_DOWN = 264;
    public static final int KEY_LEFT = 263;
    public static final int KEY_RIGHT = 262;
    public static final int KEY_UP = 265;
    public static final int KEY_ADD = 334;
    public static final int KEY_APOSTROPHE = 39;
    public static final int KEY_BACKSLASH = 92;
    public static final int KEY_COMMA = 44;
    public static final int KEY_EQUALS = 61;
    public static final int KEY_GRAVE = 96;
    public static final int KEY_LBRACKET = 91;
    public static final int KEY_MINUS = 45;
    public static final int KEY_MULTIPLY = 332;
    public static final int KEY_PERIOD = 46;
    public static final int KEY_RBRACKET = 93;
    public static final int KEY_SEMICOLON = 59;
    public static final int KEY_SLASH = 47;
    public static final int KEY_SPACE = 32;
    public static final int KEY_TAB = 258;
    public static final int KEY_LALT = 342;
    public static final int KEY_LCONTROL = 341;
    public static final int KEY_LSHIFT = 340;
    public static final int KEY_LWIN = 343;
    public static final int KEY_RALT = 346;
    public static final int KEY_RCONTROL = 345;
    public static final int KEY_RSHIFT = 344;
    public static final int KEY_RWIN = 347;
    public static final int KEY_RETURN = 257;
    public static final int KEY_ESCAPE = 256;
    public static final int KEY_BACKSPACE = 259;
    public static final int KEY_DELETE = 261;
    public static final int KEY_END = 269;
    public static final int KEY_HOME = 268;
    public static final int KEY_INSERT = 260;
    public static final int KEY_PAGEDOWN = 267;
    public static final int KEY_PAGEUP = 266;
    public static final int KEY_CAPSLOCK = 280;
    public static final int KEY_PAUSE = 284;
    public static final int KEY_SCROLLLOCK = 281;
    public static final int KEY_PRINTSCREEN = 283;
    public static final int PRESS = 1;
    public static final int RELEASE = 0;
    public static final int REPEAT = 2;
    public static final int MOUSE_BUTTON_LEFT = 0;
    public static final int MOUSE_BUTTON_MIDDLE = 2;
    public static final int MOUSE_BUTTON_RIGHT = 1;
    public static final int MOD_CONTROL = 2;
    public static final int CURSOR = 208897;
    public static final int CURSOR_DISABLED = 212995;
    public static final int CURSOR_NORMAL = 212993;
}
