package me.awperdev.command;

import me.awperdev.BuildMode;
import me.awperdev.command.logging.BuildModeLogger;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class PlayerManager {
    private List<Player> builders;
    private HashMap<Player, SavedInventory> inventories;
    private BuildMode buildMode = BuildMode.getInstance();
    private Logger logger = LogManager.getLogManager().getLogger("me.awperdev.command.logging.BuildModeLogger");

    public PlayerManager() {
        this.builders = new ArrayList<>();
        this.inventories = new HashMap<>();
    }

    public boolean playerInBuildMode(Player player) {
        return builders.contains(player);

    }

    public void removePlayer(Player player) {
        builders.remove(player);
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        player.getInventory().setContents(inventories.get(player).getInventory());
        player.getInventory().setArmorContents(inventories.get(player).getArmor());
        inventories.remove(player);
        removePerms(player);
        logger.info("meep");
        // logger.info(player.getName() + " with UUID " + player.getUniqueId() + ", disabled BuildMode");

    }

    public void addPlayer(Player player) {
        builders.add(player);
        SavedInventory savedInventory = new SavedInventory(player.getInventory().getContents(), player.getInventory().getArmorContents());
        inventories.put(player, savedInventory);
        player.getInventory().clear();
        player.setGameMode(GameMode.CREATIVE);
        addPerms(player);
        logger.info(player.getName() + " with UUID " + player.getUniqueId() + ", enabled BuildMode");
    }

    private void addPerms(Player player) {
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pex user " + player.getName() + " add worldedit.clipboard.*");
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pex user " + player.getName() + " add worldedit.generation.*");
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pex user " + player.getName() + " add worldedit.history.*");
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pex user " + player.getName() + " add worldedit.selection.*");
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pex user " + player.getName() + " add worldedit.wand");
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pex user " + player.getName() + " add worldedit.selection.*");
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pex user " + player.getName() + " add worldedit.fixwater");
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pex user " + player.getName() + " add worldedit.fixlava");
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pex user " + player.getName() + " add worldedit.region*");

    }

    private void removePerms(Player player) {
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pex user " + player.getName() + " remove worldedit.clipboard.*");
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pex user " + player.getName() + " remove worldedit.generation.*");
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pex user " + player.getName() + " remove worldedit.history.*");
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pex user " + player.getName() + " remove worldedit.selection.*");
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pex user " + player.getName() + " remove worldedit.wand");
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pex user " + player.getName() + " remove worldedit.selection.*");
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pex user " + player.getName() + " remove worldedit.fixwater");
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pex user " + player.getName() + " remove worldedit.fixlava");
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pex user " + player.getName() + " remove worldedit.region*");
    }


    public List<Player> getPlayers() {
        return builders;
    }

    public void setPlayers(List<Player> players) {
        this.builders = players;
    }

    public HashMap<Player, SavedInventory> getInventories() {
        return inventories;
    }

    public void setInventories(HashMap<Player, SavedInventory> inventories) {
        this.inventories = inventories;
    }
}
