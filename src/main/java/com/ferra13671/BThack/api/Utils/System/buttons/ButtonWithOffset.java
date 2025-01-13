package com.ferra13671.BThack.api.Utils.System.buttons;

public class ButtonWithOffset extends Button {

    private int offset;

    public ButtonWithOffset(int id, int x, int y, int width, int height, String text) {
        super(id, x, y, width, height, text);
    }

    public void setOffset(int newOffset) {
        offset = newOffset;
    }

    @Override
    public int getCenterY() {
        return (int) centerY + ((offset * (getHeight() * 2)));
    }
}
