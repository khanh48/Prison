package me.limbo.Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.limbo.Prison;

public class OCommand implements CommandExecutor, TabCompleter{
	Prison prison;
	
	OCommand(){
		prison = Prison.getIntance();
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length > 0)
			if(args[0].equalsIgnoreCase("setup"))
				prison.create.create((Player) sender, 10);
		return false;
	}

}
