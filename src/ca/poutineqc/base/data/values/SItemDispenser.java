package ca.poutineqc.base.data.values;

import org.bukkit.Material;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import com.google.gson.JsonObject;

public class SItemDispenser extends SItemContainer {

	public SItemDispenser() {
		super(Material.DISPENSER, 9);
	}

	public SItemDispenser(Inventory inventory) {
		super(Material.DISPENSER, inventory);
	}

	public SItemDispenser(ItemStack itemStack) {
		super(itemStack);
		BlockStateMeta meta = (BlockStateMeta) itemStack.getItemMeta();
		Dispenser state = (Dispenser) meta.getBlockState();

		this.container = new SInventory(state.getInventory());
		this.customName = state.getCustomName();
		this.lockKey = state.getLock();
	}

	public SItemDispenser(JsonObject json) {
		super(json);
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
		Dispenser state = (Dispenser) meta.getBlockState();

		state.setCustomName(customName);
		state.setLock(lockKey);
		state.getInventory().setContents(container.getContents());

		meta.setBlockState(state);
		item.setItemMeta(meta);
		return item;
	}

}
