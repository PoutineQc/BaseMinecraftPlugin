package ca.poutineqc.base.datastorage.serializable.sitems;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ca.poutineqc.base.datastorage.serializable.SFireworkEffect;

public class SItemFirework extends SItem {

	private final static String EFFECTS = "effects";
	private final static String POWER = "power";

	private List<SFireworkEffect> effects;
	private int power;

	public SItemFirework(FireworkEffect fireworkEffect, int power) {
		super(Material.FIREWORK);

		this.effects = new ArrayList<SFireworkEffect>();
		this.effects.add(new SFireworkEffect(fireworkEffect));

		this.power = power;
	}

	public SItemFirework(Type type, List<Color> colors, List<Color> fadeColors, boolean flicker, boolean trail,
			int power) {
		super(Material.FIREWORK);

		this.effects = new ArrayList<SFireworkEffect>();
		this.effects.add(new SFireworkEffect(type, colors, fadeColors, flicker, trail));

		this.power = power;
	}

	public SItemFirework(ItemStack itemStack) {
		super(itemStack);
		FireworkMeta meta = (FireworkMeta) itemStack.getItemMeta();

		this.effects = new ArrayList<SFireworkEffect>();
		for (FireworkEffect effect : meta.getEffects())
			this.effects.add(new SFireworkEffect(effect));

		this.power = meta.getPower();
	}

	public SItemFirework(JsonObject json) {
		super(json);

		this.effects = new ArrayList<SFireworkEffect>();
		for (JsonElement effect : json.get(EFFECTS).getAsJsonArray())
			this.effects.add(new SFireworkEffect(effect.getAsJsonObject()));

		this.power = json.get(POWER).getAsInt();
	}

	public List<SFireworkEffect> getFireworkEffects() {
		return effects;
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject json = super.toJsonObject();
		JsonArray array = new JsonArray();
		for (SFireworkEffect effect : effects)
			array.add(effect.toJsonObject());
		json.add(EFFECTS, array);
		json.addProperty(POWER, power);
		return json;
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		FireworkMeta meta = (FireworkMeta) item.getItemMeta();
		for (SFireworkEffect effect : effects)
			meta.addEffect(effect.getFireworkEffect());

		meta.setPower(power);
		item.setItemMeta(meta);
		return item;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SItemFirework))
			return false;

		SItemFirework star = (SItemFirework) o;

		if (this.power != star.power)
			return false;

		if (this.effects.size() != star.effects.size())
			return false;

		for (int i = 0; i < this.effects.size(); i++)
			if (!this.effects.get(i).equals(star.effects.get(i)))
				return false;

		return super.equals(o);
	}

	public void setPower(int power) {
		this.power = power;
	}

	public int getPower() {
		return power;
	}

}
