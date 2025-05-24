package com.ferra13671.BThack.api.Utils;

import net.minecraft.util.PlayerInput;

@SuppressWarnings("DataFlowIssue")
public final class InputUtils implements Mc {

    public static void setForward(boolean forward) {
        PlayerInput input = mc.player.input.playerInput;
        mc.player.input.playerInput = new PlayerInput(forward, input.backward(), input.left(), input.right(), input.jump(), input.sneak(), input.sprint());
    }

    public static void setBackward(boolean backward) {
        PlayerInput input = mc.player.input.playerInput;
        mc.player.input.playerInput = new PlayerInput(input.forward(), backward, input.left(), input.right(), input.jump(), input.sneak(), input.sprint());
    }

    public static void setLeft(boolean left) {
        PlayerInput input = mc.player.input.playerInput;
        mc.player.input.playerInput = new PlayerInput(input.forward(), input.backward(), left, input.right(), input.jump(), input.sneak(), input.sprint());
    }

    public static void setRight(boolean right) {
        PlayerInput input = mc.player.input.playerInput;
        mc.player.input.playerInput = new PlayerInput(input.forward(), input.backward(), input.left(), right, input.jump(), input.sneak(), input.sprint());
    }

    public static void setJumping(boolean jumping) {
        PlayerInput input = mc.player.input.playerInput;
        mc.player.input.playerInput = new PlayerInput(input.forward(), input.backward(), input.left(), input.right(), jumping, input.sneak(), input.sprint());
    }

    public static void setSneaking(boolean sneaking) {
        PlayerInput input = mc.player.input.playerInput;
        mc.player.input.playerInput = new PlayerInput(input.forward(), input.backward(), input.left(), input.right(), input.jump(), sneaking, input.sprint());
    }

    public static void setSprinting(boolean sprinting) {
        PlayerInput input = mc.player.input.playerInput;
        mc.player.input.playerInput = new PlayerInput(input.forward(), input.backward(), input.left(), input.right(), input.jump(), input.sneak(), sprinting);
    }

    public static void setInput(boolean forward, boolean backward, boolean left, boolean right, boolean jump, boolean sneak, boolean sprint) {
        mc.player.input.playerInput = new PlayerInput(forward, backward, left, right, jump, sneak, sprint);
    }
}
