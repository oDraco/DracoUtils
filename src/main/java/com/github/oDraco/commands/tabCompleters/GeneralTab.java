package com.github.oDraco.commands.tabCompleters;

import com.github.oDraco.DracoUtils;
import com.github.oDraco.entities.enums.Rarity;
import com.github.oDraco.entities.enums.Type;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GeneralTab implements TabCompleter {

    private final static String[] COMPLETIONS = new String[]{"ip","hwid","time","rotate","biome","format", "material", "unbreakable", "rarity", "type", "localID", "item", "select", "name", "lore", "info"};

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("draco.utils.general")) return null;
        if (args == null) {
            return Arrays.asList(COMPLETIONS);
        }
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], Arrays.asList(COMPLETIONS), new ArrayList<>());
        }
        if(args[0].equalsIgnoreCase("format")) {
            if(args.length == 2)
                return StringUtil.copyPartialMatches(args[args.length-1], Arrays.stream(Rarity.values()).map(Enum::name).collect(Collectors.toList()), new ArrayList<>());
            if(args.length == 3)
                return StringUtil.copyPartialMatches(args[args.length-1], Arrays.stream(Type.values()).map(Enum::name).collect(Collectors.toList()), new ArrayList<>());
        }
        if(args[0].equalsIgnoreCase("biome")) {
            if(args.length == 2)
                return StringUtil.copyPartialMatches(args[args.length-1], Arrays.asList("list", "set"), new ArrayList<>());
            if(args.length == 3 && args[1].equalsIgnoreCase("set"))
                return StringUtil.copyPartialMatches(args[args.length-1], Arrays.stream(Biome.values()).map(Enum::name).collect(Collectors.toList()), new ArrayList<>());
        }
        if(args[0].equalsIgnoreCase("item")) {
            if(args.length == 3 && args[1].equalsIgnoreCase("give")) {
                File itemFolder = new File(DracoUtils.getInstance().getDataFolder() + File.separator+"items"+File.separator);
                if(itemFolder.list() == null)
                    return null;
                return StringUtil.copyPartialMatches(args[args.length - 1], Arrays.stream(itemFolder.list()).filter(x -> x.endsWith(".nbt")).map(x -> x.replace(".nbt", "")).collect(Collectors.toSet()), new ArrayList<>());
            }
        }
        return null;
    }
}
