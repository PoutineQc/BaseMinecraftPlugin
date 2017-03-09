package ca.poutineqc.base.datastorage.serializable.sitems;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import com.google.gson.JsonObject;

import ca.poutineqc.base.datastorage.serializable.SInventory;

public class SItemShulkerBox extends SItemContainer {

	public SItemShulkerBox(DyeColor color) {
		super(getShulkerBoxFromColor(color), 27);
	}

	public SItemShulkerBox(DyeColor color, Inventory inventory) {
		super(getShulkerBoxFromColor(color), inventory);
	}

	public SItemShulkerBox(ItemStack itemStack) {
		super(itemStack);
		BlockStateMeta meta = (BlockStateMeta) itemStack.getItemMeta();
		ShulkerBox state = (ShulkerBox) meta.getBlockState();

		this.container = new SInventory(state.getInventory());
		this.customName = state.getCustomName();
		this.lockKey = state.getLock();
	}

	public SItemShulkerBox(JsonObject json) {
		super(json);
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
		ShulkerBox state = (ShulkerBox) meta.getBlockState();

		state.setCustomName(customName);
		state.setLock(lockKey);
		state.getInventory().setContents(container.getContents());

		meta.setBlockState(state);
		item.setItemMeta(meta);
		return item;
	}

	private static Material getShulkerBoxFromColor(DyeColor color) {
		switch (color) {
		case BLACK:
			return Material.BLACK_SHULKER_BOX;
		case BLUE:
			return Material.BLUE_SHULKER_BOX;
		case BROWN:
			return Material.BROWN_SHULKER_BOX;
		case CYAN:
			return Material.CYAN_SHULKER_BOX;
		case GRAY:
			return Material.GRAY_SHULKER_BOX;
		case GREEN:
			return Material.GREEN_SHULKER_BOX;
		case LIGHT_BLUE:
			return Material.LIGHT_BLUE_SHULKER_BOX;
		case LIME:
			return Material.LIME_SHULKER_BOX;
		case MAGENTA:
			return Material.MAGENTA_SHULKER_BOX;
		case ORANGE:
			return Material.ORANGE_SHULKER_BOX;
		case PINK:
			return Material.PINK_SHULKER_BOX;
		case PURPLE:
			return Material.PURPLE_SHULKER_BOX;
		case RED:
			return Material.RED_SHULKER_BOX;
		case SILVER:
			return Material.SILVER_SHULKER_BOX;
		case YELLOW:
			return Material.YELLOW_SHULKER_BOX;
		case WHITE:
		default:
			return Material.WHITE_SHULKER_BOX;
		}
	}

}
