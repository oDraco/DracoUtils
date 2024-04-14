package com.github.oDraco.entities;

import com.github.oDraco.entities.enums.Attribute;

import java.util.Objects;
import java.util.regex.Pattern;

public class AttributeBonus {

    private final static Pattern VALUE_REGEX = Pattern.compile("[+\\-*/]\\d+(?!.\\D)|nbt_");

    private final Attribute attribute;
    private final String name;
    private char type;
    private int amount;


    public AttributeBonus(Attribute attribute, String name, String value) {
        if(!VALUE_REGEX.matcher(value).matches()) throw new IllegalArgumentException(value + " is a invalid value. Examples of valid values: +1, -1, *1, /1, *nbt_jrmcAlign");
        this.attribute = attribute;
        this.name = name;
        this.type = value.charAt(0);
        this.amount = Integer.parseInt(value.substring(1));
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return ""+type+amount;
    }

    public void setValue(String value) {
        if(!VALUE_REGEX.matcher(value).matches()) throw new IllegalArgumentException(value + " is a invalid value. Examples of valid values: +1, -1, *1, /1, *nbt_jrmcAlign");
        this.type = value.charAt(0);
        this.amount = Integer.parseInt(value.substring(1));
    }

    public static AttributeBonus fromString(Attribute attr, String input) {
        String[] fields = input.split(";");
        if(fields.length != 2) throw new IllegalArgumentException(input + " is a invalid format. Example format: bonus;*2");
        return new AttributeBonus(attr, fields[0], fields[1]);
    }

    @Override
    public String toString() {
        return name+";"+getValue();
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
