package org.github.oDraco.io.entities.enums;

public enum Rarity {

    COMMON('7', "Comum"),
    UNCOMMON('2', "Incomum"),
    RARE('9', "Raro"),
    EPIC('5', "Épico"),
    LEGENDARY('e', "Lendário"),
    MYTHIC('c', "Mítico"),
    EXCLUSIVE('6', "Exclusivo"),
    UNIQUE('b', "Único");

    private final char colorCharacter;
    private final String name;

    Rarity(char colorCharacter, String name) {
        this.colorCharacter = colorCharacter;
        this.name = name;
    }

    public char getColorCharacter() {
        return colorCharacter;
    }

    public String getName() {
        return name;
    }
}
