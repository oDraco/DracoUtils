package com.github.oDraco.util;

import com.github.oDraco.entities.enums.ArmourerSkinType;
import com.github.oDraco.entities.enums.Rarity;
import com.github.oDraco.entities.enums.Type;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        if(lore != null) meta.setLore(Arrays.stream(lore).map(x -> ChatColor.translateAlternateColorCodes('&', x)).collect(Collectors.toList()));
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
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
        if(lore != null) meta.setLore(lore.stream().map(x -> ChatColor.translateAlternateColorCodes('&', x)).collect(Collectors.toList()));
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
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


    /**
     * Gets Armourer's Workshop skin local id.
     *
     * @param item the item
     * @return skin local ID
     */
    public static int getArmourersLocalID(ItemStack item) {
        if(!isValid(item)) throw new IllegalArgumentException("Item can't be null or air");
        NBTItem i = new NBTItem(item);
        if(!i.hasTag("armourersWorkshop")) throw new IllegalStateException("Item doesn't have a Armourer's Workshop skin");
        return i.getCompound("armourersWorkshop").getCompound("identifier").getInteger("localId");
    }

    /**
     * Sets Armourer's Workshop to an item.
     *
     * @param item     the item
     * @param localID  the skin's local id
     * @param skinType the skin's type
     * @return the item with skin set
     */
    public static ItemStack setArmourersSkin(ItemStack item, int localID, ArmourerSkinType skinType) {
        if(!isValid(item)) throw new IllegalArgumentException("Item can't be null or air");
        NBTItem i = new NBTItem(item);

        i.removeKey("armourersWorkshop");

        NBTCompound armourersWorkshop = i.addCompound("armourersWorkshop");
        armourersWorkshop.setByte("lock", (byte) 1);
        armourersWorkshop.addCompound("dyeData");
        NBTCompound identifier = armourersWorkshop.addCompound("identifier");
        identifier.setInteger("globalId", 0);
        identifier.setInteger("localId", localID);
        identifier.setString("skinType", skinType.getId());

        return i.getItem();
    }

    public static boolean isValid(ItemStack item) {
        return (item != null && item.getType() != Material.AIR);
    }

    public static ItemStack setUnbreakable(ItemStack item, boolean unbreakable) {
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setBoolean("Unbreakable", unbreakable);
        return nbtItem.getItem();
    }
}
