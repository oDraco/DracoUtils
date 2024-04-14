package com.github.oDraco.util;

import com.github.oDraco.entities.AttributeBonus;
import com.github.oDraco.entities.enums.Attribute;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTEntity;
import org.bukkit.entity.Player;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Player manager.
 */
public abstract class PlayerManager {

    /**
     * Gets tp.
     *
     * @param player the player
     * @return the tp
     */
    public static int getTP(Player player) {
        return getPlayerPersisted(new NBTEntity(player)).getInteger("jrmcTpint");
    }

    /**
     * Sets tp.
     *
     * @param player the player
     * @param amount the amount
     */
    public static void setTP(Player player, int amount) {
        getPlayerPersisted(new NBTEntity(player)).setInteger("jrmcTpint", amount);
    }

    /**
     * Add tp int.
     *
     * @param player the player
     * @param amount the amount
     * @return new TP
     */
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

    /**
     * Add tp int.
     *
     * @param player the player
     * @param amount the amount
     * @return new TP
     */
    public static int addTP(Player player, long amount) {
        if (amount > Integer.MAX_VALUE) return addTP(player, Integer.MAX_VALUE);
        if (amount < Integer.MIN_VALUE) return addTP(player, Integer.MIN_VALUE);
        return addTP(player, (int) amount);
    }

    /**
     * Gets attr.
     *
     * @param attribute the attribute
     * @param player    the player
     * @return the attr
     */
    public static int getAttr(Attribute attribute, Player player) {
        NBTCompound playerPersisted = getPlayerPersisted(new NBTEntity(player));
        switch (attribute) {
            case STRENGTH:
                return playerPersisted.getInteger("jrmcStrI");
            case DEXTERITY:
                return playerPersisted.getInteger("jrmcDexI");
            case CONSTITUTION:
                return playerPersisted.getInteger("jrmcCnsI");
            case WILL_POWER:
                return playerPersisted.getInteger("jrmcWilI");
            case MIND:
                return playerPersisted.getInteger("jrmcIntI");
            case SPIRIT:
                return playerPersisted.getInteger("jrmcCncI");
        }
        throw new IllegalArgumentException("Invalid attribute argument! Valid attributes are: str,dex,con,wil,mnd,spi");
    }

    /**
     * Sets attr.
     *
     * @param attribute    the attribute
     * @param player       the player
     * @param newAttribute the new attribute
     */
    public static void setAttr(Attribute attribute, Player player, int newAttribute) {
        NBTCompound playerPersisted = getPlayerPersisted(new NBTEntity(player));
        switch (attribute) {
            case STRENGTH:
                playerPersisted.setInteger("jrmcStrI", newAttribute);
                return;
            case DEXTERITY:
                playerPersisted.setInteger("jrmcDexI", newAttribute);
                return;
            case CONSTITUTION:
                playerPersisted.setInteger("jrmcCnsI", newAttribute);
                return;
            case WILL_POWER:
                playerPersisted.setInteger("jrmcWilI", newAttribute);
                return;
            case MIND:
                playerPersisted.setInteger("jrmcIntI", newAttribute);
                return;
            case SPIRIT:
                playerPersisted.setInteger("jrmcCncI", newAttribute);
                return;
        }
        throw new IllegalArgumentException("Invalid attribute argument! Valid attributes are: str,dex,con,wil,mnd,spi");
    }

    /**
     * Sets attr.
     *
     * @param attribute    the attribute index
     * @param player       the player
     * @param newAttribute the new attribute
     */
    public static void setAttr(int attribute, Player player, int newAttribute) {
        if (attribute < 0 || attribute > 5) throw new InvalidParameterException("Attribute Index must be in range 0-5");
        setAttr(Attribute.values()[attribute], player, newAttribute);
    }

    /**
     * Gets attr.
     *
     * @param attribute the attribute index
     * @param player    the player
     * @return the attr
     */
    public static int getAttr(int attribute, Player player) {
        if (attribute < 0 || attribute > 5) throw new InvalidParameterException("Attribute Index must be in range 0-5");
        return getAttr(Attribute.values()[attribute], player);
    }

    /**
     * Get attributes array.
     *
     * @param player the player
     * @return attributes in an int[]
     */
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

    /**
     * Sets attributes.
     *
     * @param player the player
     * @param attr   attributes in an int[]
     */
    public static void setAttributes(Player player, int[] attr) {
        if (attr.length != 6) throw new InvalidParameterException("Attribute array length must be 6");
        NBTCompound playerPersisted = getPlayerPersisted(new NBTEntity(player));
        playerPersisted.setInteger("jrmcStrI", attr[0]);
        playerPersisted.setInteger("jrmcDexI", attr[1]);
        playerPersisted.setInteger("jrmcCnsI", attr[2]);
        playerPersisted.setInteger("jrmcWilI", attr[3]);
        playerPersisted.setInteger("jrmcIntI", attr[4]);
        playerPersisted.setInteger("jrmcCncI", attr[5]);
    }

