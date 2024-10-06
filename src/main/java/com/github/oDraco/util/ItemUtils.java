package com.github.oDraco.util;

import com.github.oDraco.entities.AttributeWrapper;
import com.github.oDraco.entities.enums.*;
import de.tr7zw.nbtapi.*;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
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
    public static ItemStack formatItem(@Nonnull ItemStack item, @Nonnull String name, @Nonnull Rarity rarity, @Nonnull Type type, String... additionalLore) {
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
        if(additionalLore != null && additionalLore.length > 0) {
            List<String> extraLore = Arrays.stream(additionalLore).map(x -> x.replace('&', '§')).collect(Collectors.toList());
            lore.addAll(extraLore);
            lore.add("");
        }
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
    public static ItemStack formatItem(@Nonnull ItemStack item, String name, String... lore) {
        return formatItem(item, name, Arrays.asList(lore));
    }

    /**
     * Format tem stack.
     *
     * @param item the item
     * @param name the name
     * @param lore the lore
     * @return the formatted item stack
     */
    public static ItemStack formatItem(@Nonnull ItemStack item, String name, @Nullable List<String> lore) {
        if(item.getType() == Material.AIR)
            return item;
        ItemStack i = item.clone();
        ItemMeta meta = i.getItemMeta();
        if(lore != null) meta.setLore(lore.stream().map(x -> ChatColor.translateAlternateColorCodes('&', x)).collect(Collectors.toList()));
        if(name != null) meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
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
        return new ItemStack(Material.matchMaterial(fields[0]), 1, damage);
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
     * @param item the item stack to receive the attribute
     * @param attributeName the attribute's name, don't mistake with the name below. This is more like the 'ID' of the attribute. Like 'generic.attackDamage'
     * @param name attribute's name
     * @param amount attribute's value
     * @param operation attribute's operation
     * @return the item stack with attribute set
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
     * @param item the item stack to receive the attribute
     * @param attribute the attribute
     * @param name attribute's name
     * @param amount attribute's value
     * @return the item stack with attribute set
     */
    public static ItemStack addAttribute(ItemStack item, ItemAttribute attribute, String name, Double amount) {
        return addAttribute(item, attribute.getID(), name, amount, AttributeOperation.ADD);
    }

    /**
     * Add an attribute modifier to an item
     *
     * @param item the item stack to receive the attribute
     * @param attribute the attribute
     * @param name attribute's name
     * @param amount attribute's value
     * @param operation attribute's operation
     * @return the item stack with attribute set
     */
    public static ItemStack addAttribute(ItemStack item, ItemAttribute attribute, String name, Double amount, AttributeOperation operation) {
        return addAttribute(item, attribute.getID(), name, amount, operation);
    }


    /**
     * Add an attribute modifier to an item
     *
     * @param item      the item stack to receive the attribute
     * @param attribute the attribute
     * @return the item stack with attribute set
     */
    public static ItemStack addAttribute(ItemStack item, AttributeWrapper attribute) {
        return addAttribute(item, attribute.getAttributeName(), attribute.getName(), attribute.getValue(), attribute.getOperation());
    }


    /**
     * Gets an attribute from the specified item.
     *
     * @param item      the item stack
     * @param attribute the attribute name or ID
     * @param name      true if searching for name instead of ID (Attribute type, like health, etc.)
     * @return the attribute
     */
    public static AttributeWrapper getAttribute(ItemStack item, String attribute, boolean name) {
        if(!isValid(item)) throw new IllegalArgumentException("Item can't be null or air");
        ReadableNBT nbt = NBT.readNbt(item);

        for (ReadWriteNBT attr : nbt.getCompoundList("AttributeModifiers")) {
            if((name && attr.getString("Name").equalsIgnoreCase(attribute)) || attr.getString("AttributeName").equalsIgnoreCase(attribute)) {

                return new AttributeWrapper(
                        attr.getString("Name"),
                        attr.getString("AttributeName"),
                        AttributeOperation.fromID(attr.getInteger("Operation")),
                        attr.getDouble("Amount"),
                        attr.getLong("UUIDLeast"),
                        attr.getLong("UUIDMost")
                );
            }
        }
        return null;
    }

    /**
     * Gets an attribute from the specified item.
     *
     * @param item      the item stack
     * @param attribute the attribute
     * @return the attribute
     */
    public static AttributeWrapper getAttribute(ItemStack item, ItemAttribute attribute) {
        return getAttribute(item, attribute.getID(), false);
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


    /**
     * Give an item stack to the player, if the player's inventory is full: drops the items.
     *
     * @param player the player
     * @param item   the item
     */
    public static void giveItem(Player player, ItemStack item) {
        HashMap<Integer, ItemStack> overflow = player.getInventory().addItem(item);
        if(!overflow.isEmpty()) {
            Location loc = player.getLocation();
            World world = loc.getWorld();
            overflow.values().forEach(x -> world.dropItem(loc, x));
        }
    }

    /**
     * Gets a mannequin with player's skin.
     * Requires Armourer's workshop to be installed
     *
     * @param player the player
     * @return the mannequin
     */
    public static ItemStack getMannequin(Player player) {
        ItemStack i = new ItemStack(Material.matchMaterial("ARMOURERSWORKSHOP_BLOCKMANNEQUIN"));
        NBTItem nbt = new NBTItem(i);
        NBTCompound comp = nbt.addCompound("owner");
        comp.setString("Id", player.getUniqueId().toString());
        comp.setString("Name", player.getName());
        return nbt.getItem();
    }


    /**
     * Gets a mannequin with the provided URL as skin.
     * Requires Armourer's workshop to be installed
     *
     * @param url the url
     * @return the mannequin
     */
    public static ItemStack getMannequin(String url) {
        ItemStack i = new ItemStack(Material.matchMaterial("ARMOURERSWORKSHOP_BLOCKMANNEQUIN"));
        NBTItem nbt = new NBTItem(i);
        nbt.setString("imageUrl", url);
        return nbt.getItem();
    }

    /**
     * Gets a skull with player's skin.
     *
     * @param player the player
     * @return the skull
     */
    public static ItemStack getSkull(Player player) {
        ItemStack i = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) i.getItemMeta();
        meta.setOwner(player.getName());
        i.setItemMeta(meta);
        return i;
    }

    /**
     * Replaces values in a item's lore.
     *
     * @param item    the item
     * @param replace the replaces, with key/value
     * @return the item stack
     */
    public static ItemStack replaceLore(@Nonnull ItemStack item, @Nonnull Map<String, String> replace) {
        ItemStack i = item.clone();
        ItemMeta meta = i.getItemMeta();
        if(!meta.hasLore() || meta.getLore().isEmpty())
            return i;

        List<String> newLore = meta.getLore().stream().map(x -> {
            final String[] y = {x};
            replace.forEach((a,b) -> y[0] = y[0].replace(a, b));
            return y[0];
        }).collect(Collectors.toList());

        meta.setLore(newLore);
        i.setItemMeta(meta);

        return i;
    }

    /**
     * Replaces values in a item's lore.
     *
     * @param item     the item
     * @param oldValue the old value
     * @param newValue the new value
     * @return the item stack
     */
    public static ItemStack replaceLore(@Nonnull ItemStack item, @Nonnull String oldValue, @Nonnull String newValue) {
        ItemStack i = item.clone();
        ItemMeta meta = i.getItemMeta();
        if(!meta.hasLore() || meta.getLore().isEmpty())
            return i;

        meta.setLore(meta.getLore().stream().map(x -> x.replace(oldValue, newValue)).collect(Collectors.toList()));
        i.setItemMeta(meta);

        return i;
    }
}
