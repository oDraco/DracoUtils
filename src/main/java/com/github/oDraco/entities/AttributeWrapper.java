package com.github.oDraco.entities;

import com.github.oDraco.entities.enums.AttributeOperation;
import com.github.oDraco.entities.enums.ItemAttribute;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;

import java.util.UUID;

public class AttributeWrapper {

    private String name;
    private String attributeName;
    private AttributeOperation operation;
    private double value;
    private long least;
    private long most;

    private AttributeWrapper(String name, String attributeName, AttributeOperation operation, double value) {
        this.name = name;
        this.attributeName = attributeName;
        this.operation = operation;
        this.value = value;
    }

    public AttributeWrapper(String name, String attributeName, AttributeOperation operation, double value, long least, long most) {
        this(name, attributeName, operation, value);
        this.least = least;
        this.most = most;
    }

    public AttributeWrapper(String name, String attributeName, AttributeOperation operation, double value, UUID uuid) {
        this(name, attributeName, operation, value, uuid.getLeastSignificantBits(), uuid.getMostSignificantBits());
    }

    public AttributeWrapper(String name, ItemAttribute attribute, AttributeOperation operation, double value, UUID uuid) {
        this(name, attribute.getID(), operation, value, uuid.getLeastSignificantBits(), uuid.getMostSignificantBits());
    }

    public AttributeWrapper(String name, ItemAttribute attribute, AttributeOperation operation, double value) {
        this(name, attribute.getID(), operation, value);
        UUID uuid = UUID.randomUUID();
        least = uuid.getLeastSignificantBits();
        most = uuid.getMostSignificantBits();
    }

    public AttributeWrapper(String name, ItemAttribute attribute, double value) {
        this(name, attribute.getID(), AttributeOperation.ADD, value);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AttributeOperation getOperation() {
        return operation;
    }

    public void setOperation(AttributeOperation operation) {
        this.operation = operation;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public ReadWriteNBT getCompound() {
        ReadWriteNBT nbt = NBT.createNBTObject();
        nbt.setInteger("Operation", operation.getID());
        nbt.setLong("UUIDLeast", least);
        nbt.setLong("UUIDMost", most);
        nbt.setDouble("Amount", value);
        nbt.setString("AttributeName", attributeName);
        nbt.setString("Name", name);
        return nbt;
    }
}