    /**
     * Gets dbc health.
     *
     * @param player the player
     * @return the dbc health
     */
    public static int getDBCHealth(Player player) {
        return getPlayerPersisted(new NBTEntity(player)).getInteger("jrmcBdy");
    }

    /**
     * Sets dbc health.
     *
     * @param player    the player
     * @param newHealth the new health
     */
    public static void setDBCHealth(Player player, int newHealth) {
        getPlayerPersisted(new NBTEntity(player)).setInteger("jrmcBdy", newHealth);
    }

    /**
     * Gets dbc release.
     *
     * @param player the player
     * @return the dbc release
     */
    public static short getDBCRelease(Player player) {
        return getPlayerPersisted(new NBTEntity(player)).getShort("jrmcRelease");
    }

    /**
     * Sets dbc release.
     *
     * @param player     the player
     * @param newRelease the new release
     */
    public static void setDBCRelease(Player player, short newRelease) {
        getPlayerPersisted(new NBTEntity(player)).setShort("jrmcRelease", newRelease);
    }

    /**
     * Gets dbc stamina.
     *
     * @param player the player
     * @return the dbc stamina
     */
    public static int getDBCStamina(Player player) {
        return getPlayerPersisted(new NBTEntity(player)).getInteger("jrmcStamina");
    }

    /**
     * Sets dbc stamina.
     *
     * @param player     the player
     * @param newStamina the new stamina
     */
    public static void setDBCStamina(Player player, int newStamina) {
        getPlayerPersisted(new NBTEntity(player)).setInteger("jrmcStamina", newStamina);
    }

    /**
     * Gets dbc ki.
     *
     * @param player the player
     * @return the dbc ki
     */
    public static int getDBCKi(Player player) {
        return getPlayerPersisted(new NBTEntity(player)).getInteger("jrmcEnrgy");
    }

    /**
     * Sets dbc ki.
     *
     * @param player the player
     * @param newKi  the new ki
     */
    public static void setDBCKi(Player player, int newKi) {
        getPlayerPersisted(new NBTEntity(player)).setInteger("jrmcEnrgy", newKi);
    }

    /**
     * Reset player.
     *
     * @param player  the player
     * @param resetTP if player TP would be resetted
     */
    public static void resetPlayer(Player player, boolean resetTP) {
        if (resetTP) setTP(player, 0);
        getPlayerPersisted(new NBTEntity(player)).setByte("jrmcAccept", (byte) 0);
    }

    /**
     * Reset dbc skills.
     *
     * @param player the player
     */
    public static void resetDBCSkills(Player player) {
        getPlayerPersisted(new NBTEntity(player)).setString("jrmcSSlts", "");
    }

    /**
     * Gets player persisted.
     *
     * @param nbtEntity the nbt entity
     * @return the player persisted
     */
    public static NBTCompound getPlayerPersisted(NBTEntity nbtEntity) {
        return nbtEntity.getCompound("ForgeData").getCompound("PlayerPersisted");
    }

    /**
     * Gets player persisted.
     *
     * @param player the player
     * @return the player persisted
     */
    public static NBTCompound getPlayerPersisted(Player player) {
        return getPlayerPersisted(new NBTEntity(player));
    }

    /**
     * Damages a DBC player as another player;
     *
     * @param attacker the attacker
     * @param victim   the victim
     * @param damage   attack damage
     * @return the victim new health
     */
    public static int damageAsPlayer(Player attacker, Player victim, int damage) {
        NBTCompound playerPersistedAttacker = getPlayerPersisted(attacker);
        NBTCompound playerPersistedVictim = getPlayerPersisted(victim);

        playerPersistedVictim.setInteger("jrmcLastDamageReceived", damage);
        playerPersistedAttacker.setInteger("jrmcLastDamageDealt", damage);

        playerPersistedVictim.setString("jrmcLastAttacker", attacker.getName() + ";" + damage);
        playerPersistedAttacker.setString("jrmcAttackLstPlyrNam", victim.getUniqueId().toString());

        int epoch = (int) (System.currentTimeMillis() / 1000L);
        playerPersistedAttacker.setInteger("jrmcAttackLstPlyrTm", epoch + 5);
        playerPersistedVictim.setInteger("jrmcAttackTimer", epoch + 5);

        return damagePlayer(victim, damage);
    }

    /**
     * Damages a DBC player;
     *
     * @param player the victim
     * @param damage attack damage
     * @return the victim new health
     */
    public static int damagePlayer(Player player, int damage) {
        return removeStat(player, damage, "jrmcBdy");
    }

    /**
     * Removes an amount of KI from a DBC player;
     *
     * @param player the player
     * @param amount the amount to be removed
     * @return the new player's KI amount
     */
    public static int removeKI(Player player, int amount) {
        return removeStat(player, amount, "jrmcEnrgy");
    }

    /**
     * Removes an amount of stamina from a DBC player;
     *
     * @param player the player
     * @param amount the amount to be removed
     * @return the new player's stamina amount
     */
    public static int removeStamina(Player player, int amount) {
        return removeStat(player, amount, "jrmcStamina");
    }

