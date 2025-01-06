package com.ferra13671.BTbot.impl.AntiAFK.Check;

public class CheckForRadiusIntersection {
    public static boolean correct = false;
    public static void check(double checkX, double checkZ,double centerX, double centerZ, double radius) {
        boolean correctX;
        boolean correctZ;
        double xMax = centerX + radius;
        double zMax = centerZ + radius;
        double xMin = centerX - radius;
        double zMin = centerZ - radius;
        if (checkX < xMax) {
            correctX = true;
        } else if (checkX > xMin) {
            correctX = true;
        } else {
            correctX = false;
        }
        if (checkZ < zMax) {
            correctZ = true;
        } else if (checkZ > zMin) {
            correctZ = true;
        } else {
            correctZ = false;
        }
        correct = correctX && correctZ;
    }
}
