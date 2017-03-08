package ca.poutineqc.base.data.values;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SItemEnchantedBook extends SItem {

	private static final String ENCHANTMENTS = "storedEnchantments";
	private static final String ENCHANTMENT_NAME = "name";
	private static final String ENCHANTMENT_LEVEL = "level";

	private Map<Enchantment, Integer> storedEnchantments;

	public SItemEnchantedBook(Enchantment enchantment, int level) {
		super(Material.ENCHANTED_BOOK);
		this.storedEnchantments = new HashMap<Enchantment, Integer>();
		storedEnchantments.put(enchantment, level);
	}

	public SItemEnchantedBook(ItemStack itemStack) {
		super(itemStack);

		EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemStack.getItemMeta();
		this.storedEnchantments = meta.getEnchants();
	}

	public SItemEnchantedBook(JsonObject json) {
		super(json);

		this.storedEnchantments = new HashMap<Enchantment, Integer>();
		for (JsonElement storedEnchantment : json.get(ENCHANTMENTS).getAsJsonArray())
			storedEnchantments.put(
					Enchantment.getByName(storedEnchantment.getAsJsonObject().get(ENCHANTMENT_NAME).getAsString()),
					storedEnchantment.getAsJsonObject().get(ENCHANTMENT_LEVEL).getAsInt());

	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject json = super.toJsonObject();
		JsonArray enchantments = new JsonArray();
		for (Entry<Enchantment, Integer> enchantment : this.storedEnchantments.entrySet()) {
			JsonObject jsonEnchantment = new JsonObject();
			jsonEnchantment.addProperty(ENCHANTMENT_NAME, enchantment.getKey().getName());
			jsonEnchantment.addProperty(ENCHANTMENT_LEVEL, enchantment.getValue());
			enchantments.add(jsonEnchantment);
		}
		json.add(ENCHANTMENTS, enchantments);
		return json;
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
		for (Entry<Enchantment, Integer> enchantment : storedEnchantments.entrySet())
			meta.addStoredEnchant(enchantment.getKey(), enchantment.getValue(), true);
		
		item.setItemMeta(meta);
		return item;

	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SItemEnchantedBook))
			return false;

		SItemEnchantedBook item = (SItemEnchantedBook) o;
		
		if (item.storedEnchantments.size() != this.storedEnchantments.size())
			return false;
		
		for (Entry<Enchantment, Integer> enchantment : item.storedEnchantments.entrySet()) {
			if (!this.storedEnchantments.containsKey(enchantment.getKey()))
				return false;
			
			if (this.storedEnchantments.get(enchantment.getKey()) != enchantment.getValue())
				return false;
		}
		
		return super.equals(o);
	}

	public void addStoredEnchantment(Enchantment enchantment, int level) {
		this.storedEnchantments.put(enchantment, level);
	}

	public void clearStoredEnchantments() {
		this.storedEnchantments.clear();
	}

}
