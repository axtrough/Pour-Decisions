package net.raccoon.will.pour_decisions.common.enums;

public enum ChipType {
    WHITE(1, 1),
    PINK(2.5f, 2),
    RED(5, 3),
    BLUE(10, 4),
    GREEN(25, 5),
    BLACK(100, 6),
    PURPLE(500, 7);

    private final float value;
    private final int color;

    ChipType(float value, int color) {
        this.value = value;
        this.color = color;
    }

    public float getValue() {
        return value;
    }

    public int getColor() {
        return color;
    }
}
