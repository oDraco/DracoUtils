package com.github.oDraco.entities.inventory;

import com.github.oDraco.DracoUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class SimpleInventory implements InventoryHolder {

    protected final Inventory inv;
    @Getter
    protected final Map<Integer, IIcon> iconMap = new HashMap<>();
    @Getter
    @Setter
    protected Sound sound;
    @Getter
    @Setter
    protected boolean playSound = true;

    public SimpleInventory(String title, int size) {
        inv = Bukkit.createInventory(this, size, title);
    }

    @Deprecated
    public SimpleInventory(Inventory inventory) {
        inv = inventory;
    }

    public void setItem(int slot, IIcon icon) {
        if (slot < 0)
            return;
        iconMap.put(slot, icon);
        updateSlot(slot);
    }

    public void setItemIf(int slot, IIcon icon, Predicate<SimpleInventory> condition) {
        if (condition.test(this))
            setItem(slot, icon);
    }

    public void removeItem(int slot) {
        iconMap.remove(slot);
        Runnable run = () -> inv.clear(slot);
        if (Bukkit.isPrimaryThread())
            run.run();
        else
            Bukkit.getScheduler().runTask(DracoUtils.getInstance(), run);
    }

    public IIcon getIcon(int slot) {
        return iconMap.get(slot);
    }

    public void updateInventory() {
        inv.clear();
        for (int i = 0; i < inv.getSize(); i++)
            updateSlot(i);
    }

    public void updateSlot(int slot) {
        inv.clear(slot);
        if (iconMap.containsKey(slot)) {
            IIcon icon = iconMap.get(slot);
            Runnable run = () -> {
                inv.setItem(slot, icon.getIcon());
                if (icon instanceof IIconAsync)
                    ((IIconAsync) icon).getIconAsync().thenAccept(x -> inv.setItem(slot, x));
            };
            if (Bukkit.isPrimaryThread())
                run.run();
            else
                Bukkit.getScheduler().runTask(DracoUtils.getInstance(), run);
        }
    }

    public void clear() {
        iconMap.clear();
        inv.clear();
    }

    public void openInventory(Player player) {
        player.closeInventory();
        player.openInventory(getInventory());
        if (playSound)
            player.playSound(player.getLocation(), sound == null ? Sound.NOTE_PLING : sound, 1.0f, 1.0f);
    }

    public void onOpen(InventoryOpenEvent event) {
    }

    public void onClose(InventoryCloseEvent event) {
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
