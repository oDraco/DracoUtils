package com.github.oDraco.commands;

import com.github.oDraco.DracoUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Light implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration cfg = DracoUtils.getCachedConfig();
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.onlyPlayer")));
            return true;
        }
        if (!sender.hasPermission("draco.utils.luz")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.leakPermission")));
            return true;
        }
        Player p = (Player) sender;
        if (p.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            p.removePotionEffect(PotionEffectType.NIGHT_VISION);
            p.sendMessage("§2§lSUCESSO! §aVisão noturna desativada!");
        } else {
            p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 255), true);
            p.sendMessage("§2§lSUCESSO! §aVisão noturna ativada!");
        }
        return true;
    }
}
