package com.github.oDraco.entities.listeners;

import com.github.oDraco.util.SelectionManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class MiscListener implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        if (!SelectionManager.isSelecting(p)) return;
        SelectionManager.setSelection(p, e.getRightClicked());
        SelectionManager.setSelecting(p, false);
        String name = e.getRightClicked() instanceof LivingEntity && ((LivingEntity) e.getRightClicked()).getCustomName() != null ? ((LivingEntity) e.getRightClicked()).getCustomName() : e.getRightClicked().getType().name();
        p.sendMessage("§6§lINFO! §aVocê selecionou a entidade §7§o" + name);
    }
}
