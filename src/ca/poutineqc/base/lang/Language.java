package ca.poutineqc.base.lang;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import ca.poutineqc.base.data.values.SValue;
import ca.poutineqc.base.plugin.PConfigKey;
import ca.poutineqc.base.plugin.PPlugin;
import ca.poutineqc.base.utils.PYAMLFile;

public class Language implements SValue {

	public static final int MAX_STRING_LENGTH = 16;

	public static final String FOLDER_NAME = "languageFiles";

	private static ChatColor primary;
	private static ChatColor secondary;

	public static void setPrimaryAndSecondaryColors(ChatColor primary, ChatColor secondary) {
		Language.primary = primary;
		Language.secondary = secondary;
	};

	PYAMLFile yamlFile;
	private boolean prefixBeforeEveryMessage;
	private Map<Message, String> messages;
	private Message prefix;

	public Language(PPlugin plugin, String fileName, boolean builtIn, Message prefix) {
		messages = new HashMap<Message, String>();
		prefixBeforeEveryMessage = plugin.getConfig().getBoolean(PConfigKey.PREFIX.getKey(), true);

		this.prefix = prefix;
		yamlFile = new PYAMLFile(fileName, builtIn, FOLDER_NAME);
	}

	public void sendMessage(Player player, Message message) {
		if (prefixBeforeEveryMessage)
			player.sendMessage(getMessage(prefix).replace("%plugin%", PPlugin.get().getName()) + " " + getMessage(message));
		else
			player.sendMessage(getMessage(message));
	}

	public String getMessage(Message message) {
		return ChatColor.translateAlternateColorCodes('&', messages.get(message)
				.replaceAll("%p%", "&" + primary.getChar()).replaceAll("%s%", "&" + secondary.getChar()));
	}

	public void addMessages(Collection<Message> messages) {
		for (Message message : messages)
			this.messages.put(message, yamlFile.getString(message.getKey(), message.getDefaultMessage()));
	}

	public String getLanguageName() {
		return ChatColor.stripColor(getMessage(PMessages.LANGUAGE_NAME));
	}

	@Override
	public String toSString() {
		return pad(yamlFile.getName().replace(".yml", ""));
	}

	@Override
	public int getMaxToStringLength() {
		return MAX_STRING_LENGTH;
	}
}
