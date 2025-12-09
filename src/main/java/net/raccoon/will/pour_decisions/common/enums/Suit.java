package net.raccoon.will.pour_decisions.common.enums;

public enum Suit {
    SPADES,
    HEARTS,
    CLUBS,
    DIAMONDS;

    public String getSuit() {
        return name().toLowerCase();
    }
}
