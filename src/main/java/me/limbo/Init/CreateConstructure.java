package me.limbo.Init;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.limbo.Prison;

public class CreateConstructure{
	private final Prison prison;
	int x, xMin, xMax;
	int y, yMin, yMax;
	int z, zMin, zMax;
	
	CreateConstructure(){
		prison = Prison.getIntance();
	}
	
	public void create(Player player, int radius) {
		x = (int) player.getLocation().getX();
		y = (int) player.getLocation().getY();
		z = (int) player.getLocation().getZ();
		
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
}
