package com.github.oDraco;

import com.github.oDraco.commands.General;
import com.github.oDraco.commands.Light;
import com.github.oDraco.commands.ResourcePack;
import com.github.oDraco.commands.Trash;
import com.github.oDraco.commands.tabCompleters.GeneralTab;
import com.github.oDraco.entities.DracoPlugin;
import com.github.oDraco.entities.listeners.InventoryListener;
import com.github.oDraco.entities.listeners.MiscListener;
import com.github.oDraco.entities.listeners.QuitListener;
import com.github.oDraco.util.ItemUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class DracoUtils extends DracoPlugin {

    @Getter
    private static DracoUtils instance;

    @Getter
    private static boolean dracoCoreLoaded = false;
    @Getter
    private static boolean worldEditLoaded = false;

    @Getter
    private final static HashMap<String, ItemStack> defaultItems = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        loadDefaultItems();

        worldEditLoaded = Bukkit.getPluginManager().isPluginEnabled("WorldEdit");

        try {
            Class.forName("com.github.oDraco.DracoCore.Main");
            dracoCoreLoaded = true;
        } catch (Exception ignored) {};

        getCommand("dracoutils").setExecutor(new General());
        getCommand("dracoutils").setTabCompleter(new GeneralTab());

        getCommand("luz").setExecutor(new Light());

        getCommand("lixeira").setExecutor(new Trash());

        getCommand("resourcepack").setExecutor(new ResourcePack());

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

}