package ca.poutineqc.base.data.values;

import org.bukkit.Material;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import com.google.gson.JsonObject;

public class SItemHopper extends SItemContainer {

	public SItemHopper(boolean inMinecart) {
		super(inMinecart ? Material.HOPPER_MINECART : Material.HOPPER, 5);
	}

	public SItemHopper(Inventory inventory, boolean inMinecart) {
		super(inMinecart ? Material.HOPPER_MINECART : Material.HOPPER, inventory);
	}

	public SItemHopper(ItemStack itemStack) {
		super(itemStack);
		BlockStateMeta meta = (BlockStateMeta) itemStack.getItemMeta();
		Hopper state = (Hopper) meta.getBlockState();

		this.container = new SInventory(state.getInventory());
		this.customName = state.getCustomName();
		this.lockKey = state.getLock();
	}

	public SItemHopper(JsonObject json) {
		super(json);
	}
	
	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
		Hopper state = (Hopper) meta.getBlockState();

		state.setCustomName(customName);
		state.setLock(lockKey);
		state.getInventory().setContents(container.getContents());

		meta.setBlockState(state);
		item.setItemMeta(meta);
		return item;
	}

}
