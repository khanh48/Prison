package me.limbo.Init;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

import me.limbo.Prison;

public class Blocks {
	Location location;
	Block block;
	
	Blocks(Location loc, Block block){
		this.location = loc;
		this.block = block;
	}
	
	List<Blocks> load(){
		ConfigurationSection con = Prison.getIntance().getConfig().getConfigurationSection("oldBlocks");
		if(con == null) return null;
		
		for (String string : con.getKeys(false)) {
		}
	}
}
