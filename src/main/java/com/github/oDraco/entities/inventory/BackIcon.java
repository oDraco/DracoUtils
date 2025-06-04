package com.github.oDraco.entities.inventory;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class BackIcon extends BasicIconImpl {

    private final SimpleInventory menu;
    @Getter
    @Setter
    private boolean updateInventory = true;

    public BackIcon(ItemStack item, SimpleInventory menu) {
        super(item);
        this.menu = menu;
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        if (updateInventory)
            menu.updateInventory();
        menu.openInventory(((Player) e.getWhoClicked()));
    }

}
