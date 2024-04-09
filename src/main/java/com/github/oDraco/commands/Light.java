package com.github.oDraco.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Light implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("ERRO! Somente jogadores podem utilizar este comando!");
            return true;
        }
        if(!sender.hasPermission("draco.utils.luz")) {
            sender.sendMessage("§4§lERRO! §cVocê não possui permissão para utilizar este comando!");
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
