package com.github.oDraco.entities.enums;

import lombok.Getter;

@Getter
public enum Type {
    HEAVY_ARMOR("Armadura Pesada"),
    LIGHT_ARMOR("Armadura Leve"),
    ARMOR("Armadura"),
    SHORT_SWORD("Espada Curta"),
    LONG_SWORD("Espada Longa"),
    SWORD("Espada"),
    SPEAR("Lança"),
    MACE("Maça"),
    MELEE_WEAPON("Arma Corpo a Corpo"),
    RANGED_WEAPON("Arma à Distância"),
    TOOL("Ferramenta"),
    RESOURCE("Recurso"),
    INGREDIENT("Ingrediente"),
    FOOD("Alimento"),
    ACCESSORY("Acessório"),
    EQUIPMENT("Equipamento"),
    CONSUMABLE("Consumível");

    private final String description;

    Type(String description) {
        this.description = description;
    }

}
