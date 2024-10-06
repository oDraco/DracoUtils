package com.github.oDraco.entities.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SimpleInventory implements InventoryHolder {

    protected final Inventory inv;

    protected final Map<Integer, IIcon> iconMap = new HashMap<>();

    public SimpleInventory(String title, int size) {
        inv = Bukkit.createInventory(this, size, title);
    }

    @Deprecated
    public SimpleInventory(Inventory inventory) {
        inv = inventory;
    }

    public void setItem(int slot, IIcon icon) {
        if(slot < 0)
            return;
        iconMap.put(slot, icon);
        inv.setItem(slot, icon.getIcon());
    }

    public void removeItem(int slot) {
        iconMap.remove(slot);
        inv.setItem(slot, new ItemStack(Material.AIR));
    }

    public IIcon getIcon(int slot) {
        return iconMap.get(slot);
    }

    public Map<Integer, IIcon> getIconMap() {
        return iconMap;
    }

    public void updateInventory() {
        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, new ItemStack(Material.AIR));
            if(iconMap.containsKey(i))
                inv.setItem(i, iconMap.get(i).getIcon());
        }
    }

    public void clear() {
        iconMap.clear();
        inv.clear();
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
