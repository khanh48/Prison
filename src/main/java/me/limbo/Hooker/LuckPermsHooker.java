package me.limbo.Hooker;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import me.limbo.Prison;
import net.luckperms.api.LuckPerms;

public class LuckPermsHooker {
    private LuckPerms lp;
    private Prison m;
	
	public LuckPermsHooker() {
		this.m = Prison.getIntance();
		if (!setupLuckPerms()) {
            Prison.sendMessage(m.getServer().getConsoleSender(), "&cCan't hook into LuckPerms!");
            Bukkit.getPluginManager().disablePlugin(m);
            return;
        }
	}
	
	 private boolean setupLuckPerms() {
		 if (Bukkit.getPluginManager().getPlugin("LuckPerms") == null) {
	            return false;
	        }

	        RegisteredServiceProvider<LuckPerms> rsp = m.getServer().getServicesManager().getRegistration(LuckPerms.class);
	        if (rsp == null) {
	            return false;
	        }
	        lp = rsp.getProvider();
	        return lp != null;
	   }
	 
	 public LuckPerms getLuckPerms() {
		 return this.lp;
	 }

}
