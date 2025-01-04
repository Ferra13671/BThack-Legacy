package com.ferra13671.BThack.api.Managers.Memory;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import net.minecraft.util.Formatting;

public class MemoryManager implements Mc {

   public void cleanMemory() {
      if (ModuleList.memoryCleaner.isEnabled()) {
         Runnable runnable = new CleanerThread();
         Thread gcThread = new Thread(runnable, "MemoryCleaner GC Thread");
         gcThread.setDaemon(true);
         gcThread.start();
      } else {
         if (mc.player != null && mc.world != null) {
            ChatUtils.sendMessage(Formatting.YELLOW + "Memory Cleaner module is disabled, please enable it before next use.");
            if (ModuleList.cleanMemory.isEnabled()) {
               ModuleList.cleanMemory.setToggled(false);
            }
         }
      }
   }
}
