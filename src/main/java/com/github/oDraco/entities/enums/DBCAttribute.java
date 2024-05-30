package com.github.oDraco.entities.enums;

public enum DBCAttribute {
    STRENGTH("str"),
    DEXTERITY("dex"),
    CONSTITUTION("con"),
    WILL_POWER("wil"),
    MIND("mnd"),
    SPIRIT("spi");

    private final String acronym;

    DBCAttribute(String acronym) {
        this.acronym = acronym;
    }

    public String getAcronym() {
        return acronym;
    }

    public static DBCAttribute fromAcronym(String input) {
        for (DBCAttribute value : DBCAttribute.values()) {
            if(value.getAcronym().equalsIgnoreCase(input)) return value;
        }
        return null;
    }
}
