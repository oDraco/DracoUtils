package com.github.oDraco.util;

import JinRyuu.JRMCore.JRMCoreConfig;
import JinRyuu.JRMCore.JRMCoreH;
import com.github.oDraco.DracoUtils;
import com.github.oDraco.entities.AttributeBonus;
import com.github.oDraco.entities.enums.DBCAttribute;
import com.github.oDraco.entities.enums.DBCRace;
import com.github.oDraco.entities.enums.DBCSkill;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTEntity;
import de.tr7zw.nbtapi.NBTFile;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Player manager.
 */
public abstract class PlayerManager {


    public static void sendConfigMessage(CommandSender sender, String key) {
        sendConfigMessage(sender, key, DracoUtils.getCachedConfig());
    }

    public static void sendConfigMessage(CommandSender sender, String key, Map<String, String> replaces) {
        sendConfigMessage(sender, key, DracoUtils.getCachedConfig(), replaces);
    }

    public static void sendConfigMessage(CommandSender sender, String key, JavaPlugin plugin) {
        sendConfigMessage(sender, key, plugin.getConfig(), null);
    }

    public static void sendConfigMessage(CommandSender sender, String key, JavaPlugin plugin, Map<String, String> replaces) {
        sendConfigMessage(sender, key, plugin.getConfig(), replaces);
    }

    public static void sendConfigMessage(CommandSender sender, String key, FileConfiguration config) {
        sendConfigMessage(sender, key, config, null);
    }

    public static void sendConfigMessage(CommandSender sender, String key, FileConfiguration config, @Nullable Map<String, String> replaces) {
        if (config.isList(key))
            sendMessage(sender, true, true, replaces, config.getStringList(key).toArray(new String[0]));
        else
            sendMessage(sender, true, true, replaces, config.getString(key));
    }

    /**
     * Sends a colored message to the target
     *
     * @param target  the receiver of the message
     * @param message message, if null the message isn't send
     */
    public static void sendMessage(CommandSender target, @Nullable String message) {
        sendMessage(target, message, false, true);
    }

    /**
     * Sends multiple colored messages to the target
     *
     * @param target   the receiver of the message
     * @param messages message list, if null the message isn't send
     */
    public static void sendMessage(CommandSender target, @Nullable String... messages) {
        sendMessage(target, false, true, null, messages);
    }

    /**
     * Sends a message to the target
     *
     * @param target    the receiver of the message
     * @param message   message, if null or empty (depending on the sendEmpty value) the message isn't send
     * @param sendEmpty if true, empty messages are sent
     * @param colored   if the message should be colored, defaults to true
     */
    public static void sendMessage(CommandSender target, @Nullable String message, boolean sendEmpty, boolean colored) {
        sendMessage(target, sendEmpty, colored, null, message != null ? message.split("\n") : null);
    }


    public static void sendMessage(CommandSender target, boolean sendEmpty, boolean colored, @Nullable Map<String, String> replaces, @Nullable String... message) {
        if (message == null)
            return;
        for (String s : message) {
            if (s.isEmpty() && !sendEmpty)
                continue;
            if (replaces != null)
                s = StringUtils.replace(s, replaces);
            target.sendMessage(colored ? ChatColor.translateAlternateColorCodes('&', s) : s);
        }
    }

    /**
     *
     * Gets a player by their nickname, even if it is offline.
     *
     * @param nickname player's nickname, case-sensitive.
     * @return offline player OR null if none find
     */
    public static OfflinePlayer getPlayer(String nickname) {
        OfflinePlayer p = Bukkit.getPlayerExact(nickname);
        if(p != null)
            return p;
        return Arrays.stream(Bukkit.getOfflinePlayers()).filter(x -> x.getName().equals(nickname)).findAny().orElse(null);
    }

    /**
     * Sends an action bar to the specified player
     *
     * @param player  the target
     * @param message the message, if null the message isn't send
     */
    public static void sendActionBar(Player player, @Nullable String message) {
        sendActionBar(player, message, true);
    }

