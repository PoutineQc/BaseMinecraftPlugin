package ca.poutineqc.base.data.values;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import com.google.gson.JsonObject;

public class SItemChest extends SItemContainer {

	public SItemChest(boolean inMinecart, boolean trapped) {
		super(inMinecart ? Material.STORAGE_MINECART : (trapped ? Material.TRAPPED_CHEST : Material.CHEST), 27);
	}

	public SItemChest(Inventory inventory, boolean inMinecart, boolean trapped) {
		super(inMinecart ? Material.STORAGE_MINECART : (trapped ? Material.TRAPPED_CHEST : Material.CHEST), inventory);
	}

	public SItemChest(ItemStack itemStack) {
		super(itemStack);
		BlockStateMeta meta = (BlockStateMeta) itemStack.getItemMeta();
		Chest state = (Chest) meta.getBlockState();
		
		this.container = new SInventory(state.getInventory());
		this.customName = state.getCustomName();
		this.lockKey = state.getLock();
	}

	public SItemChest(JsonObject json) {
		super(json);
	}
	
	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
		Chest state = (Chest) meta.getBlockState();
		
		state.setCustomName(customName);
		state.setLock(lockKey);
		state.getInventory().setContents(container.getContents());
		
		meta.setBlockState(state);
		item.setItemMeta(meta);
		return item;
	}
}
