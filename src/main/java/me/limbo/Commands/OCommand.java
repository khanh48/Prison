package me.limbo.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.limbo.Message.Message;
import me.limbo.Prison;

public class OCommand implements CommandExecutor, TabCompleter{
	Prison prison;
	List<String> cmds;
	OCommand(){
		cmds = new ArrayList<>();
		prison = Prison.getIntance();
		cmds.add("setup");
		cmds.add("set");
		cmds.add("remove");
		cmds.add("reload");
		cmds.sort((a, b) -> a.compareTo(b));
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		boolean isPlayer = false;
		if(sender instanceof Player) {
			isPlayer = true;
			Player player = (Player) sender;
			if(args.length > 0) {
				if(args[0].equalsIgnoreCase("setup")) {
					if(!prison.isAdmin(player)) {
						Prison.sendMessage(player, Message.HASNT_PERM);
						return false;
					}
					if(args.length > 1) {
						try {
							prison.create.create(player, Integer.parseInt(args[1]));
							return true;
						}catch (Exception e) {
							Prison.sendMessage(sender, Message.NUMBER_ERROR);
						}
					}
					prison.create.create(player, 5);
					return true;
				}
				if(args.length > 1) {
					if(args[0].equalsIgnoreCase("remove")) {
						if(!prison.isAdmin(player)) {
							Prison.sendMessage(player, Message.HASNT_PERM);
							return false;
						}
						Player nplayer = Bukkit.getPlayerExact(args[1]);
						if(nplayer == null)
							Prison.sendMessage(sender, Message.PLAYER_NOT_FOUND);
						else {
							prison.freePlayer(nplayer);
							return true;
						}
						return false;
					}
					else if(args[0].equalsIgnoreCase("set")) {
						if(!prison.isAdmin(player)) {
							Prison.sendMessage(player, Message.HASNT_PERM);
							return false;
						}
						if(args.length > 2) {
							Player nPlayer = Bukkit.getPlayerExact(args[1]);
							if(nPlayer == null)
								Prison.sendMessage(sender, Message.PLAYER_NOT_FOUND);
							else {
								try {
									prison.setPrisoner(nPlayer, Integer.parseInt(args[2]));
									return true;
								}catch (Exception e) {
									Prison.sendMessage(sender, Message.NUMBER_ERROR);
								}
							}
							return false;
						}
						else if(args.length > 1) {
							Player nPlayer = Bukkit.getPlayerExact(args[1]);
							if(nPlayer == null)
								Prison.sendMessage(sender, Message.PLAYER_NOT_FOUND);
							else {
								prison.setPrisoner(nPlayer, -1);
								return true;
							}
							return false;
						}
					}
				}
			}

		}

		if(args.length > 0)
			if(args[0].equalsIgnoreCase("reload")) {
				if(!sender.hasPermission("prison.admin")) {
					Prison.sendMessage(sender, Message.HASNT_PERM);
					return false;
				}
				prison.reload();
				Prison.sendMessage(sender, Message.RELOAD);
				return true;
			}
		if(!isPlayer) {
			Prison.sendMessage(sender, Message.CONSOLE);
			return false;
		}
		help();
		return false;
	}
	
	void help() {
		
	}

}
