package me.poutineqc.utils;

import org.bukkit.ChatColor;

public class Utils {


	public static boolean isEqualOnColorStrip(String l2, String l1) {
		return ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', l1))
				.equalsIgnoreCase(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', l2)));
	}
	
}
