package com.github.oDraco;

import com.github.oDraco.commands.General;
import com.github.oDraco.commands.Light;
import com.github.oDraco.commands.Trash;
import com.github.oDraco.commands.tabCompleters.GeneralTab;
import com.github.oDraco.entities.listeners.InventoryListener;
import com.github.oDraco.entities.listeners.MiscListener;
import com.github.oDraco.entities.listeners.QuitListener;
import com.github.oDraco.util.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class DracoUtils extends JavaPlugin {

    private static DracoUtils instance;

    private static boolean worldEditLoaded = false;

    private final static HashMap<String, ItemStack> defaultItems = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        loadDefaultItems();

        worldEditLoaded = Bukkit.getPluginManager().isPluginEnabled("WorldEdit");

        getCommand("dracoutils").setExecutor(new General());
        getCommand("dracoutils").setTabCompleter(new GeneralTab());

        getCommand("luz").setExecutor(new Light());

        getCommand("lixeira").setExecutor(new Trash());

        Bukkit.getPluginManager().registerEvents(new QuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new MiscListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);

        getLogger().info("\n  _____                        _    _ _   _ _     \n" +
                " |  __ \\                      | |  | | | (_) |    \n" +
                " | |  | |_ __ __ _  ___ ___   | |  | | |_ _| |___ \n" +
                " | |  | | '__/ _` |/ __/ _ \\  | |  | | __| | / __|\n" +
                " | |__| | | | (_| | (_| (_) | | |__| | |_| | \\__ \\\n" +
                " |_____/|_|  \\__,_|\\___\\___/   \\____/ \\__|_|_|___/\n" +
                "                                                  \n" +
                "                                                  ");
        getLogger().info("Iniciado com sucesso! Versão: " + getDescription().getVersion());
    }

    public static boolean isWorldEditLoaded() {
        return worldEditLoaded;
    }

    public static DracoUtils getInstance() {
        return instance;
    }

    private static void loadDefaultItems() {
        FileConfiguration config = getInstance().getConfig();

        config.getConfigurationSection("defaultItems.GUI").getKeys(false).forEach(x -> {
            String key = "defaultItems.GUI." + x + ".";
            ItemStack i = ItemUtils.formatItem(
                    ItemUtils.parseItem(config.getString(key + "item")),
                    config.getString(key + "name"),
                    config.getStringList(key + "lore")
            );
            if(config.contains(key+"glow") && config.getBoolean(key+"glow"))
                i = ItemUtils.applyGlow(i);
            defaultItems.put(x, i);
        });
    }

    public static HashMap<String, ItemStack> getDefaultItems() {
        return defaultItems;
    }
}