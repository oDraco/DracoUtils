package com.github.oDraco.entities.enums;

public enum DBCAttribute {
    STRENGTH(0, "str", "Strength"),
    DEXTERITY(1, "dex", "Dexterity"),
    CONSTITUTION(2, "con", "Constitution"),
    WILL_POWER(3, "wil", "Will Power"),
    MIND(4, "mnd", "Mind"),
    SPIRIT(5, "spi", "Spirit");

    private final int index;
    private final String acronym;
    private final String name;

    DBCAttribute(int index, String acronym, String name) {
        this.index = index;
        this.acronym = acronym;
        this.name = name;
    }

    public static DBCAttribute fromString(String input) {
        DBCAttribute attr = fromAcronym(input);
        if (attr == null)
            try {
                attr = valueOf(input.toUpperCase());
            } catch (Exception e) {
                return null;
            }
        return attr;
    }

    public static DBCAttribute fromAcronym(String input) {
        for (DBCAttribute value : DBCAttribute.values()) {
            if (value.getAcronym().equalsIgnoreCase(input)) return value;
        }
        return null;
    }

    public String getAcronym() {
        return acronym;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }
}
