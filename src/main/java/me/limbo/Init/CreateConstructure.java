package me.limbo.Init;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import me.limbo.Prison;

public class CreateConstructure implements Listener{
	private final Prison prison;
	boolean enable;
	Location location;
	int x, xMin, xMax;
	int y, yMin, yMax;
	int z, zMin, zMax;
	World world;
	
	public CreateConstructure(){
		prison = Prison.getIntance();
	}
	
	public void create(Player player, int radius) {
		location = player.getLocation();
		x = (int) player.getLocation().getX();
		y = (int) player.getLocation().getY();
		z = (int) player.getLocation().getZ();
		world = player.getWorld();
		
		xMin = x - radius;
		yMin = y - 1;
		zMin = z - radius;
		
		xMax = x + radius;
		yMax = y + radius;
		zMax = z + radius;
		
		Bukkit.getScheduler().runTask(prison, () -> {
			Location loc = new Location(player.getWorld(), x, y, z);
			
			for(int j = yMin; j <= yMax; j++) {
				for(int i = xMin; i <= xMax; i++) {
					loc.setX(i);
					loc.setY(j);
					loc.setZ(zMin);
					loc.getBlock().setType(Material.GLASS);

					loc.setZ(zMax);
					loc.getBlock().setType(Material.GLASS);
				}
				
				for(int i = zMin; i <= zMax; i++) {
					loc.setX(i);
					loc.setY(j);
					loc.setZ(xMin);
					loc.getBlock().setType(Material.GLASS);

					loc.setZ(xMax);
					loc.getBlock().setType(Material.GLASS);
				}
			}

			for(int i = xMin; i <= xMax; i++) {
				for(int j = zMin; j <= zMax; j++) {
					loc.setX(i);
					loc.setY(j);
					loc.setZ(yMin);
					loc.getBlock().setType(Material.GLASS);

					loc.setZ(yMax);
					loc.getBlock().setType(Material.GLASS);
				}
			}
			
		});
	}
	
	void undo() {
		
	}

	@EventHandler
	public void breakBlock(BlockBreakEvent e) {
		if(inside(e.getBlock().getLocation())) e.setCancelled(true);
	}

	@EventHandler
	public void placeBlock(BlockPlaceEvent e) {
		if(inside(e.getBlockPlaced().getLocation())) e.setCancelled(true);
	}

	@EventHandler
	public void tele(PlayerTeleportEvent e) {
		if(Prison.isPrisoner(e.getPlayer())) e.setCancelled(true);
	}

	@EventHandler
	public void useItem(PlayerItemConsumeEvent e) {
		if(!e.getItem().getType().equals(Material.CHORUS_FRUIT)) return;
		if(inside(e.getPlayer().getLocation())) e.setCancelled(true);
	}

	@EventHandler
	public void throwItem(PlayerInteractEvent e) {
		if(e.getMaterial().equals(Material.ENDER_PEARL)) {
			if(inside(e.getPlayer().getLocation())) e.setCancelled(true);
		}
		if(e.getClickedBlock().getType().getKey().toString().toLowerCase().contains("door")) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void respawn(PlayerRespawnEvent e) {
		if(Prison.isPrisoner(e.getPlayer())) {
			e.setRespawnLocation(location);
		}
	}
	
	boolean inside(Location loc) {
		double xt, yt, zt;
		xt = loc.getX();
		yt = loc.getY();
		zt = loc.getZ();
		if(xt > xMax || xt < xMin || yt > yMax || yt < yMin || zt > zMax || zt < zMin || !world.equals(loc.getWorld())) return false;
		return true;
	}
}
