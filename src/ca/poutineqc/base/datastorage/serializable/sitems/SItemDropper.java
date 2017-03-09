package ca.poutineqc.base.datastorage.serializable.sitems;

import org.bukkit.Material;
import org.bukkit.block.Dropper;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import com.google.gson.JsonObject;

import ca.poutineqc.base.datastorage.serializable.SInventory;

public class SItemDropper extends SItemContainer {

	public SItemDropper() {
		super(Material.DROPPER, 9);
	}

	public SItemDropper(Inventory inventory) {
		super(Material.DROPPER, inventory);
	}

	public SItemDropper(ItemStack itemStack) {
		super(itemStack);
		BlockStateMeta meta = (BlockStateMeta) itemStack.getItemMeta();
		Dropper state = (Dropper) meta.getBlockState();

		this.container = new SInventory(state.getInventory());
		this.customName = state.getCustomName();
		this.lockKey = state.getLock();
	}

	public SItemDropper(JsonObject json) {
		super(json);
	}
	
	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
		Dropper state = (Dropper) meta.getBlockState();

		state.setCustomName(customName);
		state.setLock(lockKey);
		state.getInventory().setContents(container.getContents());

		meta.setBlockState(state);
		item.setItemMeta(meta);
		return item;
	}

}
