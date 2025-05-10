package com.github.oDraco.entities.listeners;

import com.github.oDraco.entities.inventory.IIcon;
import com.github.oDraco.entities.inventory.SimpleInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.Map;

public class InventoryListener implements Listener {

    @EventHandler
    public void inventoryDrag(InventoryDragEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof SimpleInventory)
            e.setCancelled(true);
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (!(e.getClickedInventory().getHolder() instanceof SimpleInventory)) return;

        e.setCancelled(true);

        SimpleInventory holder = (SimpleInventory) e.getClickedInventory().getHolder();
        Map<Integer, IIcon> iconMap = holder.getIconMap();
        if (iconMap.containsKey(e.getSlot()))
            iconMap.get(e.getSlot()).onClick(e);
    }
}
