package ca.poutineqc.base.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;

import com.google.gson.JsonObject;

import ca.poutineqc.base.data.UniversalSavableValue;
import ca.poutineqc.base.data.values.SItem;
import ca.poutineqc.base.data.values.SLong;

public class ColorManager implements UniversalSavableValue {
	private static final String PRIMAL_KEY = "value";

	public static final int MAX_STRING_LENGTH = SLong.MAX_STRING_LENGTH;

	private SLong colorIndice;
	private List<SItem> allBlocks;
	private List<SItem> onlyChoosenBlocks;

	public ColorManager(String value) {
		this.colorIndice = new SLong(value);
		updateLists();
	}

	public ColorManager(long value) {
		this.colorIndice = new SLong(value);
		updateLists();
	}

	public ColorManager(ConfigurationSection cs) {
		this(cs.getLong(PRIMAL_KEY));
	}

	public ColorManager(JsonObject json) {
		this(json.get(PRIMAL_KEY).getAsLong());
	}

	public void setColorIndice(long value) {
		this.colorIndice = new SLong(value);
		updateLists();
	}

	public void updateLists() {
		allBlocks = new ArrayList<SItem>();
		onlyChoosenBlocks = new ArrayList<SItem>();
		long tempColorIndice = colorIndice.getLong();

		for (int i = 31; i >= 0; i--) {
			SItem icon;
			if (i >= 16)
				icon = new SItem(Material.STAINED_CLAY);
			else
				icon = new SItem(Material.WOOL);

			icon.setData((short) (i % 16));

			int value = (int) Math.pow(2, i);
			if (value <= tempColorIndice) {
				icon.addEnchantement(Enchantment.DURABILITY, 1);
				tempColorIndice -= value;
				onlyChoosenBlocks.add(0, icon);
			}

			allBlocks.add(0, icon);
		}

		if (onlyChoosenBlocks.size() == 0)
			onlyChoosenBlocks = allBlocks;
	}

	public SItem getRandomAvailableBlock() {
		return onlyChoosenBlocks.get((int) Math.floor(Math.random() * onlyChoosenBlocks.size()));
	}

	public List<SItem> getAllBlocks() {
		return allBlocks;
	}

	public List<SItem> getOnlyChoosenBlocks() {
		return onlyChoosenBlocks;
	}

	public long getColorIndice() {
		return colorIndice.getLong();
	}

	@Override
	public String toSString() {
		return colorIndice.toSString();
	}

	@Override
	public String toString() {
		return "ColorManager{indice=" + colorIndice + "}";
	}

	@Override
	public int getMaxToStringLength() {
		return MAX_STRING_LENGTH;
	}

	@Override
	public ConfigurationSection toConfigurationSection() {
		ConfigurationSection cs = new YamlConfiguration();
		cs.set(PRIMAL_KEY, colorIndice.getLong());
		return cs;
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject json = new JsonObject();
		json.addProperty(PRIMAL_KEY, colorIndice.getLong());
		return json;
	}
}
