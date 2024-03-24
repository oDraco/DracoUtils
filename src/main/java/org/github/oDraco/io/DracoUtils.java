package org.github.oDraco.io;

import org.bukkit.plugin.java.JavaPlugin;
import org.github.oDraco.io.commands.General;

public class DracoUtils extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("[DracoUtils] Iniciado com sucesso!");
        getCommand("dracoutil").setExecutor(new General());
    }
}