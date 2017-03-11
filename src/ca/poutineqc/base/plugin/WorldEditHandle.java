package ca.poutineqc.base.plugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class WorldEditHandle {
	
	public static Selection getSelectedRegion(Player player) {
		Plugin plugin = Bukkit.getPluginManager().getPlugin(Library.WORLD_EDIT_NAME);
		if (!(plugin instanceof WorldEditPlugin))
			return null;
		
		
		WorldEditPlugin worldEdit = (WorldEditPlugin) plugin;
		Selection sel = worldEdit.getSelection(player);
		return sel;
	}

}
