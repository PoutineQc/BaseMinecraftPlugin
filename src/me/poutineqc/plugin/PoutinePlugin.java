package me.poutineqc.plugin;

import org.bukkit.plugin.java.JavaPlugin;

import me.poutineqc.lang.LanguagesManager;

public abstract class PoutinePlugin extends JavaPlugin {
	
	private static PoutinePlugin plugin;
	private LanguagesManager langs;
	
	@Override
	public void onEnable() {
		PoutinePlugin.plugin = this;
		saveDefaultConfig();
		
		
		/*
		langs = new LanguagesManager(this, BUILT_IN_LANGUAGES) {

			@Override
			public void addMessages(Language language) {
				for (Message message : Message.values()) {
					language.addMessage(message.getKey(), message.getDefaultValue());
				}
			}

			@Override
			public void setPrefixKey(Language language) {
				language.setPrefixKey(language.getMessage(Message.PREFIX.getKey()));
			}
		};
		*/
	}
	
	public static void main(String[] args) {
		System.out.println("fs");
	}

	@Override
	public void onDisable() {
	
	}
	
	public static PoutinePlugin get() {
		return plugin;
	}

	public LanguagesManager getLanguageManager() {
		return langs;
	}
}
