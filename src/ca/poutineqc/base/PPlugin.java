package ca.poutineqc.base;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import ca.poutineqc.base.commands.CommandManager;
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
	
}
