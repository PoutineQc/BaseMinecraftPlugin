package ca.sebastienchagnon.minecraft.prolib;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import ca.sebastienchagnon.minecraft.prolib.commands.CommandManager;
import ca.sebastienchagnon.minecraft.prolib.lang.LanguagesManager;

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
