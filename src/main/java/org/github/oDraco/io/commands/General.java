package org.github.oDraco.io.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class General implements CommandExecutor {

    private final String commandUsage = "§cUse /dracoutils <material>";


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(!commandSender.hasPermission("draco.utils.general")) {
            commandSender.sendMessage("§4§lERRO! §cVocê não possui permissão para utilizar este comando!");
            return true;
        }
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage("§cEste comando só pode ser utilizado por jogadores!");
            return true;
        }
        if(args.length < 1) {
            commandSender.sendMessage(commandUsage);
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "material":
                commandSender.sendMessage("§6§lINFO §aMaterial: §7§o"+((Player) commandSender).getInventory().getItemInHand().getType());
                return true;
            default:
                commandSender.sendMessage(commandUsage);
        }
        return true;
    }

}
