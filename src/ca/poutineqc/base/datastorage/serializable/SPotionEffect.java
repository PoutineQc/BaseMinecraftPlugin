package ca.poutineqc.base.datastorage.serializable;

import org.bukkit.Color;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.google.gson.JsonObject;

import ca.poutineqc.base.datastorage.JSONSerializable;

public class SPotionEffect implements JSONSerializable {
	private static final String TYPE = "type";
	private static final String AMPLIFIER = "amplifier";
	private static final String DURATION = "duration";
	private static final String AMBIENT = "ambient";
	private static final String PARTICLES = "particles";
	private static final String COLOR = "color";

	private PotionEffectType type;
	private int amplifier;
	private int duration;
	private boolean ambient;
	private boolean particles;
	private Color color;

	public SPotionEffect(PotionEffect effect) {
		this.type = effect.getType();
		this.amplifier = effect.getAmplifier();
		this.duration = effect.getDuration();
		this.ambient = effect.isAmbient();
		this.particles = effect.hasParticles();
		this.color = effect.getColor();
	}

	public SPotionEffect(PotionEffectType type, int amplifier, int duration, boolean ambiant, boolean particles,
			Color color) {
		this.type = type;
		this.amplifier = amplifier;
		this.duration = duration;
		this.ambient = ambiant;
		this.particles = particles;
		this.color = color;
	}

	public SPotionEffect(JsonObject json) {
		System.out.println(json.toString());
		this.type = PotionEffectType.getByName(json.get(TYPE).getAsString());
		this.amplifier = json.get(AMPLIFIER).getAsInt();
		this.duration = json.get(DURATION).getAsInt();
		this.ambient = json.get(AMBIENT).getAsBoolean();
		this.particles = json.get(PARTICLES).getAsBoolean();

		if (!json.get(COLOR).isJsonNull())
			this.color = Color.fromRGB(json.get(COLOR).getAsInt());
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject json = new JsonObject();
		json.addProperty(TYPE, type.getName());
		json.addProperty(AMPLIFIER, amplifier);
		json.addProperty(DURATION, duration);
		json.addProperty(AMBIENT, ambient);
		json.addProperty(PARTICLES, particles);
		json.addProperty(COLOR, color != null ? color.asRGB() : null);
		return json;
	}

	public PotionEffect getPotionEffect() {
		return new PotionEffect(type, duration, amplifier, ambient, particles, color);
	}

	public void setType(PotionEffectType type) {
		this.type = type;
	}

	public void setAmplifier(int amplifier) {
		this.amplifier = amplifier;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void setAmbient(boolean ambient) {
		this.ambient = ambient;
	}

	public void setParticles(boolean particles) {
		this.particles = particles;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public PotionEffectType getType() {
		return type;
	}

	public int getAmplifier() {
		return amplifier;
	}

	public int getDuration() {
		return duration;
	}

	public boolean isAmbient() {
		return ambient;
	}

	public boolean hasParticles() {
		return particles;
	}

	public Color getColor() {
		return color;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SPotionEffect))
			return false;

		SPotionEffect effect = (SPotionEffect) o;

		if (!this.type.equals(effect.type))
			return false;

		if (this.amplifier != effect.amplifier)
			return false;

		if (this.duration != effect.duration)
			return false;

		if (this.ambient ^ effect.ambient)
			return false;

		if (this.particles ^ effect.particles)
			return false;

		if (this.color.asRGB() != effect.color.asRGB())
			return false;

		return true;
	}

}
