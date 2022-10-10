package me.limbo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.limbo.Commands.RegisterCommands;
import me.limbo.Config.ConfigManager;
import me.limbo.Hooker.LuckPermsHooker;
import me.limbo.Hooker.VaultHooker;
import me.limbo.Init.CreateConstructure;
import me.limbo.Init.Prisoner;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.DisplayNameNode;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.node.types.PrefixNode;

public class Prison extends JavaPlugin{
	public static final String PRISONER = "prisoner";
	private static Prison intance;
	private LuckPermsHooker luckPerms;
	private VaultHooker vault;
	public ConfigManager data, message;
	public CreateConstructure create;
	
	public Prison() {
		intance = this;
	}
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		data = new ConfigManager("data");
		message = new ConfigManager("message.yml");
		vault = new VaultHooker();
		luckPerms = new LuckPermsHooker();
		create = new CreateConstructure();
		getServer().getPluginManager().registerEvents(create, this);
		new RegisterCommands();
		addPrefixToGroup(PRISONER, nonFormat("[&7&lPrisoner&r]"), 3);
		CreateConstructure.bossBarLoader.runTaskTimer(intance, 20, 20);
	}
	
	@Override
	public void onDisable() {
		create.saveOnDisable();
		sendMessage(Bukkit.getConsoleSender(), "Goodbye...");
		super.onDisable();
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
		DisplayNameNode displayNameNode = DisplayNameNode.builder("Prisoner").build();
		group.data().add(displayNameNode);
		group.data().add(node);
	}
	
	public void setPrisoner(Player player, double time) {
		CreateConstructure.prisoners.put(player.getUniqueId(), new Prisoner(player, time, time, player.getLocation()));
		this.addGroup(player, PRISONER);
		player.teleport(CreateConstructure.location);
	}

	public void unSetPrisoner(Player player) {
		this.removeGroup(player, PRISONER);
	}
	
	public void addGroup(Player player, String group) {
		if(Prison.isPrisoner(player)) return;
		User user = luckPerms.getLuckPerms().getPlayerAdapter(Player.class).getUser(player);
		user.data().add(InheritanceNode.builder(group).build());
	}
	
	public void removeGroup(Player player, String group) {
		if(!Prison.isPrisoner(player)) return;
		User user = luckPerms.getLuckPerms().getPlayerAdapter(Player.class).getUser(player);
		user.data().remove(InheritanceNode.builder(group).build());
	}
	
	boolean isPlayerInGroup(Player player, String group){
		return player.hasPermission("group." + group);
	}
	
	public ConfigManager getData() {
		return this.data;
	}
	
	public boolean isAdmin(Player player) {
		return player.hasPermission("prison.admin") || player.hasPermission("prison.manager");
	}
	
	public void reload() {
		
	}
	
	public void freePlayer(Player player) {
		unSetPrisoner(player);
		Prisoner p = CreateConstructure.prisoners.get(player.getUniqueId());
		if(p != null) {
			p.time = 0;
			p.timeLeft = 0;
			p.save();
			player.teleport(p.oldLocation);
			p.bar.setVisible(false);
			CreateConstructure.prisoners.remove(player.getUniqueId());
		}
	}
}
