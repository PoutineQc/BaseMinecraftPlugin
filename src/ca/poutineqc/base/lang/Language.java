package ca.poutineqc.base.lang;

import java.util.Collection;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.gson.JsonObject;

import ca.poutineqc.base.datastorage.UniversalSerializable;
import ca.poutineqc.base.datastorage.YAML;
import ca.poutineqc.base.plugin.Library;
import ca.poutineqc.base.plugin.PConfigKey;
import ca.poutineqc.base.plugin.PPlugin;
import ca.poutineqc.base.utils.Utils;

public class Language extends HashMap<Message, String> implements UniversalSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5172580943430082727L;

	public static final int MAX_STRING_LENGTH = 16;
	private static final String PRIMAL_KEY = "value";

	public static final String FOLDER_NAME = "languageFiles";

	YAML yamlFile;
	private boolean prefixBeforeEveryMessage;
	private boolean serverLanguage;

	public Language(PPlugin plugin, String fileName, boolean builtIn, boolean serverLanguage) {
		prefixBeforeEveryMessage = plugin.getConfig().getBoolean(PConfigKey.PREFIX.getKey(), true);

		this.serverLanguage = serverLanguage;
		yamlFile = new YAML(plugin, fileName, builtIn, FOLDER_NAME);
	}

	Language(Language defaultLanguage) {
		this.putAll(defaultLanguage);

		this.prefixBeforeEveryMessage = defaultLanguage.prefixBeforeEveryMessage;

		this.serverLanguage = true;
		this.yamlFile = defaultLanguage.yamlFile;
	}

	public void addMessages(PPlugin plugin, Collection<Message> messages) {
		for (Message message : messages) {
			String rawMessage = yamlFile.getYAML().getString(message.getKey(), message.getDefaultMessage());

			String refinedMessage = rawMessage.replaceAll("%p%", "&" + plugin.getPrimaryColor().getChar())
					.replaceAll("%s%", "&" + plugin.getSecondaryColor().getChar());

			this.put(message, Utils.color(refinedMessage));
		}
	}

	@Override
	public String get(Object key) {
		if (!(key instanceof Message))
			throw new IllegalArgumentException("The key must be a Message");
			
		Message message = (Message) key;
		if (this.containsKey(message))
			return super.get(message);
		else
			return Library.getLanguageManager().getDefault().get(message);
	}

	public void sendError(PPlugin plugin, CommandSender player, String string) {
		sendMessage(plugin, player, ChatColor.RED + ChatColor.stripColor(string));
	}

	public void sendError(PPlugin plugin, CommandSender player, Message message) {
		sendError(plugin, player, this.get(message));
		
	}

	public void sendMessage(PPlugin plugin, CommandSender player, Message message) {
		sendMessage(plugin, player, get(message));
	}

	public void sendMessage(PPlugin plugin, CommandSender player, String message) {
		if (prefixBeforeEveryMessage)
			player.sendMessage(ChatColor.translateAlternateColorCodes('&',
					get(PMessages.PREFIX).replace("%plugin%", plugin.getPrefix()) + " " + message));
		else
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}

	public String getLanguageName() {
		if (this.serverLanguage)
			return ChatColor.stripColor(this.get(PMessages.KEYWORD_SERVER));

		return ChatColor.stripColor(this.get(PMessages.LANGUAGE_NAME));
	}

	@Override
	public String toSString() {
		return pad(serverLanguage ? LanguagesManager.DEFAULT : yamlFile.getFileName().replace(".yml", ""));
	}

	@Override
	public String toString() {
		return "Language:{name:" + this.getLanguageName() + "}";
	}

	@Override
	public int getMaxToStringLength() {
		
		return MAX_STRING_LENGTH;
	}

	public static String getKey(ConfigurationSection cs) {
		return cs.getString(PRIMAL_KEY);
		
	}

	@Override
	public ConfigurationSection toConfigurationSection() {
		ConfigurationSection cs = new YamlConfiguration();
		cs.set(PRIMAL_KEY, serverLanguage ? LanguagesManager.DEFAULT : yamlFile.getFileName().replace(".yml", ""));
		return cs;
	}

	public static String getKey(JsonObject json) {
		return json.get(PRIMAL_KEY).getAsString();
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject json = new JsonObject();
		json.addProperty(PRIMAL_KEY,
				serverLanguage ? LanguagesManager.DEFAULT : yamlFile.getFileName().replace(".yml", ""));
		return json;
	}

	@Override
	public String getSqlDataTypeName() {
		return "VARCHAR(" + getMaxToStringLength() + ")";
	}

	@Override
	public String pad(String toPad) {
		return Utils.padLeft(toPad, getMaxToStringLength()).replace(' ', PAD_CHAR);
	}

	@Override
	public String unpad(String toUnpad) {
		return (toUnpad.replace(PAD_CHAR, ' ')).trim();
	}

	@Override
	public boolean isSame(UniversalSerializable o) {
		return toSString().equals(o.toSString());
	}
}
