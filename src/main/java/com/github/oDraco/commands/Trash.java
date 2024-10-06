package com.github.oDraco.commands;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Trash implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player) && (args == null || args.length == 0)) {
            sender.sendMessage("§cUtilize /"+label+" <player>");
            return true;
        }
        if(!sender.hasPermission("draco.utils.lixo")) {
            sender.sendMessage("§4§lERRO! §cVocê não possui permissão para utilizar este comando!");
            return true;
        }
        Player target;
        if(args != null && args.length > 0 && sender.hasPermission("draco.utils.lixo.others")) {
            target = Bukkit.getPlayerExact(args[0]);
        } else {
            if(!(sender instanceof Player)) {
                sender.sendMessage("ERRO! Somente jogadores podem utilizar este comando!");
                return true;
            }
            target = (Player) sender;
        }
        if(target == null) {
            sender.sendMessage("§4§lERRO! §cJogador não encontrado!");
            return true;
        }
        target.openInventory(Bukkit.createInventory(null, 27, "§7§l"+label.toUpperCase()));
        target.playSound(target.getLocation(), Sound.CHEST_OPEN, 1.0f, 1.0f);
        return false;
    }
}
