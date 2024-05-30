package com.github.oDraco.util;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

public class MiscUtils {

    public static <T> T getRandomByWeight(List<T> elements, List<Integer> weights) {
        int total = weights.stream().reduce(0, Integer::sum);

        int random = (int) Math.round(Math.random()*total);

        int cursor = 0;
        for(int i=0; i<weights.size(); i++) {
            cursor+=weights.get(i);
            if(cursor >= random) return elements.get(i);
        }

        return null;
    }

    public static YamlConfiguration getYamlConfiguration(JavaPlugin plugin, String fileName) {
        File configFile = new File(plugin.getDataFolder() + "/"+fileName);
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource(fileName, false);
        }
        return YamlConfiguration.loadConfiguration(configFile);
    }
}
