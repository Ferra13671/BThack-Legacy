package com.ferra13671.BThack.api.Events;

import com.ferra13671.MegaEvents.Base.Event;
import net.minecraft.client.util.InputUtil;

public class InputEvent extends Event {
    protected int keyCode;
    public InputEvent() {
    }

    public static class KeyInputEvent extends InputEvent {
        private final Action action;
        private final InputUtil.Key key;

        public KeyInputEvent(int keyCode, InputUtil.Key key, Action action) {
            this.keyCode = keyCode;
            this.key = key;
            this.action = action;
        }

        public Action getAction() {
            return this.action;
        }

        public InputUtil.Key getKey() {
            return this.key;
        }

        public int getKeyCode() {
            return keyCode;
        }

        public enum Action {
            PRESS,
            RELEASE,
            JAMMED
        }
    }

    public static class MouseInputEvent extends InputEvent {
        private final int button;
        private final int action;

        public MouseInputEvent(int button, int action) {
            this.button = button;
            this.action = action;
        }

        public int getButton() {
            return button;
        }

        public int getAction() {
            return action;
        }
    }
}