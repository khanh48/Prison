package me.limbo.Init;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.limbo.Prison;

public class CreateConstructure implements Listener{
	private final Prison prison;
	public static Location location;
	public int x, xMin, xMax;
	public int y, yMin, yMax;
	public int z, zMin, zMax;
	World world;
	static List<Blocks> oldBlock;
	public static BukkitRunnable bossBarLoader;
	public static ConcurrentHashMap<UUID, Prisoner> prisoners;
	static int interval = 3, count = 0;
	
	
	public CreateConstructure(){
		prison = Prison.getIntance();
		oldBlock = new ArrayList<>();
		prisoners = new ConcurrentHashMap<>();
		load();
		
		bossBarLoader = new BukkitRunnable() {
			
			@Override
			public void run() {
				count++;
				
				if(prisoners.isEmpty()) {
					this.cancel();
					return;
				}
				
				for (Prisoner pr : prisoners.values()) {
					if(count == interval) {
						prison.data.getConfig().set("prisoners." + pr.player.getName() + ".time", pr.time);
						prison.data.getConfig().set("prisoners." + pr.player.getName() + ".timeLeft", pr.timeLeft);
					}
					if(pr.timeLeft == 0) {
						prison.freePlayer(pr.player);
					}
					if(pr.time < 0) return;
					double cur = pr.timeLeft / pr.time;
					pr.bar.setProgress(cur);
					pr.timeLeft -= 1;
				}
				if(count == interval) {
					prison.data.saveConfig();
					count = 0;
				}
				
			}
		};
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
			Location loc = new Location(world, x, y, z);
			
			for(int j = yMin + 1; j < yMax; j++) {
				for(int i = xMin; i <= xMax; i++) {
					loc.setX(i);
					loc.setY(j);
					loc.setZ(zMin);
					oldBlock.add(new Blocks(loc.clone(), world.getBlockAt(loc).getType()));
					world.getBlockAt(loc).setType(Material.IRON_BARS);

					loc.setZ(zMax);
					oldBlock.add(new Blocks(loc.clone(), world.getBlockAt(loc).getType()));
					world.getBlockAt(loc).setType(Material.IRON_BARS);
				}

				for(int i = zMin + 1; i < zMax; i++) {
					loc.setX(xMin);
					loc.setY(j);
					loc.setZ(i);
					oldBlock.add(new Blocks(loc.clone(), world.getBlockAt(loc).getType()));
					world.getBlockAt(loc).setType(Material.IRON_BARS);
					loc.setX(xMax);
					oldBlock.add(new Blocks(loc.clone(), world.getBlockAt(loc).getType()));
					world.getBlockAt(loc).setType(Material.IRON_BARS);
				}
			}

			for(int i = xMin; i <= xMax; i++) {
				for(int j = zMin; j <= zMax; j++) {
					loc.setX(i);
					loc.setY(yMin);
					loc.setZ(j);
					oldBlock.add(new Blocks(loc.clone(), world.getBlockAt(loc).getType()));
					world.getBlockAt(loc).setType(Material.SEA_LANTERN);

					loc.setY(yMax);
					oldBlock.add(new Blocks(loc.clone(), world.getBlockAt(loc).getType()));
					world.getBlockAt(loc).setType(Material.GLASS);
				}
			}
			save();
		});
	}
	
	void undo() {
		for (Blocks blocks : oldBlock) {
			world.getBlockAt(blocks.location).setType(blocks.block);
		}
		oldBlock.clear();
	}

	@EventHandler
	public void breakBlock(BlockBreakEvent e) {
		if(prison.isAdmin(e.getPlayer())) return;
		if(inside(e.getBlock().getLocation())) e.setCancelled(true);
	}

	@EventHandler
	public void placeBlock(BlockPlaceEvent e) {
		if(prison.isAdmin(e.getPlayer())) return;
		if(inside(e.getBlockPlaced().getLocation())) e.setCancelled(true);
	}

	@EventHandler
	public void tele(PlayerTeleportEvent e) {
		if(prison.isAdmin(e.getPlayer())) return;
		if(Prison.isPrisoner(e.getPlayer())) e.setCancelled(true);
	}

	@EventHandler
	public void useItem(PlayerItemConsumeEvent e) {
		if(prison.isAdmin(e.getPlayer())) return;
		if(!e.getItem().getType().equals(Material.CHORUS_FRUIT)) return;
		if(inside(e.getPlayer().getLocation())) e.setCancelled(true);
	}

	@EventHandler
	public void throwItem(PlayerInteractEvent e) {
		if(prison.isAdmin(e.getPlayer())) return;
		if(e.getMaterial().equals(Material.ENDER_PEARL)) {
			if(inside(e.getPlayer().getLocation())) e.setCancelled(true);
		}
		if(e.getClickedBlock() == null) return;
		if(e.getClickedBlock().getType().getKey().toString().toLowerCase().contains("door")) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void respawn(PlayerRespawnEvent e) {
		if(location == null) return;
		if(Prison.isPrisoner(e.getPlayer())) {
			e.setRespawnLocation(location);
		}
	}
	
	@EventHandler
	public void loadOnJoin(PlayerJoinEvent e) {
		int timeLeft = prison.data.getConfig().getInt("prisoners." + e.getPlayer().getName() + ".timeLeft");
		if(timeLeft != 0) {
			int totalTime = prison.data.getConfig().getInt("prisoners." + e.getPlayer().getName() + ".time");
			prisoners.put(e.getPlayer().getUniqueId(), new Prisoner(e.getPlayer(), totalTime, timeLeft));
			if(bossBarLoader.isCancelled())
				bossBarLoader.runTaskTimer(prison, 20, 20);
		}
	}
	
	@EventHandler
	public void saveOnLeave(PlayerQuitEvent e) {
		Prisoner tmp = prisoners.get(e.getPlayer().getUniqueId());
		if(tmp != null) {
			tmp.save();
			prisoners.remove(e.getPlayer().getUniqueId());
		}
	}
	
	@EventHandler
	public void saveOnDisable() {
		for (Prisoner prisoner : prisoners.values()) {
			prisoner.save();
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
	
	void load(){
		oldBlock.clear();
		ConfigurationSection con = Prison.getIntance().data.getConfig().getConfigurationSection("redo");
		if(con == null) return;
		
		for (String string : con.getKeys(false)) {
			Location loc;
			Material mat;
			loc = Prison.getIntance().data.getConfig().getLocation("redo." + string + ".location");
			mat = (Material) Prison.getIntance().data.getConfig().get("redo." + string + ".material");
			oldBlock.add(new Blocks(loc, mat));
		}
		
		location = prison.data.getConfig().getLocation("location");
		x = prison.data.getConfig().getInt("position.x");
		xMin = prison.data.getConfig().getInt("position.xMin");
		xMax = prison.data.getConfig().getInt("position.xMax");
		y = prison.data.getConfig().getInt("position.y");
		yMin = prison.data.getConfig().getInt("position.yMin");
		yMax = prison.data.getConfig().getInt("position.yMax");
		z = prison.data.getConfig().getInt("position.z");
		zMin = prison.data.getConfig().getInt("position.zMin");
		zMax = prison.data.getConfig().getInt("position.zMax");
		world = location.getWorld();
	}
	
	void save() {
		for(int i = 0; i < oldBlock.size(); i++) {
			Blocks blocks = oldBlock.get(i);
			prison.data.getConfig().set("redo." + i + ".location", blocks.location);
			prison.data.getConfig().set("redo." + i + ".material", blocks.block);
		}
		
		prison.data.getConfig().set("location", location);
		prison.data.getConfig().set("position.x", x);
		prison.data.getConfig().set("position.xMin", xMin);
		prison.data.getConfig().set("position.xMax", xMax);
		prison.data.getConfig().set("position.y", y);
		prison.data.getConfig().set("position.yMin", yMin);
		prison.data.getConfig().set("position.yMax", yMax);
		prison.data.getConfig().set("position.z", z);
		prison.data.getConfig().set("position.zMin", zMin);
		prison.data.getConfig().set("position.zMax", zMax);
		prison.data.saveConfig();
	}
}
