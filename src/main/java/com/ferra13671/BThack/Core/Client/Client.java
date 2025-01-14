package com.ferra13671.BThack.Core.Client;


import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Category.Categories;
import com.ferra13671.BThack.api.Category.Category;
import com.ferra13671.BThack.api.IMixin.ModifyWindow;
import com.ferra13671.BThack.api.Managers.ColourTheme.ColorTheme;
import com.ferra13671.BThack.api.HudComponent.HudComponent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.DataList.DataLists;

import java.text.SimpleDateFormat;
import java.util.*;

public final class Client implements Mc {
    public static final ClientInfo clientInfo = new ClientInfo();

    static final ArrayList<Module> modules = new ArrayList<>();
    /** Only needed for optimized tick and render cycle */
    public static final ArrayList<HudComponent> hudComponents = new ArrayList<>();

    public static boolean inited = false;

    public static void startup() {
        updateTitle();

        InitializeHelper.initCustomCategories();

        ModuleList.initModules();
        BThack.log("All modules have been initialized! Number of modules: " + getAllModules().size());

        DataLists.init();

        Managers.COMMAND_MANAGER.init();

        InitializeHelper.initManagers();

        InitializeHelper.initLibraries();

        InitializeHelper.initSystems();

        inited = true;
    }

    public static void updateTitle() {
        ((ModifyWindow) (Object) mc.getWindow()).updateTitle();
    }



    public static ArrayList<Module> getModulesInCategory(Category c) {
        ArrayList<Module> mods = new ArrayList<>();
        for (Module m : modules) {
            if (m.getCategory().name().equalsIgnoreCase(c.name())) {
                mods.add(m);
            }
        }
        return mods;
    }

    /**
     * This method should be used as a last resort when it is impossible to get a module via ModuleList.
     * In any other cases, this method will be less productive than obtaining the module directly.
     */
    public static Module getModuleByName(String name) {
        return modules.stream()
                .filter(module -> module.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }


    public static void keyPress(int key) {
        for (Module m : modules) {
            if (m.getKey() == key) {
                m.toggle();
            }
        }
    }


    public static void addCTheme(String name, int frontColour, int backgroundFontColour, int backgroundFontHoveredColour, int moduleEnabledColour, int moduleDisabledColour, int arrayListColour) {
        Managers.COLOR_THEME_MANAGER.addColorTheme(new ColorTheme(name, frontColour, backgroundFontColour, backgroundFontHoveredColour, moduleEnabledColour, moduleDisabledColour, arrayListColour));
    }

    public static String getRealTime(String format) {
        return format.equals("12") ? new SimpleDateFormat("h:mm").format(new Date()) : new SimpleDateFormat("k:mm").format(new Date());
    }

    public static boolean isOptionActivated(Module module, BooleanSetting setting) {
        return module.isEnabled() && setting.getValue();
    }

    public static List<Module> getAllModules() {
        List<Module> tempModules = new ArrayList<>(modules);
        tempModules.removeIf(module -> module.getCategory() == Categories.HUD);
        return tempModules;
    }
}
