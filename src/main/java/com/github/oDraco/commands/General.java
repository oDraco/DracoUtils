package com.github.oDraco.commands;

import com.github.oDraco.DracoUtils;
import com.github.oDraco.entities.enums.Rarity;
import com.github.oDraco.entities.enums.Type;
import com.github.oDraco.util.ItemUtils;
import com.github.oDraco.util.SelectionManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;

public class General implements CommandExecutor {

    private static final String commandBaseUsage = "§cUse /dracoutils <item|material|format|unbreakable|localID|name|lore|ip|select>";
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
        boolean advMaterial = false;
        ItemStack hand = player.getItemInHand();
        ItemMeta meta = null;
        if(hand != null && hand.getType() != Material.AIR)
            meta = hand.getItemMeta();
        switch (args[0].toLowerCase()) {
            case "item":
                advMaterial = true;
            case "material":
                TextComponent materialInfo = new TextComponent("§6§lINFO §a"+(advMaterial ? "Item":"Material")+": ");
                TextComponent material = new TextComponent("§7§o"+ hand.getType().name()+(advMaterial ? ":"+ hand.getDurability(): ""));
                material.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, hand.getType().name()+(advMaterial ? ":"+ hand.getDurability(): "")));
                materialInfo.addExtra(material);
                player.spigot().sendMessage(materialInfo);
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
            case "lore":
                if(meta == null) return true;
                commandSender.sendMessage("§6§lINFO §aLore do Item:");
                meta.getLore().forEach(x -> {
                    TextComponent line = new TextComponent("§e- §r" + x);
                    line.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, x.replace('§','&')));
                    player.spigot().sendMessage(line);
                });
                return true;
            case "name":
            case "nome":
                if(meta == null) return true;
                TextComponent name = new TextComponent("§6§lINFO §aNome do Item: §r" + meta.getDisplayName());
                name.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, meta.getDisplayName().replace('§', '&')));
                player.spigot().sendMessage(name);
                return true;
            case "format":
                return handleFormat(player, args);
            case "unbreaking":
            case "unbreakable":
                return handleUnbreakable(player, args);
            case "localid":
                return handleLocalID(player);
            case "ip":
                try {
                    return handleIP(player);
                } catch (UnknownHostException e) {
                    player.sendMessage("§4§lERRO! §cUm erro ocorreu ao buscar o IP do servidor: " + e.getMessage());
                }
            case "selecionar":
            case "select":
                boolean select = !SelectionManager.isSelecting(player);
                if(select)
                    player.sendMessage("§2§lSUCESSO! §aClique com o direito em uma entidade para seleciona-lá!");
                else
                    player.sendMessage("§2§lSUCESSO! §aSeleção de entidades desabilitada!");
                SelectionManager.setSelecting(player, select);
                return true;
            case "biome":
            case "bioma":
                return handleBiome(player, args, label);
            default:
                commandSender.sendMessage(commandBaseUsage);
        }
        return true;
    }

    private boolean handleBiome(Player p, String[] args, String label) {
        if(!DracoUtils.isWorldEditLoaded()) {
            p.sendMessage("§4§lERRO! §cPara utilizar esta função é necessario o plugin §eWorldEdit§c!");
            return true;
        }
        if(args.length < 2) {
            p.sendMessage(String.format("§cUse /%s %s <list|set> [<biome>]", label, args[0]));
            return true;
        }
        if(args[0].equalsIgnoreCase("list")) {
            p.sendMessage("§6§lINFO! §aBiomas disponíveis: ");
            p.sendMessage("§7"+ Arrays.toString(Biome.values()));
            return true;
        }
        if(args[0].equalsIgnoreCase("set")) {
            if(args.length < 3) {
                p.sendMessage(String.format("§cUse /%s %s %s <biome>", label, args[0], args[1]));
                return true;
            }
            Biome b;
            try {
                b = Biome.valueOf(args[2]);
            }
            catch (IllegalArgumentException e) {
                p.sendMessage("§4§lERRO! §cBioma inexistente!");
                return true;
            }
            // WIP
            return true;
        }
        p.sendMessage(String.format("§cUse /%s %s <list|set> [<biome>]", label, args[0]));
        return true;
    }

    private boolean handleIP(Player player) throws UnknownHostException {
        Server server = Bukkit.getServer();
        String IP;

        try {
            URL whatsmyip = new URL("https://checkip.amazonaws.com/");
            BufferedReader ipReader = new BufferedReader(new InputStreamReader(whatsmyip.openConnection().getInputStream()));

            IP = ipReader.readLine() + ":" + server.getPort();

            ipReader.close();
        }
        catch (Exception e) {
            player.sendMessage("§4§lERRO! §cUm erro ocorreu ao verificar o IP do servidor. Por-favor, verifique o console!");
            e.printStackTrace();
            return true;
        }

        TextComponent ipInfo = new TextComponent("§6§lINFO §aIP & Porta do Servidor: ");
        TextComponent ip = new TextComponent("§7§o"+ IP);
        ip.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, IP));
        ipInfo.addExtra(ip);

        player.spigot().sendMessage(ipInfo);
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

    private boolean handleFormat(Player player, String[] args) {
        if(args.length < 4) {
            player.sendMessage(commandFormatUsage);
            return true;
        }
        Rarity rarity;
        Type type;
        try {
            rarity = Rarity.valueOf(args[1].toUpperCase());
            type = Type.valueOf(args[2].toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage("§4§lERRO! §cRaridade/tipo inválido, utilize /dracoutils <rarity|type> para ver as raridades/tipos disponíveis.");
            return true;
        }
        String name = args[3];
        for(int i=4; i<args.length; i++) {
            name = String.join(" ", name, args[i]);
        }
        ItemStack item = player.getItemInHand();
        if(!ItemUtils.isValid(item)) {
            player.sendMessage("§4§lERRO! §cVocê precisa estar segurando um item para utilizar este comando!");
            return true;
        }
        player.setItemInHand(ItemUtils.formatItem(item,name,rarity,type));
        return true;
    }

    private boolean handleLocalID(Player player) {
        ItemStack item = player.getItemInHand();
        if(!ItemUtils.isValid(item)) {
            player.sendMessage("§4§lERRO! §cVocê precisa estar segurando um item para utilizar este comando!");
            return true;
        }
        try {
            TextComponent base = new TextComponent("§6§lINFO §aID Local: ");
            TextComponent id = new TextComponent("§7§o"+ItemUtils.getArmourersLocalID(item));
            id.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.valueOf(ItemUtils.getArmourersLocalID(item))));
            base.addExtra(id);
            player.spigot().sendMessage(base);
        }
        catch (IllegalStateException e) {
            player.sendMessage("§4§lERRO! §cO item que você segura não contém uma skin do Armourer's Workshop!");
        }
        return true;
    }

}
