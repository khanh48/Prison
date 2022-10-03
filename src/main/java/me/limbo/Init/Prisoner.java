package me.limbo.Init;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import me.limbo.Prison;

public class Prisoner {
	public Player player;
	public BossBar bar;
	public int time, timeLeft;
	
	public Prisoner(Player player, int time, int timeLeft) {
		this.player = player;
		this.time = time;
		this.timeLeft = timeLeft;
		bar = Bukkit.createBossBar("Time Left", BarColor.RED, BarStyle.SOLID, BarFlag.DARKEN_SKY);
		bar.addPlayer(player);
		if(time <= -1)
			bar.setProgress(1D); 
		else
			bar.setProgress((double)timeLeft / time); 
		bar.setVisible(true);
	}
	
	void save() {
		Prison prison = Prison.getIntance();
		prison.data.getConfig().set("prisoners." + this.player.getPlayer().getName() + ".time", this.time);
		prison.data.getConfig().set("prisoners." + this.player.getPlayer().getName() + ".timeLeft", this.timeLeft);
		prison.data.saveConfig();
	}
}
