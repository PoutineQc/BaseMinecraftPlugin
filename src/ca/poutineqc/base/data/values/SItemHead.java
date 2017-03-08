package ca.poutineqc.base.data.values;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.google.gson.JsonObject;

public class SItemHead extends SItem {

	private static final String PLAYER_NAME = "playerName";

	private String playerName;

	public SItemHead(String playerName) {
		super(Material.SKULL_ITEM, (short) 3);
		this.playerName = playerName;
	}

	public SItemHead(ItemStack itemStack) {
		super(itemStack);

		SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
		this.playerName = meta.hasOwner() ? meta.getOwner() : null;
	}

	public SItemHead(JsonObject json) {
		super(json);

		this.playerName = json.get(PLAYER_NAME).getAsString();
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
		if (!(o instanceof SItemHead))
			return false;
		
		SItemHead head = (SItemHead) o;

		if (head.playerName.equals(head))
			return false;

		return super.equals(o);
	}
	
	@Override
	public JsonObject toJsonObject() {
		JsonObject json = super.toJsonObject();
		json.addProperty(PLAYER_NAME, playerName);
		return json;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getPlayerName() {
		return playerName;
	}
}