package me.limbo.Message;

import me.limbo.Prison;

public class Message {
	public static String HASNT_PERM = message("hasnt-perm");
	public static String CONSOLE = message("console");
	public static String RELOAD = message("reload");
	public static String NUMBER_ERROR = message("number-error");
	public static String PLAYER_NOT_FOUND = message("player-not-found");

	public static boolean contains(String src, String string) {
		return src.toLowerCase().contains(string.toLowerCase());
	}
	
	private static String message(String name) {
		return Prison.getIntance().message.getConfig().getString("message." + name);
	}

	public static String replace(String src, String target, String replacement) {
        return src.replaceAll("(?i)" + target, replacement);
    }
	
	public static void reload() {
		HASNT_PERM = message("hasnt-perm");
		CONSOLE = message("console");
		RELOAD = message("reload");
		PLAYER_NOT_FOUND = message("player-not-found");
	}
}
