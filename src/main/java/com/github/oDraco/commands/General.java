package com.github.oDraco.commands;

import com.github.oDraco.entities.enums.Rarity;
import com.github.oDraco.entities.enums.Type;
import com.github.oDraco.util.ItemUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class General implements CommandExecutor {

    private static final String commandBaseUsage = "§cUse /dracoutils <material|format|unbreakable>";
    private static final String commandFormatUsage = "§cUse /dracoutils format <raridade> <categoria> <nome>";


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
            commandSender.sendMessage(commandBaseUsage);
            return true;
        }
        Player player = (Player) commandSender;
        switch (args[0].toLowerCase()) {
            case "material":
                commandSender.sendMessage("§6§lINFO §aMaterial: §7§o"+((Player) commandSender).getInventory().getItemInHand().getType());
                return true;
            case "rarity":
                commandSender.sendMessage("§6§lINFO §eRaridades disponíveis:");
                for(Rarity r : Rarity.values()) {
                    commandSender.sendMessage(String.format("§%s%s §7(§o%s§7)", r.getColorCharacter(), r.getName(), r));
                }
                return true;
            case "type":
                commandSender.sendMessage("§6§lINFO §eTipos/categorias disponíveis:");
                for(Type t : Type.values()) {
                    commandSender.sendMessage(String.format("§7%s §7(§o%s§7)", t.getDescription(), t));
                }
                return true;
            case "format":
                if(args.length < 4) {
                    commandSender.sendMessage(commandFormatUsage);
                    return true;
                }
                Rarity rarity;
                Type type;
                try {
                    rarity = Rarity.valueOf(args[1].toUpperCase());
                    type = Type.valueOf(args[2].toUpperCase());
                } catch (IllegalArgumentException e) {
                    commandSender.sendMessage("§4§lERRO! §cRaridade/tipo inválido, utilize /dracoutils <rarity|type> para ver as raridades/tipos disponíveis.");
                    return true;
                }
                String name = args[3];
                for(int i=4; i<args.length; i++) {
                    name = String.join(" ", name, args[i]);
                }
                ItemStack item = player.getItemInHand();
                if(item == null || item.getType() == Material.AIR) {
                    commandSender.sendMessage("§4§lERRO! §cVocê precisa estar segurando um item para utilizar este comando!");
                    return true;
                }
                player.setItemInHand(ItemUtils.formatItem(item,name,rarity,type));
                return true;
            case "unbreaking":
            case "unbreakable":
                return handleUnbreakable(player, args);
            default:
                commandSender.sendMessage(commandBaseUsage);
        }
        return true;
    }

    private boolean handleUnbreakable(Player player, String[] args) {
        if(args.length < 2) {
            player.sendMessage("§cUse /dracoutils unbreakable <false|true>");
            return true;
        }
        boolean unbreaking;
        try {
            unbreaking = Boolean.parseBoolean(args[1]);
        } catch (Exception e) {
            player.sendMessage("§4§lERRO! §cUtilize somente true/false");
            return true;
        }
        ItemStack item = player.getItemInHand();
        if(item == null || item.getType() == Material.AIR) {
            player.sendMessage("§4§lERRO! §cVocê precisa estar segurando um item para utilizar este comando!");
            return true;
        }
        player.setItemInHand(ItemUtils.setUnbreakable(item, unbreaking));
        return true;
    }

}
