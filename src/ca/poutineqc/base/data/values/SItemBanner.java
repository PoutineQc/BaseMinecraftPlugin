package ca.poutineqc.base.data.values;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SItemBanner extends SItem {

	private static final String PATTERNS = "patterns";
	private static final String BACKGROUND_COLOR = "background";

	private DyeColor baseColor;
	private List<SPattern> patterns = new ArrayList<SPattern>();

	public SItemBanner() {
		super(Material.BANNER);
		this.baseColor = DyeColor.WHITE;
	}

	public SItemBanner(ItemStack itemStack) {
		super(itemStack);

		BannerMeta meta = (BannerMeta) itemStack.getItemMeta();
		baseColor = meta.getBaseColor();
		
		patterns = new ArrayList<SPattern>();
		for (Pattern pattern : meta.getPatterns())
			patterns.add(new SPattern(pattern));
	}

	public SItemBanner(CustomPattern pattern) {
		this(pattern, DyeColor.BLACK);
	}

	public SItemBanner(CustomPattern pattern, DyeColor dyeColor) {
		super(Material.BANNER);
		pattern.setPattern(this, dyeColor);
	}

	public SItemBanner(JsonObject json) {
		super(json);

		this.baseColor = DyeColor.getByColor(Color.fromRGB(json.get(BACKGROUND_COLOR).getAsInt()));

		for (JsonElement pattern : json.get(PATTERNS).getAsJsonArray())
			patterns.add(new SPattern(pattern.getAsJsonObject()));

	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject json = super.toJsonObject();

		json.addProperty(BACKGROUND_COLOR, baseColor.getColor().asRGB());

		JsonArray patterns = new JsonArray();
		for (SPattern pattern : this.patterns) {
			patterns.add(pattern.toJsonObject());
		}
		json.add(PATTERNS, patterns);

		return json;
	}

	@Override
	public ItemStack getItem() {
		ItemStack itemStack = super.getItem();
		BannerMeta meta = (BannerMeta) itemStack.getItemMeta();
		meta.setBaseColor(baseColor);


		List<Pattern> patterns = new ArrayList<Pattern>();
		for (SPattern pattern : this.patterns)
			patterns.add(pattern.getPattern());
		meta.setPatterns(patterns);

		itemStack.setItemMeta(meta);
		return itemStack;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SItemBanner))
			return false;

		SItemBanner banner = (SItemBanner) o;

		if (!banner.baseColor.equals(this.baseColor))
			return false;

		if (banner.patterns.size() != this.patterns.size())
			return false;

		for (int i = 0; i < patterns.size(); i++)
			if (!banner.patterns.get(i).equals(this.patterns.get(i)))
				return false;

		return super.equals(o);
	}

	public void add(Pattern pattern) {
		patterns.add(new SPattern(pattern));
	}

	public void setPatterns(List<Pattern> patterns) {
		this.patterns = new ArrayList<SPattern>();
		for (Pattern pattern : patterns)
			this.patterns.add(new SPattern(pattern));
	}

	public void clearPatterns() {
		patterns.clear();
	}

	public List<Pattern> getPatterns() {
		List<Pattern> patterns = new ArrayList<Pattern>();
		for (SPattern pattern : this.patterns)
			patterns.add(pattern.getPattern());
		return patterns;
	}

	public DyeColor getBaseColor() {
		return baseColor;
	}

	public enum CustomPattern {
		ARROW_RIGHT {
			@Override
			public void setPattern(SItemBanner banner, DyeColor dyeColor) {
				banner.baseColor = dyeColor;
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.RHOMBUS_MIDDLE));
				banner.patterns.add(new SPattern(dyeColor, PatternType.HALF_VERTICAL));
			}
		},
		ARROW_LEFT {
			@Override
			public void setPattern(SItemBanner banner, DyeColor dyeColor) {
				banner.baseColor = dyeColor;
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.RHOMBUS_MIDDLE));
				banner.patterns.add(new SPattern(dyeColor, PatternType.HALF_VERTICAL_MIRROR));
			}
		},
		ARROW_ACTUAL {
			@Override
			public void setPattern(SItemBanner banner, DyeColor dyeColor) {
				banner.baseColor = dyeColor;
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.CIRCLE_MIDDLE));
			}
		},
		ZERO {
			@Override
			public void setPattern(SItemBanner banner, DyeColor dyeColor) {
				banner.baseColor = DyeColor.WHITE;
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_BOTTOM));
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_TOP));
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_RIGHT));
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_LEFT));
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_DOWNLEFT));
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.BORDER));
			}
		},
		ONE {
			@Override
			public void setPattern(SItemBanner banner, DyeColor dyeColor) {
				banner.baseColor = DyeColor.WHITE;
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_TOP));
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT));
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_CENTER));
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.CURLY_BORDER));
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.BORDER));
			}
		},
		TWO {
			@Override
			public void setPattern(SItemBanner banner, DyeColor dyeColor) {
				banner.baseColor = DyeColor.WHITE;
				banner.patterns.add(new SPattern(dyeColor, PatternType.TRIANGLE_TOP));
				banner.patterns.add(new SPattern(dyeColor, PatternType.TRIANGLE_BOTTOM));
				banner.patterns.add(new SPattern(dyeColor, PatternType.SQUARE_TOP_LEFT));
				banner.patterns.add(new SPattern(dyeColor, PatternType.SQUARE_BOTTOM_RIGHT));
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.RHOMBUS_MIDDLE));
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_DOWNLEFT));
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.BORDER));
			}
		},
		THREE {
			@Override
			public void setPattern(SItemBanner banner, DyeColor dyeColor) {
				banner.baseColor = DyeColor.WHITE;
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_MIDDLE));
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.STRIPE_LEFT));
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_BOTTOM));
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_RIGHT));
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_TOP));
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.BORDER));
			}
		},
		FOUR {
			@Override
			public void setPattern(SItemBanner banner, DyeColor dyeColor) {
				banner.baseColor = dyeColor;
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL));
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_LEFT));
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM));
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_RIGHT));
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_MIDDLE));
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.BORDER));
			}
		},
		FIVE {
			@Override
			public void setPattern(SItemBanner banner, DyeColor dyeColor) {
				banner.baseColor = dyeColor;
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.HALF_VERTICAL_MIRROR));
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL_MIRROR));
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_BOTTOM));
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.DIAGONAL_RIGHT_MIRROR));
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_DOWNRIGHT));
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_TOP));
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.BORDER));
			}
		},
		SIX {
			@Override
			public void setPattern(SItemBanner banner, DyeColor dyeColor) {
				banner.baseColor = DyeColor.WHITE;
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_RIGHT));
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL));
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_BOTTOM));
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_MIDDLE));
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_LEFT));
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_TOP));
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.BORDER));
			}
		},
		SEVEN {
			@Override
			public void setPattern(SItemBanner banner, DyeColor dyeColor) {
				banner.baseColor = DyeColor.WHITE;
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_TOP));
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.SQUARE_BOTTOM_LEFT));
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_DOWNLEFT));
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.BORDER));
			}
		},
		EIGHT {
			@Override
			public void setPattern(SItemBanner banner, DyeColor dyeColor) {
				banner.baseColor = DyeColor.WHITE;
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_TOP));
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_BOTTOM));
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_MIDDLE));
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_LEFT));
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_RIGHT));
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.BORDER));
			}
		},
		NINE {
			@Override
			public void setPattern(SItemBanner banner, DyeColor dyeColor) {
				banner.baseColor = DyeColor.WHITE;
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_LEFT));
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL_MIRROR));
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_MIDDLE));
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_TOP));
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_RIGHT));
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.BORDER));
			}
		},
		SYMBOL_PLUS {
			@Override
			public void setPattern(SItemBanner banner, DyeColor dyeColor) {
				banner.baseColor = DyeColor.WHITE;
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_CENTER));
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.BORDER));
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_MIDDLE));
			}
		},
		SYMBOL_MINUS {
			@Override
			public void setPattern(SItemBanner banner, DyeColor dyeColor) {
				banner.baseColor = DyeColor.WHITE;
				banner.patterns.add(new SPattern(dyeColor, PatternType.STRIPE_MIDDLE));
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.BORDER));
			}
		},
		DOT {
			@Override
			public void setPattern(SItemBanner banner, DyeColor dyeColor) {
				banner.baseColor = DyeColor.WHITE;
				banner.patterns.add(new SPattern(dyeColor, PatternType.TRIANGLE_BOTTOM));
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.BORDER));
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.STRIPE_DOWNLEFT));
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.STRIPE_DOWNRIGHT));
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.STRIPE_LEFT));
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT));
				banner.patterns.add(new SPattern(DyeColor.WHITE, PatternType.CREEPER));
			}
		};

		abstract public void setPattern(SItemBanner banner, DyeColor dyeColor);

		public static CustomPattern getPattern(int number) {
			switch (number) {
			case 0:
				return CustomPattern.ZERO;
			case 1:
				return CustomPattern.ONE;
			case 2:
				return CustomPattern.TWO;
			case 3:
				return CustomPattern.THREE;
			case 4:
				return CustomPattern.FOUR;
			case 5:
				return CustomPattern.FIVE;
			case 6:
				return CustomPattern.SIX;
			case 7:
				return CustomPattern.SEVEN;
			case 8:
				return CustomPattern.EIGHT;
			case 9:
				return CustomPattern.NINE;
			default:
				return null;
			}
		}
	}
}