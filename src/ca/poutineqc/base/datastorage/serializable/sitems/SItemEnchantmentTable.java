package ca.poutineqc.base.datastorage.serializable.sitems;

import org.bukkit.Material;
import org.bukkit.block.EnchantingTable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import com.google.gson.JsonObject;

public class SItemEnchantmentTable extends SItem {

	private static final String CUSTOM_NAME = "customName";
	
	private String customName;
	
	public SItemEnchantmentTable(String customName) {
		super(Material.ENCHANTMENT_TABLE);
		this.customName = customName;
	}

	public SItemEnchantmentTable(ItemStack itemStack) {
		super(itemStack);
		BlockStateMeta meta = (BlockStateMeta) itemStack.getItemMeta();
		EnchantingTable state = (EnchantingTable) meta.getBlockState();
		
		this.customName = state.getCustomName();
	}

	public SItemEnchantmentTable(JsonObject json) {
		super(json);
		
		this.customName = json.get(CUSTOM_NAME).getAsString();
	}
	
	@Override
	public JsonObject toJsonObject() {
		JsonObject json = super.toJsonObject();
		json.addProperty(CUSTOM_NAME, customName);
		return json;
	}
	
	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
		EnchantingTable state = (EnchantingTable) meta.getBlockState();
		state.setCustomName(customName);
		meta.setBlockState(state);
		item.setItemMeta(meta);
		return item;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SItemEnchantmentTable))
			return false;
		
		SItemEnchantmentTable enchTable = (SItemEnchantmentTable) o;
		
		if (!super.equals(enchTable))
			return false;
		
		if (!this.customName.equals(enchTable.customName))
			return false;
		
		return super.equals(o);
	}

}
