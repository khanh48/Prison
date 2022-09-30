package me.limbo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.limbo.Commands.RegisterCommands;
import me.limbo.Config.ConfigManager;
import me.limbo.Hooker.LuckPermsHooker;
import me.limbo.Hooker.VaultHooker;
import me.limbo.Init.CreateConstructure;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PrefixNode;

public class Prison extends JavaPlugin{
	public static final String PRISONER = "prisoner";
	private static Prison intance;
	private LuckPermsHooker luckPerms;
	private VaultHooker vault;
	public ConfigManager data;
	public CreateConstructure create;
	
	public Prison() {
		intance = this;
	}
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		data = new ConfigManager("data");
		vault = new VaultHooker();
		luckPerms = new LuckPermsHooker();
		create = new CreateConstructure();
		getServer().getPluginManager().registerEvents(create, this);
		new RegisterCommands();
		addPrefixToGroup(PRISONER, nonFormat("[&0&lPrisoner&r]"), 3);
	}
	
	public static Prison getIntance() {
		return intance;
	}
	
	public static String format(String msg) {
		return nonFormat("&3&l[Prison]&r " + msg);
	}

	public static String nonFormat(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}

	public static void sendMessage(CommandSender sender, String message) {
		sender.sendMessage(format(message));
	}
	
	public static boolean isPrisoner(Player player) {
	    return intance.vault.getPermissions().playerInGroup(player, PRISONER);
	}
	
	public void addPrefixForPlayer(Player player, String prefix, int priority) {
		User user = luckPerms.getLuckPerms().getPlayerAdapter(Player.class).getUser(player);
		PrefixNode node = PrefixNode.builder(prefix, priority).build();
		user.data().add(node);
	}
	
	public void addPrefixToGroup(String groupName, String prefix, int priority) {
		PrefixNode node = PrefixNode.builder(prefix, priority).build();
		Group group = luckPerms.getLuckPerms().getGroupManager().createAndLoadGroup(groupName).join();
		group.data().add(node);
	}
	
	public void setPrisoner(Player player) {
		vault.getPermissions().playerAddGroup(player, PRISONER);
	}

	public void unSetPrisoner(Player player) {
		vault.getPermissions().playerRemoveGroup(player, PRISONER);
	}
	
	public void createRegions(Player p) {

	}
	
	public ConfigManager getData() {
		return this.data;
	}
}
