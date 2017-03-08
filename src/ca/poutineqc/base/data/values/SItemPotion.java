package ca.poutineqc.base.data.values;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SItemPotion extends SItem {

	private static final String BASE_POTION = "basePotion";
	private static final String BASE_POTION_TYPE = "type";
	private static final String BASE_POTION_EXTENDED = "extended";
	private static final String BASE_POTION_UPGRATED = "upgrated";
	private static final String EFFECTS = "effects";
	private static final String COLOR = "color";

	protected PotionData basePotion;
	protected List<SPotionEffect> effects = new ArrayList<SPotionEffect>();
	protected Color color;

	public SItemPotion(PotionEffectItem item, PotionData basePotion) {
		super(item.material);

		this.basePotion = basePotion;
	}

	public SItemPotion(ItemStack itemStack) {
		super(itemStack);

		PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
		this.basePotion = meta.getBasePotionData();

		if (meta.hasCustomEffects())
			for (PotionEffect effect : meta.getCustomEffects())
				effects.add(new SPotionEffect(effect));

		this.color = meta.getColor();

	}

	public SItemPotion(JsonObject json) {
		super(json);

		for (JsonElement effect : json.get(EFFECTS).getAsJsonArray())
			effects.add(new SPotionEffect(effect.getAsJsonObject()));

		JsonObject basePotionJson = json.get(BASE_POTION).getAsJsonObject();
		String potionTypeName = basePotionJson.get(BASE_POTION_TYPE).getAsString();
		for (PotionType type : PotionType.values())
			if (type.name().equals(potionTypeName))
				this.basePotion = new PotionData(type, basePotionJson.get(BASE_POTION_EXTENDED).getAsBoolean(),
						basePotionJson.get(BASE_POTION_UPGRATED).getAsBoolean());

		if (!json.get(COLOR).isJsonNull())
			this.color = Color.fromRGB(json.get(COLOR).getAsInt());
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject json = super.toJsonObject();

		JsonObject basePotion = new JsonObject();
		basePotion.addProperty(BASE_POTION_TYPE, this.basePotion.getType().name());
		basePotion.addProperty(BASE_POTION_EXTENDED, this.basePotion.isExtended());
		basePotion.addProperty(BASE_POTION_UPGRATED, this.basePotion.isUpgraded());
		json.add(BASE_POTION, basePotion);

		JsonArray effects = new JsonArray();
		for (SPotionEffect effect : this.effects)
			effects.add(effect.toJsonObject());

		json.add(EFFECTS, effects);

		json.addProperty(COLOR, color == null ? null : color.asRGB());

		return json;
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();

		PotionMeta meta = (PotionMeta) item.getItemMeta();

		meta.setBasePotionData(basePotion);

		for (SPotionEffect effect : this.effects)
			meta.addCustomEffect(effect.getPotionEffect(), true);

		meta.setColor(color);

		item.setItemMeta(meta);
		return item;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SItemPotion))
			return false;

		SItemPotion potion = (SItemPotion) o;

		if (!potion.basePotion.equals(this.basePotion))
			return false;

		if (potion.effects.size() != this.effects.size())
			return false;

		for (int i = 0; i < this.effects.size(); i++)
			if (!potion.effects.get(i).equals(this.effects.get(i)))
				return false;

		if (potion.color.asRGB() != this.color.asRGB())
			return false;

		return super.equals(o);
	}

	public enum PotionEffectItem {

		ARROW(Material.TIPPED_ARROW), POTION(Material.POTION), LINGERING_POTION(
				Material.LINGERING_POTION), SPLASH_POTION(Material.SPLASH_POTION);

		private Material material;

		private PotionEffectItem(Material material) {
			this.material = material;
		}

	}

	public void addEffect(SPotionEffect potionEffect) {
		this.effects.add(potionEffect);
	}

	public void clearEffects() {
		this.effects.clear();
	}

	public void setBasePotion(PotionData potionData) {
		this.basePotion = potionData;
	}

}
