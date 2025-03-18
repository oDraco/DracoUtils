package com.github.oDraco.entities.inventory;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class CmdIconImpl implements IIcon {

    private final ItemStack icon;
    private final String command;
    @Setter
    @Getter
    private boolean closeGUI = false;

    public CmdIconImpl(ItemStack icon, String command) {
        this.icon = icon;
        this.command = command;
    }

    public CmdIconImpl(ItemStack icon, String command, boolean closeGUI) {
        this(icon, command);
        this.closeGUI = closeGUI;
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if(closeGUI)
            p.closeInventory();
        p.performCommand(command.replace("{player}", p.getName()));
    }

    @Override
    public ItemStack getIcon() {
        return icon;
    }
}
