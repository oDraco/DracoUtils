package com.github.oDraco.commands;

import com.github.oDraco.DracoCore.api.Misc;
import com.github.oDraco.DracoUtils;
import com.github.oDraco.entities.enums.Rarity;
import com.github.oDraco.entities.enums.Type;
import com.github.oDraco.util.ItemUtils;
import com.github.oDraco.util.MiscUtils;
import com.github.oDraco.util.PlayerManager;
import com.github.oDraco.util.SelectionManager;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTFile;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.UUID;

public class General implements CommandExecutor {

    private static final String commandBaseUsage = "§cUse /dracoutils <info|material|format|unbreakable|localID|name|lore|ip|select|item|rotate|time|biome|entityinfo>";
    private static final String commandFormatUsage = "§cUse /dracoutils format <raridade> <categoria> <nome>";


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("draco.utils.general")) {
            sender.sendMessage("§4§lERRO! §cVocê não possui permissão para utilizar este comando!");
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(commandBaseUsage);
            return true;
        }

        boolean isPlayer = sender instanceof Player;
        boolean advMaterial = false;
        ItemStack hand = null;
        ItemMeta meta = null;

        if (isPlayer)
            hand = ((Player) sender).getItemInHand();
        if (hand != null && hand.getType() != Material.AIR)
            meta = hand.getItemMeta();
        switch (args[0].toLowerCase()) {
            case "info":
                advMaterial = true;
            case "material":
                if (!isPlayer) return sendOnlyPlayer(sender);
                TextComponent materialInfo = new TextComponent("§6§lINFO §a" + (advMaterial ? "Item" : "Material") + ": ");
                TextComponent material = new TextComponent("§7§o" + hand.getType().name() + (advMaterial ? ":" + hand.getDurability() : ""));
                material.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, hand.getType().name() + (advMaterial ? ":" + hand.getDurability() : "")));
                materialInfo.addExtra(material);
                ((Player) sender).spigot().sendMessage(materialInfo);
                return true;
            case "rarity":
                sender.sendMessage("§6§lINFO §eRaridades disponíveis:");
                for (Rarity r : Rarity.values()) {
                    sender.sendMessage(String.format("§%s%s §7(§o%s§7)", r.getColorCharacter(), r.getName(), r));
                }
                return true;
            case "type":
                sender.sendMessage("§6§lINFO §eTipos/categorias disponíveis:");
                for (Type t : Type.values()) {
                    sender.sendMessage(String.format("§7%s §7(§o%s§7)", t.getDescription(), t));
                }
                return true;
            case "lore":
                if (!isPlayer) return sendOnlyPlayer(sender);
                if (meta == null) return true;
                sender.sendMessage("§6§lINFO §aLore do Item:");
                meta.getLore().forEach(x -> {
                    TextComponent line = new TextComponent("§e- §r" + x);
                    line.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, x.replace('§', '&')));
                    ((Player) sender).spigot().sendMessage(line);
                });
                return true;
            case "name":
            case "nome":
                if (!isPlayer) return sendOnlyPlayer(sender);
                if (meta == null) return true;
                TextComponent name = new TextComponent("§6§lINFO §aNome do Item: §r" + meta.getDisplayName());
                name.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, meta.getDisplayName().replace('§', '&')));
                ((Player) sender).spigot().sendMessage(name);
                return true;
            case "format":
                if (!isPlayer) return sendOnlyPlayer(sender);
                return handleFormat((Player) sender, args);
            case "unbreaking":
            case "unbreakable":
                if (!isPlayer) return sendOnlyPlayer(sender);
                return handleUnbreakable((Player) sender, args);
            case "localid":
                if (!isPlayer) return sendOnlyPlayer(sender);
                return handleLocalID((Player) sender);
            case "ip":
                try {
                    return handleIP(sender);
                } catch (UnknownHostException e) {
                    sender.sendMessage("§4§lERRO! §cUm erro ocorreu ao buscar o IP do servidor: " + e.getMessage());
                }
            case "selecionar":
            case "select":
                if (!isPlayer) return sendOnlyPlayer(sender);
                boolean select = !SelectionManager.isSelecting((Player) sender);
                if (select)
                    sender.sendMessage("§2§lSUCESSO! §aClique com o direito em uma entidade para seleciona-lá!");
                else
                    sender.sendMessage("§2§lSUCESSO! §aSeleção de entidades desabilitada!");
                SelectionManager.setSelecting((Player) sender, select);
                return true;
            case "biome":
            case "bioma":
                if (!isPlayer) return sendOnlyPlayer(sender);
                return handleBiome((Player) sender, args, label);
            case "item":
                return handleItem(sender, args, label);
            case "rotate":
                if (!isPlayer) return sendOnlyPlayer(sender);
                return handleRotate((Player) sender, args, label);
            case "time":
            case "tempo":
                return handleTime(sender);
            case "hwid":
                return handleHWID(sender);
            case "entityinfo":
            case "entity":
                if (!isPlayer) return sendOnlyPlayer(sender);
                return handleEntityInfo((Player) sender);
            case "infinitepower":
                if (!DracoUtils.isDracoCoreLoaded()) {
                    PlayerManager.sendConfigMessage(sender, "messages.dracoCoreNeeded");
                    return true;
                }
                return handleInfinitePower(sender, label, args);
            case "test":
                if (!isPlayer) return sendOnlyPlayer(sender);
                return handleTest((Player) sender);
            default:
                sender.sendMessage(commandBaseUsage);
        }
        return true;
    }

    private static boolean handleTest(Player p) {
        return true;
    }

    private static boolean handleInfinitePower(CommandSender sender, String label, String[] args) {
        if(args.length < 3) {
            PlayerManager.sendMessage(sender, String.format("&cUse /%s %s <add|remove|clear> <UUID>", label, args[0]));
            return true;
        }
        if (args[1].equalsIgnoreCase("clear") || args[1].equalsIgnoreCase("limpar")) {
            Misc.getInfinitePower().clear();
            PlayerManager.sendConfigMessage(sender, "messages.successOperation");
            return true;
        }
        UUID uuid;
        try {
            uuid = UUID.fromString(args[2]);
        } catch (Exception e) {
            PlayerManager.sendConfigMessage(sender, "messages.invalidUUID");
            return true;
        }
        boolean add = false;
        switch (args[1].toLowerCase()) {
            case "add":
            case "adicionar":
                add = true;
            case "remove":
            case "remover":
                if(add)
                    Misc.addInfinitePower(uuid);
                else
                    Misc.getInfinitePower().remove(uuid);
                PlayerManager.sendConfigMessage(sender, "messages.successOperation");
                return true;
            default:
                PlayerManager.sendMessage(sender, String.format("&cUse /%s %s <add|remove|clear> <UUID>", label, args[0]));
                return true;
        }
    }

    private static boolean handleEntityInfo(Player p) {
        Entity e = SelectionManager.getSelection(p);
        if(e == null) {
            PlayerManager.sendConfigMessage(p, "messages.selectionNotFound");
            return true;
        }
        PlayerManager.sendMessage(p, "&m-+-------------------+-");
        PlayerManager.sendMessage(p, "&7Name: "+e.getName());
        if(e instanceof LivingEntity)
            PlayerManager.sendMessage(p, "&7Custom Name: "+((LivingEntity) e).getCustomName());
        PlayerManager.sendMessage(p, "&7UUID: "+e.getUniqueId());
        if(e instanceof LivingEntity)
            PlayerManager.sendMessage(p, String.format("&7Health: %.2f/%.2f", ((LivingEntity) e).getHealth(), ((LivingEntity) e).getMaxHealth()));
        PlayerManager.sendMessage(p, "&m-+-------------------+-");
        return true;
    }

    private static boolean handleHWID(CommandSender sender) {
        String hwid = MiscUtils.getHWID();
        String text = String.format("§6§lINFO! §aO HWID (em MD5) é: §7%s", hwid);
        if(sender instanceof Player) {
            TextComponent textComponent = new TextComponent(text);
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, hwid));
            ((Player) sender).spigot().sendMessage(textComponent);
        } else
            sender.sendMessage(text);
        return true;
    }

    private static boolean handleTime(CommandSender sender) {
        sender.sendMessage(String.format("§6§lINFO! §aCurrent time is: §e%s §7(§c%s§7)", LocalDateTime.now(), ZoneOffset.systemDefault()));
        return true;
    }

    private static boolean handleItem(CommandSender sender, String[] args, String label) {
        if (args.length < 3) {
            sender.sendMessage(String.format("§cUse /%s %s <save|give> <name> [<player>] [<amount>]", label, args[0]));
            return true;
        }

        String fileName = args[2] + ".nbt";
        ItemStack i;
        DracoUtils pl = DracoUtils.getInstance();
        File itemFolder = new File(pl.getDataFolder() + File.separator+"items"+File.separator);
        File iFile = new File(itemFolder.getPath() + File.separator + fileName);
        switch (args[1].toLowerCase()) {
            case "save":
            case "salvar":
                if (!(sender instanceof Player)) {
                    sender.sendMessage("§4§lERRO! §cEste comando só pode ser executado por jogadores!");
                    break;
                }
                Player p = (Player) sender;
                i = p.getItemInHand();
                if (!ItemUtils.isValid(i)) {
                    sender.sendMessage("§4§lERRO! §cVocê precisa estar segurando um item para utilizar este comando!");
                    break;
                }
                try {
                    if (!itemFolder.exists())
                        itemFolder.mkdirs();
                    NBTFile.saveTo(iFile, (NBTCompound) NBT.itemStackToNBT(i));
                    sender.sendMessage("§2§lSUCESSO! §aItem salvo como §7§o" + fileName + "§a!");
                } catch (Exception e) {
                    sender.sendMessage("§4§lERRO! §aUm erro ocorreu ao salvar o arquivo. Para mais informações veja o console.");
                    pl.getLogger().warning(String.format("Error while saving %s item file: %s", fileName, e.getMessage()));
                }
                break;
            case "give":
            case "givar":
            case "dar":
                if (args.length < 4) {
                    sender.sendMessage(String.format("§cUse /%s %s %s %s <player> [<amount>]", label, args[0], args[1], args[2]));
                    break;
                }
                if (!iFile.exists()) {
                    sender.sendMessage("§4§lERRO! §cItem não encontrado!");
                    break;
                }
                try {
                    Player target = Bukkit.getPlayer(args[3]);
                    if (target == null) {
                        sender.sendMessage("§4§lERRO! §cJogador não encontrado!");
                        break;
                    }
                    i = NBT.itemStackFromNBT(NBTFile.readFrom(iFile));
                    if (i == null)
                        throw new RuntimeException("Item not found. i == null");
                    int amount = 1;
                    if (args.length >= 5)
                        try {
                            amount = Integer.parseInt(args[4]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage("§4§lERRO! §cPor-favor utilize números válidos!");
                            break;
                        }
                    i.setAmount(amount);
                    ItemUtils.giveItem(target, i);
                } catch (Exception e) {
                    sender.sendMessage("§4§lERRO! §aUm erro ocorreu ao carregar o arquivo. Para mais informações veja o console.");
                    pl.getLogger().warning(String.format("Error while reading %s item file: %s", fileName, e.getMessage()));
                }
                break;
            default:
                sender.sendMessage(String.format("§cUse /%s %s <save|give> <name> [<player>] [<amount>]", label, args[0]));
        }
        return true;
    }

    private static boolean handleBiome(Player p, String[] args, String label) {
        if (!DracoUtils.isWorldEditLoaded()) {
            p.sendMessage("§4§lERRO! §cPara utilizar esta função é necessario o plugin §eWorldEdit§c!");
            return true;
        }
        if (args.length < 2) {
            p.sendMessage(String.format("§cUse /%s %s <list|set> [<biome>]", label, args[0]));
            return true;
        }
        if (args[1].equalsIgnoreCase("list")) {
            p.sendMessage("§6§lINFO! §aBiomas disponíveis: ");
            p.sendMessage("§7" + Arrays.toString(Biome.values()));
            return true;
        }
        if (args[1].equalsIgnoreCase("set")) {
            if (args.length < 3) {
                p.sendMessage(String.format("§cUse /%s %s %s <biome>", label, args[0], args[1]));
                return true;
            }
            Biome b;
            try {
                StringBuilder name = new StringBuilder(args[2]);
                if(args.length > 3)
                    for (int i = 0; i < args.length - 3; i++) {
                        name.append(" ");
                        name.append(args[3 + i]);
                    }
                b = Biome.valueOf(name.toString());
            } catch (IllegalArgumentException e) {
                p.sendMessage("§4§lERRO! §cBioma inexistente!");
                return true;
            }
            WorldEditPlugin we = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
            Selection selection = we.getSelection(p);
            if (selection == null) {
                p.sendMessage("§4§lERRO! §cVocê precisa de uma seleção do WorldEdit para utilizar este comando!");
                return true;
            }


            Location p1 = selection.getMaximumPoint();
            Location p2 = selection.getMinimumPoint();

            World w = p1.getWorld();

            int minX = Math.min(p1.getBlockX(), p2.getBlockX());
            int maxX = Math.max(p1.getBlockX(), p2.getBlockX());
            int minZ = Math.min(p1.getBlockZ(), p2.getBlockZ());
            int maxZ = Math.max(p1.getBlockZ(), p2.getBlockZ());

            int blockCount = 0;
            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    w.setBiome(x, z, b);
                    blockCount++;
                }
            }
            p.sendMessage("§2§lSUCESSO! §e"+blockCount+"§a bloco(s) foram mudados para §c§o"+b.name()+"§a!");
            return true;
        }
        p.sendMessage(String.format("§cUse /%s %s <list|set> [<biome>]", label, args[0]));
        return true;
    }

    private static boolean handleIP(CommandSender sender) throws UnknownHostException {
        Server server = Bukkit.getServer();
        String IP;

        try {
            URL whatsmyip = new URL("https://checkip.amazonaws.com/");
            BufferedReader ipReader = new BufferedReader(new InputStreamReader(whatsmyip.openConnection().getInputStream()));

            IP = ipReader.readLine() + ":" + server.getPort();

            ipReader.close();
        } catch (Exception e) {
            sender.sendMessage("§4§lERRO! §cUm erro ocorreu ao verificar o IP do servidor. Por-favor, verifique o console!");
            e.printStackTrace();
            return true;
        }

        TextComponent ipInfo = new TextComponent("§6§lINFO §aIP & Porta do Servidor: ");
        TextComponent ip = new TextComponent("§7§o" + IP);
        ip.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, IP));
        ipInfo.addExtra(ip);

        if (sender instanceof Player)
            ((Player) sender).spigot().sendMessage(ipInfo);
        else
            sender.sendMessage(ipInfo.getText());
        return true;
    }

    private static boolean handleUnbreakable(Player player, String[] args) {
        if (args.length < 2) {
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
        if (item == null || item.getType() == Material.AIR) {
            player.sendMessage("§4§lERRO! §cVocê precisa estar segurando um item para utilizar este comando!");
            return true;
        }
        player.setItemInHand(ItemUtils.setUnbreakable(item, unbreaking));
        return true;
    }

    private static boolean handleFormat(Player player, String[] args) {
        if (args.length < 4) {
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
        for (int i = 4; i < args.length; i++) {
            name = String.join(" ", name, args[i]);
        }
        ItemStack item = player.getItemInHand();
        if (!ItemUtils.isValid(item)) {
            player.sendMessage("§4§lERRO! §cVocê precisa estar segurando um item para utilizar este comando!");
            return true;
        }
        player.setItemInHand(ItemUtils.formatItem(item, name, rarity, type));
        return true;
    }

    private static boolean handleLocalID(Player player) {
        ItemStack item = player.getItemInHand();
        if (!ItemUtils.isValid(item)) {
            player.sendMessage("§4§lERRO! §cVocê precisa estar segurando um item para utilizar este comando!");
            return true;
        }
        try {
            TextComponent base = new TextComponent("§6§lINFO §aID Local: ");
            TextComponent id = new TextComponent("§7§o" + ItemUtils.getArmourersLocalID(item));
            id.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.valueOf(ItemUtils.getArmourersLocalID(item))));
            base.addExtra(id);
            player.spigot().sendMessage(base);
        } catch (IllegalStateException e) {
            player.sendMessage("§4§lERRO! §cO item que você segura não contém uma skin do Armourer's Workshop!");
        }
        return true;
    }

    private static boolean handleRotate(Player p, String[] args, String label) {
        if(args.length != 3) {
            p.sendMessage(String.format("§cUse /%s %s <pitch> <yaw>", label, args[0]));
            return true;
        }
        float pitch,yaw;
        try {
            pitch = Float.parseFloat(args[1]);
            yaw = Float.parseFloat(args[2]);
        } catch (NumberFormatException e) {
            p.sendMessage("§4§lERRO! §cPor-favor utilize números válidos!");
            return true;
        }
        Location loc = p.getLocation();
        loc.setPitch(pitch);
        loc.setYaw(yaw);
        p.teleport(loc);
        p.sendMessage("§2§lSUCESSO! §aPitch e Yaw ajustados!");
        return true;
    }

    private static boolean sendOnlyPlayer(CommandSender sender) {
        sender.sendMessage("§cEste comando só pode ser utilizado por jogadores!");
        return true;
    }
}
