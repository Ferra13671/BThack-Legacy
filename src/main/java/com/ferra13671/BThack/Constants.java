package com.ferra13671.BThack;

import com.ferra13671.BThack.core.render.utils.ColorUtils;
import com.ferra13671.BThack.api.animation.Animation;
import com.ferra13671.BThack.api.animation.Easing;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.Random;

public final class Constants {
    public static final Random RANDOM = new Random();
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final String BTHACK_PREFIX = "[" + Formatting.BLUE + "BThack" + Formatting.RESET + "] ";
    public static final Logger BTHACK_LOGGER = LoggerFactory.getLogger("BThack");
    public static final String BTHACK_APP_ID = "1221431287852826676";

    public static final Identifier BLUR_IDENTIFIER = Identifier.ofVanilla("blur");

    public static final int GUISYSTEM_BUTTON_RECT_COLOR = ColorUtils.fastRGBA(0, 0, 0, 76);
    public static final int GUISYSTEM_BUTTON_HOVERED_LIGHT_COLOR = ColorUtils.fastRGBA(255,255,255, 178);

    public static final int CLICKGUI_BAR_HEIGHT = 12;
    public static final int CLICKGUI_FRAME_WIDTH = 100;
    public static final int CLICKGUI_FRAME_MOVE_STEP = 5;
    public static final int CLICKGUI_BUTTON_OUTLINE_COLOR = ColorUtils.fastRGBA(0, 0, 0, 100);
    public static final int CLICKGUI_BUTTON_HEIGHT = 14;
    public static final int CLICKGUI_SLIDER_ROUND_TO_PLACE_VALUE = 2;

    public static final int SCREEN_BACKGROUND_TABLE_COLOR = ColorUtils.fastRGBA(0,0,0,40);
    public static final double SCREEN_ACTIONBOT_TASK_OFFSET_FACTOR = 1.5;
    public static final int SCREEN_ACTIONBOT_MENU_COLOR = ColorUtils.fastRGBA(85, 85, 85, 255);

    public static final int FONT_RENDERER_DEFAULT_PAGE_SIZE = 256;
    public static final int FONT_RENDERER_DEFAULT_PADDING_BETWEEN_CHARS = 5;

    public static final Animation STANDARD_FLIP_ANIMATION = new Animation(Easing.LINEAR, 500);
}
