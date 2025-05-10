package com.github.oDraco.entities.enums;

public enum AttributeOperation {
    ADD(0),
    MULTIPLY_BASE(1),
    MULTIPLY(2);

    private final int ID;

    AttributeOperation(int ID) {
        this.ID = ID;
    }

    public static AttributeOperation fromID(int id) {
        for (AttributeOperation op : values()) {
            if (op.getID() == id)
                return op;
        }
        return null;
    }

    public int getID() {
        return ID;
    }
}
