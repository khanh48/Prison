package me.limbo.Init;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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
	List<Blocks> oldBlock;
	List<Location> newBlock;
	
	public CreateConstructure(){
		prison = Prison.getIntance();
		oldBlock = new ArrayList<>();
		newBlock = new ArrayList<>();
	}
	
	public void create(Player player, int radius) {
		if(world != null) undo();
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
					oldBlock.add(new Blocks(loc, world.getBlockAt(loc)));
					world.getBlockAt(loc).setType(Material.GLASS);
					newBlock.add(loc);

					loc.setZ(zMax);
					oldBlock.add(new Blocks(loc, world.getBlockAt(loc)));
					world.getBlockAt(loc).setType(Material.GLASS);
					newBlock.add(loc);
				}
				
				for(int i = zMin; i <= zMax; i++) {
					loc.setX(i);
					loc.setY(j);
					loc.setZ(xMin);
					oldBlock.add(new Blocks(loc, world.getBlockAt(loc)));
					world.getBlockAt(loc).setType(Material.GLASS);
					newBlock.add(loc);

					loc.setZ(xMax);
					oldBlock.add(new Blocks(loc, world.getBlockAt(loc)));
					world.getBlockAt(loc).setType(Material.GLASS);
					newBlock.add(loc);
				}
			}

			for(int i = xMin; i <= xMax; i++) {
				for(int j = zMin; j <= zMax; j++) {
					loc.setX(i);
					loc.setY(j);
					loc.setZ(yMin);
					oldBlock.add(new Blocks(loc, world.getBlockAt(loc)));
					world.getBlockAt(loc).setType(Material.GLASS);
					newBlock.add(loc);

					loc.setZ(yMax);
					oldBlock.add(new Blocks(loc, world.getBlockAt(loc)));
					world.getBlockAt(loc).setType(Material.GLASS);
					newBlock.add(loc);
				}
			}
			
		});
	}
	
	void undo() {
		for (Location location : newBlock) {
			world.getBlockAt(location).setType(Material.AIR);
		}
		newBlock.clear();
		for (Blocks blocks : oldBlock) {
			world.setBlockData(blocks.location, blocks.block.getBlockData());
		}
		oldBlock.clear();
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
