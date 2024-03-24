package org.github.oDraco.io.util;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTEntity;
import org.bukkit.entity.Player;

public abstract class PlayerManager {

    public static int getTP(Player player) {
        return getPlayerPersisted(new NBTEntity(player)).getInteger("jrmcTpint");
    }

    public static void setTP(Player player, int amount) {
        getPlayerPersisted(new NBTEntity(player)).setInteger("jrmcTpint", amount);
    }

    public static int addTP(Player player, int amount) {
        NBTCompound playerPersisted = getPlayerPersisted(new NBTEntity(player));
        int oldTp = playerPersisted.getInteger("jrmcTpint");
        long newTp = (long) oldTp + amount; // Cast to long to prevent overflow

        if (newTp > Integer.MAX_VALUE) {
            newTp = Integer.MAX_VALUE; // Set to MAX_VALUE if it exceeds
        } else if (newTp < Integer.MIN_VALUE) {
            newTp = Integer.MIN_VALUE; // Set to MIN_VALUE if it's less
        }
        playerPersisted.setInteger("jrmcTpint", (int) newTp);
        return (int) newTp;
    }

    public static int addTP(Player player, long amount) {
        if(amount > Integer.MAX_VALUE) return addTP(player, Integer.MAX_VALUE);
        if(amount < Integer.MIN_VALUE) return addTP(player, Integer.MIN_VALUE);
        return addTP(player, (int) amount);
    }

    public static int getAttr(String attribute, Player player) {
        NBTCompound playerPersisted = getPlayerPersisted(new NBTEntity(player));
        switch (attribute.toLowerCase()) {
            case "strength":
            case "str":
                return playerPersisted.getInteger("jrmcStrI");
            case "dexterity":
            case "dex":
                return playerPersisted.getInteger("jrmcDexI");
            case "constitution":
            case "con":
                return playerPersisted.getInteger("jrmcCnsI");
            case "willpower":
            case "wil":
                return playerPersisted.getInteger("jrmcWilI");
            case "mind":
            case "mnd":
                return playerPersisted.getInteger("jrmcIntI");
            case "spirit":
            case "spi":
                return playerPersisted.getInteger("jrmcCncI");
        }
        throw new IllegalArgumentException("Invalid attribute argument! Valid attributes are: str,dex,con,wil,mnd,spi");
    }

    public static void setAttr(String attribute, Player player, int newAttribute) {
        NBTCompound playerPersisted = getPlayerPersisted(new NBTEntity(player));
        switch (attribute.toLowerCase()) {
            case "strength":
            case "str":
                playerPersisted.setInteger("jrmcStrI", newAttribute);
                return;
            case "dexterity":
            case "dex":
                playerPersisted.setInteger("jrmcDexI", newAttribute);
                return;
            case "constitution":
            case "con":
                playerPersisted.setInteger("jrmcCnsI", newAttribute);
                return;
            case "willpower":
            case "wil":
                playerPersisted.setInteger("jrmcWilI", newAttribute);
                return;
            case "mind":
            case "mnd":
                playerPersisted.setInteger("jrmcIntI", newAttribute);
                return;
            case "spirit":
            case "spi":
                playerPersisted.setInteger("jrmcCncI", newAttribute);
                return;
        }
        throw new IllegalArgumentException("Invalid attribute argument! Valid attributes are: str,dex,con,wil,mnd,spi");
    }

    public static int[] getAttributes(Player player) {
        NBTCompound playerPersisted = getPlayerPersisted(new NBTEntity(player));
        int[] attr = new int[6];
        attr[0] = playerPersisted.getInteger("jrmcStrI");
        attr[1] = playerPersisted.getInteger("jrmcDexI");
        attr[2] = playerPersisted.getInteger("jrmcCnsI");
        attr[3] = playerPersisted.getInteger("jrmcWilI");
        attr[4] = playerPersisted.getInteger("jrmcIntI");
        attr[5] = playerPersisted.getInteger("jrmcCncI");
        return attr;
    }

    public static int getDBCHealth(Player player) {
        return getPlayerPersisted(new NBTEntity(player)).getInteger("jrmcBdy");
    }

    public static void setDBCHealth(Player player, int newHealth) {
        getPlayerPersisted(new NBTEntity(player)).setInteger("jrmcBdy",newHealth);
    }

    public static short getDBCRelease(Player player) {
        return getPlayerPersisted(new NBTEntity(player)).getShort("jrmcRelease");
    }

    public static void setDBCRelease(Player player, short newRelease) {
        getPlayerPersisted(new NBTEntity(player)).setShort("jrmcRelease", newRelease);
    }

    public static int getDBCStamina(Player player) {
        return getPlayerPersisted(new NBTEntity(player)).getInteger("jrmcStamina");
    }

    public static void setDBCStamina(Player player, int newStamina) {
        getPlayerPersisted(new NBTEntity(player)).setInteger("jrmcStamina", newStamina);
    }

    public static int getDBCKi(Player player) {
        return getPlayerPersisted(new NBTEntity(player)).getInteger("jrmcEnrgy");
    }

    public static void setDBCKi(Player player, int newKi) {
        getPlayerPersisted(new NBTEntity(player)).setInteger("jrmcEnrgy", newKi);
    }

    private static NBTCompound getPlayerPersisted(NBTEntity nbtEntity) {
        return nbtEntity.getCompound("ForgeData").getCompound("PlayerPersisted");
    }
}
