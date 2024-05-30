package com.github.oDraco.util;

import com.github.oDraco.entities.enums.*;
import de.tr7zw.nbtapi.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class ItemUtils {

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
        lore.add("");
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
     * @param id  the skin's ID
     * @param skinType the skin's type
     * @param global if true, use global ID instead
     * @return the item with skin set
     */
    public static ItemStack setArmourersSkin(ItemStack item, int id, ArmourerSkinType skinType, boolean global) {
        if(!isValid(item)) throw new IllegalArgumentException("Item can't be null or air");
        NBTItem i = new NBTItem(item);

        i.removeKey("armourersWorkshop");

        NBTCompound armourersWorkshop = i.addCompound("armourersWorkshop");
        armourersWorkshop.setByte("lock", (byte) 1);
        armourersWorkshop.addCompound("dyeData");
        NBTCompound identifier = armourersWorkshop.addCompound("identifier");
        identifier.setInteger("globalId", global ? id : 0);
        identifier.setInteger("localId", !global ? id : 0);
        identifier.setString("skinType", skinType.getId());

        return i.getItem();
    }

    /**
     * Check if an item is non-null and non-air
     *
     * @param item the itemstack for check
     * @return true if the item is valid
     */
    public static boolean isValid(ItemStack item) {
        return (item != null && item.getType() != Material.AIR);
    }

    /**
     * Apply's the unbreakable tag to an item
     *
     * @param item the itemstack
     * @param unbreakable true/false
     * @return a new itemstack with the unbreakable tag set
     */
    public static ItemStack setUnbreakable(ItemStack item, boolean unbreakable) {
        if(!isValid(item)) throw new IllegalArgumentException("Item can't be null or air");
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setBoolean("Unbreakable", unbreakable);
        return nbtItem.getItem();
    }

    /**
     * Add an attribute modifier to an item
     *
     * @param item the itemstack to receive the attribute
     * @param attributeName the attribute's name, don't mistake with the name below. This is more like the 'ID' of the attribute. Like 'generic.attackDamage'
     * @param name attribute's name
     * @param amount attribute's value
     * @param operation attribute's operation
     * @return the itemstack with attribute set
     */
    public static ItemStack addAttribute(ItemStack item, String attributeName, String name, Double amount, AttributeOperation operation) {
        if(!isValid(item)) throw new IllegalArgumentException("Item can't be null or air");
        NBTItem i = new NBTItem(item);
        NBTCompoundList attributeModifiers = i.getCompoundList("AttributeModifiers");
        for (de.tr7zw.nbtapi.iface.ReadWriteNBT attributeModifier : attributeModifiers) {
            if (attributeModifier.getString("Name").equals(name))
                throw new IllegalStateException("Item already have a attribute with the name: " + name);
        }
        NBTListCompound attribute = i.getCompoundList("AttributeModifiers").addCompound();
        attribute.setInteger("Operation", operation.getID());
        UUID uuid = UUID.randomUUID();
        attribute.setLong("UUIDLeast", uuid.getLeastSignificantBits());
        attribute.setLong("UUIDMost", uuid.getMostSignificantBits());
        attribute.setDouble("Amount", amount);
        attribute.setString("AttributeName", attributeName);
        attribute.setString("Name", name);
        return i.getItem();
    }

    /**
     * Add an attribute modifier to an item
     *
     * @param item the itemstack to receive the attribute
     * @param attribute the attribute
     * @param name attribute's name
     * @param amount attribute's value
     * @return the itemstack with attribute set
     */
    public static ItemStack addAttribute(ItemStack item, ItemAttribute attribute, String name, Double amount) {
        return addAttribute(item, attribute.getID(), name, amount, AttributeOperation.ADD);
    }

    /**
     * Add an attribute modifier to an item
     *
     * @param item the itemstack to receive the attribute
     * @param attribute the attribute
     * @param name attribute's name
     * @param amount attribute's value
     * @param operation attribute's operation
     * @return the itemstack with attribute set
     */
    public static ItemStack addAttribute(ItemStack item, ItemAttribute attribute, String name, Double amount, AttributeOperation operation) {
        return addAttribute(item, attribute.getID(), name, amount, operation);
    }


    /**
     * Apply glow effect to the item stack.
     *
     * @param item the item
     * @return the item stack with glow effect
     */
    public static ItemStack applyGlow(ItemStack item) {
        NBTItem i = new NBTItem(item);
        NBTCompoundList ench = i.getCompoundList("ench");
        NBTListCompound compound = ench.addCompound();
        compound.setShort("id", (short)127);
        compound.setShort("lvl", (short)0);
        return i.getItem();
    }
}
