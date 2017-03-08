package ca.poutineqc.base.data.values;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.google.gson.JsonObject;

public class SItemLeatherArmour extends SItem {

	private final static String COLOR = "color";
	
	private Color color;

	public SItemLeatherArmour(LeatherArmour leatherArmour, Color color) {
		super(leatherArmour.material);
		this.color = color;
	}

	public SItemLeatherArmour(ItemStack itemStack) {
		super(itemStack);

		LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
		this.color = meta.getColor();
	}

	public SItemLeatherArmour(JsonObject json) {
		super(json);
		
		this.color = Color.fromRGB(json.get(COLOR).getAsInt());
	}
	
	@Override
	public JsonObject toJsonObject() {
		JsonObject json = super.toJsonObject();
		json.addProperty(COLOR, this.color.asRGB());
		return json;
	}
	
	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(color);
		item.setItemMeta(meta);
		return item;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SItemBook))
			return false;

		SItemLeatherArmour armour = (SItemLeatherArmour) o;
		
		if (this.color.asRGB() == armour.color.asRGB())
			return false;
		
		return super.equals(o);
	}

	public enum LeatherArmour {
		HELMET(Material.LEATHER_HELMET), CHEST(Material.LEATHER_CHESTPLATE), LEGS(Material.LEATHER_LEGGINGS), BOOTS(Material.LEATHER_BOOTS);
		
		private Material material;
		
		private LeatherArmour(Material material) {
			this.material = material;
		}
	}

}
