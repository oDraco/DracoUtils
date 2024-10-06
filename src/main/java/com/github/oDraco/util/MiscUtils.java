package com.github.oDraco.util;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

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

    /**
     * Gets a Vector from a string in the format: '0, 0, 0'.
     *
     * @param input the input
     * @return the vector
     */
    public static Vector vectorFromString(String input) {
        String[] fields = input.split(",");
        if(fields.length != 3)
            throw new IllegalArgumentException("Invalid format! Valid format is: 0,0,0");
        double[] vectorValues = new double[3];
        for (int i = 0; i < 3; i++) {
            vectorValues[i] = Double.parseDouble(fields[i]);
        }
        return new Vector(vectorValues[0], vectorValues[1], vectorValues[2]);
    }

    public static File getPlayerdata(OfflinePlayer player) {
        return new File("world/playerdata/"+player.getUniqueId()+".dat");
    }
}
