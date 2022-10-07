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
			Player nPlayer;
			if(args.length > 0)
			switch (args[0].hashCode()) {
			case 109329021: //setup
				if(!prison.isAdmin(player)) {
					Prison.sendMessage(player, Message.HASNT_PERM);
					return false;
				}
				if(args.length > 1) {
					try {
						prison.create.create(player, Integer.parseInt(args[1]));
						return true;
					}catch (Exception e) {
						Prison.sendMessage(sender, e.getMessage());
					}
				}
				prison.create.create(player, 5);
				return true;

			case 113762:  //set
				if(args.length < 2) break;
				if(!prison.isAdmin(player)) {
					Prison.sendMessage(player, Message.HASNT_PERM);
					return false;
				}
				nPlayer = Bukkit.getPlayerExact(args[1]);
				if(nPlayer == null)
					Prison.sendMessage(sender, Message.PLAYER_NOT_FOUND);
				else {
					if(args.length > 2) {
						try {
							prison.setPrisoner(nPlayer, Double.parseDouble(args[2]));
						}catch (Exception e) {
							Prison.sendMessage(sender, e.getMessage());
						}
					}else {
						prison.setPrisoner(nPlayer, -1);
					}
					return true;
				}
				return false;

			case -934610812: //remove
				if(args.length < 2) break;
				if(!prison.isAdmin(player)) {
					Prison.sendMessage(player, Message.HASNT_PERM);
					return false;
				}
				nPlayer = Bukkit.getPlayerExact(args[1]);
				if(nPlayer == null)
					Prison.sendMessage(sender, Message.PLAYER_NOT_FOUND);
				else {
					prison.freePlayer(nPlayer);
					return true;
				}
				return false;
			case -934641255: //reload

				if(!player.hasPermission("prison.admin")) {
					Prison.sendMessage(player, Message.HASNT_PERM);
					return false;
				}
				prison.reload();
				Prison.sendMessage(player, Message.RELOAD);
				return true;
			default:
				help();
				return true;
			}
		}
		if(args.length > 0)
			if(args[0].equalsIgnoreCase("reload") && !isPlayer) {
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
