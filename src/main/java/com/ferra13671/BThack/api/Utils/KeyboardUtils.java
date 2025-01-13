package com.ferra13671.BThack.api.Utils;

import com.google.common.collect.Sets;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Set;

@SuppressWarnings("unused")
public final class KeyboardUtils {
    private static final String[] keyNames = new String[256];
    private static final HashMap<String, Integer> keyMap = new HashMap<>();

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
                    String name = field.getName().substring(4).toLowerCase();
                    keyNames[key] = name;
                    keyMap.put(name, key);
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

    public static int getKeyIndex(String key) {
        return keyMap.get(key.toLowerCase());
    }

    public static String getKeyName(int key) {
        return switch (key) {
            case 48 -> "0";
            case 49 -> "1";
            case 50 -> "2";
            case 51 -> "3";
            case 52 -> "4";
            case 53 -> "5";
            case 54 -> "6";
            case 55 -> "7";
            case 56 -> "8";
            case 57 -> "9";
            case 65 -> "A";
            case 66 -> "B";
            case 67 -> "C";
            case 68 -> "D";
            case 69 -> "E";
            case 70 -> "F";
            case 71 -> "G";
            case 72 -> "H";
            case 73 -> "I";
            case 74 -> "J";
            case 75 -> "K";
            case 76 -> "L";
            case 77 -> "M";
            case 78 -> "N";
            case 79 -> "O";
            case 80 -> "P";
            case 81 -> "Q";
            case 82 -> "R";
            case 83 -> "S";
            case 84 -> "T";
            case 85 -> "U";
            case 86 -> "V";
            case 87 -> "W";
            case 88 -> "X";
            case 89 -> "Y";
            case 90 -> "Z";
            case 290 -> "F1";
            case 291 -> "F2";
            case 292 -> "F3";
            case 293 -> "F4";
            case 294 -> "F5";
            case 295 -> "F6";
            case 296 -> "F7";
            case 297 -> "F8";
            case 298 -> "F9";
            case 299 -> "F10";
            case 300 -> "F11";
            case 301 -> "F12";
            case 302 -> "F13";
            case 303 -> "F14";
            case 304 -> "F15";
            case 305 -> "F16";
            case 306 -> "F17";
            case 307 -> "F18";
            case 308 -> "F19";
            case 309 -> "F20";
            case 310 -> "F21";
            case 311 -> "F22";
            case 312 -> "F23";
            case 313 -> "F24";
            case 314 -> "F25";
            case 282 -> "NUMLOCK";
            case 320 -> "NUMPAD0";
            case 321 -> "NUMPAD1";
            case 322 -> "NUMPAD2";
            case 323 -> "NUMPAD3";
            case 324 -> "NUMPAD4";
            case 325 -> "NUMPAD5";
            case 326 -> "NUMPAD6";
            case 327 -> "NUMPAD7";
            case 328 -> "NUMPAD8";
            case 329 -> "NUMPAD9";
            case 330 -> "NUMPADCOMMA";
            case 335 -> "NUMPADENTER";
            case 336 -> "NUMPADEQUALS";
            case 264 -> "DOWN";
            case 263 -> "LEFT";
            case 262 -> "RIGHT";
            case 265 -> "UP";
            case 334 -> "ADD";
            case 39 -> "APOSTROPHE";
            case 92 -> "BACKSLASH";
            case 44 -> "COMMA";
            case 61 -> "EQUALS";
            case 96 -> "GRAVE";
            case 91 -> "LBRACKET";
            case 45 -> "MINUS";
            case 332 -> "MULTIPLY";
            case 46 -> "PERIOD";
            case 93 -> "RBRACKET";
            case 59 -> "SEMICOLON";
            case 47 -> "SLASH";
            case 32 -> "SPACE";
            case 258 -> "TAB";
            case 342 -> "LALT";
            case 341 -> "LCONTROL";
            case 340 -> "LSHIFT";
            case 343 -> "LWIN";
            case 346 -> "RALT";
            case 345 -> "RCONTROL";
            case 344 -> "RSHIFT";
            case 347 -> "RWIN";
            case 257 -> "RETURN";
            case 256 -> "ESCAPE";
            case 259 -> "BACKSPACE";
            case 261 -> "DELETE";
            case 269 -> "END";
            case 268 -> "HOME";
            case 260 -> "INSERT";
            case 267 -> "PAGEDOWN";
            case 266 -> "PAGEUP";
            case 280 -> "CAPSLOCK";
            case 284 -> "PAUSE";
            case 281 -> "SCROLLLOCK";
            case 283 -> "PRINTSCREEN";
            case 0 -> "NONE";
            default -> "UNKNOWN";
        };
    }

    public static String[] getKeyNames() {
        return keyNames;
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
