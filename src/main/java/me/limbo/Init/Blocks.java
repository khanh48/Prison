package me.limbo.Init;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;

public class Blocks {
	Location location;
	Material block;
	static List<Blocks> list;
	
	Blocks(Location loc, Material block){
		this.location = loc;
		this.block = block;
		list = new ArrayList<>();
		list.add(this);
	}
	
}