    /**
     * Add bonus attribute.
     *
     * @param player      the player
     * @param bonus       the bonus
     * @param addToBottom if the attribute should go to bottom/end (true) or top/start (true)
     * @param unrestrict if true, allows to have 2+ bonuses with the same ID (can be buggy)
     */
    public static void addBonusAttribute(Player player, AttributeBonus bonus, boolean addToBottom, boolean unrestrict) {
        NBTCompound playerPersisted = getPlayerPersisted(player);
        String key = "jrmcAttrBonus" + bonus.getAttribute().getAcronym();
        String currentBonus = playerPersisted.getString(key);
        if (currentBonus == null || currentBonus.isEmpty()) {
            playerPersisted.setString(key, bonus.toString());
            return;
        }
        if (!unrestrict && currentBonus.contains(bonus.getName()))
            throw new IllegalStateException("Player already have a attribute bonus with the same ID");
        currentBonus = addToBottom ?
                currentBonus + "|" + bonus :
                bonus + "|" + currentBonus;
        playerPersisted.setString(key, currentBonus);
    }

    /**
     * Add bonus attribute.
     *
     * @param player      the player
     * @param bonus       the bonus
     */
    public static void addBonusAttribute(Player player, AttributeBonus bonus) {
        addBonusAttribute(player, bonus, true, false);
    }

    /**
     * Remove bonus attribute boolean.
     *
     * @param player the player
     * @param bonus  the bonus
     * @return true, if the desired bonus is removed
     */
    public static boolean removeBonusAttribute(Player player, AttributeBonus bonus) {
        NBTCompound playerPersisted = getPlayerPersisted(player);
        Attribute attr = bonus.getAttribute();
        String key = "jrmcAttrBonus" + attr.getAcronym();
        String currentBonus = playerPersisted.getString(key);
        if(currentBonus == null || currentBonus.isEmpty()) return false;
        List<AttributeBonus> bonuses = Arrays.stream(currentBonus.split("\\|")).map(x -> AttributeBonus.fromString(attr, x)).collect(Collectors.toList());
        if(!bonuses.remove(bonus)) return false;
        String newBonus = bonuses.stream().map(AttributeBonus::toString).collect(Collectors.joining("|"));
        playerPersisted.setString(key, newBonus);
        return true;
    }

    /**
     * Remove bonus attribute boolean.
     *
     * @param player the player
     * @param attr the bonus attribute
     * @param bonusID  the bonus ID (name)
     * @return true, if the desired bonus is removed
     */
    public static boolean removeBonusAttribute(Player player, Attribute attr, String bonusID) {
        return removeBonusAttribute(player, new AttributeBonus(attr, bonusID, "+0"));
    }

    /**
     * Change bonus attribute.
     *
     * @param player     the player
     * @param attr       the attribute bonus
     * @param oldBonusID the old bonus id
     * @param newValue   the new bonus value
     */
    public static void changeBonusAttribute(Player player, Attribute attr, String oldBonusID, String newValue) {
        NBTCompound playerPersisted = getPlayerPersisted(player);
        String key = "jrmcAttrBonus" + attr.getAcronym();
        String currentBonus = playerPersisted.getString(key);
        if(currentBonus == null || currentBonus.isEmpty() || !currentBonus.contains(oldBonusID)) return;
        List<AttributeBonus> bonuses = Arrays.stream(currentBonus.split("\\|")).map(x -> AttributeBonus.fromString(attr, x)).collect(Collectors.toList());
        for(AttributeBonus bonus : bonuses) {
            if(!bonus.getName().equals(oldBonusID)) continue;
            bonus.setValue(newValue);
            break;
        }
        String newBonus = bonuses.stream().map(AttributeBonus::toString).collect(Collectors.joining("|"));
        playerPersisted.setString(key, newBonus);
    }

    /**
     * Check if the player has an attribute bonus with the specified ID
     *
     * @param player  the player
     * @param attr    the attr
     * @param bonusID the bonus id
     * @return if player has the attribute bonus
     */
    public static boolean hasBonusAttribute(Player player, Attribute attr, String bonusID) {
        NBTCompound playerPersisted = getPlayerPersisted(player);
        String currentBonus = playerPersisted.getString("jrmcAttrBonus" + attr.getAcronym());
        return currentBonus != null && !currentBonus.isEmpty() && currentBonus.contains(bonusID);
    }

    private static int removeStat(Player player, int amount, String tag) {
        NBTCompound playerPersisted = getPlayerPersisted(player);
        int oldValue = playerPersisted.getInteger(tag);
        long newValue = (long) oldValue - amount;
        if (newValue < 0) newValue = 0;
        if (newValue > Integer.MAX_VALUE) newValue = Integer.MIN_VALUE;
        playerPersisted.setInteger(tag, Math.toIntExact(newValue));
        return Math.toIntExact(newValue);
    }
}
