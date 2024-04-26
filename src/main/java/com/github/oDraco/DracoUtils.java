package com.github.oDraco;

import com.github.oDraco.commands.Light;
import com.github.oDraco.commands.tabCompleters.GeneralTab;
import com.github.oDraco.entities.listeners.QuitListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import com.github.oDraco.commands.General;

public class DracoUtils extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("dracoutils").setExecutor(new General());
        getCommand("dracoutils").setTabCompleter(new GeneralTab());

        getCommand("luz").setExecutor(new Light());

        Bukkit.getPluginManager().registerEvents(new QuitListener(), this);

        getLogger().info("\n  _____                        _    _ _   _ _     \n" +
                " |  __ \\                      | |  | | | (_) |    \n" +
                " | |  | |_ __ __ _  ___ ___   | |  | | |_ _| |___ \n" +
                " | |  | | '__/ _` |/ __/ _ \\  | |  | | __| | / __|\n" +
                " | |__| | | | (_| | (_| (_) | | |__| | |_| | \\__ \\\n" +
                " |_____/|_|  \\__,_|\\___\\___/   \\____/ \\__|_|_|___/\n" +
                "                                                  \n" +
                "                                                  ");
        getLogger().info("Iniciado com sucesso! Versão: " + getDescription().getVersion());
    }
}