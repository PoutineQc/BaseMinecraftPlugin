package ca.poutineqc.base.datastorage.serializable;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import ca.poutineqc.base.datastorage.JSONSerializable;

public class SFireworkEffect implements JSONSerializable {

	private static final String COLORS = "colors";
	private static final String FADE_COLORS = "fadeColors";
	private static final String TYPE = "type";
	private static final String FLICKER = "flicker";
	private static final String TRAIL = "trail";

	private List<Color> colors;
	private List<Color> fadeColors;
	private Type type;
	private boolean flicker;
	private boolean trail;

	public SFireworkEffect(FireworkEffect fireworkEffect) {
		this.type = fireworkEffect.getType();
		this.colors = fireworkEffect.getColors();
		this.fadeColors = fireworkEffect.getFadeColors();
		this.flicker = fireworkEffect.hasFlicker();
		this.trail = fireworkEffect.hasTrail();
	}

	public SFireworkEffect(Type type, List<Color> colors, List<Color> fadeColors, boolean flicker, boolean trail) {
		this.type = type == null ? Type.BALL : type;
		this.colors = colors == null ? new ArrayList<Color>() : colors;
		if (this.colors.size() == 0)
			this.colors.add(Color.fromRGB(11743532));

		this.fadeColors = fadeColors == null ? new ArrayList<Color>() : fadeColors;
		this.flicker = flicker;
		this.trail = trail;
	}

	public SFireworkEffect(JsonObject json) {
		String typeName = json.get(TYPE).getAsString();
		for (Type type : Type.values())
			if (type.name().equals(typeName))
				this.type = type;

		this.colors = new ArrayList<Color>();
		for (JsonElement color : json.get(COLORS).getAsJsonArray())
			this.colors.add(Color.fromRGB(color.getAsInt()));

		this.fadeColors = new ArrayList<Color>();
		for (JsonElement color : json.get(FADE_COLORS).getAsJsonArray())
			this.fadeColors.add(Color.fromRGB(color.getAsInt()));

		this.flicker = json.get(FLICKER).getAsBoolean();
		this.trail = json.get(TRAIL).getAsBoolean();
	}

	@Override
	public String toString() {
		return "SFireworkEffect" + ":" + toJsonObject().toString();
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject json = new JsonObject();

		json.addProperty(TYPE, this.type == null ? null : this.type.name());

		JsonArray jsonColors = new JsonArray();
		if (colors != null)
			for (Color color : colors)
				jsonColors.add(new JsonPrimitive(color.asRGB()));
		json.add(COLORS, jsonColors);

		JsonArray jsonFadeColors = new JsonArray();
		if (fadeColors != null)
			for (Color color : fadeColors)
				jsonFadeColors.add(new JsonPrimitive(color.asRGB()));
		json.add(FADE_COLORS, jsonFadeColors);

		json.addProperty(FLICKER, this.flicker);
		json.addProperty(TRAIL, this.trail);

		return json;
	}

	public FireworkEffect getFireworkEffect() {
		return FireworkEffect.builder().with(type).withColor(colors).withFade(fadeColors).flicker(flicker).trail(trail)
				.build();

	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SFireworkEffect))
			return false;

		SFireworkEffect effect = (SFireworkEffect) o;

		if (!effect.type.equals(this.type))
			return false;

		if (effect.colors.size() != this.colors.size())
			return false;

		out: for (Color c1 : this.colors) {
			for (Color c2 : effect.colors)
				if (c1.asRGB() == c2.asRGB())
					continue out;
			return false;
		}

		if (effect.fadeColors.size() != this.fadeColors.size())
			return false;

		out: for (Color c1 : this.fadeColors) {
			for (Color c2 : effect.fadeColors)
				if (c1.asRGB() == c2.asRGB())
					continue out;
			return false;
		}

		if (effect.trail != this.trail)
			return false;

		if (effect.flicker != this.flicker)
			return false;

		return true;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	public void setColors(List<Color> colors) {
		this.colors = colors;
	}

	public void addColor(Color color) {
		this.colors.add(color);
	}

	public List<Color> getColors() {
		return colors;
	}

	public void clearColors() {
		colors.clear();
	}

	public void setFadeColors(List<Color> fadeColors) {
		this.fadeColors = fadeColors;
	}

	public void addFadeColor(Color color) {
		this.fadeColors.add(color);
	}

	public List<Color> getFadeColors() {
		return fadeColors;
	}

	public void clearFadeColors() {
		fadeColors.clear();
	}

	public void setFlicker(boolean flicker) {
		this.flicker = flicker;
	}

	public boolean isFlicker() {
		return flicker;
	}

	public void setTrail(boolean trail) {
		this.trail = trail;
	}

	public boolean isTrail() {
		return trail;
	}

}
