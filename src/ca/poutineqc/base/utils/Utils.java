package ca.poutineqc.base.utils;

import java.security.InvalidParameterException;

import org.bukkit.ChatColor;

public class Utils {

	public static boolean isEqualOnColorStrip(String l2, String l1) {
		return ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', l1))
				.equalsIgnoreCase(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', l2)));
	}

	public static String color(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	public static ChatColor getColor(String colorCode) throws InvalidParameterException {
		return ChatColor.getByChar(colorCode.toLowerCase().replace("&", ""));
	}
	
	public static String padRight(String s, int n) {
	     return String.format("%1$-" + n + "s", s);  
	}

	public static String padLeft(String s, int n) {
	    return String.format("%1$" + n + "s", s);  
	}
}
