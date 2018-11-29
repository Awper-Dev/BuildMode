package me.awperdev.command;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.awperdev.BuildMode;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainHandler implements CommandExecutor {

    private WorldGuardPlugin worldGuardPlugin = BuildMode.getWg();
    private PlayerManager manager = BuildMode.getManager();
    private String prefix = ChatColor.RED + "[BuildMode] ";

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (!(player.hasPermission("BuildMode.access"))) {
                player.sendMessage(prefix + ChatColor.RED + "You do not have the permission to do this!");
                return true;
            }
            if (command.getName().equalsIgnoreCase("buildmode")) {
                if (manager.playerInBuildMode(player)) {
                    manager.removePlayer(player);
                    player.sendMessage(prefix + ChatColor.RED + "BuildMode disabled!");
                } else {
                    RegionManager regionManager = worldGuardPlugin.getRegionManager(player.getWorld());
                    ApplicableRegionSet regionSet = regionManager.getApplicableRegions(player.getLocation());
                    if (isInBuilderRegion(regionSet)) {
                        manager.addPlayer(player);
                        player.sendMessage(prefix + ChatColor.GREEN + ChatColor.BOLD + "You are now in build mode");
                        player.sendMessage(prefix + ChatColor.GREEN + ChatColor.BOLD + "You can not use any commands and you can't leave this region");
                        player.sendMessage(prefix + ChatColor.GREEN + ChatColor.BOLD + "Use /buildmode to disable this mode");
                    } else {
                        player.sendMessage(prefix + ChatColor.RED + "You are not in a build region!");
                    }
                }

            }
        }
        return true;
    }


    private boolean isInBuilderRegion(ApplicableRegionSet regionset) {
        for (ProtectedRegion region : regionset.getRegions()) {
            if (region.getId().equalsIgnoreCase("builder")) {
                return true;
            }
        }
        return false;
    }
}
