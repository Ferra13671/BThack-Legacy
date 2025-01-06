package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.Memory.MemoryManager;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class CleanMemory extends Module {
    public CleanMemory() {
        super("CleanMemory",
                "lang.module.CleanMemory",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );
    }

    @Override
    public void onEnable() {
        Managers.MEMORY_MANAGER.cleanMemory();
    }
}
