package com.github.oDraco.entities.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PageChangeIconImpl implements IIcon {

    private final ItemStack icon;
    private final PaginatedInventory parent;
    private final int value;

    public PageChangeIconImpl(PaginatedInventory parent, ItemStack itemStack, int value) {
        icon = itemStack;
        this.parent = parent;
        this.value = value;
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        parent.setCurrentPage(parent.getCurrentPage()+value);
    }

    @Override
    public ItemStack getIcon() {
        return icon;
    }
}
