package ca.poutineqc.base.plugin;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import ca.poutineqc.base.commands.CommandManager;
import ca.poutineqc.base.instantiable.Playable;
import ca.poutineqc.base.lang.LanguagesManager;

public interface PPlugin {

	public ChatColor getPrimaryColor();
	public ChatColor getSecondaryColor();
	public boolean reload();
	public PluginDescriptionFile getDescription();
	public CommandManager getCommandManager();
	public LanguagesManager getLanguages();
	public ConfigurationSection getConfig();
	public JavaPlugin get();
	public boolean canBeReloaded();
	public String getPrefix();
	public Playable getGameFromArenaName(String string);
	public List<Playable> getGames();
	public void newArena(String string);
	public void deleteArena(Playable game);
	
}
