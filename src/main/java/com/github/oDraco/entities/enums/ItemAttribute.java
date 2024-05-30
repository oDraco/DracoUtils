package com.github.oDraco.entities.enums;

public enum ItemAttribute {
    MAX_HEALTH("generic.maxHealth"),
    FOLLOW_RANGE("generic.followRange"),
    KNOCKBACK_RESISTANCE("generic.knockbackResistance"),
    MOVEMENT_SPEED("generic.movementSpeed"),
    FLYING_SPEED("generic.flyingSpeed"),
    ATTACK_DAMAGE("generic.attackDamage");

    private final String ID;

    ItemAttribute(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }
}
