package com.github.oDraco.util;

import com.github.oDraco.DracoUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.File;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MiscUtils {

    private static final Pattern TIME_REGEX = Pattern.compile("(\\d+[sSmMhHdD])");
    private static final Map<Character, Long> TIME_VALUE = new HashMap<>();

    static {
        TIME_VALUE.put('s', 1L); // second
        TIME_VALUE.put('m', 60L); // minute
        TIME_VALUE.put('h', 3600L); // hour
        TIME_VALUE.put('d', 86400L); // day
        TIME_VALUE.put('D', 86400L); // day too
        TIME_VALUE.put('S', 604800L); // week
        TIME_VALUE.put('M', 2592000L); // month
    }

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
        File configFile = new File(plugin.getDataFolder(), fileName);
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

    public static boolean canBeFormattedToSeconds(String in) {
        Matcher matcher = TIME_REGEX.matcher(in);
        return matcher.replaceAll("").isEmpty();
    }

    public static Long secondsFromString(String in) {
        Matcher matcher = TIME_REGEX.matcher(in);
        long seconds = 0L;
        while (matcher.find()) {
            String group = matcher.group();
            char c = group.toCharArray()[group.length() - 1];
            seconds += TIME_VALUE.get(c)*Integer.parseInt(group.substring(0, group.length()-1));
        }
        return seconds <= 0 ? -1L : seconds;
    }

    public static String secondsToString(long seconds) {
        int[] timeUnits = {2592000, 604800, 86400, 3600, 60, 1};
        String[] timeLabels = {"month", "week", "day", "hour", "minute", "second"};
        FileConfiguration config = DracoUtils.getInstance().getConfig();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < timeUnits.length; i++) {
            long amount = seconds / timeUnits[i];
            if (amount > 0) {
                seconds -= amount * timeUnits[i];
                if(sb.length() > 0)
                    sb.append(config.getString("time.and"));
                sb.append(amount).append(" ")
                        .append(config.getString("time." + (amount > 1 ? timeLabels[i] + "s" : timeLabels[i])));
            }
        }
        return sb.toString();
    }

    public static boolean allNull(Object... obj) {
        for (Object o : obj) {
            if(o != null)
                return false;
        }
        return true;
    }

    // https://github.com/Kqnth/Java-HWID/blob/master/HWID.java
    public static String getHWID() {
        try {
            String toEncrypt = System.getenv("COMPUTERNAME") + System.getProperty("user.name") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL");
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(toEncrypt.getBytes());
            StringBuilder hexString = new StringBuilder();

            byte[] byteData = md.digest();

            for (byte aByteData : byteData) {
                String hex = Integer.toHexString(0xff & aByteData);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }

}
