package com.ferra13671.BTbot.impl.AntiAFK.Start;


import com.ferra13671.BTbot.api.Utils.Generate.GenerateNumber;
import com.ferra13671.BTbot.api.Utils.Motion.Goto.CollisionAction;
import com.ferra13671.BTbot.impl.AntiAFK.Doing.ActivateHand;
import com.ferra13671.BTbot.api.Utils.Motion.Goto.Goto;
import com.ferra13671.BTbot.impl.AntiAFK.Doing.GotoN.PrepareGotoN;
import com.ferra13671.BTbot.impl.AntiAFK.Doing.Jump;
import com.ferra13671.BTbot.impl.AntiAFK.Doing.Nothing;
import com.ferra13671.BTbot.impl.AntiAFK.Doing.SendToChat;

public class StartAntiAFKThread extends Thread{
    public static boolean startDoing = false;

    PrepareGotoN prepareGotoN;
    Goto gotoN;

    public void run() {
        if (!StartAntiAFK.active) return;
        if (startDoing) return;

        reset();

        byte a = (byte) GenerateNumber.generateInt(0,4);
        if (a == 0) {
            new Nothing().start();
        } else if (a == 1) {
            prepareGotoN = new PrepareGotoN();
            prepareGotoN.prepare();

            gotoN = new Goto(prepareGotoN.newX, prepareGotoN.newZ, CollisionAction.JUMPING);
            gotoN.setPostAction(() -> {
                StartAntiAFKThread.startDoing = false;
                new Nothing().start();
            });
            gotoN.start();
        } else if (a == 2) {
            new ActivateHand().start();
        } else if (a == 3) {
            new Jump().start();
        } else if (a == 4) {
            new SendToChat().start();
        }
    }

    private static void reset() {
        SendToChat.number = 0;
    }
}
