package com.ferra13671.BThack.api.Managers.managers;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.Initializable;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import net.minecraft.util.Formatting;

public class MemoryManager implements Initializable, Mc {

   @Override
   public void init() {
      //no action
   }

   public void cleanMemory() {
      if (ModuleList.memoryCleaner.isEnabled()) {
         Thread gcThread = new Thread(() -> {
            BThack.log("Memory cleaner thread started!");
            if (ModuleList.memoryCleaner.showMessages.getValue() && mc.player != null && mc.world != null) {
               ChatUtils.sendMessage(Formatting.LIGHT_PURPLE + "Starting memory cleaning, please wait...");
            }

            System.gc();

            try {
               Thread.sleep(1000L);
            } catch (InterruptedException ignored) {}

            System.gc();
            if (ModuleList.memoryCleaner.showMessages.getValue()) {
               ChatUtils.sendMessage(Formatting.LIGHT_PURPLE + "Memory clearing completed successfully!");
            }

            BThack.log("Memory cleaner thread finished!");

            if (ModuleList.cleanMemory.isEnabled()) {
               ModuleList.cleanMemory.setEnabled(false);
            }
         }, "MemoryCleaner GC Thread");
         gcThread.setDaemon(true);
         gcThread.start();
      } else {
         if (mc.player != null && mc.world != null) {
            ChatUtils.sendMessage(Formatting.YELLOW + "Memory Cleaner module is disabled, please enable it before next use.");
            if (ModuleList.cleanMemory.isEnabled()) {
               ModuleList.cleanMemory.setEnabled(false);
            }
         }
      }
   }
}
