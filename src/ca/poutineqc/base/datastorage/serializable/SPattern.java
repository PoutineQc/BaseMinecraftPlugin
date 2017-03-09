package ca.poutineqc.base.datastorage.serializable;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

import com.google.gson.JsonObject;

import ca.poutineqc.base.datastorage.JSONSerializable;

public class SPattern implements JSONSerializable {

	private static final String SHAPE = "shape";
	private static final String COLOR = "color";
	
	private PatternType shape;
	private DyeColor color;

	public SPattern(DyeColor color, PatternType shape) {
		this.shape = shape;
		this.color = color;
	}

	public SPattern(Pattern pattern) {
		this.shape = pattern.getPattern();
		this.color = pattern.getColor();
	}

	public SPattern(JsonObject json) {
		this.shape = PatternType.getByIdentifier(json.get(SHAPE).getAsString());
		this.color = DyeColor.getByColor(Color.fromRGB(json.get(COLOR).getAsInt()));
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject json = new JsonObject();
		json.addProperty(SHAPE, shape.getIdentifier());
		json.addProperty(COLOR, color.getColor().asRGB());
		return json;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SPattern))
			return false;
		
		SPattern pattern = (SPattern) o;
		
		if (!this.shape.equals(pattern.shape))
			return false;
		
		if (!this.color.equals(pattern.color))
			return false;
		
		return true;
	}
	
	public Pattern getPattern() {
		return new Pattern(color, shape);
	}

	public PatternType getShape() {
		return shape;
	}

	public void setShape(PatternType shape) {
		this.shape = shape;
	}

	public DyeColor getColor() {
		return color;
	}

	public void setColor(DyeColor color) {
		this.color = color;
	}

}
