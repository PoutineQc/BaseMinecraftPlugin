package ca.poutineqc.base.lang;

import java.util.Collection;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.google.gson.JsonObject;

import ca.poutineqc.base.data.YAML;
import ca.poutineqc.base.data.values.StringSavableValue;
import ca.poutineqc.base.data.values.UniversalSavableValue;
import ca.poutineqc.base.plugin.Library;
import ca.poutineqc.base.plugin.PConfigKey;
import ca.poutineqc.base.plugin.PPlugin;

public class Language extends HashMap<Message, String> implements UniversalSavableValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5172580943430082727L;

	public static final int MAX_STRING_LENGTH = 16;
	private static final String PRIMAL_KEY = "value";

	public static final String FOLDER_NAME = "languageFiles";

	private static ChatColor primary;
	private static ChatColor secondary;

	public static void setPrimaryAndSecondaryColors(ChatColor primary, ChatColor secondary) {
		Language.primary = primary;
		Language.secondary = secondary;
	};

	YAML yamlFile;
	private boolean prefixBeforeEveryMessage;
	private Message prefix;

	public Language(PPlugin plugin, String fileName, boolean builtIn, Message prefix) {
		prefixBeforeEveryMessage = plugin.getConfig().getBoolean(PConfigKey.PREFIX.getKey(), true);

		this.prefix = prefix;
		yamlFile = new YAML(plugin, fileName, builtIn, FOLDER_NAME);
	}

	public void sendMessage(Player player, Message message) {
		if (prefixBeforeEveryMessage)
			player.sendMessage(getMessage(prefix).replace("%plugin%", getMessage(message.getPrefixMessage())) + " "
					+ getMessage(message));
		else
			player.sendMessage(getMessage(message));
	}

	public String getMessage(Message message) {
		if (this.containsKey(message))
		return ChatColor.translateAlternateColorCodes('&', this.get(message)
				.replaceAll("%p%", "&" + primary.getChar()).replaceAll("%s%", "&" + secondary.getChar()));
		else
			return ChatColor.translateAlternateColorCodes('&', Library.getLanguageManager().getDefault().get(message)
					.replaceAll("%p%", "&" + primary.getChar()).replaceAll("%s%", "&" + secondary.getChar()));
	}

	public void addMessages(Collection<Message> messages) {
		for (Message message : messages)
			this.put(message, yamlFile.getYAML().getString(message.getKey(), message.getDefaultMessage()));
	}

	public String getLanguageName() {
		return ChatColor.stripColor(getMessage(PMessages.LANGUAGE_NAME));
	}

	@Override
	public String toSString() {
		return pad(yamlFile.getFileName().replace(".yml", ""));
	}

	@Override
	public int getMaxToStringLength() {
		return MAX_STRING_LENGTH;
	}

	public static String getKey(String value) {
		return StringSavableValue.unpad(value);
	}

	public static String getKey(ConfigurationSection cs) {
		return cs.getString(PRIMAL_KEY);
	}

	@Override
	public ConfigurationSection toConfigurationSection() {
		ConfigurationSection cs = new YamlConfiguration();
		cs.set(PRIMAL_KEY, yamlFile.getFileName().replace(".yml", ""));
		return cs;
	}

	public static String getKey(JsonObject json) {
		return json.get(PRIMAL_KEY).getAsString();
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject json = new JsonObject();
		json.addProperty(PRIMAL_KEY, yamlFile.getFileName().replace(".yml", ""));
		return json;
	}
}
