package ca.sebastienchagnon.minecraft.prolib.utils;

import java.security.InvalidParameterException;
import java.util.ListIterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;

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

	public static Block getFirstAvailableAirBlock() {
		ListIterator<World> it = Bukkit.getWorlds().listIterator();
		if (!it.hasNext())
			return null;
		
		World world = Bukkit.getWorlds().listIterator().next();
		Block block;

		int pos = 0;
		do {
			block = world.getHighestBlockAt(pos, 0);
			pos += (pos + 1);
		} while (block.getY() >= 256);

		return block;
	}

	public static String stripAll(String toStrip) {
		return ChatColor.stripColor(color(toStrip));
	}
}
