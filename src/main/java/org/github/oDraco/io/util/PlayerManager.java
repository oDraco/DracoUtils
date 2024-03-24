package org.github.oDraco.io.util;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTEntity;
import org.bukkit.entity.Player;

public abstract class PlayerManager {

    public static int getTP(Player player) {
        NBTEntity nbtPlayer = new NBTEntity(player);
        return getPlayerPersisted(nbtPlayer).getInteger("jrmcTpint");
    }

    public static void setTP(Player player, int amount) {
        getPlayerPersisted(new NBTEntity(player)).setInteger("jrmcTpint", amount);
    }

    public static int addTP(Player player, int amount) {
        NBTEntity nbtPlayer = new NBTEntity(player);
        NBTCompound playerPersisted = getPlayerPersisted(nbtPlayer);
        int oldTp = playerPersisted.getInteger("jrmcTpint");
        int newTp = oldTp+amount;
        playerPersisted.setInteger("jrmcTpint", newTp);
        return newTp;
    }

    private static NBTCompound getPlayerPersisted(NBTEntity nbtEntity) {
        return nbtEntity.getCompound("ForgeData").getCompound("PlayerPersisted");
    }
}