    /**
     * Sends an action bar to the specified player
     *
     * @param player  the target
     * @param message the message, if null the message isn't send
     * @param colored if the message should be colored, defaults to true
     */
    public static void sendActionBar(Player player, @Nullable String message, boolean colored) {
        if (message == null)
            return;
        if (colored)
            message = ChatColor.translateAlternateColorCodes('&', message);
        if (DracoUtils.isMActionBarLoaded())
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("mactionbarapi send %s %s", player.getName(), message));
        else
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }

    /**
     * Gets tp.
     *
     * @param player the player
     * @return the tp
     */
    public static int getTP(OfflinePlayer player) {
        return getPlayerPersisted(player).getInteger("jrmcTpint");
    }

    /**
     * Sets tp.
     *
     * @param player the player
     * @param amount the amount
     */
    public static void setTP(OfflinePlayer player, int amount) {
        NBTCompound playerPersisted = getPlayerPersisted(player);
        playerPersisted.setInteger("jrmcTpint", amount);

        if (!player.isOnline()) {
            savePlayerData(MiscUtils.getPlayerdata(player), playerPersisted);
        }
    }

    /**
     * Add tp int.
     *
     * @param player the player
     * @param amount the amount
     * @return new TP
     */
    public static int addTP(OfflinePlayer player, int amount) {
        NBTCompound playerPersisted = getPlayerPersisted(player);
        int oldTp = playerPersisted.getInteger("jrmcTpint");
        long newTp = (long) oldTp + amount; // Cast to long to prevent overflow

        newTp = Math.min(newTp, Integer.MAX_VALUE); // Set to MAX_VALUE if it exceeds
        newTp = Math.max(newTp, Integer.MIN_VALUE); // Set to MIN_VALUE if it's less
        playerPersisted.setInteger("jrmcTpint", (int) newTp);

        if (!player.isOnline()) {
            savePlayerData(MiscUtils.getPlayerdata(player), playerPersisted);
        }

        return (int) newTp;
    }

    /**
     * Add tp int.
     *
     * @param player the player
     * @param amount the amount
     * @return new TP
     */
    public static int addTP(OfflinePlayer player, long amount) {
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
    public static int getAttr(DBCAttribute attribute, OfflinePlayer player) {
        NBTCompound playerPersisted = getPlayerPersisted(player);
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
    public static void setAttr(DBCAttribute attribute, OfflinePlayer player, int newAttribute) {
        NBTCompound playerPersisted = getPlayerPersisted(player);
        switch (attribute) {
            case STRENGTH:
                playerPersisted.setInteger("jrmcStrI", newAttribute);
                break;
            case DEXTERITY:
                playerPersisted.setInteger("jrmcDexI", newAttribute);
                break;
            case CONSTITUTION:
                playerPersisted.setInteger("jrmcCnsI", newAttribute);
                break;
            case WILL_POWER:
                playerPersisted.setInteger("jrmcWilI", newAttribute);
                break;
            case MIND:
                playerPersisted.setInteger("jrmcIntI", newAttribute);
                break;
            case SPIRIT:
                playerPersisted.setInteger("jrmcCncI", newAttribute);
                break;
        }
        if (!player.isOnline()) {
            savePlayerData(MiscUtils.getPlayerdata(player), playerPersisted);
        }
    }

    /**
     * Sets attr.
     *
     * @param attribute    the attribute index
     * @param player       the player
     * @param newAttribute the new attribute
     */
    public static void setAttr(int attribute, OfflinePlayer player, int newAttribute) {
        if (attribute < 0 || attribute > 5) throw new InvalidParameterException("Attribute Index must be in range 0-5");
        setAttr(DBCAttribute.values()[attribute], player, newAttribute);
    }

    /**
     * Gets attr.
     *
     * @param attribute the attribute index
     * @param player    the player
     * @return the attr
     */
    public static int getAttr(int attribute, OfflinePlayer player) {
        if (attribute < 0 || attribute > 5) throw new InvalidParameterException("Attribute Index must be in range 0-5");
        return getAttr(DBCAttribute.values()[attribute], player);
    }

    /**
     * Get attributes array.
     *
     * @param player the player
     * @return attributes in an int[]
     */
    public static int[] getAttributes(OfflinePlayer player) {
        NBTCompound playerPersisted = getPlayerPersisted(player);
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
    public static void setAttributes(OfflinePlayer player, int[] attr) {
        if (attr.length != 6) throw new InvalidParameterException("Attribute array length must be 6");
        NBTCompound playerPersisted = getPlayerPersisted(player);
        playerPersisted.setInteger("jrmcStrI", attr[0]);
        playerPersisted.setInteger("jrmcDexI", attr[1]);
        playerPersisted.setInteger("jrmcCnsI", attr[2]);
        playerPersisted.setInteger("jrmcWilI", attr[3]);
        playerPersisted.setInteger("jrmcIntI", attr[4]);
        playerPersisted.setInteger("jrmcCncI", attr[5]);

        if (!player.isOnline()) {
            savePlayerData(MiscUtils.getPlayerdata(player), playerPersisted);
        }
    }

    /**
     * Gets dbc health.
     *
     * @param player the player
     * @return the dbc health
     */
    public static int getDBCHealth(OfflinePlayer player) {
        return getPlayerPersisted(player).getInteger("jrmcBdy");
    }

    /**
     * Sets dbc health.
     *
     * @param player    the player
     * @param newHealth the new health
     */
    public static void setDBCHealth(OfflinePlayer player, int newHealth) {
        NBTCompound playerPersisted = getPlayerPersisted(player);
        playerPersisted.setInteger("jrmcBdy", newHealth);

        if (!player.isOnline()) {
            savePlayerData(MiscUtils.getPlayerdata(player), playerPersisted);
        }
    }

    /**
     * Gets dbc release.
     *
     * @param player the player
     * @return the dbc release
     */
    public static byte getDBCRelease(OfflinePlayer player) {
        return getPlayerPersisted(player).getByte("jrmcRelease");
    }

    /**
     * Sets dbc release.
     *
     * @param player     the player
     * @param newRelease the new release
     */
    public static void setDBCRelease(OfflinePlayer player, byte newRelease) {
        NBTCompound playerPersisted = getPlayerPersisted(player);
        playerPersisted.setByte("jrmcRelease", newRelease);

        if (!player.isOnline()) {
            savePlayerData(MiscUtils.getPlayerdata(player), playerPersisted);
        }
    }

    /**
     * Gets dbc stamina.
     *
     * @param player the player
     * @return the dbc stamina
     */
    public static int getDBCStamina(OfflinePlayer player) {
        return getPlayerPersisted(player).getInteger("jrmcStamina");
    }

    /**
     * Sets dbc stamina.
     *
     * @param player     the player
     * @param newStamina the new stamina
     */
    public static void setDBCStamina(OfflinePlayer player, int newStamina) {
        NBTCompound playerPersisted = getPlayerPersisted(player);
        playerPersisted.setInteger("jrmcStamina", newStamina);

        if (!player.isOnline()) {
            savePlayerData(MiscUtils.getPlayerdata(player), playerPersisted);
        }
    }

    /**
     * Gets dbc ki.
     *
     * @param player the player
     * @return the dbc ki
     */
    public static int getDBCKi(OfflinePlayer player) {
        return getPlayerPersisted(player).getInteger("jrmcEnrgy");
    }

    /**
     * Sets dbc ki.
     *
     * @param player the player
     * @param newKi  the new ki
     */
    public static void setDBCKi(OfflinePlayer player, int newKi) {
        NBTCompound playerPersisted = getPlayerPersisted(player);
        playerPersisted.setInteger("jrmcEnrgy", newKi);

        if (!player.isOnline()) {
            savePlayerData(MiscUtils.getPlayerdata(player), playerPersisted);
        }
    }

    /**
     * Reset player.
     *
     * @param player  the player
     * @param resetTP if player TP would be resetted
     */
    public static void resetPlayer(OfflinePlayer player, boolean resetTP) {
        if (resetTP) setTP(player, 0);
        NBTCompound playerPersisted = getPlayerPersisted(player);
        playerPersisted.setByte("jrmcAccept", (byte) 0);

        playerPersisted.setInteger("jrmcState", 0);
        playerPersisted.setInteger("jrmcState2", 0);

        if (!player.isOnline()) {
            savePlayerData(MiscUtils.getPlayerdata(player), playerPersisted);
        }
        setDBCRelease(player, (byte) 0);
    }

    /**
     * Reset dbc skills.
     *
     * @param player the player
     */
    public static void resetDBCSkills(OfflinePlayer player) {
        NBTCompound playerPersisted = getPlayerPersisted(player);
        playerPersisted.setString("jrmcSSlts", "");

        if (!player.isOnline()) {
            savePlayerData(MiscUtils.getPlayerdata(player), playerPersisted);
        }
    }


    /**
     * Gets the DBC skill level for a player.
     *
     * @param player the player
     * @param skill  the skill
     * @return the skill level, or 0 if the player doesn't have the skill.
     */
    public static int getSkillLevel(OfflinePlayer player, DBCSkill skill) {
        int level = 0;
        String skillsTxt = getPlayerPersisted(player).getString("jrmcSSlts");
        if (skillsTxt.contains(skill.getSymbol())) {
            for (String skillString : skillsTxt.split(",")) {
                if (skillString.contains(skill.getSymbol()))
                    return Integer.parseInt(skillString.replace(skill.getSymbol(), "")) + 1;
            }
        }
        return level;
    }

    /**
     * Sets the DBC skill level for a player.
     *
     * @param player the player
     * @param skill  the skill
     * @param level  the level (if 0, removes the skill)
     */
    public static void setSkillLevel(OfflinePlayer player, DBCSkill skill, int level) {
        NBTCompound playerPersisted = getPlayerPersisted(player);
        String skillsTxt = playerPersisted.getString("jrmcSSlts");
        String[] fields = skillsTxt.split(",");
        Set<String> newFields = new HashSet<>();
        for (String field : fields) {
            if (field.contains(skill.getSymbol())) {
                if (level <= 0)
                    continue;
                field = skill.getSymbol() + (level - 1);
            }
            newFields.add(field);
        }
        playerPersisted.setString("jrmcSSlts", String.join(",", newFields));

        if (!player.isOnline()) {
            savePlayerData(MiscUtils.getPlayerdata(player), playerPersisted);
        }
    }

    /**
     * Gets player persisted.
     *
     * @param nbt the nbt entity
     * @return the player persisted
     */
    public static NBTCompound getPlayerPersisted(NBTCompound nbt) {
        return nbt.getCompound("ForgeData").getCompound("PlayerPersisted");
    }

    /**
     * Gets player persisted.
     *
     * @param player the player
     * @return the player persisted
     */
    public static NBTCompound getPlayerPersisted(OfflinePlayer player) {
        try {
            return getPlayerPersisted(player.isOnline() ?
                    new NBTEntity(player.getPlayer()) :
                    NBTFile.readFrom(MiscUtils.getPlayerdata(player))
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
     * @param unrestrict  if true, allows to have 2+ bonuses with the same ID (can be buggy)
     */
    public static void addBonusAttribute(OfflinePlayer player, AttributeBonus bonus, boolean addToBottom, boolean unrestrict) {
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

        if (!player.isOnline()) {
            savePlayerData(MiscUtils.getPlayerdata(player), playerPersisted);
        }
    }

    /**
     * Add bonus attribute.
     *
     * @param player the player
     * @param bonus  the bonus
     */
    public static void addBonusAttribute(OfflinePlayer player, AttributeBonus bonus) {
        addBonusAttribute(player, bonus, true, false);
    }

    /**
     * Remove bonus attribute boolean.
     *
     * @param player the player
     * @param bonus  the bonus
     * @return true, if the desired bonus is removed
     */
    public static boolean removeBonusAttribute(OfflinePlayer player, AttributeBonus bonus) {
        NBTCompound playerPersisted = getPlayerPersisted(player);
        DBCAttribute attr = bonus.getAttribute();
        String key = "jrmcAttrBonus" + attr.getAcronym();
        String currentBonus = playerPersisted.getString(key);

        if (currentBonus == null)
            return false;

        currentBonus = currentBonus.trim();
        if (currentBonus.isEmpty())
            return false;

        List<AttributeBonus> bonuses = Arrays.stream(currentBonus.split("\\|")).map(x -> x.isEmpty() ? null : AttributeBonus.fromString(attr, x)).collect(Collectors.toList());
        if (!bonuses.remove(bonus)) return false;
        String newBonus = bonuses.stream().map(AttributeBonus::toString).collect(Collectors.joining("|"));
        playerPersisted.setString(key, newBonus);

        if (!player.isOnline()) {
            savePlayerData(MiscUtils.getPlayerdata(player), playerPersisted);
        }

        return true;
    }

    /**
     * Remove bonus attribute boolean.
     *
     * @param player  the player
     * @param attr    the bonus attribute
     * @param bonusID the bonus ID (name)
     * @return true, if the desired bonus is removed
     */
    public static boolean removeBonusAttribute(OfflinePlayer player, DBCAttribute attr, String bonusID) {
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
    public static void changeBonusAttribute(OfflinePlayer player, DBCAttribute attr, String oldBonusID, String newValue) {
        NBTCompound playerPersisted = getPlayerPersisted(player);
        String key = "jrmcAttrBonus" + attr.getAcronym();
        String currentBonus = playerPersisted.getString(key);
        if (currentBonus == null || currentBonus.isEmpty() || !currentBonus.contains(oldBonusID)) return;
        List<AttributeBonus> bonuses = Arrays.stream(currentBonus.split("\\|")).map(x -> AttributeBonus.fromString(attr, x)).collect(Collectors.toList());
        for (AttributeBonus bonus : bonuses) {
            if (!bonus.getName().equals(oldBonusID)) continue;
            bonus.setValue(newValue);
            break;
        }
        String newBonus = bonuses.stream().map(AttributeBonus::toString).collect(Collectors.joining("|"));
        playerPersisted.setString(key, newBonus);

        if (!player.isOnline()) {
            savePlayerData(MiscUtils.getPlayerdata(player), playerPersisted);
        }
    }

    /**
     * Check if the player has an attribute bonus with the specified ID
     *
     * @param player  the player
     * @param attr    the attr
     * @param bonusID the bonus id
     * @return if player has the attribute bonus
     */
    public static boolean hasBonusAttribute(OfflinePlayer player, DBCAttribute attr, String bonusID) {
        NBTCompound playerPersisted = getPlayerPersisted(player);
        String currentBonus = playerPersisted.getString("jrmcAttrBonus" + attr.getAcronym());
        return currentBonus != null && !currentBonus.isEmpty() && currentBonus.contains(bonusID);
    }


    /**
     * Gets player's DBC level.
     *
     * @param player the player
     * @return the level
     */
    public static int getLevel(OfflinePlayer player) {
        int i = Arrays.stream(getAttributes(player)).sum() / 5 - 11;
        return Math.max(i, 0);
    }

    /**
     * Gets player's DBC race.
     *
     * @param player the player
     * @return the race
     */
    public static DBCRace getDBCRace(OfflinePlayer player) {
        return DBCRace.fromID(getPlayerPersisted(player).getByte("jrmcRace"));
    }

    /**
     * Sets player's DBC race.
     *
     * @param player the player
     * @param race   the race
     */
    public static void setDBCRace(OfflinePlayer player, DBCRace race) {
        NBTCompound playerPersisted = getPlayerPersisted(player);
        playerPersisted.setByte("jrmcRace", race.getID());

        if (!player.isOnline()) {
            savePlayerData(MiscUtils.getPlayerdata(player), playerPersisted);
        }
    }

    /**
     * Gets dbc upgrade cost with a custom multiplier.
     * Disclaimer, the code is from JRMCore. ( I just removed the cap O.O)
     *
     * @param player     the player
     * @param multiplier the multiplier
     * @return the dbc upgrade cost
     */
    public static long getDBCUpgradeCost(OfflinePlayer player, int multiplier) {
        int[] attributes = getAttributes(player);


        // Part 1

        double result = 0.0D;
        for (int i = 0; i < 6; i++) {
            if (JRMCoreConfig.AttributeUpgradeCost_AttributeMulti[i] > 0.0F) {
                float attribute = attributes[i] * JRMCoreConfig.AttributeUpgradeCost_AttributeMulti[i];
                result += attribute;
                if (result <= 0.0D)
                    return JRMCoreH.getMaxTP();
            }
        }
        int att = (int) result;

        // Part 2

        if (att == 0) {
            if (multiplier <= 1)
                return JRMCoreH.getMaxTP();
            return 0;
        }
        long attributeCost = 0;
        for (int j = 0; j < multiplier; j++) {
            int ac = JRMCoreH.attrCst(att++);
            attributeCost += ac;
            if (ac == 0)
                return 0;
        }
        if (attributeCost <= 0)
            return 0;
        return attributeCost;
    }


    /**
     * Gets a fusion member.
     *
     * @param player    the player
     * @param spectator if true, will return the spectator player of the fusion
     * @return the player, can be null
     */
    public static Player getFusionMember(OfflinePlayer player, boolean spectator) {
        return getFusionMembers(player)[spectator ? 1 : 0];
    }


    /**
     * Get fusion members for determined player.
     *
     * @param player the player participating in a fusion
     * @return a player array with both players in the fusion (First one is the controller)
     */
    public static Player[] getFusionMembers(OfflinePlayer player) {
        NBTCompound nbt = getPlayerPersisted(player);
        String[] members = nbt.getString("jrmcFuzion").split(",");
        Player[] players = new Player[2];
        if (members.length >= 2)
            for (int i = 0; i < 2; i++) {
                players[i] = Bukkit.getPlayerExact(members[i]);
            }
        return players;
    }

    /**
     * Unfuse player.
     *
     * @param player the player
     */
    public static void unfusePlayer(OfflinePlayer player) {
        for (Player p : getFusionMembers(player)) {
            if (p == null)
                continue;
            NBTCompound nbt = getPlayerPersisted(p);
            nbt.setString("jrmcFuzion", "");
            if (!p.isOnline())
                savePlayerData(MiscUtils.getPlayerdata(p), nbt);
        }
    }

    private static int removeStat(Player player, int amount, String tag) {
        NBTCompound playerPersisted = getPlayerPersisted(player);
        int oldValue = playerPersisted.getInteger(tag);
        long newValue = (long) oldValue - amount;
        newValue = Math.min(newValue, Integer.MAX_VALUE);
        newValue = Math.max(newValue, 0);
        playerPersisted.setInteger(tag, Math.toIntExact(newValue));
        return Math.toIntExact(newValue);

    }

    private static void savePlayerData(File playerdata, NBTCompound playerPersisted) {
        try {
            NBTFile.saveTo(playerdata, playerPersisted.getParent().getParent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
