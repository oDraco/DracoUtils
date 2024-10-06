package com.github.oDraco.entities.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class BasicIconImpl implements IIcon {

    private final ItemStack item;

    public BasicIconImpl(ItemStack item) {
        this.item = item;
    }


    @Override
    public void onClick(InventoryClickEvent event) {}

    @Override
    public ItemStack getIcon() {
        return item;
    }
}
