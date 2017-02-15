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
		switch (colorCode.toLowerCase()) {
		case "&0":
			return ChatColor.BLACK;
		case "&1":
			return ChatColor.DARK_BLUE;
		case "&2":
			return ChatColor.DARK_GREEN;
		case "&3":
			return ChatColor.DARK_AQUA;
		case "&4":
			return ChatColor.DARK_RED;
		case "&5":
			return ChatColor.DARK_PURPLE;
		case "&6":
			return ChatColor.GOLD;
		case "&7":
			return ChatColor.GRAY;
		case "&8":
			return ChatColor.DARK_GRAY;
		case "&9":
			return ChatColor.BLUE;
		case "&a":
			return ChatColor.GREEN;
		case "&b":
			return ChatColor.AQUA;
		case "&c":
			return ChatColor.RED;
		case "&d":
			return ChatColor.LIGHT_PURPLE;
		case "&e":
			return ChatColor.YELLOW;
		case "&f":
			return ChatColor.WHITE;
		case "&k":
			return ChatColor.MAGIC;
		case "&l":
			return ChatColor.BOLD;
		case "&m":
			return ChatColor.STRIKETHROUGH;
		case "&n":
			return ChatColor.UNDERLINE;
		case "&o":
			return ChatColor.ITALIC;
		case "&r":
			return ChatColor.RESET;
		default:
			throw new InvalidParameterException("The String is not a color code");
		}
	}
}
