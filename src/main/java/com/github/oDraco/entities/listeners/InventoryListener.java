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
        if (e.getInventory() == null) return;
        if (!(e.getInventory().getHolder() instanceof SimpleInventory)) return;

        e.setCancelled(true);

        SimpleInventory holder = (SimpleInventory) e.getInventory().getHolder();
        Map<Integer, IIcon> iconMap = holder.getIconMap();
        if (iconMap.containsKey(e.getRawSlot()))
            iconMap.get(e.getRawSlot()).onClick(e);
    }
}
