package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;

import java.util.ArrayList;

public class Language extends Module {

    public static ModeSetting language;

    public Language() {
        super("Language",
                "lang.module.Language",
                KeyboardUtils.RELEASE,
                MCategory.CLIENT,
                true
        );

        allowRemapKeyCode = false;

        language = new ModeSetting("Lang", this, new ArrayList<>(LanguageSystem.getLoadedLangs()));

        initSettings(
                language
        );
    }


    @Override
    public void onDisable() {
        setToggled(true);
    }
}
