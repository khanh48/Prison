package me.limbo;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.BukkitRegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import me.limbo.Hooker.LuckPermsHooker;
import me.limbo.Hooker.VaultHooker;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PrefixNode;

public class Prison extends JavaPlugin{
	static Prison intance;
	private LuckPermsHooker luckPerms;
	private VaultHooker vault;
	public Prison() {
		intance = this;
	}
	
	@Override
	public void onEnable() {
		vault = new VaultHooker();
		luckPerms = new LuckPermsHooker();
	}
	
	public static Prison getIntance() {
		return intance;
	}
	
	public static void sendMessage(CommandSender sender, String message) {
		
	}
	
	public static boolean isPlayerInGroup(Player player, String group) {
	    return player.hasPermission("group." + group);
	}
	
	public void addPrefixForPlayer(Player player, String prefix, int priority) {
		User user = luckPerms.getLuckPerms().getPlayerAdapter(Player.class).getUser(player);
		PrefixNode node = PrefixNode.builder(prefix, priority).build();
		user.data().add(node);
	}
	
	public void addPrefixToGroup(String groupName, String prefix, int priority) {
		PrefixNode node = PrefixNode.builder(prefix, priority).build();
		Group group = luckPerms.getLuckPerms().getGroupManager().getGroup(groupName);
		group.data().add(node);
	}
	
	public void addGroup(Player player, String group) {
		vault.getPermissions().playerAddGroup(player, group);
	}

	public void removeGroup(Player player, String group) {
		vault.getPermissions().playerRemoveGroup(player, group);
	}
	
	public void createRegions(Player p) {
	}
}
