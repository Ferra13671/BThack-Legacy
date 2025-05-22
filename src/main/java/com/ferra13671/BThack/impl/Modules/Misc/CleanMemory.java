package com.ferra13671.BThack.impl.Modules.Misc;

import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;

@ModuleInfo(name = "CleanMemory", description = "lang.module.CleanMemory", category = "MISC")
public class CleanMemory extends Module {

    @Override
    public void onEnable() {
        Managers.MEMORY_MANAGER.cleanMemory();
    }
}
