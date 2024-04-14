package com.github.oDraco.entities.enums;

public enum Attribute {
    STRENGTH("str"),
    DEXTERITY("dex"),
    CONSTITUTION("con"),
    WILL_POWER("wil"),
    MIND("mnd"),
    SPIRIT("spi");

    private final String acronym;

    Attribute(String acronym) {
        this.acronym = acronym;
    }

    public String getAcronym() {
        return acronym;
    }

    public static Attribute fromAcronym(String input) {
        for (Attribute value : Attribute.values()) {
            if(value.getAcronym().equalsIgnoreCase(input)) return value;
        }
        return null;
    }
}
