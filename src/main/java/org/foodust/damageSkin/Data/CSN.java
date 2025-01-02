package org.foodust.damageSkin.Data;

public enum CSN {
    MATERIAL,
    MAIN,
    ITEMS,
    NAME,
    DESCRIPTION,
    DATA,
    AMOUNT,
    SLOT,
    MYTHIC,
    COMMAND,
    COOL_TIME,
    COOL_TIME_DATA,
    SPAWN,
    BLOCK,
    ITEM,
    SUB,
    PLAYER,
    DEATH,
    SIZE,
    MODEL,
    ACTION,
    IN,
    OUT,
    SPEED,
    DURATION,
    FORCE,
    ROTATE,
    PASSENGER,
    ANIMATION,
    WEAPON,
    ACTION_BAR,
    COOL_TIME_ACTION_BAR,
    ;

    public String getLower() {
        return this.name().toLowerCase();
    }
}
