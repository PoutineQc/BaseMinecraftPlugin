package ca.poutineqc.base.datastorage.serializable.sitems;

import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import com.google.gson.JsonObject;

import ca.poutineqc.base.datastorage.serializable.SInventory;
import ca.poutineqc.base.utils.ItemStackManager;

public class SItemBrewingStand extends SItemContainer {

	private static final String BREWING_TIME = "brewingTime";
	private static final String FUEL = "fuel";
	
	private int brewingTime = 0;
	private int fuel = 0;

	public SItemBrewingStand() {
		super(Material.BREWING_STAND_ITEM, 5);
	}

	public SItemBrewingStand(Inventory inventory) {
		super(Material.BREWING_STAND_ITEM, inventory);
	}

	public SItemBrewingStand(ItemStack itemStack) {
		super(itemStack);
		BlockStateMeta meta = (BlockStateMeta) itemStack.getItemMeta();
		BrewingStand state = (BrewingStand) meta.getBlockState();

		this.container = new SInventory(state.getInventory());
		this.customName = state.getCustomName();
		this.lockKey = state.getLock();
		this.brewingTime = state.getBrewingTime();
		this.fuel = state.getFuelLevel();
	}

	public SItemBrewingStand(JsonObject json) {
		super(json);
		this.brewingTime = json.get(BREWING_TIME).getAsInt();
		this.fuel = json.get(FUEL).getAsInt();
	}
	
	@Override
	public JsonObject toJsonObject() {
		JsonObject json = super.toJsonObject();
		json.addProperty(BREWING_TIME, brewingTime);
		json.addProperty(FUEL, fuel);
		return json;
	}
	
	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
		BrewingStand state = (BrewingStand) meta.getBlockState();

		state.setCustomName(customName);
		state.setLock(lockKey);
		state.getInventory().setContents(container.getContents());
		state.setBrewingTime(brewingTime);
		state.setFuelLevel(fuel);

		meta.setBlockState(state);
		item.setItemMeta(meta);
		return item;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SItemBrewingStand))
			return false;

		SItemBrewingStand brewingStand = (SItemBrewingStand) o;

		if (brewingStand.brewingTime != this.brewingTime)
			return false;

		if (brewingStand.fuel != this.fuel)
			return false;

		return super.equals(o);
	}
	
	public void setFuel(ItemStack fuel) {
		this.container.getItems()[4] = ItemStackManager.getSItem(fuel);
	}
	
	public void setIngredient(ItemStack ingredient) {
		this.container.getItems()[3] = ItemStackManager.getSItem(ingredient);
	}
	
	public void setResult(int position, ItemStack result) {
		this.container.getItems()[position] = ItemStackManager.getSItem(result);
	}

}
