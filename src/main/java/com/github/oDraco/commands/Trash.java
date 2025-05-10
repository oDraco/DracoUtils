package com.github.oDraco.commands;

import com.github.oDraco.DracoUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Trash implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration cfg = DracoUtils.getCachedConfig();
        if(!(sender instanceof Player) && (args == null || args.length == 0)) {
            sender.sendMessage("§cUse /"+label+" <player>");
            return true;
        }
        if(!sender.hasPermission("draco.utils.lixo")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.leakPermission")));
            return true;
        }
        Player target;
        if(args != null && args.length > 0 && sender.hasPermission("draco.utils.lixo.others")) {
            target = Bukkit.getPlayerExact(args[0]);
        } else {
            if(!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.onlyPlayer")));
                return true;
            }
            target = (Player) sender;
        }
        if(target == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.playerNotFound")));
            return true;
        }
        target.openInventory(Bukkit.createInventory(null, 27, "§7§l"+label.toUpperCase()));
        target.playSound(target.getLocation(), Sound.CHEST_OPEN, 1.0f, 1.0f);
        return false;
    }
}
