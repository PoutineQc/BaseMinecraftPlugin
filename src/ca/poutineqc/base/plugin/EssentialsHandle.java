package ca.poutineqc.base.plugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.earth2me.essentials.Essentials;

public class EssentialsHandle {
	
	public static boolean isGodModeEnabled(Player player) {
		Plugin plugin = Bukkit.getPluginManager().getPlugin(Library.ESSENTIALS_NAME);
		if (plugin instanceof Essentials)
			return ((Essentials) plugin).getUser(player).isGodModeEnabled();
		
		return false;
	}

	public static void setGodModeEnabled(Player player, boolean enabled) {
		Plugin plugin = Bukkit.getPluginManager().getPlugin(Library.ESSENTIALS_NAME);
		if (plugin instanceof Essentials)
			((Essentials) plugin).getUser(player).setGodModeEnabled(enabled);
	}

}
