package com.github.oDraco.entities.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public interface IIcon {

    void onClick(InventoryClickEvent event);

    ItemStack getIcon();
}
