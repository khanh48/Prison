package me.limbo.Init;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import me.limbo.Prison;

public class Prisoner {
	public Player player;
	public Location oldLocation;
	public BossBar bar;
	public double time, timeLeft;
	
	public Prisoner(Player player, double time, double timeLeft, Location oldLocation) {
		this.player = player;
		this.time = time;
		this.timeLeft = timeLeft;
		this.oldLocation = oldLocation;
		bar = Bukkit.createBossBar("Time Left", BarColor.RED, BarStyle.SOLID, BarFlag.DARKEN_SKY);
		bar.addPlayer(player);
		if(time <= -1)
			bar.setProgress(1D); 
		else
			bar.setProgress(timeLeft / time); 
		bar.setVisible(true);
	}
	
	public void save() {
		Prison prison = Prison.getIntance();
		prison.data.getConfig().set("prisoners." + this.player.getName() + ".time", this.time);
		prison.data.getConfig().set("prisoners." + this.player.getName() + ".timeLeft", this.timeLeft);
		prison.data.getConfig().set("prisoners." + this.player.getName() + ".oldLocation", this.oldLocation);
		prison.data.saveConfig();
	}
}
