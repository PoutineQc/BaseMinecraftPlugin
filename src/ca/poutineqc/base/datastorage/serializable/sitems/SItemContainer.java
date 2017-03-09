package ca.poutineqc.base.datastorage.serializable.sitems;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonObject;

import ca.poutineqc.base.datastorage.serializable.SInventory;

public abstract class SItemContainer extends SItem {
	private static final String INVENTORY = "container";
	private static final String CUSTOM_NAME = "customName";
	private static final String LOCK_KEY = "key";

	protected SInventory container;
	protected String customName;
	protected String lockKey;

	public SItemContainer(Material material, int containerSize) {
		super(material);

		this.container = new SInventory(containerSize);
	}

	public SItemContainer(Material material, Inventory inventory) {
		super(material);
		this.container = new SInventory(inventory);
	}

	public SItemContainer(ItemStack itemStack) {
		super(itemStack);
	}

	public SItemContainer(JsonObject json) {
		super(json);
		this.container = new SInventory(json.get(INVENTORY).getAsJsonObject());

		if (!json.get(CUSTOM_NAME).isJsonNull())
			this.customName = json.get(CUSTOM_NAME).getAsString();
		
		this.lockKey = json.get(LOCK_KEY).getAsString();
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject json = super.toJsonObject();

		json.add(INVENTORY, container.toJsonObject());
		json.addProperty(CUSTOM_NAME, customName);
		json.addProperty(LOCK_KEY, lockKey);

		return json;
	}

	@Override
	public ItemStack getItem() {
		return super.getItem();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SItemChest))
			return false;

		SItemChest chest = (SItemChest) o;

		if (!this.container.equals(chest.container))
			return false;

		if (!this.customName.equals(chest.customName))
			return false;

		if (!this.lockKey.equals(chest.lockKey))
			return false;

		return super.equals(o);
	}

	public SInventory getContainer() {
		return container;
	}

	public void setContainer(SInventory container) {
		this.container = container;
	}

	public String getCustomName() {
		return customName;
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}

	public String getLockKey() {
		return lockKey;
	}

	public void setLockKey(String lockKey) {
		this.lockKey = lockKey;
	}

}
