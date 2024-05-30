package com.github.oDraco.entities.enums;

public enum DBCSkill {

    FUSION("FZ"),
    JUMP("JP"),
    DASH("DS"),
    FLY("FL"),
    ENDURANCE("EN"),
    POTENTIAL_UNLOCK("OC"),
    KI_SENSE("KS"),
    MEDITATION("MD"),
    KAIOKEN("KK"),
    GOD_FORM("GF"),
    POTENTIAL_UNLEASHED("OK"),
    KI_PROTECTION("KP"),
    KI_FIST("KF"),
    KI_BOOST("KB"),
    DEFENSE_PENETRATION("DF"),
    KI_INFUSE("KI"),
    ULTRA_INSTINCT("UI"),
    INSTANT_TRANSMISSION("IT"),
    GOD_OF_DESTRUCTION("GD");

    private final String symbol;

    DBCSkill(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    /**
     * Returns the skill by its symbol
     * Ignores case.
     *
     * @param symbol the symbol ("KK", "UI", ...)
     * @return the dbc skill, or null if non-existent
     */
    public static DBCSkill fromSymbol(String symbol) {
        for (DBCSkill skill : DBCSkill.values()) {
            if(skill.symbol.equalsIgnoreCase(symbol)) return skill;
        }
        return null;
    }
}
