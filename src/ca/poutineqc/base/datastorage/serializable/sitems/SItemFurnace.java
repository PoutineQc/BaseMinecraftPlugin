package ca.poutineqc.base.datastorage.serializable.sitems;

import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import com.google.gson.JsonObject;

import ca.poutineqc.base.datastorage.serializable.SInventory;

public class SItemFurnace extends SItemContainer {

	private static final String COOK_TIME = "cookTime";
	private static final String BURN_TIME = "burnTime";
	
	private short burnTime = 0;
	private short cookTime = 0;
	
	public SItemFurnace() {
		super(Material.FURNACE, 3);
	}

	public SItemFurnace(ItemStack itemStack) {
		super(itemStack);
		BlockStateMeta meta = (BlockStateMeta) itemStack.getItemMeta();
		Furnace state = (Furnace) meta.getBlockState();

		this.container = new SInventory(state.getInventory());
		this.customName = state.getCustomName();
		this.lockKey = state.getLock();
		this.burnTime = state.getBurnTime();
		this.cookTime = state.getCookTime();
	}

	public SItemFurnace(JsonObject json) {
		super(json);
		this.burnTime = json.get(BURN_TIME).getAsShort();
		this.cookTime = json.get(COOK_TIME).getAsShort();
	}
	
	@Override
	public JsonObject toJsonObject() {
		JsonObject json = super.toJsonObject();
		json.addProperty(COOK_TIME, cookTime);
		json.addProperty(BURN_TIME, burnTime);
		return json;
	}
	
	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
		Furnace state = (Furnace) meta.getBlockState();

		state.setCustomName(customName);
		state.setLock(lockKey);
		state.getInventory().setContents(container.getContents());
		state.setCookTime(cookTime);
		state.setBurnTime(burnTime);

		meta.setBlockState(state);
		item.setItemMeta(meta);
		return item;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SItemFurnace))
			return false;

		SItemFurnace furnace = (SItemFurnace) o;

		if (furnace.cookTime != this.cookTime)
			return false;

		if (furnace.burnTime != this.burnTime)
			return false;

		return super.equals(o);
	}
	
	public void setFuel(SItem fuel) {
		this.container.getItems()[1] = fuel;
	}
	
	public void setSmelting(SItem smelting) {
		this.container.getItems()[0] = smelting;
	}
	
	public void setResult(SItem result) {
		this.container.getItems()[2] = result;
	}

}
