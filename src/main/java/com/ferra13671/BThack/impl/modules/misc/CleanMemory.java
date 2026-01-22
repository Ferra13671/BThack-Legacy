package com.ferra13671.BThack.impl.modules.misc;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.utils.ChatUtils;
import com.ferra13671.BThack.core.client.ModuleList;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import net.minecraft.util.Formatting;

@ModuleInfo(name = "CleanMemory", description = "lang.module.CleanMemory", category = "MISC")
public class CleanMemory extends Module {

    @Override
    public void onEnable() {
        cleanMemory();
    }

    public static void cleanMemory() {
        Thread gcThread = new Thread(() -> {
            BThack.log("Memory cleaner thread started!");
            if (!Module.nullCheck())
                ChatUtils.sendMessage(Formatting.LIGHT_PURPLE + "Starting memory cleaning, please wait...");

            System.gc();

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException ignored) {}

            System.gc();
            if (!Module.nullCheck())
                ChatUtils.sendMessage(Formatting.LIGHT_PURPLE + "Memory clearing completed successfully!");

            BThack.log("Memory cleaner thread finished!");

            if (ModuleList.cleanMemory.isEnabled())
                ModuleList.cleanMemory.setEnabled(false);
        }, "MemoryCleaner GC Thread");
        gcThread.setDaemon(true);
        gcThread.start();
    }
}
