package com.ferra13671.BThack.api.Utils;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Interfaces.Mc;
import net.minecraft.util.Util;

public final class DesktopUtils implements Mc {

    public static void openURI(String link) {
        try {
            Util.getOperatingSystem().open(link);
        } catch (Exception e) {
            BThack.log("An error occurred while trying to open the '" + link + "' link!");
        }
    }
}
