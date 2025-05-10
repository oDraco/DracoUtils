package com.github.oDraco.entities.enums;

public enum DBCRace {
    HUMAN((byte) 0),
    SAIYAN((byte) 1),
    HALF_SAIYAN((byte) 2),
    NAMEKIAN((byte) 3),
    ARCOSIAN((byte) 4),
    MAJIN((byte) 5);

    private final byte ID;

    private DBCRace(byte id) {
        ID = id;
    }

    public static DBCRace fromID(byte ID) {
        for (DBCRace value : values()) {
            if (value.getID() == ID) return value;
        }
        return null;
    }

    public byte getID() {
        return ID;
    }
}
