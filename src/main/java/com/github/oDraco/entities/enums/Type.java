package com.github.oDraco.entities.enums;

public enum Type {
    HEAVY_ARMOR("Armadura Pesada"),
    LIGHT_ARMOR("Armadura Leve"),
    SHORT_SWORD("Espada Curta"),
    LONG_SWORD("Espada Longa"),
    TOOL("Ferramenta"),
    RESOURCE("Recurso"),
    FOOD("Alimento"),
    CONSUMABLE("Consumível");

    private final String description;

    Type(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
