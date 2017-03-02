package ca.poutineqc.base.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import ca.poutineqc.base.data.values.JSONSavableValue;

public class SItem implements JSONSavableValue {

	private static final String MATERIAL_NAME = "material";
	private static final String AMOUNT = "amount";
	private static final String DURABILITY = "durability";
	private static final String NAME = "name";
	private static final String ENCHANTMENTS = "enchantments";
	private static final String ENCHANTMENTS_NAME = "name";
	private static final String ENCHANTMENTS_VALUE = "value";
	private static final String LORE = "lore";

	protected Material material;
	protected byte amount;
	protected short durability;

	protected String name;
	protected List<String> lore;
	protected Map<Enchantment, Integer> enchantments;

	public SItem(Material material) {
		this.material = material;
		this.amount = 1;
		this.durability = 0;

		this.name = null;
		this.enchantments = new HashMap<Enchantment, Integer>();
		this.lore = null;
	}

	public SItem(ItemStack itemStack) {
		this.material = itemStack.getType();
		this.amount = (byte) itemStack.getAmount();
		this.durability = itemStack.getDurability();

		ItemMeta meta = itemStack.getItemMeta();
		this.name = meta.hasDisplayName() ? meta.getDisplayName() : null;
		this.enchantments = meta.hasEnchants() ? meta.getEnchants() : new HashMap<Enchantment, Integer>();
		this.lore = meta.hasLore() ? meta.getLore() : null;
	}

	public SItem(JsonObject json) {
		json.get(MATERIAL_NAME);
		json.get(MATERIAL_NAME).getAsString();
		material = Material.getMaterial(json.get(MATERIAL_NAME).getAsString());
		amount = json.get(AMOUNT).getAsByte();
		durability = json.get(DURABILITY).getAsShort();

		name = json.has(NAME) ? json.get(NAME).getAsString() : null;

		enchantments = new HashMap<Enchantment, Integer>();
		for (JsonElement element : json.get(ENCHANTMENTS).getAsJsonArray()) {
			JsonObject object = element.getAsJsonObject();
			enchantments.put(Enchantment.getByName(object.get(ENCHANTMENTS_NAME).getAsString()),
					object.get(ENCHANTMENTS_VALUE).getAsInt());
		}
		
		if (json.has(LORE)) {
			lore = new ArrayList<String>();
			for (JsonElement element : json.get(LORE).getAsJsonArray())
				lore.add(element.getAsString());
			
		} else
			lore = null;

	}

	public ConfigurationSection toConfigurationSection() {
		ConfigurationSection cs = new YamlConfiguration();

		return cs;
	}

	public ItemStack getItem() {
		ItemStack itemStack = new ItemStack(material, amount, durability);

		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		for (Entry<Enchantment, Integer> enchantment : enchantments.entrySet())
			meta.addEnchant(enchantment.getKey(), enchantment.getValue(), true);

		itemStack.setItemMeta(meta);
		return itemStack;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ItemStack))
			return false;

		ItemStack itemStack = (ItemStack) o;

		if (material != itemStack.getType())
			return false;

		if (durability != itemStack.getDurability())
			return false;

		if (itemStack.getItemMeta().hasDisplayName()) {
			if (name == null)
				return false;

			if (!Utils.isEqualOnColorStrip(itemStack.getItemMeta().getDisplayName(), name))
				return false;

		} else if (name != null)
			return false;

		if (itemStack.getItemMeta().hasEnchants()) {
			for (Entry<Enchantment, Integer> enchantment : itemStack.getItemMeta().getEnchants().entrySet())
				if (!hasEnchantement(enchantment.getKey(), enchantment.getValue()))
					return false;
		} else if (enchantments.size() > 0)
			return false;

		return true;
	}

	public void setData(short durability) {
		this.durability = durability;
	}

	public void setDisplayName(String displayName) {
		this.name = ChatColor.translateAlternateColorCodes('&', displayName);
	}

	public void addToLore(String loreLine) {
		if (lore == null)
			lore = new ArrayList<String>();
		
		lore.add(ChatColor.translateAlternateColorCodes('&', loreLine));
	}

	public void setLore(List<String> lore) {
		this.lore = lore;
	}

	public void clearLore() {
		lore = null;
	}

	public void addEnchantement(Enchantment enchantment, int level) {
		enchantments.put(enchantment, level);
	}

	public void setEnchantements(Map<Enchantment, Integer> enchantments) {
		this.enchantments = enchantments;
	}

	public void clearEnchantements() {
		enchantments.clear();
	}

	public int getMaxStackSize() {
		return material.getMaxStackSize();
	}

	public Material getMaterial() {
		return material;
	}

	public short getDurability() {
		return durability;
	}

	public boolean hasEnchantement(Enchantment enchantement) {
		for (Entry<Enchantment, Integer> entry : enchantments.entrySet())
			if (entry.getKey() == enchantement)
				return true;

		return false;
	}

	public boolean hasEnchantement(Enchantment enchantement, int value) {
		for (Entry<Enchantment, Integer> entry : enchantments.entrySet())
			if (entry.getKey() == enchantement)
				return entry.getValue() == value;

		return false;
	}

	public String getDisplayName() {
		return name;
	}

	public List<String> getLore() {
		return lore;
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject json = new JsonObject();

		json.addProperty(MATERIAL_NAME, material.name());
		json.addProperty(AMOUNT, amount);
		json.addProperty(DURABILITY, durability);

		if (name != null)
			json.addProperty(NAME, name);

		JsonArray enchantments = new JsonArray();
		for (Entry<Enchantment, Integer> enchantment : this.enchantments.entrySet()) {
			JsonObject element = new JsonObject();
			System.out.println(enchantment.getKey());
			element.addProperty(ENCHANTMENTS_NAME, enchantment.getKey().getName());
			element.addProperty(ENCHANTMENTS_VALUE, enchantment.getValue());
			enchantments.add(element);
		}
		json.add(ENCHANTMENTS, enchantments);

		if (lore != null) {
			JsonArray lore = new JsonArray();
			for (String loreRow : this.lore) {
				JsonPrimitive element = new JsonPrimitive(loreRow);
				lore.add(element);
			}

			json.add(LORE, lore);
		}

		return json;
	}
}