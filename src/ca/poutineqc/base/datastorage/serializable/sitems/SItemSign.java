package ca.poutineqc.base.datastorage.serializable.sitems;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import ca.poutineqc.base.utils.Utils;

public class SItemSign extends SItem {

	private static final String LINES = "lines";

	private String[] lines = new String[4];

	public SItemSign() {
		super(Material.SIGN);
	}

	public SItemSign(ItemStack itemStack) {
		super(itemStack);

		BlockStateMeta meta = (BlockStateMeta) itemStack.getItemMeta();
		Sign state = (Sign) meta.getBlockState();

		this.lines = state.getLines();
	}

	public SItemSign(JsonObject json) {
		super(json);
		JsonArray lines = json.get(LINES).getAsJsonArray();
		int index = 0;
		for (JsonElement line : lines)
			this.lines[index++] = line.getAsString();
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject json = super.toJsonObject();

		JsonArray lines = new JsonArray();
		for (String line : this.lines)
			lines.add(new JsonPrimitive(line));

		return json;
	}

	public String getLine(int index) {
		return lines[index];
	}

	public void setLines(int index, String line) {
		this.lines[index] = line;
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
		meta.setBlockState(getSignBlockState(lines));
		item.setItemMeta(meta);
		return item;
	}

	private Sign getSignBlockState(String[] lines) {
		Block block = Utils.getFirstAvailableAirBlock();
		if (block == null)
			return null;

		block.setType(Material.SIGN_POST);
		Sign blockState = (Sign) block.getState();
		for (int i = 0; i < 4; i++)
			blockState.setLine(i, lines[i]);

		blockState.update();

		block.setType(Material.AIR);
		return blockState;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SItemSign))
			return false;

		SItemSign sign = (SItemSign) o;

		for (int i = 0; i < 4; i++)
			if (!this.lines[i].equals(sign.lines[i]))
				return false;

		return super.equals(o);
	}

}
