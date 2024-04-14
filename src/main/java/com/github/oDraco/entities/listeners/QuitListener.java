package com.github.oDraco.entities.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;

public class QuitListener implements Listener {

    @EventHandler
    public void onExit(PlayerQuitEvent e) {
        e.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
    }
}
