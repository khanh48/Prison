package me.limbo.Commands;

import me.limbo.Prison;

public class RegisterCommands {

	public RegisterCommands() {
		Prison.getIntance().getCommand("prison").setExecutor(new OCommand());
	}
}
