package com.github.oDraco.commands;

import com.github.oDraco.DracoUtils;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ResourcePack implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration cfg = DracoUtils.getInstance().getConfig();
        if(!(sender instanceof Player) && (args == null || args.length == 0)) {
            sender.sendMessage("§cUse /"+label+" <player> [<url>]");
            return true;
        }
        if(!sender.hasPermission("draco.utils.resource")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.leakPermission")));
            return true;
        }
        Player target;
        if(args != null && args.length > 0 && sender.hasPermission("draco.utils.resource.others")) {
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
        String URL = args != null && args.length >= 2 && sender.hasPermission("draco.utils.resource.custom") ? args[2] : cfg.getString("resourcePackURL");
        target.setResourcePack(URL);
        return true;
    }
}
