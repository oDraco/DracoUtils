package com.github.oDraco.entities;

import com.sun.istack.internal.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;

public abstract class DracoPlugin extends JavaPlugin {

    protected final boolean handleCheck(@NotNull String url) {
        if(!isAllowed(url)) {
            getLogger().warning("Servidor não encontrado na whitelist! Plugin desligando...");
            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        }
        return true;
    }

    protected final boolean isAllowed(@NotNull String txt_url) {
        if(txt_url.isEmpty()) return true;
        boolean allow = false;

        try {
            String IP = InetAddress.getLocalHost().getHostAddress()+":"+getServer().getPort();
            URL url = new URL(txt_url);
            URLConnection urlConnection = url.openConnection();
            try(BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                String line = br.readLine();
                while(line != null && !line.isEmpty()) {
                    if(IP.equals(line)) {
                        allow = true;
                        break;
                    }
                    line = br.readLine();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            getLogger().severe("Um erro ocorreu ao verificar o plugin: " + e.getMessage());
            return false;
        }

        return allow;
    }
}
