package com.github.oDraco;

import com.github.oDraco.commands.Light;
import org.bukkit.plugin.java.JavaPlugin;
import com.github.oDraco.commands.General;

public class DracoUtils extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("  _____                        _    _ _   _ _     \n" +
                " |  __ \\                      | |  | | | (_) |    \n" +
                " | |  | |_ __ __ _  ___ ___   | |  | | |_ _| |___ \n" +
                " | |  | | '__/ _` |/ __/ _ \\  | |  | | __| | / __|\n" +
                " | |__| | | | (_| | (_| (_) | | |__| | |_| | \\__ \\\n" +
                " |_____/|_|  \\__,_|\\___\\___/   \\____/ \\__|_|_|___/\n" +
                "                                                  \n" +
                "                                                  ");
        getLogger().info("[DracoUtils] Iniciado com sucesso!");
        getCommand("dracoutils").setExecutor(new General());
        getCommand("luz").setExecutor(new Light());
    }
}