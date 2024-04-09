package com.github.oDraco.commands.tabCompleters;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeneralTab implements TabCompleter {

    private final static String[] COMPLETIONS = new String[] {"format", "material", "unbreakable", "rarity", "type"};
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("draco.utils.general")) return null;
        if(args == null) {
            return Arrays.asList(COMPLETIONS);
        }
        if(args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], Arrays.asList(COMPLETIONS), new ArrayList<>());
        }
        return null;
    }
}
