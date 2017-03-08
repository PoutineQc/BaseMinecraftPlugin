package ca.poutineqc.base.data.values;

import java.util.List;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;

import com.google.gson.JsonObject;

public class SItemFireworkStar extends SItem {

	private final static String EFFECT = "effect";

	private SFireworkEffect effect;

	public SItemFireworkStar(FireworkEffect fireworkEffect) {
		super(Material.FIREWORK_CHARGE);

		this.effect = new SFireworkEffect(fireworkEffect);
	}

	public SItemFireworkStar(Type type, List<Color> colors, List<Color> fadeColors, boolean flicker, boolean trail) {
		super(Material.FIREWORK_CHARGE);

		this.effect = new SFireworkEffect(type, colors, fadeColors, flicker, trail);
	}

	public SItemFireworkStar(ItemStack itemStack) {
		super(itemStack);
		FireworkEffectMeta meta = (FireworkEffectMeta) itemStack.getItemMeta();
		this.effect = new SFireworkEffect(meta.getEffect());
	}

	public SItemFireworkStar(JsonObject json) {
		super(json);
		this.effect = new SFireworkEffect(json.get(EFFECT).getAsJsonObject());
	}

	public SFireworkEffect getFireworkEffect() {
		return effect;
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject json = super.toJsonObject();
		json.add(EFFECT, effect.toJsonObject());
		return json;
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		FireworkEffectMeta meta = (FireworkEffectMeta) item.getItemMeta();
		meta.setEffect(effect.getFireworkEffect());
		item.setItemMeta(meta);
		return item;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SItemFireworkStar))
			return false;

		SItemFireworkStar star = (SItemFireworkStar) o;

		if (!this.effect.equals(star.effect))
			return false;

		return super.equals(o);
	}

}
