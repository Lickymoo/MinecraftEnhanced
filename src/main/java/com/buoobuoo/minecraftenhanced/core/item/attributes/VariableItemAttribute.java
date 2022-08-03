package com.buoobuoo.minecraftenhanced.core.item.attributes;

public abstract class VariableItemAttribute extends ItemAttribute {
    protected final double value;

    public VariableItemAttribute(String name, String id, double value) {
        super(name, id, 0, 0);
        this.value = value;
    }

    @Override
    public double roll(){
        return value;
    }
}
