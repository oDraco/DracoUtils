package com.github.oDraco.entities.inventory;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SimpleInventory implements InventoryHolder {

    protected final Inventory inv;

    @Getter
    @Setter
    protected Sound sound;
    @Getter
    @Setter
    protected boolean playSound = true;

    @Getter
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
        if(icon instanceof IIconAsync)
            ((IIconAsync) icon).getIconAsync().thenAccept(x -> inv.setItem(slot, x));
        else
            inv.setItem(slot, icon.getIcon());
    }

    public void removeItem(int slot) {
        iconMap.remove(slot);
        inv.setItem(slot, new ItemStack(Material.AIR));
    }

    public IIcon getIcon(int slot) {
        return iconMap.get(slot);
    }

    public void updateInventory() {
        inv.clear();
        for (int i = 0; i < inv.getSize(); i++) {
            if(iconMap.containsKey(i)) {
                IIcon icon = iconMap.get(i);
                inv.setItem(i, icon.getIcon());
                if(icon instanceof IIconAsync) {
                    int finalI = i;
                    ((IIconAsync) icon).getIconAsync().thenAccept(x -> inv.setItem(finalI,x));
                }
            }
        }
    }

    public void clear() {
        iconMap.clear();
        inv.clear();
    }

    public void openInventory(Player player) {
        player.closeInventory();
        player.openInventory(getInventory());
        if(playSound)
            player.playSound(player.getLocation(), sound == null ? Sound.NOTE_PLING : sound, 1.0f, 1.0f);
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
