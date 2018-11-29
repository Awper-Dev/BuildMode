package me.awperdev;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.awperdev.command.MainHandler;
import me.awperdev.command.PlayerManager;
import me.awperdev.features.StopFeatures;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class BuildMode extends JavaPlugin {


    private static BuildMode instance;
    private static PlayerManager manager;
    private static WorldGuardPlugin wg;
    private File logfile;

    public static BuildMode getInstance() {
        return instance;
    }

    public static PlayerManager getManager() {
        return manager;
    }

    public static WorldGuardPlugin getWg() {
        return wg;
    }


    @Override
    public void onEnable() {
        instance = this;
        manager = new PlayerManager();
        wg = getWorldGuard();
        try {
            setupLogger();
        } catch (Exception e) {
            Bukkit.getLogger().warning(e.getMessage());
        }
        getCommand("buildmode").setExecutor(new MainHandler());
        Bukkit.getServer().getPluginManager().registerEvents(new StopFeatures(), this);
        Bukkit.getLogger().info("BuildMode ACTIVATED!");
        Bukkit.getLogger().info("PM Marni when having trouble with this plugin!");

    }

    @Override
    public void onDisable() {
        for (Player p : manager.getPlayers()) {
            manager.removePlayer(p);
        }
    }

    private void setupLogger() throws Exception {
        //creates logfile
        setupLogFile();
        //creates logger
        Logger logger = Logger.getLogger("me.awperdev.command.logging.BuildModeLogger");
        logger.setUseParentHandlers(false);
        FileHandler fh = new FileHandler(logfile.getPath());
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);

    }

    private void setupLogFile() throws Exception {
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }
        logfile = new File(this.getDataFolder(), "BuildMode.log");
        if (!logfile.exists()) {
            logfile.createNewFile();
        }
    }

    private WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            throw new RuntimeException();
        }

        return (WorldGuardPlugin) plugin;
    }
}
