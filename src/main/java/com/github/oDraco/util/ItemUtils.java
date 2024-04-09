package com.github.oDraco.util;

import com.github.oDraco.entities.enums.Rarity;
import com.github.oDraco.entities.enums.Type;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ItemUtils {

    private static final String NULL_CHAR = "§r";

    /**
     * Format itemstack to an 'RPG' format.
     *
     * @param item   the item
     * @param name   the name
     * @param rarity the rarity
     * @param type   the type
     * @return the formatted item
     */
    public static ItemStack formatItem(ItemStack item, String name, Rarity rarity, Type type) {
        ItemStack i = item.clone();
        ItemMeta meta = i.getItemMeta();

        char nameColor = rarity == Rarity.COMMON ? 'f' : rarity.getColorCharacter();
        name = ("§" + nameColor + name).replace("&", "§");
        meta.setDisplayName(name);

        List<String> lore = new ArrayList<>();
        String typeLore = ("&7&o" + type.getDescription()).replace("&", "§");
        String rarityLore = ("&" + rarity.getColorCharacter() + rarity.getName()).replace("&", "§");

        lore.add(typeLore);
        lore.add(NULL_CHAR);
        lore.add(rarityLore);
        meta.setLore(lore);
        i.setItemMeta(meta);
        return i;
    }

    /**
     * Format item stack.
     *
     * @param item the item
     * @param name the name
     * @param lore the lore
     * @return the formatted item stack
     */
    public static ItemStack formatItem(ItemStack item, String name, String... lore) {
        ItemStack i = item.clone();
        ItemMeta meta = i.getItemMeta();
        if(lore != null) meta.setLore(Arrays.asList(lore));
        meta.setDisplayName(name);
        i.setItemMeta(meta);
        return i;
    }

    /**
     * Format tem stack.
     *
     * @param item the item
     * @param name the name
     * @param lore the lore
     * @return the formatted item stack
     */
    public static ItemStack formatItem(ItemStack item, String name, List<String> lore) {
        ItemStack i = item.clone();
        ItemMeta meta = i.getItemMeta();
        if(lore != null) meta.setLore(lore);
        meta.setDisplayName(name);
        i.setItemMeta(meta);
        return i;
    }

    /**
     * Parse item stack from string.
     * In format: MATERIAL:DAMAGE
     *
     * @param item the item string
     * @return the item stack
     */
    public static ItemStack parseItem(String item) {
        if(item == null || item.isEmpty()) return new ItemStack(Material.AIR);
        String[] fields = item.split(":");
        short damage = fields.length == 2 ? Short.parseShort(fields[1]) : 0;
        return new ItemStack(Material.getMaterial(fields[0]), 1, damage);
    }

    public static ItemStack setUnbreakable(ItemStack item, boolean unbreakable) {
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setBoolean("Unbreakable", unbreakable);
        return nbtItem.getItem();
    }
}
