package ca.poutineqc.base.plugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.earth2me.essentials.Essentials;

public class EssentialsHandle {
	
	public static boolean isGodModeEnabled(Player player) {
		Essentials plugin = (Essentials) Bukkit.getPluginManager().getPlugin(Library.ESSENTIALS_NAME);
		return plugin.getUser(player).isGodModeEnabled();
	}

	public static void setGodModeEnabled(Player player, boolean enabled) {
		Essentials plugin = (Essentials) Bukkit.getPluginManager().getPlugin(Library.ESSENTIALS_NAME);
		plugin.getUser(player).setGodModeEnabled(enabled);
	}

}
