package org.github.oDraco.io.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.github.oDraco.io.entities.enums.Rarity;
import org.github.oDraco.io.entities.enums.Type;

import java.util.ArrayList;
import java.util.List;

public abstract class ItemUtils {

    private static final String NULL_CHAR = "§r";

    public static ItemStack formatItem(ItemStack item, String name, Rarity rarity, Type type) {
        ItemStack i = item.clone();
        ItemMeta meta = i.getItemMeta();

        char nameColor = rarity == Rarity.COMMON ? 'f' : rarity.getColorCharacter();
        name = ("&" + nameColor + name).replace("&", "§");
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
}
