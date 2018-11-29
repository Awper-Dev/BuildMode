package me.awperdev.features;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.awperdev.BuildMode;
import me.awperdev.command.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;

import java.util.logging.LogManager;
import java.util.logging.Logger;

public class StopFeatures implements Listener {
    private PlayerManager manager = BuildMode.getManager();
    private WorldGuardPlugin worldGuardPlugin = BuildMode.getWg();
    private String prefix = ChatColor.RED + "[BuildMode] ";
    private BuildMode buildMode = BuildMode.getInstance();
    private Logger logger = LogManager.getLogManager().getLogger("me.awperdev.command.logging.BuildModeLogger");

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (manager.playerInBuildMode(event.getPlayer())) {
            if (event.getMessage().equals("/buildmode")) return;
            if (event.getMessage().substring(0, 2).equalsIgnoreCase("//")) return;
            Player p = event.getPlayer();
            p.sendMessage(prefix + ChatColor.RED + "You cannot use any commands while in BuildMode!");
            logger.info(event.getPlayer().getName() + " with UUID " + event.getPlayer().getUniqueId() + ", tried running " + event.getMessage() + " but failed! [in BuildMode]");
            event.setMessage("dummy");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (manager.playerInBuildMode(player)) {
            manager.removePlayer(player);
        }
    }

    @EventHandler
    public void playermove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (manager.playerInBuildMode(event.getPlayer())) {
            RegionManager regionManager = worldGuardPlugin.getRegionManager(event.getTo().getWorld());
            ApplicableRegionSet regionSet = regionManager.getApplicableRegions(event.getTo());
            if (!(isInBuilderRegion(regionSet))) {
                event.setCancelled(true);
                player.sendMessage(prefix + ChatColor.RED + "You can't leave this area while in BuildMode!");
            }
        }
    }

    @EventHandler
    public void playerDrop(PlayerDropItemEvent event) {
        if (manager.playerInBuildMode(event.getPlayer())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(prefix + ChatColor.RED + "You can't drop items while in BuildMode");
        }
    }

    @EventHandler
    public void playerpickup(PlayerPickupItemEvent event) {
        if (manager.playerInBuildMode(event.getPlayer())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(prefix + ChatColor.RED + "You can't pickup items while in BuildMode");
        }
    }

    @EventHandler
    public void playerBlockBreak(BlockBreakEvent event) {
        if (manager.playerInBuildMode(event.getPlayer())) {
            RegionManager rm = worldGuardPlugin.getRegionManager(event.getBlock().getWorld());
            ApplicableRegionSet set = rm.getApplicableRegions(event.getBlock().getLocation());
            if (!(isInBuilderRegion(set))) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(prefix + ChatColor.RED + "You can't break blocks outside of the building zone");
            }
        }
    }

    @EventHandler
    public void playerBlockPlace(BlockPlaceEvent event) {
        if (manager.playerInBuildMode(event.getPlayer())) {
            RegionManager rm = worldGuardPlugin.getRegionManager(event.getBlock().getWorld());
            ApplicableRegionSet set = rm.getApplicableRegions(event.getBlock().getLocation());
            if (!(isInBuilderRegion(set))) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(prefix + ChatColor.RED + "You can't place blocks outside of the building zone");
            }
        }
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent event) {
        if (manager.playerInBuildMode(event.getPlayer())) {
            if (event.getClickedBlock() == null) return;
            Block block = event.getClickedBlock();
            RegionManager rm = worldGuardPlugin.getRegionManager(block.getWorld());
            ApplicableRegionSet set = rm.getApplicableRegions(block.getLocation());
            if (!(isInBuilderRegion(set))) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(prefix + ChatColor.RED + "You can't interact with blocks outside of the building zone");
            }
        }
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
