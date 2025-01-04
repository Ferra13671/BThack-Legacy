package com.ferra13671.BThack.api.Managers.Memory;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import net.minecraft.util.Formatting;

public class CleanerThread implements Runnable, Mc {

   protected CleanerThread() {
   }

   public void run() {
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
         ModuleList.cleanMemory.setToggled(false);
      }
   }
}
