package ca.poutineqc.base.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.google.gson.JsonObject;


public class SHead extends SItem {

	private String playerName;

	public SHead(String playerName) {
		super(Material.SKULL_ITEM);
		this.durability = 3;
		this.playerName = playerName;
	}

	public SHead(ItemStack itemStack) {
		super(itemStack);

		SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
		this.playerName = meta.hasOwner() ? meta.getOwner() : null;
	}

	public SHead() {
		super(Material.SKULL_ITEM);
		this.durability = 3;
	}

	public SHead(JsonObject json) {
		super(json);
	}

	@Override
	public ItemStack getItem() {
		ItemStack itemStack = super.getItem();
		SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
		if (playerName != null)
			meta.setOwner(playerName);

		itemStack.setItemMeta(meta);
		return itemStack;
	}

	@Override
	public boolean equals(Object o) {
		if (!super.equals(o))
			return false;
		
		ItemStack itemStack = (ItemStack) o;

		if (durability != 3)
			return true;

		SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
		if (meta.hasOwner()) {
			if (playerName == null)
				return false;
			else if (!meta.getOwner().equalsIgnoreCase(playerName))
				return false;
		} else if (playerName != null)
			return false;

		return true;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getPlayerName() {
		return playerName;
	}
}