package com.ferra13671.BThack.core;

import com.ferra13671.BThack.BThack;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DeviceSystem {
    private static LaunchDevice launchDevice = LaunchDevice.PC;

    public static void check() {
        try {
            /*
            When trying to get FontMetrics on the phone, a HeadlessException will be thrown because Java will not be able to get the display information.
             */
            @SuppressWarnings("unused")
            FontMetrics metrics = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB).getGraphics().getFontMetrics();
        } catch (HeadlessException exception) {
            launchDevice = LaunchDevice.PHONE;
        }
        BThack.log("Launch device: " + launchDevice.name());
    }

    public static LaunchDevice getLaunchDevice() {
        return launchDevice;
    }

    public enum LaunchDevice {
        PC,
        PHONE
    }
}
