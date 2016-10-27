package me.poutineqc.base;

import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {

	private static Plugin plugin;
	
	private PluginConfig config;
	
	@Override
	public void onEnable() {
		Plugin.plugin = this;
	}

	@Override
	public void onDisable() {
		
	}
	
	public static Plugin get() {
		return plugin;
	}
	
	public PluginConfig getConf() {
		return config;
	}
	
}
