package com.github.oDraco.entities.enums;

public enum ArmourerSkinType {

    SWORD("armourers:sword"),
    BLOCK("armourers:block"),
    HEAD("armourers:head"),
    CHEST("armourers:chest"),
    LEGS("armourers:legs"),
    FEET("armourers:feet"),
    ITEM("armourers:item"),
    WINGS("armourers:wings"),
    SHIELD("armourers:shield"),
    HOE("armourers:hoe"),
    SHOVEL("armourers:shovel"),
    BOW("armourers:bow"),
    PICKAXE("armourers:pickaxe"),
    AXE("armourers:axe"),
    OUTFIT("armourers:outfit");

    private final String id;

    ArmourerSkinType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
