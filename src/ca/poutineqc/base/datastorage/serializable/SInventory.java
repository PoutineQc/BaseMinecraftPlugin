package ca.poutineqc.base.datastorage.serializable;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ca.poutineqc.base.datastorage.JSONSerializable;
import ca.poutineqc.base.datastorage.YAMLSerializable;
import ca.poutineqc.base.datastorage.serializable.sitems.SItem;
import ca.poutineqc.base.utils.ItemStackManager;

public class SInventory implements JSONSerializable, YAMLSerializable {

	private static final String SIZE = "size";
	private static final String CONTENTS = "contents";
	private static final String CONTENTS_POS = "pos";
	private static final String CONTENTS_ITEM = "item";

	private SItem[] items;

	public SInventory(int size) {
		items = new SItem[size];
	}

	public SInventory(Inventory inventory) {
		ItemStack[] contents = inventory.getContents();
		items = new SItem[contents.length];
		for (int i = 0; i < contents.length; i++)
			if (contents[i] != null)
				items[i] = ItemStackManager.getSItem(contents[i]);
	}

	public SInventory(JsonObject json) {
		items = new SItem[json.get(SIZE).getAsInt()];

		JsonArray jsonItems = json.get(CONTENTS).getAsJsonArray();
		for (JsonElement e : jsonItems) {
			JsonObject item = e.getAsJsonObject();
			items[item.get(CONTENTS_POS).getAsInt()] = ItemStackManager
					.fromFileFormat(item.get(CONTENTS_ITEM).getAsJsonObject());
		}

	}

	public SInventory(ConfigurationSection cs) {
		items = new SItem[cs.getInt(SIZE)];

		ConfigurationSection items = cs.getConfigurationSection(CONTENTS);
		for (String key : items.getKeys(false)) {
			this.items[Integer.parseInt(key)] = ItemStackManager.getSItem(items.getItemStack(key));
		}
	}

	@Override
	public ConfigurationSection toConfigurationSection() {
		ConfigurationSection config = new YamlConfiguration();
		config.set(SIZE, items.length);

		ConfigurationSection contents = new YamlConfiguration();
		for (int i = 0; i < items.length; i++)
			if (items[i] != null)
				contents.set(String.valueOf(i), items[i].getItem());

		config.set(CONTENTS, contents);
		return config;
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject json = new JsonObject();
		json.addProperty(SIZE, items.length);

		JsonArray jsonItems = new JsonArray();
		for (int i = 0; i < items.length; i++)
			if (items[i] != null) {
				JsonObject item = new JsonObject();
				item.addProperty(CONTENTS_POS, i);
				item.add(CONTENTS_ITEM, ItemStackManager.getFileFormat(items[i]));
				jsonItems.add(item);
			}

		json.add(CONTENTS, jsonItems);
		return json;
	}

	public SItem[] getItems() {
		return items;
	}

	public ItemStack[] getContents() {
		ItemStack[] contents = new ItemStack[items.length];
		for (int i = 0; i < contents.length; i++)
			if (items[i] != null)
				contents[i] = items[i].getItem();

		return contents;
	}

	public void setItem(int index, ItemStack item) {
		items[index] = ItemStackManager.getSItem(item);
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SInventory))
			return false;

		SInventory inventory = (SInventory) o;

		if (inventory.items.length != this.items.length)
			return false;

		for (int i = 0; i < items.length; i++)
			if (this.items[i] == null) {
				if (inventory.items[i] != null)
					return false;
				
			} else if (!this.items[i].equals(inventory.items[i]))
				return false;
		
		return true;
	}

}
