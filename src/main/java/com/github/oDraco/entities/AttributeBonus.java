package com.github.oDraco.entities;

import com.github.oDraco.entities.enums.Attribute;

import java.util.Objects;

public class AttributeBonus {

    private final Attribute attribute;
    private final String name;
    private String value;


    public AttributeBonus(Attribute attribute, String name, String value) {
        this.attribute = attribute;
        this.name = name;
        this.value = value;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static AttributeBonus fromString(Attribute attr, String input) {
        String[] fields = input.split(";");
        if(fields.length != 2) throw new IllegalArgumentException("Invalid format. Example format: bonus;*2");
        return new AttributeBonus(attr, fields[0], fields[1]);
    }

    @Override
    public String toString() {
        return name+";"+value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttributeBonus that = (AttributeBonus) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
