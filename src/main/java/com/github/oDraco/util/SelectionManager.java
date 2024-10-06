package com.github.oDraco.util;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

public class SelectionManager {

    private static final Map<UUID, Entity> selections;
    private static final Set<UUID> selecting;

    static {
        selections = new HashMap<>();
        selecting = new HashSet<>();
    }

    public static void setSelection(Player player, Entity selection) {
        selections.put(player.getUniqueId(), selection);
    }

    public static Entity getSelection(Player player) {
        return selections.get(player.getUniqueId());
    }

    public static void clearSelection(Player player) {
        selections.remove(player.getUniqueId());
    }

    public static boolean isSelecting(Player player) {
        return selecting.contains(player.getUniqueId());
    }

    public static void setSelecting(Player player, boolean selecting) {
        if(selecting)
            SelectionManager.selecting.add(player.getUniqueId());
        else
            SelectionManager.selecting.remove(player.getUniqueId());
    }
}
